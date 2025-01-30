package accountingController;

import accountingModel.MOHCostAllocation;
import accountingModel.MOHCostAllocation.MOHAllocationMethod;
import accountingModel.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import accountingModel.Department;

public class MOHCostAllocationController {

	private MOHCostAllocation obj;
	private HashMap<ArrayList<String>, Double> abcAns;
	private HashMap<ArrayList<String>, Double> pbAns;

	public MOHCostAllocationController() {
		this.obj = new MOHCostAllocation();
		this.abcAns = new HashMap<>();
		this.pbAns = new HashMap<>();
	}

	public void addProduct(String name, double cost, double units) {
		obj.addProduct(new Product(name, cost, units));
	}

	public void addSupportDepartment(String name, double costs) {
		obj.addDepartment(new Department(false, name, costs));
	}

	public double getNumberOfProducts() {
		return obj.getProducts().size();// number of products inserted by the user regardless of how many actually are
										// there in the not all products are defined mode
	}

	public int getNumberOfMOHDepartments() {
		return obj.getMohDepartments().size();
	}

	public ArrayList<Department> getDepartments() {
		return obj.getMohDepartments();
	}

	public ArrayList<Product> getProducts() {
		return obj.getProducts();
	}

	public boolean departmentExists(String name) {
		return getDepartment(name) != null;
	}

	public void setupUndefinedMode(double moh, double products, double totalDriverForSingle) {
		obj.setUpUndefinedMode(moh, products, totalDriverForSingle);
	}

	public String getABCAns() {
		StepsCollector collector = new StepsCollector();
		this.abcAns = obj.calculate(MOHAllocationMethod.ABC, collector);
		return formatAnswer(abcAns, collector);
	}

	public String getPBAns(String singleDep) {// getting the bp answer automatically sets the single department which
												// cannot be changed later in this version, but the feature is easy to
												// implement using a button and set single department method from
												// controller
		StepsCollector collector = new StepsCollector();
		setSingleDepartment(singleDep);
		this.pbAns = obj.calculate(MOHAllocationMethod.PEANUTBUTTER, collector);
		return formatAnswer(pbAns, collector);
	}

	public String compare() {
		if (abcAns.isEmpty())
			abcAns = obj.calculate(MOHAllocationMethod.ABC, new StepsCollector());// if didnt calculate abc before comparison
		return formatComparisonOutput(obj.comparePeanutToABC(pbAns,abcAns));
	}

	public void allocateUnits(double allocation, String department, String product) {
		Department dep = getDepartment(department);// has to exist since we are prompting the user for allocation only
		// on names he inserted
		Product prod = getProduct(product);
		prod.addUnits(dep, allocation);
	}

	private Product getProduct(String product) {
		ArrayList<Product> arr = obj.getProducts();
		for (Product p : arr) {
			if (p.getName().equals(product))
				return p;
		}
		return null;
	}

	public Department getDepartment(String name) {
		ArrayList<Department> arr = obj.getMohDepartments();
		for (Department dep : arr) {
			if (dep.getName().equals(name))
				return dep;
		}
		return null;
	}

	public void setSingleDepartment(String department) {
		obj.setSingleDepartment(department);
	}

	public Department getSingleDepartment() {
		return obj.getSingleDepartment();
	}

	public String formatAnswer(HashMap<ArrayList<String>, Double> answer, StepsCollector collector) {
		StringBuilder formattedAnswer = new StringBuilder();
		formattedAnswer.append("<html><body style='font-family:Arial;font-size:16px;'>");

		// Start of the table
		formattedAnswer.append("<table style='border-collapse:collapse;width:100%;'>");
		formattedAnswer.append("<tr style='background-color:#c1c1ff;'>")
				.append("<th style='text-align:left;padding:8px;'>From Department</th>")
				.append("<th style='text-align:left;padding:8px;'>To Product</th>")
				.append("<th style='text-align:right;padding:8px;'>Allocated Cost</th>").append("</tr>");

		// Add rows to the table
		for (Map.Entry<ArrayList<String>, Double> entry : answer.entrySet()) {
			ArrayList<String> departments = entry.getKey();
			Double value = entry.getValue();

			if (departments.size() == 2) {
				String fromDepartment = departments.get(0);
				String toDepartment = departments.get(1);

				formattedAnswer.append("<tr>").append("<td style='padding:8px;'>").append(fromDepartment)
						.append("</td>").append("<td style='padding:8px;'>").append(toDepartment).append("</td>")
						.append("<td style='text-align:right;padding:8px;'>").append(String.format("%.2f", value))
						.append("</td>").append("</tr>");
			}
		}

		// End of the table
		formattedAnswer.append("</table>");

		// Add StepsCollector content
		ArrayList<String> steps = collector.getSteps();
		if (steps != null && !steps.isEmpty()) {
			formattedAnswer.append(
					"<div style='margin-top:20px;padding:10px;background-color:#f9f9f9;border:1px solid #ddd;'>")
					.append("<h3 style='margin-bottom:10px;'>Steps:</h3>").append("<ul style='padding-left:20px;'>");

			// Add each step as a list item
			for (String step : steps) {
				formattedAnswer.append("<li>").append(step).append("</li>");
			}

			formattedAnswer.append("</ul>").append("</div>");
		}

		formattedAnswer.append("</body></html>");
		return formattedAnswer.toString();
	}

	public String formatComparisonOutput(HashMap<String, ArrayList<Double>> comparisonData) {
		StringBuilder formattedOutput = new StringBuilder();

		formattedOutput.append("<html><body>");
		formattedOutput.append("<h2 style='text-align: center;'>Comparison of Pricing Methods</h2>");
		formattedOutput.append("<table border='1' style='width: 100%; border-collapse: collapse;'>");
		formattedOutput.append("<tr>").append("<th style='padding: 8px;'>Product</th>")
				.append("<th style='padding: 8px;'>Price Under Peanut Butter</th>")
				.append("<th style='padding: 8px;'>Price Under ABC</th>").append("</tr>");

		for (Map.Entry<String, ArrayList<Double>> entry : comparisonData.entrySet()) {
			String product = entry.getKey();
			ArrayList<Double> prices = entry.getValue();
			double peanutButterPrice = prices.get(0);
			double abcPrice = prices.get(1);

			formattedOutput.append("<tr>").append("<td style='padding: 8px; text-align: center;'>").append(product)
					.append("</td>").append("<td style='padding: 8px; text-align: center;'>")
					.append(String.format("%.2f", peanutButterPrice)).append("</td>")
					.append("<td style='padding: 8px; text-align: center;'>").append(String.format("%.2f", abcPrice))
					.append("</td>").append("</tr>");
		}

		formattedOutput.append("</table>");
		formattedOutput.append("</body></html>");

		return formattedOutput.toString();
	}

	public void resetUndefinedMode() {
		obj.setUndefinedMode(false);
	}

}
