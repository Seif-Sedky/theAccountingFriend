package accountingModel;

import java.util.HashMap;

public class CostObjectClassifier {
//remaining : continue the dictionary 

	private HashMap<String, String> classificationRules;

	public CostObjectClassifier() {
		classificationRules = new HashMap<>();

		// Define rules for specific phrases

		// PROBABLY THEIR IS A WAY BETTER WAY TO IMPLEMENT THE IDEA OF CLASSIFICATION,
		// BUT THIS IS JUST A SIMPLE PROJECT, AND INTEGRATION IS PRETTY EASY TO BE DONE
		// LATER

		// Factory Costs (Product Costs)
		classificationRules.put("factory rent", "product,indirect,fixed");
		classificationRules.put("factory utilities", "product,indirect,variable");
		classificationRules.put("depreciation on factory equipment", "product,indirect,fixed");
		classificationRules.put("factory supervisor salary", "product,indirect,fixed");
		classificationRules.put("plant rent", "product,indirect,fixed");
		classificationRules.put("plant utilities", "product,indirect,variable");
		classificationRules.put("depreciation on plant equipment", "product,indirect,fixed");
		classificationRules.put("plant supervisor salary", "product,indirect,fixed");
		classificationRules.put("indirect materials", "product,indirect,variable");
		classificationRules.put("indirect material", "product,indirect,variable");
		classificationRules.put("indirect labor", "product,indirect,variable");
		classificationRules.put("direct materials", "product,direct,variable");
		classificationRules.put("direct material", "product,direct,variable");
		classificationRules.put("direct labor", "product,direct,variable");
		classificationRules.put("factory maintenance", "product,indirect,variable");
		classificationRules.put("factory electricity", "product,indirect,variable");
		classificationRules.put("factory insurance", "product,indirect,fixed");
		classificationRules.put("plant maintenance", "product,indirect,variable");
		classificationRules.put("plant electricity", "product,indirect,variable");
		classificationRules.put("plant insurance", "product,indirect,fixed");
		classificationRules.put("fixed moh", "product,indirect,fixed");
		classificationRules.put("fixed manufacturing overhead", "product,indirect,fixed");
		classificationRules.put("variable moh", "product,indirect,variable");
		classificationRules.put("variable manufacturing overhead", "product,indirect,variable");

		// Office Costs (Period Costs)
		classificationRules.put("office rent", "period,-,fixed");
		classificationRules.put("office utilities", "period,-,variable");
		classificationRules.put("depreciation on office equipment", "period,-,fixed");
		classificationRules.put("administrative salaries", "period,-,fixed");
		classificationRules.put("advertising expenses", "period,-,fixed");
		classificationRules.put("variable marketing expenses", "period,-,variable");
		classificationRules.put("fixed marketing expenses", "period,-,fixed");
		classificationRules.put("variable marketing costs", "period,-,variable");
		classificationRules.put("fixed marketing costs", "period,-,fixed");
		classificationRules.put("fixed administrative expenses", "period,-,fixed");
		classificationRules.put("administrative expenses", "period,-,fixed");
		classificationRules.put("administrative costs", "period,-,fixed");
		classificationRules.put("sales commissions", "period,-,variable");
		classificationRules.put("legal fees", "period,-,fixed");
		classificationRules.put("research and development costs", "period,-,fixed");
		classificationRules.put("customer service expenses", "period,-,variable");

		// Selling Costs (Period Costs)
		classificationRules.put("sales department salaries", "period,-,fixed");
		classificationRules.put("shipping costs", "period,-,variable");
		classificationRules.put("marketing costs", "period,-,fixed");
		classificationRules.put("sales team travel expenses", "period,-,variable");
		classificationRules.put("customer discounts", "period,-,variable");

		// Miscellaneous Costs
		classificationRules.put("insurance for factory", "product,indirect,fixed");
		classificationRules.put("insurance for office", "period,-,fixed");
		classificationRules.put("property taxes on factory", "product,indirect,fixed");
		classificationRules.put("property taxes on office", "period,-,fixed");
		classificationRules.put("temporary labor", "product,indirect,variable");
		classificationRules.put("repairs on factory equipment", "product,indirect,variable");
		classificationRules.put("training expenses for factory workers", "product,indirect,fixed");
		classificationRules.put("utilities for office", "period,-,variable");

		// Miscellaneous Revenue Adjustments
		classificationRules.put("scrap sales", "product,-,variable");
		classificationRules.put("returns and allowances", "period,-,variable");
	}

	public HashMap<String, String> getClassificationRules() {
		return classificationRules;
	}

	public void setClassificationRules(HashMap<String, String> classificationRules) {
		this.classificationRules = classificationRules;
	}

}
