package accountingModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import accountingController.StepsCollector;

//remaning: steps
public class InventoryCosting {

	private double unitsSold;
	private double price;
	private double unitsProduced;
	private double beginningUnits;
	private double endingUnits;

	
	//IDEA: you can make it as a set, oveerride methods of set, and if the user enters the same cost object twice
	//it means that he wants to change it idk something like that 
	private Set<String> allCostObjects = new HashSet<>();
	
	private ArrayList<CostObject> periodCostObjects = new ArrayList<>();
	private ArrayList<CostObject> productCostObjects = new ArrayList<>();
	private ArrayList<CostObject> fixedCostObjects = new ArrayList<>();
	private ArrayList<CostObject> variableCostObjects = new ArrayList<>();
	private ArrayList<CostObject> directCostObjects = new ArrayList<>();
	private ArrayList<CostObject> inDirectCostObjects = new ArrayList<>();

	public InventoryCosting(double price, double unitsSold, double unitsProduced, double beginningUnits,
			double endingUnits) {
		super();
		this.unitsSold = unitsSold;
		this.price = price;
		this.unitsProduced = unitsProduced;
		this.beginningUnits = beginningUnits;
		this.endingUnits = endingUnits;
	}

	public boolean addCostObject(CostObject e) {
		boolean found = e.classifyCostObject();// first classify it
		if (!found)
			return false;
		
		allCostObjects.add(e.getName());
		
		if (e.isProduct()) {
			productCostObjects.add(e);
			if (e.isDirect()) {
				directCostObjects.add(e);
			} else {
				inDirectCostObjects.add(e);
			}
		} else
			periodCostObjects.add(e);

		if (e.isVariable())
			variableCostObjects.add(e);
		else
			fixedCostObjects.add(e);
		return true;

	}

	public void calculate(String method, StepsCollector collector) {
		collector.addStep("Revenue = units sold x price = " + price * unitsSold);
		// bug here was that input from gui was with capital letter, so you needed to
		// ignore
		// case, shows how much enum is beneficial
		if (method.equalsIgnoreCase("absorption")) {
			calculateAbsorption(collector);
		} else if (method.equalsIgnoreCase("variable")) {
			calculateVariable(collector);
		} else {
			calculateThroughput(collector);
		}

	}

	private void calculateAbsorption(StepsCollector stepsCollector) {
		// first we have to get the per unit of the fixed moh
		double totalRatePerUnit = 0;
		for (CostObject x : productCostObjects) {
			if (!x.isDirect() && !x.isVariable()) {// fixed cost, cost driver in bigs
				double fixedMOHPerUnit = x.getCostDriver() / unitsProduced;
				totalRatePerUnit += fixedMOHPerUnit;
			} else {
				totalRatePerUnit += x.getCostDriver();
			}
		}
	
		double cogs = getCOGS(totalRatePerUnit);
		stepsCollector.addStep("COGS = (DM+DL+VMOH+(FMOH / units produced)) x units Sold = " + cogs);

		double grossMargin = getMargin(cogs);
		stepsCollector.addStep("Gross margin = revenue - COGS = " + grossMargin);

		double totalPeriodCosts = 0;
		for (CostObject x : periodCostObjects) {
			if (x.isVariable()) {
				totalPeriodCosts += x.getCostDriver() * unitsSold;
			} else
				totalPeriodCosts += x.getCostDriver();
		}
		double netIncome = grossMargin - totalPeriodCosts;
		stepsCollector.addStep("Net income for absorption = gross Margin - total period costs = " + netIncome);

	}

	private void calculateVariable(StepsCollector stepsCollector) {
		double totalRatePerUnit = 0;
		double totalPeriodVariable = 0;
		for (CostObject x : productCostObjects) {
			if (x.isVariable()) {// fixed cost, cost driver in bigs
				totalRatePerUnit += x.getCostDriver();
			}
		}
		for (CostObject x : periodCostObjects) {
			if (x.isVariable()) {// fixed cost, cost driver in bigs
				totalPeriodVariable += x.getCostDriver();
			}
		}
		double cogs = getCOGS(totalRatePerUnit);
		stepsCollector.addStep("COGS = (DM+DL+VMOH) x units sold = " + cogs);

		double contributionMargin = getMargin(cogs + (totalPeriodVariable * unitsSold));// since variable period is
																						// sales

		stepsCollector
				.addStep("Contribution margin = revenue - (COGS + variable period costs) = " + contributionMargin);
		double totalFixed = 0;
		for (CostObject x : fixedCostObjects) {
			totalFixed += x.getCostDriver();
		}
		double netIncome = contributionMargin - totalFixed;
		stepsCollector.addStep("Net income =  contribution margin - total fixed costs = " + netIncome);

	}

	private void calculateThroughput(StepsCollector stepsCollector) {
		double directMaterial = 0;
		double totalOther = 0;

		for (CostObject x : productCostObjects) {
			if (x.getName().toLowerCase().equals("direct material")) {
				directMaterial += x.getCostDriver();
			} else {
				if (x.isVariable())
					totalOther += x.getCostDriver() * unitsProduced;
				else {
					totalOther += x.getCostDriver();
				}
			}
		}
		double cogs = getCOGS(directMaterial);
		stepsCollector.addStep("COGS for throughput = DM x units sold = " + cogs);

		double throughputMargin = getMargin(cogs);
		stepsCollector.addStep("Throughput margin = revenue - COGS = " + throughputMargin);

		for (CostObject x : periodCostObjects) {
			if (x.isVariable())
				totalOther += x.getCostDriver() * unitsSold;// marketing/commisions
			else {
				totalOther += x.getCostDriver();
			}
		}

		double netIncome = throughputMargin - totalOther;
		stepsCollector.addStep("Net income =  throughput margin - total other costs = " + netIncome);

	}

	private double getCOGS(double rate) {
		return rate * unitsSold;
	}

	private double getMargin(double cogs) {
		return (unitsSold * price) - cogs;
	}

	public void reconcilliation() {
//to be done 
	}

	public double getSales() {
		return unitsSold;
	}

	public void setSales(int sales) {
		this.unitsSold = sales;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getUnitsProduced() {
		return unitsProduced;
	}

	public void setUnitsProduced(double unitsProduced) {
		this.unitsProduced = unitsProduced;
	}

	public double getBeginningUnits() {
		return beginningUnits;
	}

	public void setBeginningUnits(double beginningUnits) {
		this.beginningUnits = beginningUnits;
	}

	public double getEndingUnits() {
		return endingUnits;
	}

	public void setEndingUnits(double endingUnits) {
		this.endingUnits = endingUnits;
	}
	public Set<String> getAllCostObjects() {
		return allCostObjects;
	}


}
