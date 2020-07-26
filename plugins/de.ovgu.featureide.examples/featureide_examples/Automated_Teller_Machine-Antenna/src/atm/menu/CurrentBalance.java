// #if Bank_Statement
package atm.menu;
import atm.Account;

public class CurrentBalance extends MenuItem {
	
	Account account;
	
	public boolean open() {
		System.out.println(bundle.getObject("balance_your_balance_is") + account.toString());
		// #if Print_Statement
		System.out.println(bundle.getObject("balance_print"));
		// #endif
		return true;
	}
	public CurrentBalance(Account account) {
		label = (String) bundle.getObject("balance_label");
		this.account = account;
	}
}
// #endif
