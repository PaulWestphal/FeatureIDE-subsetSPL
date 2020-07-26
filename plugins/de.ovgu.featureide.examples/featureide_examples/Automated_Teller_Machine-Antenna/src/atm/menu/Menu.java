package atm.menu;

import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

import atm.ATM;

public class Menu {

	private ResourceBundle bundle;
	private ArrayList<MenuItem> menuitems = new ArrayList<MenuItem>();
	private Scanner scan;
	
	public Menu(Scanner scan) {
		this.scan = scan;
	}
	
	public boolean show() {
		if (menuitems.size() == 1) {
			menuitems.get(0).open();
		} else {
			for (int i = 0; i < menuitems.size(); i++) {
				System.out.print(i+1 + ": ");
				System.out.println(menuitems.get(i).getLabel());
			}
			
			while (!scan.hasNextInt()) {
				System.out.println(bundle.getObject("invalid_input"));
				scan.next();
			}
			int input = scan.nextInt();
			MenuItem selection = menuitems.get(input - 1);
			if (!selection.requiresAuthentication() || (selection.requiresAuthentication() && ATM.getInstance().authenticate())) {
				return selection.open();
			};
		}
		return true;
	}
	
	public void add(MenuItem item) {
		menuitems.add(item);
	}
}
