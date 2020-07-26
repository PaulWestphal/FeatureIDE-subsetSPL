// #if Deposit_Cash
package atm.menu;

import java.util.Scanner;

import atm.ATM;

public class DepositCash extends MenuItem {
	Scanner scan;
	// #if EUR
//@	private String CURRENCY_SYMBOL = "\u20ac";
	// #elif GBP
	final private String CURRENCY_SYMBOL = "\u00a3";
	// #endif
	
	public DepositCash(Scanner scan) {
		this.scan = scan;
		label = (String) bundle.getObject("deposit_cash");
		requiresAuth = false;
	}
	
	public boolean requiresAuthentication() {
		return false;
	}
	
	public boolean open() {
		System.out.println((String) bundle.getObject("deposit_cash_how_much"));
		
		while (!scan.hasNextDouble()) {
			System.out.println(bundle.getObject("invalid_input"));
			scan.next();
		}
		double input = scan.nextDouble();
		input =  ((double)((int)(input * 100.0))) / 100.0;
		
		if (input >= 0.00) {
			System.out.print(bundle.getObject("deposit_accepted_1"));
			System.out.print(input);
			System.out.print(CURRENCY_SYMBOL);
			System.out.println(bundle.getObject("deposit_accepted_2"));
			ATM.getInstance().deposit(input);
		}
		return true;
	}
}
// #endif
