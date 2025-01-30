package accountingModel;

import java.util.ArrayList;
import java.util.HashMap;
//a method to allocate a cost of a department to something (either a department or a product ), and provide a dictionary for the allocation

@FunctionalInterface
public interface Allocatable {
	 void allocate(Department x, double rate, HashMap<ArrayList<String>, Double> EachSupportToMainAllocation);
}
