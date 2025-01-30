package accountingView;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class MainView extends JFrame {
	
	//overall, all the view classes could be implemented in way better and organized way, implementing better reusabillity, but for the sake 
	//of this being a small project and my first time, it was more than sufficient, i used direct sequential seperate methods for each window,
	//and tried to group common methods like the finish button in this class

	public MainView() {
		// Set up the frame
		setTitle("The Accounting Friend");
		setSize(600, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		// Set up the main panel
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(new Color(220, 240, 255)); // Light blue theme

		// Title
		JLabel titleLabel = new JLabel("The Accounting Friend", SwingConstants.CENTER);

		titleLabel.setFont(new Font("MV Boli", Font.BOLD, 38));
		titleLabel.setForeground(new Color(0, 51, 102)); // Dark blue color
		mainPanel.add(titleLabel, BorderLayout.CENTER);

		// Start Button
		JButton startButton = new JButton("Start");
		startButton.setFont(new Font("Arial", Font.BOLD, 18));
		startButton.setBackground(new Color(0, 102, 153)); // Deep blue
		startButton.setForeground(Color.WHITE);
		startButton.setFocusPainted(false);
		startButton.addActionListener(e -> openOptionSelectionView());
		mainPanel.add(startButton, BorderLayout.SOUTH);// adding the button bottom center across the border

		// Add main panel to the frame
		add(mainPanel);
		setVisible(true);
	}

	private void openOptionSelectionView() {
		// Close the current frame
		this.dispose();

		// Open the next view
		JFrame optionFrame = new JFrame("Select an Option");
		optionFrame.setSize(600, 400);
		optionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		optionFrame.setLocationRelativeTo(null);

		// Option panel setup
		JPanel optionPanel = new JPanel();
		optionPanel.setLayout(new GridLayout(3, 1, 10, 10));
		optionPanel.setBackground(new Color(245, 245, 245)); // Light gray background
		optionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// Buttons for options
		JButton supportAllocationButton = createOptionButton("Allocating Support Department Costs", optionFrame);
		JButton inventoryCostingButton = createOptionButton("Inventory Costing", optionFrame);
		JButton mohAllocationButton = createOptionButton("Allocating MOH Costs", optionFrame);

		// Add buttons to panel
		optionPanel.add(supportAllocationButton);
		optionPanel.add(inventoryCostingButton);
		optionPanel.add(mohAllocationButton);

		// Add panel to frame
		optionFrame.add(optionPanel);
		optionFrame.setVisible(true);
	}

	private JButton createOptionButton(String text, JFrame parentFrame) {
		JButton button = new JButton(text);
		button.setFont(new Font("Arial", Font.BOLD, 16));
		button.setBackground(new Color(0, 102, 180)); // Deeper blue button color
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.addActionListener(e -> {
			parentFrame.dispose(); // Close the option selection window

			// Instantiate the respective view based on the button text that was executed in
			// the action listener
			switch (text) {
			case "Allocating Support Department Costs":
				new SupportDepartmentCostAllocationView(); // Replace with your class
				break;
			case "Inventory Costing":
				new InventoryCostingView(); // Replace with your class
				break;
			case "Allocating MOH Costs":
				new MOHCostAllocationView(); // Replace with your class
				break;
			}
		});
		return button;
	}

	public static JButton getFinishButton(JFrame currentFrame) {
		JButton finishButton = new JButton("Finish");
		finishButton.setBackground(new Color(84, 199, 84)); // A nice green color for a positive vibe
		finishButton.setForeground(Color.WHITE); // White text
		finishButton.setFocusable(false);

		// Add action listener to close the current frame and open a "Thank You" window
		finishButton.addActionListener(e -> {
			currentFrame.dispose(); // Close the current window

			// Create the "Thank You" window
			JFrame thankYouFrame = new JFrame("Thank You");
			thankYouFrame.setLocationRelativeTo(null);
			thankYouFrame.setSize(500, 400);
			thankYouFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			thankYouFrame.setLayout(new BorderLayout());
			thankYouFrame.setForeground(new Color(238, 217, 196));// beige

			JLabel thankYouLabel = new JLabel("Thank you! 😊", JLabel.CENTER);
			thankYouLabel.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 40));
			thankYouFrame.add(thankYouLabel, BorderLayout.CENTER);
			thankYouLabel.setForeground(new Color(253, 61, 181));

			thankYouFrame.setVisible(true);
		});

		return finishButton;
	}

}
