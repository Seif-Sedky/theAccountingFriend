package accountingView;

import javax.swing.*;
import java.awt.*;

import java.util.ArrayList;

import accountingController.SupportDepartmentCostAllocationController;
import accountingModel.Department;
import accountingModel.SupportDepartmentsCostAllocation.AllocationMethod;

public class SupportDepartmentCostAllocationView {
	private SupportDepartmentCostAllocationController controller = new SupportDepartmentCostAllocationController();
	private JFrame currentFrame;

	public SupportDepartmentCostAllocationView() {
		openDepartmentEntryWindow();
	}

	private void openDepartmentEntryWindow() {
		currentFrame = new JFrame("Enter Department Details");
		currentFrame.setSize(400, 300);
		currentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		currentFrame.setLocationRelativeTo(null);
		currentFrame.setBackground(new Color(220, 240, 255));
		JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
		panel.setBackground(currentFrame.getBackground());
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		Font fieldFont = new Font("Arial", Font.PLAIN, 16);

		JTextField nameField = new JTextField();
		nameField.setFont(fieldFont);
		nameField.setForeground(new Color(128, 0, 128));
		JTextField costField = new JTextField();
		costField.setFont(fieldFont);
		costField.setForeground(new Color(128, 0, 128));

		JRadioButton supportButton = new JRadioButton("Support Department");
		supportButton.setBackground(currentFrame.getBackground());
		JRadioButton mainButton = new JRadioButton("Main Department");
		mainButton.setBackground(currentFrame.getBackground());
		ButtonGroup group = new ButtonGroup();
		group.add(supportButton);
		group.add(mainButton);

		JButton addButton = new JButton("Add Department");
		JButton submitButton = new JButton("Submit");
		addButton.setBackground(Color.MAGENTA);
		submitButton.setBackground(new Color(210, 230, 255));
		panel.add(new JLabel("Department Name:"));
		panel.add(nameField);
		panel.add(new JLabel("Interdepartmental Costs:"));
		panel.add(costField);
		panel.add(supportButton);
		panel.add(mainButton);
		panel.add(addButton);
		panel.add(submitButton);

		addButton.addActionListener(e -> {
			String name = nameField.getText();// could add a check if department already exists
			try {
				double cost = Double.parseDouble(costField.getText());
				boolean isSupport = supportButton.isSelected();
				if ((!supportButton.isSelected() && !mainButton.isSelected()) || name == null) {
					throw new IllegalArgumentException();
				}

				controller.addDepartment(isSupport, name, cost);
				nameField.setText("");
				costField.setText("");

			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(currentFrame, "Invalid numbers. Please enter a number.", "",
						JOptionPane.ERROR_MESSAGE);
			} catch (IllegalArgumentException ex) {
				JOptionPane.showMessageDialog(currentFrame, "please fill out all the data", "",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		submitButton.addActionListener(e -> {
			if (controller.getMainDepartments().size() < 1 || controller.getSupportDepartments().size() < 1) {
				JOptionPane.showMessageDialog(currentFrame, "you have to enter atleast 1 department of each type", "",
						JOptionPane.ERROR_MESSAGE);
			} else {
				currentFrame.dispose();
				openUnitsAllocationWindow();
			}
		});

		currentFrame.add(panel);
		currentFrame.setVisible(true);
	}

	private void openUnitsAllocationWindow() {
		currentFrame = new JFrame("Allocate Units Between Departments");
		currentFrame.setSize(500, 400);
		currentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		currentFrame.setLocationRelativeTo(null);

		// Set the background color of the content pane
		currentFrame.getContentPane().setBackground(new Color(195, 215, 255));

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Set panel background to match the frame
		panel.setBackground(new Color(195, 215, 255));

		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.getViewport().setBackground(new Color(195, 215, 255)); // Set scroll pane viewport color
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		ArrayList<Department> allDepartments = controller.getSupportDepartments();
		allDepartments.addAll(controller.getMainDepartments());
		Font labelFont = new Font("Arial", Font.BOLD, 22);
		Font fieldFont = new Font("Arial", Font.PLAIN, 16);
		for (Department fromDep : controller.getSupportDepartments()) {
			for (Department toDep : allDepartments) {
				if (!fromDep.equals(toDep)) {
					JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
					row.setBackground(new Color(195, 215, 255)); // Match row background color

					JLabel fromToLabel = new JLabel(fromDep.getName() + " -> " + toDep.getName());
					fromToLabel.setFont(labelFont);
					fromToLabel.setForeground(new Color(0, 0, 0)); // Set text color for contrast
					row.add(fromToLabel);

					JTextField unitField = new JTextField(5);
					unitField.setFont(fieldFont);
					unitField.setForeground(new Color(128, 0, 128));
					unitField.setPreferredSize(new Dimension(55, 30));
					row.add(unitField);
					panel.add(row);
				}
			}
		}

		JButton submitButton = new JButton("Submit");
		submitButton.addActionListener(e -> {
			boolean hasError = false; // Track errors to prevent window closure if any exist

			for (Component comp : panel.getComponents()) {
				if (comp instanceof JPanel) {
					JPanel row = (JPanel) comp;

					// Ensure JPanel has at least two components (label + textfield)
					if (row.getComponentCount() < 2)
						continue;

					JLabel label = (JLabel) row.getComponent(0);
					JTextField textField = (JTextField) row.getComponent(1);
					String[] departments = label.getText().split(" -> ");

					try {
						String inputText = textField.getText().trim();
						if (inputText.isEmpty()) {
							throw new IllegalArgumentException("Empty field detected.");
						}

						double units = Double.parseDouble(inputText);
						if (units < 0) {
							throw new IllegalArgumentException("Units cannot be negative.");
						}
						controller.addUnitsProvided(units, departments[1], departments[0]);

					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(currentFrame,
								"Invalid input for units. Please enter a valid number.", "Input Error",
								JOptionPane.ERROR_MESSAGE);
						hasError = true;
					} catch (IllegalArgumentException ex) {
						JOptionPane.showMessageDialog(currentFrame, ex.getMessage(), "Input Error",
								JOptionPane.ERROR_MESSAGE);
						hasError = true;
					}
				}
			}

			if (!hasError) { // Proceed only if no errors occurred
				currentFrame.dispose();
				openMethodSelectionWindow();
			}
		});

		panel.add(submitButton);
		currentFrame.add(scrollPane);
		currentFrame.setVisible(true);
	}

	private void openMethodSelectionWindow() {
		currentFrame = new JFrame("Select Allocation Method");
		currentFrame.setSize(400, 300);
		currentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		currentFrame.setLocationRelativeTo(null);
		currentFrame.getContentPane().setBackground(new Color(240, 240, 240)); // Light gray background

		Font labelFont = new Font("Arial", Font.BOLD, 20);

		JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
		panel.setBackground(currentFrame.getBackground());
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JButton directButton = new JButton("Direct Method");
		directButton.setBackground(new Color(72, 61, 139)); // Dark Slate Blue
		directButton.setForeground(Color.WHITE);
		directButton.setFocusable(false);
		directButton.setFont(labelFont);

		JButton stepDownButton = new JButton("Step-Down Method");
		stepDownButton.setBackground(new Color(46, 139, 87)); // Sea Green
		stepDownButton.setForeground(Color.WHITE);
		stepDownButton.setFocusable(false);
		stepDownButton.setFont(labelFont);

		JButton reciprocalButton = new JButton("Reciprocal Method");
		reciprocalButton.setBackground(new Color(30, 144, 255)); // Dodger Blue
		reciprocalButton.setForeground(Color.WHITE);
		reciprocalButton.setFocusable(false);
		reciprocalButton.setFont(labelFont);

		directButton.addActionListener(e -> showAnswer(AllocationMethod.DIRECT));
		stepDownButton.addActionListener(e -> {
			if (controller.getSupportDepartments().size() != 2) {
				JOptionPane.showMessageDialog(currentFrame, "Only 2 departments are supported for the step-down method",
						"Error", JOptionPane.ERROR_MESSAGE);
			} else {
				showAnswer(AllocationMethod.STEP_DOWN);
			}
		});
		reciprocalButton.addActionListener(e -> showAnswer(AllocationMethod.RECIPROCAL));

		panel.add(directButton);
		panel.add(stepDownButton);
		panel.add(reciprocalButton);

		currentFrame.add(panel);
		currentFrame.setVisible(true);
	}

	private void showAnswer(AllocationMethod method) {
	    currentFrame.dispose();
	    currentFrame = new JFrame("Answer");
	    currentFrame.setSize(500, 400);
	    currentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    currentFrame.setLocationRelativeTo(null);
	    currentFrame.getContentPane().setBackground(new Color(240, 240, 240)); // Light gray background

	    String ans = controller.getAns(method);

	    // Create main content panel
	    JPanel contentPanel = new JPanel(new BorderLayout());
	    contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    contentPanel.setBackground(new Color(240, 240, 240));

	    // Answer panel (centers the content)
	    JPanel panel = new JPanel();
	    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	    panel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    panel.setBackground(Color.WHITE); // Keeps it visually separate

	    // Answer label
	    JLabel answerLabel = new JLabel("<html>" + ans.replaceAll("\n", "<br>") + "</html>", JLabel.CENTER);
	    answerLabel.setFont(new Font("Arial", Font.BOLD, 16));
	    answerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    panel.add(answerLabel);

	    // Scroll pane (ensures answer is scrollable)
	    JScrollPane scrollPane = new JScrollPane(panel);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Removes default border

	    // Panel for buttons
	    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Buttons centered
	    buttonPanel.setBackground(new Color(240, 240, 240));

	    // Finish Button
	    JButton finishButton = MainView.getFinishButton(currentFrame);
	    finishButton.setPreferredSize(new Dimension(120, 40));
	    finishButton.setFont(new Font("Arial", Font.BOLD, 18));

	    // Back Button
	    JButton backButton = new JButton("Back");
	    backButton.setBackground(new Color(72, 61, 139));
	    backButton.setForeground(Color.WHITE);
	    backButton.setFocusable(false);
	    backButton.setFont(new Font("Arial", Font.BOLD, 18));
	    backButton.setPreferredSize(new Dimension(120, 40));
	    backButton.addActionListener(e -> {
	        currentFrame.dispose();
	        openMethodSelectionWindow();
	    });

	    buttonPanel.add(finishButton);
	    buttonPanel.add(backButton);

	    // Add components to content panel
	    contentPanel.add(scrollPane, BorderLayout.CENTER);
	    contentPanel.add(buttonPanel, BorderLayout.SOUTH);

	    // Set content panel to frame
	    currentFrame.setContentPane(contentPanel);
	    currentFrame.setVisible(true);
	}



}
