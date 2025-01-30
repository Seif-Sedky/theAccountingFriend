package accountingModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import accountingController.StepsCollector;

// tips here: we always use budgeted,
public class SupportDepartmentsCostAllocation implements Allocatable {
	public enum AllocationMethod {
		DIRECT, STEP_DOWN, RECIPROCAL
	}

	private ArrayList<Department> mainDepartments;
	private ArrayList<Department> supportDepartments;
	private int supportDepartmentsNumber;

	public SupportDepartmentsCostAllocation() {
		this.mainDepartments = new ArrayList<>();
		this.supportDepartments = new ArrayList<>();
	}

	public void addDepartment(Department e) {
		if (e.isSupporting()) {
			supportDepartments.add(e);
			supportDepartmentsNumber++;
		} else {
			mainDepartments.add(e);
		}
	}

	public HashMap<ArrayList<String>, Double> calculate(AllocationMethod method, StepsCollector collector) {
		collector.addStep("Supporting: " + supportDepartments);
		collector.addStep("Main: " + supportDepartments);

		switch (method) {
		case DIRECT:
			return this.calculateDirect(collector);
		case STEP_DOWN:
			return this.calculateStepDown(collector);
		case RECIPROCAL:
			return this.calculateReciprocal(collector);
		default:
			return null;
		}
	}

	private HashMap<ArrayList<String>, Double> calculateStepDown(StepsCollector collector) {

		// Retrieve the support departments
		Department dep1 = supportDepartments.get(0);
		Department dep2 = supportDepartments.get(1);

		// Calculate the total costs for each department
		Double dep1ProvidedUnits = getDriverToSupport(dep1);
		Double dep2ProvidedUnits = getDriverToSupport(dep2);
		dep1ProvidedUnits = (dep1ProvidedUnits != null) ? dep1ProvidedUnits : 0.0;
		dep2ProvidedUnits = (dep2ProvidedUnits != null) ? dep2ProvidedUnits : 0.0;

		double totalDep1 = getDriverToMain(dep1) + dep1ProvidedUnits;
		double totalDep2 = getDriverToMain(dep2) + dep2ProvidedUnits;
		collector.addStep("Get the total units provided by each support department");
		collector.addStep(dep1.getName() + "= " + totalDep1 + ", " + dep2.getName() + "= " + totalDep2);

		double dep1Ratio = dep1ProvidedUnits / totalDep1;
		double dep2Ratio = dep2ProvidedUnits / totalDep2;

		// Determine which department allocates first based on their allocation ratios
		Department bigger, smaller;
		if (dep1Ratio > dep2Ratio) {
			bigger = dep1;
			smaller = dep2;
		} else {
			bigger = dep2;
			smaller = dep1;
		}

		collector.addStep("Next, Determine which department allocates first based on their allocation ratios");
		collector.addStep(dep1.getName() + " ratio = " + dep1Ratio + ", " + dep2.getName() + " ratio = " + dep2Ratio
				+ " so big department is " + bigger.getName());

		// Calculate budgeted rates for cost allocation
		double budgetedRateForMax = bigger.getInterdepartmentalCosts()
				/ (getDriverToMain(bigger) + getDriverToSupport(bigger));
		double newCostForMin = smaller.getInterdepartmentalCosts() + (budgetedRateForMax * getDriverToSupport(bigger));// only
																														// one
																														// other
																														// dep
		double budgetedRateForMin = newCostForMin / getDriverToMain(smaller);

		collector.addStep(
				"Next, get budgeted rate for big department, and allocate the costs to the small department and after getting the new cost get it's budgeted rate");
		collector.addStep("budgeted rate for big department = " + budgetedRateForMax + ", " + "new cost of smaller = "
				+ newCostForMin + " rate of smaller = " + budgetedRateForMin);

		// Prepare the map to store the allocation results
		HashMap<ArrayList<String>, Double> allocationResult = new HashMap<>();

		// Allocate costs to the main departments
		allocate(bigger, budgetedRateForMax, allocationResult);
		allocate(smaller, budgetedRateForMin, allocationResult);

		collector.addStep(
				"Finally, continue allocating to main departments for big using it's budgeted rate, and allocate small department costs to main using it's budgeted rate");
		return allocationResult;
	}

	private HashMap<ArrayList<String>, Double> calculateDirect(StepsCollector collector) {
		HashMap<ArrayList<String>, Double> EachSupportToMainAllocation = new HashMap<>();
		for (Department x : supportDepartments) {
			Double TotaldriverAllocatedToMain = this.getDriverToMain(x);
			if (TotaldriverAllocatedToMain != 0) {
				double budgetedRate = x.getInterdepartmentalCosts() / TotaldriverAllocatedToMain;
				collector.addStep("Budgeted rate for department" + x.getName() + " = " + budgetedRate
						+ ", total units provided to main departments = " + TotaldriverAllocatedToMain);
				allocate(x, budgetedRate, EachSupportToMainAllocation);
			}
		}
		return EachSupportToMainAllocation;

	}
	// Reconstruct the equation to 100k=marketing - 2.5%accounting etc

