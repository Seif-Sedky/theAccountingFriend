package accountingView;

//ensure no critical input is zero or invalid
//add tip on how to enter name of costobject 
//fadel to get steps from controller and sending things to controller and printing the res
import javax.swing.*;
import accountingController.InventoryCostingController;
import java.awt.*;
import java.util.ArrayList;

public class InventoryCostingView {// insatnce to be shared across methods
	private JFrame frame;
	private double price;
	private double unitsSold;
	private double unitsProduced;
	private double beginningInventory;
	private double endingInventory;
	private String costingMethod; // Absorption, Variable, Throughput
	private InventoryCostingController controller;

	public InventoryCostingView() {
		initializeFirstWindow();
	}

	private void initializeFirstWindow() {
		frame = new JFrame("Inventory Costing - Inputs");
		frame.setSize(500, 400);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(8, 2, 10, 20));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		panel.setBackground(new Color(220, 240, 255)); // Light blue background

		// Font for all labels and inputs
		Font labelFont = new Font("Arial", Font.BOLD, 14);
		Font inputFont = new Font("Arial", Font.PLAIN, 14);

		// Input fields
		JTextField priceField = new JTextField();
		JTextField unitsSoldField = new JTextField();
		JTextField unitsProducedField = new JTextField();
		JTextField beginningInventoryField = new JTextField();
		JTextField endingInventoryField = new JTextField();

		priceField.setFont(inputFont);
		unitsSoldField.setFont(inputFont);
		unitsProducedField.setFont(inputFont);
		beginningInventoryField.setFont(inputFont);
		endingInventoryField.setFont(inputFont);

		JLabel priceLabel = new JLabel("Price:");
		priceLabel.setFont(labelFont);
		JLabel unitsSoldLabel = new JLabel("Units Sold:");
		unitsSoldLabel.setFont(labelFont);
		JLabel unitsProducedLabel = new JLabel("Units Produced:");
		unitsProducedLabel.setFont(labelFont);
		JLabel beginningInventoryLabel = new JLabel("Beginning Inventory:");
		beginningInventoryLabel.setFont(labelFont);
		JLabel endingInventoryLabel = new JLabel("Ending Inventory:");
		endingInventoryLabel.setFont(labelFont);

		panel.add(priceLabel);
		panel.add(priceField);

		panel.add(unitsSoldLabel);
		panel.add(unitsSoldField);

		panel.add(unitsProducedLabel);
		panel.add(unitsProducedField);

		panel.add(beginningInventoryLabel);
		panel.add(beginningInventoryField);

		panel.add(endingInventoryLabel);
		panel.add(endingInventoryField);

		// Costing method selection
		JLabel costingMethodLabel = new JLabel("Costing Method:");
		costingMethodLabel.setFont(labelFont);
		JComboBox<String> costingMethodCombo = new JComboBox<>(new String[] { "Absorption", "Variable", "Throughput" });
		costingMethodCombo.setFont(new Font("Arial", Font.BOLD, 14));
		panel.add(costingMethodLabel);
		panel.add(costingMethodCombo);

		// Submit button
		JButton nextButton = new JButton("Next");
		nextButton.setFont(new Font("Arial", Font.BOLD, 16));
		nextButton.setBackground(new Color(0, 51, 102));
		nextButton.setForeground(Color.WHITE); // White text
		nextButton.setFocusPainted(false);
		nextButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
		panel.add(nextButton);
		panel.add(new JLabel(""));// add empty space to make button down

