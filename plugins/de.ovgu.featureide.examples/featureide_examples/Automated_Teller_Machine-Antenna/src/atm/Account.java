package atm;

import java.util.concurrent.ThreadLocalRandom;

public class Account {
	// #if EUR
//@	final private double CURRENCY_FACTOR = 1.0;
//@	private String CURRENCY_SYMBOL = "\u20ac";
	// #elif GBP
	final private double CURRENCY_FACTOR = 0.90;
	final private String CURRENCY_SYMBOL = "\u00a3";
	// #endif
	
	private double balance;
	
	public Account() {
		double random = ThreadLocalRandom.current().nextDouble(0.05, 8000);
		balance = random * CURRENCY_FACTOR;
		balance =  ((double)((int)(balance * 100.0))) / 100.0;
	}
	
	public double getBalance() {
		return balance;
	}
	
	public String toString() {
		return new String(balance + CURRENCY_SYMBOL);
		
	}
	
	// #if Deposit
	public void deposit(double amount) {
		balance+=amount;
	}
	// #endif
	
	// #if Withdraw_Cash
//@	public void withdraw(double amount) {
//@		balance-=amount;
//@	}
	// #endif
}
