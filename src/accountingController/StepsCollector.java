package accountingController;
//part of the contorller package since it get the answer from model, get fromatted and sent to view 
import java.util.ArrayList;

public class StepsCollector {
	private ArrayList<String> steps;

	public StepsCollector() {
		this.steps = new ArrayList<>();
	}

	public void addStep(String step) {
		steps.add(step);
	}

	public ArrayList<String> getSteps() {
		return steps;
	}

	public void clear() {
		steps=new ArrayList<>();//cleaned off by gc 
	}
}