	private HashMap<ArrayList<String>, Double> calculateReciprocal(StepsCollector collector) {
		// Initialize the matrix and the augmented right-hand side (RHS) vector
		double[][] mat = new double[supportDepartmentsNumber][supportDepartmentsNumber];
		double augRHS[] = new double[supportDepartmentsNumber];

		for (int i = 0; i < supportDepartmentsNumber; i++) { // Creating the matrix
			Department iDep = supportDepartments.get(i);
			double total = this.getDriverToMain(iDep) + this.getDriverToSupport(iDep);

			// Validate total to prevent division by zero

			for (int j = 0; j < supportDepartmentsNumber; j++) {
				if (i == j) {
					mat[i][j] = 1; // Diagonal elements are set to 1
				} else {
					// Handle potential null returns from getUnitsProvided()
					Double unitsProvided = supportDepartments.get(j).getUnitsProvided().get(iDep);
					mat[i][j] = -(unitsProvided != null ? unitsProvided / total : 0.0); // Default to 0 if null
				}
			}

			// Assign interdepartmental costs to the augmented RHS vector
			augRHS[i] = iDep.getInterdepartmentalCosts();
		}

		// Solve the system of linear equations to find the reciprocated costs
		double[] completeReciprocatedCosts = this.solveMatrix(mat, augRHS);
		collector.addStep("complete reciprocated cost for " + supportDepartments + " is"
				+ Arrays.toString(completeReciprocatedCosts) + " respectivley");
		// Perform allocation to main departments using the new reciprocated costs
		HashMap<ArrayList<String>, Double> EachSupportToMainAllocation = new HashMap<>();
		for (int i = 0; i < supportDepartmentsNumber; i++) {
			Department x = supportDepartments.get(i);
			double total = this.getDriverToMain(x) + this.getDriverToSupport(x);

			double budgetedRate = completeReciprocatedCosts[i] / total;
			allocate(x, budgetedRate, EachSupportToMainAllocation);
		}

		return EachSupportToMainAllocation;
	}

	public void allocate(Department x, double rate, HashMap<ArrayList<String>, Double> EachSupportToMainAllocation) {
		// Allocate costs to main departments using the budgeted rate
		
		//TODO : ROUND TO NEAREST 2 DECIMALS TO AVOID DIFFERENCES IN ASNWERS
		for (Department y : mainDepartments) {

			Double unitsProvided = y.getUnitsProvided().get(x);

			double amountAllocatedToYfromX = rate * (unitsProvided != null ? unitsProvided : 0.0);

			// Add allocation to the result map
			ArrayList<String> arr = new ArrayList<String>();
			arr.add(x.getName());
			arr.add(y.getName());
			EachSupportToMainAllocation.put(arr, amountAllocatedToYfromX);
		}
	}

	private double getDriverToMain(Department x) {
		double total = 0;

		for (Department m : mainDepartments) {
			// Handle potential null return from getUnitsProvided()
			Double unitsProvided = m.getUnitsProvided().get(x);
			total += (unitsProvided != null ? unitsProvided : 0.0);
		}

		return total;
	}

	private double getDriverToSupport(Department x) {
		double total = 0;

		for (Department s : supportDepartments) {
			if (s != x) {
				Double unitsProvided = s.getUnitsProvided().get(x);
				total += (unitsProvided != null ? unitsProvided : 0.0);
			}
		}

		return total;
	}

	private double[] solveMatrix(double[][] matrix, double[] rhs) {// assuming main diagonal is always one
		int n = matrix.length;
		double[] result = new double[n];

		// Gaussian elimination
		for (int i = 0; i < n; i++) {
			// Make the elements below the diagonal 0
			for (int k = i + 1; k < n; k++) {
				double factor = matrix[k][i];
				for (int j = 0; j < n; j++) {
					matrix[k][j] -= factor * matrix[i][j];
				}
				rhs[k] -= factor * rhs[i];
			}
		}

		// Back substitution
		for (int i = n - 1; i >= 0; i--) {
			result[i] = rhs[i];
			for (int j = i + 1; j < n; j++) {
				result[i] -= matrix[i][j] * result[j];
			}
		}

		return result;
	}

	public int getSupportDepartmentsNumber() {
		return supportDepartmentsNumber;
	}

	public void setSupportDepartmentsNumber(int supportDepartmentsNumber) {
		this.supportDepartmentsNumber = supportDepartmentsNumber;
	}

	public ArrayList<Department> getMainDepartments() {// returns a copy
		ArrayList<Department> arr = new ArrayList<>();
		arr.addAll(mainDepartments);
		return arr;
	}

	public ArrayList<Department> getSupportDepartments() {// returns a copy
		ArrayList<Department> arr = new ArrayList<>();
		arr.addAll(supportDepartments);
		return arr;
	}
}
