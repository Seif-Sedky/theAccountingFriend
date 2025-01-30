package accountingModel;

import java.util.HashMap;

public class Product extends AccountingObject{

	private String name;
	private HashMap<Department, Double> unitsProvided;
	private double directCosts;
	private double unitsProduced;
	public Product(String name, double directCosts,double unitsProduced) {
		this.name = name;
		this.unitsProvided = new HashMap<>();
		this.directCosts = directCosts;
		this.unitsProduced=unitsProduced;
	}

	public void addUnits(Department d, double x) {
		unitsProvided.put(d, x);
	}

	public String getName() {
		return name;
	}

	public HashMap<Department, Double> getUnitsProvided() {
		return unitsProvided;
	}

	public double getDirectCosts() {
		return directCosts;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUnitsProvided(HashMap<Department, Double> unitsProvided) {
		this.unitsProvided = unitsProvided;
	}

	public void setDirectCosts(double directCosts) {
		this.directCosts = directCosts;
	}

	public void addDirectCosts(double directCosts) {
		this.directCosts += directCosts;
	}

	public double getUnitsProduced() {
		return unitsProduced;
	}

	public void setUnitsProduced(int unitsProduced) {
		this.unitsProduced = unitsProduced;
	}

}
