// #if Withdraw_Cash
//@package atm.menu;
//@
//@import java.util.ArrayList;
//@import java.util.Scanner;
//@
//@import atm.Account;
//@
//@public class Withdraw extends MenuItem {
//@	Scanner scan;
//@	Account acc;
	// #if EUR
//@	private String CURRENCY_SYMBOL = "\u20ac";
	// #elif GBP
//@	final private String CURRENCY_SYMBOL = "\u00a3";
	// #endif
//@	
//@	public Withdraw(Scanner scan, Account acc) {
//@		label = (String) bundle.getObject("withdraw");
//@		this.scan = scan;
//@		this.acc = acc;
//@	}
//@	
//@	public boolean open() {
//@		System.out.println(bundle.getObject("withdraw_how_much"));
//@		
//@		while (!scan.hasNextDouble()) {
//@			System.out.println(bundle.getObject("invalid_input"));
//@			scan.next();
//@		}
//@		double input = scan.nextDouble();
//@		input =  ((double)((int)(input * 100.0))) / 100.0;
//@		
//@		if (input >= 0.00) {
//@			if (input > acc.getBalance()) {
//@				System.out.println(bundle.getObject("withdraw_insufficient_balance"));
//@			} else {
//@				double leftover = calculateDenomination(input);
//@				acc.withdraw(input - leftover);
//@			}
//@		}
//@		return true;
//@	}
//@	
//@	private double calculateDenomination(double amount) {
//@		double leftoverAmount = amount;
//@		
//@		ArrayList<Double> availableDenominations = new ArrayList<Double>();
		// #if EUR
//@		availableDenominations.add(500.0);
//@		availableDenominations.add(200.0);
		// #endif
		// #if EUR || GBP
//@		availableDenominations.add(50.0);
//@		availableDenominations.add(20.0);
//@		availableDenominations.add(10.0);
//@		availableDenominations.add(5.0);
		// #endif
		// #if EUR && Coins
//@		availableDenominations.add(2.0);
//@		availableDenominations.add(1.0);
//@		availableDenominations.add(0.5);
//@		availableDenominations.add(0.2);
//@		availableDenominations.add(0.1);
//@		availableDenominations.add(0.05);
//@		availableDenominations.add(0.02);
//@		availableDenominations.add(0.01);
		// #endif
//@		
//@		ArrayList<Integer> numberEach = new ArrayList<Integer>();
//@		for (int i = 0; i < availableDenominations.size(); i++) {
//@			numberEach.add(0);
//@		}
//@		
//@		for (int i = 0; i < availableDenominations.size(); i++) {
//@			while(leftoverAmount - availableDenominations.get(i) >= 0) {
//@				leftoverAmount -= availableDenominations.get(i);
//@				numberEach.set(i, numberEach.get(i) + 1);
//@			}
//@		}
//@		
//@		System.out.println(bundle.getObject("withdraw_you_withdrew"));
//@		for (int i = 0; i < availableDenominations.size(); i++) {
//@			if (numberEach.get(i) != 0) {
//@				System.out.println(numberEach.get(i).toString() + " x " + availableDenominations.get(i).toString() + CURRENCY_SYMBOL);
//@			}
//@		}
//@		
//@		return leftoverAmount;
//@	}
//@}
// #endif
