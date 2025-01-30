package accountingController;

import java.util.ArrayList;

import accountingModel.CostObject;
import accountingModel.InventoryCosting;
import accountingModel.MainModel;

public class InventoryCostingController {

	private InventoryCosting obj;

	public InventoryCostingController(double price, double unitsSold, double unitsProduced, double beginningInventory,
			double endingInventory) {
		obj = new InventoryCosting(price, unitsSold, unitsProduced, beginningInventory, endingInventory);

	}

	public boolean createCostObject(String name, double cost) {
		return obj.addCostObject(new CostObject(name, cost));// follow the method chain,
		// verfication in addCost object not createcostObject for flexibillity and not
		// double search
		// if cost object name not correct, its detected in the add cost object method
		// via classifiy cost object
	}

	public ArrayList<String> getSolution(String method) {
		StepsCollector collector = new StepsCollector();
		obj.calculate(method, collector);
		return collector.getSteps();
	}

	public String getCorrectedName(String name) {/// returns closest match (identical has priority)
		return MainModel.levenshteinDistance(name, obj.getAllCostObjects());
	}

}
