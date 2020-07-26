package atm.menu;

import java.util.ResourceBundle;

import atm.ATM;

public abstract class MenuItem {
	ResourceBundle bundle = ResourceBundle.getBundle("resources.MyClassBundle", ATM.getInstance().getCurrentLocale());
	protected String label;
	boolean requiresAuth = true;
	
	public boolean open() {
		return true;
	}
	
	public String getLabel() {
		return label;
	}
	
	public boolean requiresAuthentication() {
		return requiresAuth;
	}
}
