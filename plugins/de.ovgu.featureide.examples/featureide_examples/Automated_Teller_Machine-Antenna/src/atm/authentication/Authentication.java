package atm.authentication;

import java.util.ResourceBundle;

import atm.ATM;

public abstract class Authentication {
	protected ResourceBundle bundle;
	
	public Authentication() {
		bundle = ResourceBundle.getBundle("resources.MyClassBundle", ATM.getInstance().getCurrentLocale());
	}
	
	public boolean authenticate() {
		return false;
	}
}
