package accountingModel;

//a try to implement inheritance, would be way more useful in case of more concepts added

public class AccountingObject {
	private String name;


	public boolean equals(AccountingObject two) {
		return two != null && this.name.equals(two.getName()); // Handle null check
	}

	public String getName() {
		return name;
	}
}
