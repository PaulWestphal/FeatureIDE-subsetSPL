package atm.menu;

import java.util.Scanner;

public class Deposit extends MenuItem {
	private Menu menu;
	
	public Deposit(Scanner scan) {
		menu = new Menu(scan);
		
		label = (String) bundle.getObject("deposit");
		// #if Deposit_Cash
		menu.add(new DepositCash(scan));
		// #endif
		// #if Deposit_Cheques
		menu.add(new DepositCheque());
		// #endif
	}
	
	public boolean open() {
		menu.show();
		return true;
	}
}
