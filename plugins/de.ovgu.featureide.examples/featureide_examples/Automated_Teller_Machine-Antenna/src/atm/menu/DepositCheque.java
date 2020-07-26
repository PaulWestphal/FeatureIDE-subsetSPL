// #if Deposit_Cheques
package atm.menu;

import atm.ATM;

public class DepositCheque extends MenuItem {
	
	public DepositCheque() {
		label = (String) bundle.getObject("deposit_cheques");
		requiresAuth = false;
	}
	
	public boolean open() {
		System.out.println(bundle.getObject("deposit_accepted_cheque"));
		System.out.print(new Double(50.0));
		// #if GBP
		System.out.print("\u00a3");
		// #elif EUR
//@		System.out.print("\u20ac");
		// #endif
		System.out.println(bundle.getObject("deposit_accepted_2"));
		ATM.getInstance().deposit(new Double(50.0));
		return true;
	}
	
}
// #endif
