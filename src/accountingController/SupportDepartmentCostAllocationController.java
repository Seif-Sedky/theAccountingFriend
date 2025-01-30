package accountingController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import accountingModel.Department;
import accountingModel.SupportDepartmentsCostAllocation;
import accountingModel.SupportDepartmentsCostAllocation.AllocationMethod;

public class SupportDepartmentCostAllocationController {

	private SupportDepartmentsCostAllocation obj = new SupportDepartmentsCostAllocation();

	public String getAns(AllocationMethod method) {// not consistent with moh as moh has seperate an methods, this
													// operates in a different fashion
		StepsCollector collector = new StepsCollector();
		HashMap<ArrayList<String>, Double> ans = obj.calculate(method, collector);
		String StringAnswer = format(ans, collector);// repeated logic in moh allocation class, format method should be
														// a static method in main view class
		// but i am tiredddddddd
		return StringAnswer;
	}

	public ArrayList<Department> getSupportDepartments() {
		return obj.getSupportDepartments();
	}

	public ArrayList<Department> getMainDepartments() {
		return obj.getMainDepartments();
	}

	public void addDepartment(boolean supporting, String name, double costs) {
		obj.addDepartment(new Department(supporting, name, costs));

	}

	public void addUnitsProvided(double units, String department1, String department2) {// also repeated logic in moh
																						// allocation
		Department taker = getDepartment(department1);
		Department giver = getDepartment(department2);
		taker.addProvidingDepartment(giver, units);
	}

	public Department getDepartment(String name) {
		ArrayList<Department> arr = obj.getMainDepartments();
		arr.addAll(obj.getSupportDepartments());
		for (Department dep : arr) {
			if (dep.getName().equals(name))
				return dep;
		}
		return null;
	}

	public boolean departmentExists(String name) {// repeated logic in moh allocation
		for (Department x : obj.getMainDepartments()) {
			if (x.getName().equals(name))
				return true;
		}
		for (Department x : obj.getSupportDepartments()) {
			if (x.getName().equals(name))
				return true;
		}
		return false;
	}

	public String format(HashMap<ArrayList<String>, Double> answer, StepsCollector collector) {
	    StringBuilder formattedAnswer = new StringBuilder();
	    formattedAnswer.append("<html><body style='font-family:Arial;font-size:16px;background-color:#F0F0F0;'>");

	    // Table Start
	    formattedAnswer.append("<table style='border-collapse:collapse;width:100%;margin-top:10px;'>");
	    formattedAnswer.append("<tr style='background-color:#483D8B;color:white;'>") // Dark Slate Blue header
	            .append("<th style='text-align:left;padding:6px;'>From Department</th>")
	            .append("<th style='text-align:left;padding:6px;'>To Department</th>")
	            .append("<th style='text-align:right;padding:6px;'>Allocated Cost</th>")
	            .append("</tr>");

	    // Populate table with data
	    for (Map.Entry<ArrayList<String>, Double> entry : answer.entrySet()) {
	        ArrayList<String> departments = entry.getKey();
	        Double cost = entry.getValue();

	        if (departments.size() == 2) {
	            String fromDepartment = departments.get(0);
	            String toDepartment = departments.get(1);

	            formattedAnswer.append("<tr style='background-color:#ffffff;'>")
	                    .append("<td style='padding:6px;'>").append(fromDepartment).append("</td>")
	                    .append("<td style='padding:6px;'>").append(toDepartment).append("</td>")
	                    .append("<td style='text-align:right;padding:6px;'>").append(String.format("%.2f", cost))
	                    .append("</td>")
	                    .append("</tr>");
	        }
	    }

	    formattedAnswer.append("</table>"); // Close table

	    // Append Steps
	    formattedAnswer.append("<h3 style='margin-top:15px; color:#483D8B; text-align:center; font-size:20px; font-weight:bold; text-decoration:underline;'>Calculation Steps:</h3>");
	    formattedAnswer.append("<ul style='padding-left:18px;margin-top:5px;'>");
	    for (String step : collector.getSteps()) {
	        formattedAnswer.append("<li style='margin-bottom:3px;'>").append(step).append("</li>");
	    }
	    formattedAnswer.append("</ul>");

	    formattedAnswer.append("</body></html>");
	    return formattedAnswer.toString();
	}



}
