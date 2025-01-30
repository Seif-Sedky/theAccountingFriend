package accountingModel;

import java.util.ArrayList;
import java.util.HashMap;

import accountingController.StepsCollector;

//remaining:  steps 

public class MOHCostAllocation implements Allocatable {

	private ArrayList<Department> mohDepartments;
	private ArrayList<Product> products;
	private double totalMOH;
	private double totalProducts;
	private Department singleDepartment;
	private boolean undefinedMode;
	private double undefinedTotalMOH;
	private double undefinedTotalProducts;
	private double undefinedTotalDriver;

	public MOHCostAllocation() {// default contructor
		mohDepartments = new ArrayList<>();
		products = new ArrayList<>();
	}

	public void setUpUndefinedMode(double totalMOH, double products, double totalDriver) {// if called, replaces the
																							// total moh and the
		undefinedMode = true; // number of products for undefined departments
		undefinedTotalMOH = totalMOH;
		setUndefinedTotalProducts((products));
		undefinedTotalDriver = totalDriver;
	}

	public void addDepartment(Department d) {
		mohDepartments.add(d);
		totalMOH += d.getInterdepartmentalCosts();
	}

	public void addProduct(Product p) {
		products.add(p);
		setTotalProducts(getTotalProducts() + 1);
	}

	public enum MOHAllocationMethod {
		ABC, PEANUTBUTTER
	}

	public HashMap<ArrayList<String>, Double> calculate(MOHAllocationMethod method, StepsCollector collector) {// handle
		// exceptions

		HashMap<ArrayList<String>, Double> EachDepToProductAllocation = new HashMap<>();

		if (method == MOHAllocationMethod.PEANUTBUTTER) {
			// before calculate method is called, the controller sets the single department

			EachDepToProductAllocation = this.calculatePeanut(singleDepartment, collector);

		} else {
			EachDepToProductAllocation = this.calculateABC(collector);
		}
		return EachDepToProductAllocation;

	}

	public HashMap<String, ArrayList<Double>> comparePeanutToABC(HashMap<ArrayList<String>, Double> pbRes,
			HashMap<ArrayList<String>, Double> abcRes) {
		HashMap<String, ArrayList<Double>> methodsCombined = new HashMap<>();// first slot peanut second slot abc
		//System.out.println(pbRes);
		// for peanut, each department gets allocated once, so its easier
		for (Product x : products) {
			String xName = x.getName();
			// second one is the allocated to
			for (ArrayList<String> s : pbRes.keySet()) {// searching thru the allocated from to key set
				ArrayList<Double> arr = new ArrayList<>();
				if (s.get(1).equals(xName)) {// if found allocated to product
					arr.add((x.getDirectCosts() + pbRes.get(s)) / x.getUnitsProduced());// add the per unit cost of the
																						// pb cost allocation0
					methodsCombined.put(xName, arr);
					break;// since in peanut, only a single allocation, so if found were done
				}
			}
		}
		// now abc
		for (Product x : products) {
			String xName = x.getName();
			// second one is the allocated to
			double totalAllocationFromMOH = 0;
			for (ArrayList<String> s : abcRes.keySet()) {
				if (s.get(1).equals(xName)) {// found allocated to
					totalAllocationFromMOH += abcRes.get(s);
				}
			}

			methodsCombined.get(xName).add((x.getDirectCosts() + totalAllocationFromMOH) / x.getUnitsProduced());
		}
		return methodsCombined;
	}

	private HashMap<ArrayList<String>, Double> calculateABC(StepsCollector stepsCollector) {
		HashMap<ArrayList<String>, Double> res = new HashMap<>();
		for (Department x : mohDepartments) {
			double total = getDriverToProducts(x);
			if (total == 0) {
				continue; // Skip this department if no drivers are found
			}
			double overheadRate = x.getInterdepartmentalCosts() / total;
			stepsCollector.addStep("overhead rate for department " + x.getName() + " is " + overheadRate);
			allocate(x, overheadRate, res);
		}
		return res;
	}

	private HashMap<ArrayList<String>, Double> calculatePeanut(Department singleDepartment,
			StepsCollector stepsCollector) {
		double total = getDriverToProducts(singleDepartment);

		if (total == 0) {
			return null;
		}

		double overheadRate;
		if (undefinedMode) {
			overheadRate = undefinedTotalMOH / undefinedTotalDriver;
			stepsCollector.addStep("overhead rate for the single department " + singleDepartment.getName() + "= "
					+ undefinedTotalMOH + " / " + undefinedTotalDriver + " = " + overheadRate);
			
		} else {
			overheadRate = totalMOH / total;
			stepsCollector.addStep("overhead rate for the single department " + singleDepartment.getName() + "= "
					+ totalMOH + " / " + total + " = " + overheadRate);

		}

		HashMap<ArrayList<String>, Double> res = new HashMap<>();
		allocate(singleDepartment, overheadRate, res);
	//	System.out.println(res);
		return res;
	}

	public void allocate(Department x, double rate, HashMap<ArrayList<String>, Double> res) {
		for (Product y : products) {
			// Handle potential null return from getUnitsProvided()
			Double unitsProvided = y.getUnitsProvided().get(x);
			double amountAllocatedToYfromX = rate * (unitsProvided != null ? unitsProvided : 0.0);

			// Add allocation to the result map
			ArrayList<String> arr = new ArrayList<String>();
			arr.add(x.getName());
			arr.add(y.getName());
			res.put(arr, amountAllocatedToYfromX);
		}
	}

	private double getDriverToProducts(Department singleDepartment) {
		double total = 0;
		for (Product x : products) {
			// Handle potential null values for the driver
			Double unitsProvided = x.getUnitsProvided().get(singleDepartment);
			total += (unitsProvided != null ? unitsProvided : 0.0);
		}
		return total;
	}

	public ArrayList<Department> getMohDepartments() {
		return mohDepartments;
	}

	public void setMohDepartments(ArrayList<Department> mohDepartments) {
		this.mohDepartments = mohDepartments;
	}

	public ArrayList<Product> getProducts() {
		return products;
	}

	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}

	public double getTotalMOH() {
		return totalMOH;
	}

	public void setTotalMOH(double totalMOH) {
		this.totalMOH = totalMOH;
	}

	public double getTotalProducts() {
		return totalProducts;
	}

	public void setTotalProducts(double totalProducts) {
		this.totalProducts = totalProducts;
	}

	public Department getSingleDepartment() {
		return singleDepartment;
	}

	public void setSingleDepartment(String s) {
		for (Department x : mohDepartments) {
			if (x.getName().equals(s)) {
				this.singleDepartment = x;
				return;
			}
		}
		this.singleDepartment = null;
		if (singleDepartment == null)
			throw new IllegalArgumentException();
	}

	public boolean isUndefinedMode() {
		return undefinedMode;
	}

	public void setUndefinedMode(boolean undefinedMode) {
		this.undefinedMode = undefinedMode;
	}

	public double getUndefinedTotalMOH() {
		return undefinedTotalMOH;
	}

	public void setUndefinedTotalMOH(double undefinedTotalMOH) {
		this.undefinedTotalMOH = undefinedTotalMOH;
	}

	public double getUndefinedTotalProducts() {
		return undefinedTotalProducts;
	}

	public void setUndefinedTotalProducts(double undefinedTotalProducts) {
		this.undefinedTotalProducts = undefinedTotalProducts;
	}

}
