package accountingView;

import javax.swing.*;
import accountingController.MOHCostAllocationController;
import accountingModel.Department;
import accountingModel.MOHCostAllocation.MOHAllocationMethod;
import accountingModel.Product;
import java.awt.*;
import java.util.ArrayList;


public class MOHCostAllocationView {
	private JFrame currentFrame; // To manage the currently open frame
	private MOHCostAllocationController controller;

	public MOHCostAllocationView() {
		openMainDepartmentsWindow();// different design than opening first window in constructor
		controller = new MOHCostAllocationController();
	}

	// 1. Main Departments Input Window
	private void openMainDepartmentsWindow() {
		currentFrame = new JFrame("Enter Main Departments");
		currentFrame.setSize(500, 400);
		currentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		currentFrame.setLocationRelativeTo(null);
		Font labelFont = new Font("Arial", Font.BOLD, 17);
		Font inputFont = new Font("Arial", Font.BOLD, 15);

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4, 2, 10, 20));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		panel.setBackground(new Color(220, 240, 255)); // Light blue background

		JTextField prodNameField = new JTextField();
		JTextField costField = new JTextField();
		JTextField unitsField = new JTextField();

		prodNameField.setFont(inputFont);
		costField.setFont(inputFont);
		unitsField.setFont(inputFont);
		prodNameField.setForeground(new Color(128, 0, 128));// purple
		costField.setForeground(new Color(128, 0, 128));
		unitsField.setForeground(new Color(128, 0, 128));

		JButton addButton = new JButton("Add Product");
		JButton submitButton = new JButton("Submit");

		JLabel prodNameLabel = new JLabel("Product Name:");
		JLabel costLabel = new JLabel("Total Direct Costs:");
		JLabel unitsLabel = new JLabel("Units Produced:");

		prodNameLabel.setFont(labelFont);
		costLabel.setFont(labelFont);
		unitsLabel.setFont(labelFont);

		panel.add(prodNameLabel);
		panel.add(prodNameField);
		panel.add(unitsLabel);
		panel.add(unitsField);
		panel.add(costLabel);
		panel.add(costField);
		panel.add(addButton);
		panel.add(submitButton);

		submitButton.setBackground(Color.BLACK);
		submitButton.setForeground(Color.WHITE);

		currentFrame.add(panel);
		currentFrame.setVisible(true);

		addButton.addActionListener(e -> {
			String name = prodNameField.getText().trim();
			String costText = costField.getText().trim();
			String unitsText = unitsField.getText().trim();
			if (!name.isEmpty() && !costText.isEmpty() && !unitsText.isEmpty()) {
				try {
					double cost = Double.parseDouble(costText);
					double units = Double.parseDouble(unitsText);
					controller.addProduct(name, cost, units);// number of units produced
					JOptionPane.showMessageDialog(currentFrame, "Product added successfully!");
					prodNameField.setText("");
					costField.setText("");
					unitsField.setText("");
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(currentFrame, "Invalid numbers. Please enter a number.", "",
							JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(currentFrame, "Please fill out all fields.", "",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		submitButton.addActionListener(e -> {
			if (controller.getNumberOfProducts() == 0) {
				JOptionPane.showMessageDialog(currentFrame, "Please add at least one product before submitting.", "",
						JOptionPane.ERROR_MESSAGE);
			} else {
				currentFrame.dispose();
				openMOHDepartmentsWindow();
			}
		});
	}

	// 2. MOH Departments Input Window
	private void openMOHDepartmentsWindow() {
		currentFrame = new JFrame("Enter MOH Departments");
		currentFrame.setSize(500, 400);
		currentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		currentFrame.setLocationRelativeTo(null);
		Font labelFont = new Font("Arial", Font.BOLD, 17);
		Font inputFont = new Font("Arial", Font.BOLD, 15);

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 2, 10, 20));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		panel.setBackground(new Color(155, 185, 255)); // Darker light blue background

		JTextField mohDeptField = new JTextField();
		JTextField costsField = new JTextField();
		mohDeptField.setFont(inputFont);
		costsField.setFont(inputFont);
		mohDeptField.setForeground(new Color(128, 0, 128));// purple
		costsField.setForeground(new Color(128, 0, 128));

		JLabel mohDeptLabel = new JLabel("MOH Department Name:");
		JLabel costsLabel = new JLabel("Incurred Costs:");

		mohDeptLabel.setFont(labelFont);
		costsLabel.setFont(labelFont);

		JButton addButton = new JButton("Add MOH Department");
		JButton submitButton = new JButton("Submit");

		panel.add(mohDeptLabel);
		panel.add(mohDeptField);

		panel.add(costsLabel);
		panel.add(costsField);

		panel.add(addButton);
		panel.add(submitButton);

		submitButton.setBackground(Color.BLACK);
		submitButton.setForeground(Color.WHITE);

		addButton.setBackground(new Color(230, 230, 255));

		currentFrame.add(panel);
		currentFrame.setVisible(true);

		addButton.addActionListener(e -> {
			String mohDeptName = mohDeptField.getText().trim();
			String costsText = costsField.getText().trim();
			if (!mohDeptName.isEmpty() && !costsText.isEmpty()) {
				try {
					Double costs = Double.parseDouble(costsText);
					controller.addSupportDepartment(mohDeptName, costs);// get interdepartmental costs
					JOptionPane.showMessageDialog(currentFrame, "MOH Department added successfully!");
					mohDeptField.setText("");
					costsField.setText("");

				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(currentFrame, "Invalid costs.", "",
							JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(currentFrame, "Please fill out all fields.", "",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		submitButton.addActionListener(e -> {
			if (controller.getNumberOfMOHDepartments() == 0) {
				JOptionPane.showMessageDialog(currentFrame, "Please add at least one MOH department before submitting.",
						"", JOptionPane.ERROR_MESSAGE);
			} else {
				currentFrame.dispose();
				openCostDriverAllocationWindow();
			}
		});
	}

	private void openCostDriverAllocationWindow() {

		// setting up frame
		currentFrame = new JFrame("Cost Driver Allocation");
		currentFrame.setSize(600, 400);
		currentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		currentFrame.setLocationRelativeTo(null);

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(new Color(185, 185, 255));

		JPanel allocationPanel = new JPanel(new GridBagLayout());
		allocationPanel.setBackground(panel.getBackground());
		panel.add(allocationPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));// the button looks cooler with a panel
		JButton continueButton = new JButton("Continue");
		continueButton.setFocusable(false);
		continueButton.setPreferredSize(new Dimension(120, 40));// change size (bigger)
		buttonPanel.add(continueButton);
		panel.add(buttonPanel, BorderLayout.SOUTH);

		currentFrame.add(panel);
		currentFrame.setVisible(true);

		// Define the list of departments and products
		ArrayList<Department> mohDepartments = controller.getDepartments();
		ArrayList<Product> products = controller.getProducts();

		// Array to keep track of the current department index passing by ref
		int[] currentDepartmentIndex = { 0 };

		// Initialize the first panel
		updateAllocationPanel(allocationPanel, mohDepartments, products, currentDepartmentIndex[0]);

		// Action listener for continue button
		continueButton.addActionListener(e -> {

			Department currentDepartment = mohDepartments.get(currentDepartmentIndex[0]);
			Component[] components = allocationPanel.getComponents();
			boolean isValid = true;
			int fieldIndex = 0; // index of the ith textfield within the compomnents array, really nice trick,
								// also represents product

			for (Component component : components) {// loop on all components to get values from all text fields
				if (component instanceof JTextField) {// if it is a text field
					JTextField field = (JTextField) component;// cast it
					try {
						double allocation = Double.parseDouble(field.getText().trim());// get data if correct
						controller.allocateUnits(allocation, currentDepartment.getName(),
								products.get(fieldIndex).getName());
						fieldIndex++;// next product
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(currentFrame,
								"Please enter a valid number for " + products.get(fieldIndex).getName(),
								"Invalid Input", JOptionPane.ERROR_MESSAGE);
						isValid = false;
						break;
					}
				}
			}
			// after the loop, all text fields are valid
			if (isValid) {
				if (currentDepartmentIndex[0] < mohDepartments.size() - 1) {
					currentDepartmentIndex[0]++;// update for next department
					updateAllocationPanel(allocationPanel, mohDepartments, products, currentDepartmentIndex[0]);
				} else {// done all
					JOptionPane.showMessageDialog(currentFrame, "Cost Driver Allocations Saved!");
					currentFrame.dispose();
					methodSelectionWindow(); // Proceed to the next window
				}
			}
		});

	}

	// Utility method to update the allocation panel
	private void updateAllocationPanel(JPanel allocationPanel, ArrayList<Department> mohDepartments,
			ArrayList<Product> products, int departmentIndex) {

		allocationPanel.removeAll(); // Clear panel

		Department currentDepartment = mohDepartments.get(departmentIndex);// only one dep per panel
		JLabel departmentLabel = new JLabel("Allocate units for " + currentDepartment.getName() + ":", JLabel.LEFT);
		departmentLabel.setFont(new Font("Arial", Font.BOLD, 22));
		departmentLabel.setForeground(Color.black);

		GridBagConstraints gbc = new GridBagConstraints();// hpw the component will be placed in a grid structure
		gbc.insets = new Insets(5, 5, 5, 5); // Padding between components
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 0;
		allocationPanel.add(departmentLabel, gbc);

		gbc.gridwidth = 1; // Reset grid width for subsequent components
		int row = 1; // Start adding fields from row 1
		for (Product product : products) {
			JLabel productLabel = new JLabel(product.getName() + " used :");
			productLabel.setFont(new Font("Arial", Font.BOLD, 16));
			productLabel.setForeground(new Color(0, 0, 0));
			gbc.gridx = 0;
			gbc.gridy = row;
			allocationPanel.add(productLabel, gbc);

			JTextField allocationField = new JTextField(10);
			allocationField.setFont(new Font("Arial", Font.BOLD, 14));
			allocationField.setForeground(new Color(253, 61, 181));
			gbc.gridx = 1;// next to zero
			allocationPanel.add(allocationField, gbc);

			row++;
		}

		allocationPanel.revalidate();
		allocationPanel.repaint();
	}

	private void methodSelectionWindow() {
		// Create frame
		JFrame fourthFrame = new JFrame("Select MOH Allocation Method");
		fourthFrame.setSize(600, 400);
		fourthFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		fourthFrame.setLocationRelativeTo(null);

		// Main panel
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(new Color(255, 255, 255));
		fourthFrame.add(panel);

		// Header
		JLabel header = new JLabel("Choose MOH Allocation Method", JLabel.CENTER);
		header.setFont(new Font("Arial", Font.BOLD, 20));
		header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panel.add(header, BorderLayout.NORTH);

		// Method Selection Panel
		JPanel methodPanel = new JPanel(new GridLayout(2, 1, 10, 10));
		methodPanel.setBackground(panel.getBackground());
		JButton abcButton = new JButton("ABC Method");
		abcButton.setFocusable(false);
		JButton pbButton = new JButton("Peanut Butter Method");
		pbButton.setFocusable(false);
		// Customize buttons
		abcButton.setBackground(new Color(100, 149, 237));
		abcButton.setForeground(Color.WHITE);
		abcButton.setFont(new Font("Arial", Font.BOLD, 16));

		pbButton.setBackground(new Color(253, 61, 181));
		pbButton.setForeground(Color.WHITE);
		pbButton.setFont(new Font("Arial", Font.BOLD, 16));

		methodPanel.add(abcButton);
		methodPanel.add(pbButton);
		panel.add(methodPanel, BorderLayout.CENTER);

		// Checkbox Panel
		JPanel checkboxPanel = new JPanel(new FlowLayout());
		checkboxPanel.setBackground(panel.getBackground());
		JCheckBox undefinedUnitsCheckbox = new JCheckBox("Not all products are defined!");
		undefinedUnitsCheckbox.setFocusable(false);
		undefinedUnitsCheckbox.setForeground(Color.red);
		undefinedUnitsCheckbox.setBackground(Color.white);
		undefinedUnitsCheckbox.setFont(new Font("Arial", Font.BOLD, 20));
		checkboxPanel.add(undefinedUnitsCheckbox);
		panel.add(checkboxPanel, BorderLayout.SOUTH);

		// Action listeners for method selection
		abcButton.addActionListener(e -> {
			fourthFrame.dispose();
			openAnswerWindow(MOHAllocationMethod.ABC, null);
		});

		pbButton.addActionListener(e -> {
			String singleDep = "";
			while (true) {
				singleDep = JOptionPane.showInputDialog(fourthFrame, "Enter name of single department:");
				if (singleDep == null)
					break;
				else if (controller.departmentExists(singleDep.trim().toLowerCase())) {
					fourthFrame.dispose();
					openAnswerWindow(MOHAllocationMethod.PEANUTBUTTER, singleDep);
					break;
				} else {
					JOptionPane.showMessageDialog(fourthFrame, "Invalid input. Please enter an existing department.",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}

		});

		// Checkbox action listener
		undefinedUnitsCheckbox.addActionListener(e -> {
			if (undefinedUnitsCheckbox.isSelected()) {
				// Prompt user for total units and MOH
				String unitsInput = JOptionPane.showInputDialog(fourthFrame, "Enter total number of products:");
				String mohInput = JOptionPane.showInputDialog(fourthFrame, "Enter total MOH:");
				String totalDriverOfSingle = JOptionPane.showInputDialog(fourthFrame,
						"Enter total driver units of single department:");

				try {
					double products = Integer.parseInt(unitsInput);
					double moh = Double.parseDouble(mohInput);
					double totalDriver = Double.parseDouble(totalDriverOfSingle);
					controller.setupUndefinedMode(moh, products, totalDriver);
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(fourthFrame, "Invalid input. Please enter valid numbers.", "Error",
							JOptionPane.ERROR_MESSAGE);
					undefinedUnitsCheckbox.setSelected(false); // Reset checkbox
				}
			}
		});

		fourthFrame.setVisible(true);
	}

	private void openAnswerWindow(MOHAllocationMethod method, String singleDep) {
	    // Create frame
	    currentFrame = new JFrame("Answer");
	    currentFrame.setSize(500, 400);
	    currentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    currentFrame.setLocationRelativeTo(null);

	    // Get the answer string
	    String ans;
	    if (method == MOHAllocationMethod.ABC) {
	        ans = controller.getABCAns();
	    } else {
	        ans = controller.getPBAns(singleDep);
	    }

	    // Main panel
	    JPanel mainPanel = new JPanel(new BorderLayout());
	    mainPanel.setBackground(new Color(195, 215, 255)); // Light blue/purple theme

	    // Title label
	    JLabel titleLabel = new JLabel("Result", JLabel.CENTER);
	    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
	    titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    mainPanel.add(titleLabel, BorderLayout.NORTH);

	    // Answer panel
	    JPanel answerPanel = new JPanel();
	    answerPanel.setBackground(mainPanel.getBackground());
	    answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.Y_AXIS));

	    JLabel answerLabel = new JLabel("<html>" + ans.replaceAll("\n", "<br>") + "</html>", JLabel.CENTER);
	    answerLabel.setFont(new Font("Arial", Font.BOLD, 16));
	    answerLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
	    answerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    answerPanel.add(answerLabel);

	    // Make answer panel scrollable
	    JScrollPane scrollPane = new JScrollPane(answerPanel);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	    mainPanel.add(scrollPane, BorderLayout.CENTER);

	    // Buttons panel
	    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
	    buttonPanel.setBackground(mainPanel.getBackground());

	    // Create buttons
	    JButton backButton = new JButton("Back");
	    JButton compareButton = new JButton("Compare");
	    JButton finishButton = MainView.getFinishButton(currentFrame);

	    finishButton.setBackground(Color.WHITE);
	    finishButton.setForeground(Color.BLACK);
	    finishButton.setFocusable(false);

	    compareButton.setBackground(new Color(255, 215, 0));
	    compareButton.setForeground(Color.WHITE);
	    compareButton.setFocusable(false);

	    backButton.setBackground(Color.BLACK);
	    backButton.setForeground(Color.WHITE);
	    backButton.setFocusable(false);

	    // Add button actions
	    backButton.addActionListener(e -> {
	        currentFrame.dispose();
	        controller.resetUndefinedMode();
	        methodSelectionWindow(); // Open previous window
	    });

	    compareButton.addActionListener(e -> {
	        // Create a compare window
	        JFrame compareFrame = new JFrame("Comparison: ABC vs PB");
	        compareFrame.setSize(400, 300);
	        compareFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        compareFrame.setLocationRelativeTo(null);

	        // Check for single department input
	        if (controller.getSingleDepartment() == null) {
	            while (true) {
	                String singleDepartment = JOptionPane.showInputDialog(compareFrame,
	                        "Enter name of single department:");
	                if (singleDepartment == null) // User clicked X or didn't enter a department
	                    break;
	                else if (controller.departmentExists(singleDepartment.trim().toLowerCase())) {
	                    controller.getPBAns(singleDepartment); // Ensure data is ready for comparison
	                    break;
	                } else {
	                    JOptionPane.showMessageDialog(compareFrame,
	                            "Invalid input. Please enter an existing department.", "Error",
	                            JOptionPane.ERROR_MESSAGE);
	                }
	            }
	        }

	        // Get comparison string
	        String comparison = controller.compare();

	        // Display comparison in a formatted way
	        JLabel compareLabel = new JLabel("<html>" + comparison.replaceAll("\n", "<br>") + "</html>", JLabel.CENTER);
	        compareLabel.setFont(new Font("Arial", Font.BOLD, 16));
	        compareLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	        // Comparison panel with scroll
	        JPanel comparePanel = new JPanel(new BorderLayout());
	        comparePanel.setBackground(new Color(195, 215, 255));
	        comparePanel.add(compareLabel, BorderLayout.CENTER);

	        JScrollPane compareScrollPane = new JScrollPane(comparePanel);
	        compareScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	        compareScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	        compareFrame.add(compareScrollPane);
	        compareFrame.setVisible(true);
	    });

	    // Add buttons to the button panel
	    buttonPanel.add(backButton);
	    buttonPanel.add(compareButton);
	    buttonPanel.add(finishButton);

	    // Add button panel to the main panel
	    mainPanel.add(buttonPanel, BorderLayout.SOUTH);

	    // Set main panel and make frame visible
	    currentFrame.add(mainPanel);
	    currentFrame.setVisible(true);
	}



}