		JLabel note = new JLabel("*ending = (beg+prod)-sold");
		note.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 16));// setting both with or???
		JLabel note2 = new JLabel("*sold = (beg+prod)-ending");
		note2.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 16));// setting both with or???
		note.setForeground(new Color(255, 110, 0));
		note2.setForeground(new Color(255, 110, 0));

		panel.add(note);
		panel.add(note2);
		frame.add(panel);
		frame.setVisible(true);

		// Button listener
		nextButton.addActionListener(e -> {
			try {
				// Parse inputs
				price = Double.parseDouble(priceField.getText());
				unitsSold = Double.parseDouble(unitsSoldField.getText());
				unitsProduced = Double.parseDouble(unitsProducedField.getText());
				beginningInventory = Double.parseDouble(beginningInventoryField.getText());
				costingMethod = costingMethodCombo.getSelectedItem().toString();

				controller = new InventoryCostingController(price, unitsSold, unitsProduced, beginningInventory,
						endingInventory);
				// Close this window and open the next
				frame.dispose();
				initializeSecondWindow();
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(frame, "Please enter valid numeric values.", "Input Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});
	}

	private void initializeSecondWindow() {
		JFrame secondFrame = new JFrame("Inventory Costing - Cost Objects");
		secondFrame.setSize(500, 400);
		secondFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		secondFrame.setLocationRelativeTo(null);

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 2, 7, 10));// number of rows, number of col, vertical and horizontal height
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		panel.setBackground(new Color(185, 205, 255));

		// Font styling
		Font labelFont = new Font("Serif", Font.BOLD, 18);
		Font inputFont = new Font("Serif", Font.PLAIN, 18);

		// Input fields for cost objects
		JTextField costObjectNameField = new JTextField();
		JTextField costObjectCostField = new JTextField();

		costObjectNameField.setFont(inputFont);
		costObjectCostField.setFont(inputFont);
		costObjectNameField.setForeground(new Color(0, 0, 0));
		costObjectCostField.setForeground(new Color(0, 0, 0));

		JLabel nameLabel = new JLabel("Cost Object Name:");
		nameLabel.setFont(labelFont);
		nameLabel.setForeground(new Color(0, 0, 0));
		JLabel costLabel = new JLabel("Cost per Unit / Total Cost:");
		costLabel.setFont(labelFont);
		costLabel.setForeground(new Color(0, 0, 0));

		panel.add(nameLabel);
		panel.add(costObjectNameField);

		panel.add(costLabel);
		panel.add(costObjectCostField);

		// Buttons
		JButton addButton = new JButton("Add Cost Object");
		addButton.setFont(new Font("Arial", Font.BOLD, 14));
		addButton.setBackground(new Color(253, 61, 181)); // magenta
		addButton.setForeground(Color.WHITE);
		addButton.setFocusPainted(false);
		addButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

		JButton submitButton = new JButton("Submit");
		submitButton.setFont(new Font("Arial", Font.BOLD, 14));
		submitButton.setBackground(new Color(0, 51, 102)); // deep blue
		submitButton.setForeground(Color.WHITE);
		submitButton.setFocusPainted(false);
		submitButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

		panel.add(addButton);
		panel.add(submitButton);// order of adding matters

		secondFrame.add(panel);
		secondFrame.setVisible(true);

		// Button listeners
		addButton.addActionListener(e -> {
			String name = costObjectNameField.getText();
			try {
				double cost = Double.parseDouble(costObjectCostField.getText());

				// send to controller
				if (!controller.createCostObject(name, cost)) {// if couldnt create cost object

					throw new IllegalArgumentException();
				}

				// successful insertion and successful name correction if needed
				JOptionPane.showMessageDialog(secondFrame, "Cost Object Added: " + controller.getCorrectedName(name), "Success",
						JOptionPane.INFORMATION_MESSAGE);

				// Clear fields for next entry
				costObjectNameField.setText("");
				costObjectCostField.setText("");
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(secondFrame, "Please enter a valid cost value.", "Input Error",
						JOptionPane.ERROR_MESSAGE);
			} catch (IllegalArgumentException ex) {// invalid cost object name, couldnt add it
				JOptionPane.showMessageDialog(secondFrame, "Cost Object name unidentified.", "Input Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		submitButton.addActionListener(e -> {
			secondFrame.dispose();
			finalFrame();
		});
	}

	private void finalFrame() {
	    // Set up frame
	    JFrame finalFrame = new JFrame("Solution");
	    finalFrame.setSize(500, 400);
	    finalFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    finalFrame.setLocationRelativeTo(null);

	    // Create a light blue/purple background color
	    Color backgroundColor = new Color(195, 215, 255); // Light blue 3ala purple

	    // Set the frame's content pane background color
	    JPanel mainPanel = new JPanel(new BorderLayout());
	    mainPanel.setBackground(backgroundColor); // Set background color for the main panel

	    // Title or header
	    JLabel title = new JLabel("Solution Steps", JLabel.CENTER);
	    title.setFont(new Font("Arial", Font.BOLD, 20));
	    title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    mainPanel.add(title, BorderLayout.NORTH);

	    // Steps or solution content in the center
	    JPanel stepsPanel = new JPanel();
	    stepsPanel.setLayout(new BoxLayout(stepsPanel, BoxLayout.Y_AXIS));
	    stepsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    stepsPanel.setBackground(backgroundColor); // Match the background

	    // Create JLabel to hold the solution
	    JLabel solLabel = new JLabel();
	    solLabel.setFont(new Font("Serif", Font.PLAIN, 16));
	    solLabel.setVerticalAlignment(JLabel.TOP);
	    stepsPanel.add(solLabel);

	    // Wrap the steps in a scrollable view
	    JScrollPane scrollPane = new JScrollPane(stepsPanel);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.getViewport().setBackground(backgroundColor); // Ensure the scroll pane matches the background
	    mainPanel.add(scrollPane, BorderLayout.CENTER);

	    // Navigation buttons at the bottom
	    JPanel buttonPanel = new JPanel();
	    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
	    buttonPanel.setBackground(backgroundColor); // Match the background

	    JButton backButton = new JButton("Back");
	    backButton.setBackground(Color.BLACK);
	    backButton.setForeground(Color.WHITE);
	    backButton.setFocusable(false);
	    backButton.addActionListener(e -> { // Add more cost objects
	        finalFrame.dispose();
	        initializeSecondWindow();
	    });

	    JButton methodSwitchButton = new JButton("Switch Method");
	    methodSwitchButton.setBackground(Color.yellow);
	    methodSwitchButton.setForeground(Color.black);
	    methodSwitchButton.setFocusable(false);
	    methodSwitchButton.addActionListener(e -> {
	        // Let the user choose another costing method
	        String[] methods = {"Absorption", "Variable", "Throughput"};
	        String newMethod = (String) JOptionPane.showInputDialog(
	                finalFrame,
	                "Select a costing method:",
	                "Switch Method",
	                JOptionPane.QUESTION_MESSAGE,
	                null,
	                methods,
	                methods[0]
	        );

	        if (newMethod != null) {
	            updateSolution(newMethod.toLowerCase(), solLabel); // Call the updated method
	        }
	    });

	    buttonPanel.add(backButton);
	    buttonPanel.add(methodSwitchButton);
	    buttonPanel.add(MainView.getFinishButton(finalFrame)); // Static method

	    mainPanel.add(buttonPanel, BorderLayout.SOUTH);

	    finalFrame.setContentPane(mainPanel); // Add main panel to the frame
	    finalFrame.setVisible(true);

	    // Initial call to updateSolution with the default costing method
	    updateSolution(costingMethod, solLabel); 
	}
	private void updateSolution(String method, JLabel solLabel) {
	    // Clear previous content
	    solLabel.setText("");

	    // Get the steps from the controller
	    ArrayList<String> steps = controller.getSolution(method);

	    

	    // Build the income statement
	    StringBuilder solutionHtml = new StringBuilder("<html>");
	    solutionHtml.append("<h3>Income Statement</h3>");
	    solutionHtml.append("<table border='1' cellpadding='5'>");
	    solutionHtml.append("<tr><th>Category</th><th>Details</th></tr>");

	    // Map each index to the respective slot in the income statement
	    String[] categories = {"Revenues", "COGS", "Margin", "Net Income"};
	    for (int i = 0; i < steps.size(); i++) {
	        String step = steps.get(i);
	        solutionHtml.append("<tr><td>")
	                .append(categories[i])
	                .append("</td><td>")
	                .append(step).append("$")
	                .append("</td></tr>");
	    }

	    solutionHtml.append("</table></html>");
	    solLabel.setText(solutionHtml.toString());
	}
}
