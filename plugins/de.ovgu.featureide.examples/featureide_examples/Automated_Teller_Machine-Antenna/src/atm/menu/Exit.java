package atm.menu;

public class Exit extends MenuItem {
	public Exit() {
		requiresAuth = false;
		label = (String) bundle.getObject("exit");
	}
	
	public boolean open() {
		return false;
	}
}
