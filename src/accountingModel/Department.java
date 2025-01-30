package accountingModel;

import java.util.HashMap;

public class Department extends AccountingObject {
	private boolean supporting;// main or supporting department
	private String name;
	private double interdepartmentalCosts;
	private HashMap<Department, Double> unitsProvided;

	public Department(boolean supporting, String name, double interdepartmentalCosts) {
		this.supporting = supporting;
		this.name = name;
		this.interdepartmentalCosts = interdepartmentalCosts;
		this.unitsProvided = new HashMap<>();

	}

	public void addProvidingDepartment(Department provider, Double units) {
		unitsProvided.put(provider, units);
	}

	@Override
	public String toString() {
		return "Department " + name;
	}

	public boolean isSupporting() {
		return supporting;
	}

	public HashMap<Department, Double> getUnitsProvided() {
		return unitsProvided;
	}

	@Override
	public String getName() {
		return name;
	}

	public double getInterdepartmentalCosts() {
		return interdepartmentalCosts;
	}

	public boolean equals(Department two) {
		return this.name.equals(two.getName());
	}
}
