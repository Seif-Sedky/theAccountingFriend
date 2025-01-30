package accountingModel;

import java.util.HashMap;

public class CostObject extends AccountingObject {
	private String name;
	

	private boolean product;
	private boolean direct;
	private boolean variable;
	private double costDriver;//for better reusabillty, add a total variable and per unit variable
	private CostObjectClassifier costClassifier = new CostObjectClassifier();;



	public CostObject(String name, double costDriver) {
		this.name = name;
		this.costDriver = costDriver;
	}

	public boolean classifyCostObject() {

		String costName = this.getName().toLowerCase(); // Normalize input
		costName = costName.trim();// remove extra spaces
		// Check for exact matches first

		HashMap<String, String> classificationRules = costClassifier.getClassificationRules();
		if (classificationRules.get(costName) == null) {//if not found even after normalization
			costName = MainModel.levenshteinDistance(costName, classificationRules.keySet());//look for match <=2
			this.setName(costName);//update name after finding the real one
		}

		if (costName != null) {
			String classifications = classificationRules.get(costName);
			String[] classes = classifications.split(",");
			if (classes[0].equals("product")) {
				this.setProduct(true);
			}
			if (classes[1].equals("direct")) {
				this.setDirect(true);
			}
			if (classes[2].equals("variable")) {
				this.setVariable(true);
			}
			return true;
		} else {
			//destroy the object??
			return false;
		}
	}

	public boolean isProduct() {
		return product;
	}

	public void setProduct(boolean product) {
		this.product = product;
	}

	public boolean isDirect() {
		return direct;
	}

	public void setDirect(boolean direct) {
		this.direct = direct;
	}

	public boolean isVariable() {
		return variable;
	}

	public void setVariable(boolean variable) {
		this.variable = variable;
	}

	@Override
	public String toString() {
		return "CostObject [name=" + name + ", product=" + product + ", direct=" + direct + ", variable=" + variable
				+ ", costDriver=" + costDriver + ", costClassifier=" + costClassifier + "]";
	}

	public double getCostDriver() {
		return costDriver;
	}

	public void setCostDriver(double costDriver) {
		this.costDriver = costDriver;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) return true; // Check if they are the same object
	    if (obj == null || getClass() != obj.getClass()) return false; // Check for null and class type

	    CostObject other = (CostObject) obj; // Cast to CostObject
	    return name != null && name.equalsIgnoreCase(other.name); // Compare names ignoring case
	}

	@Override
	public int hashCode() {
	    return name != null ? name.toLowerCase().hashCode() : 0; // Generate hash based on lowercase name
	}

}
