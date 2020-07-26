// #if (English && German) || (German && French) || (English && French)
//@
//@package atm.menu;
//@
//@import java.util.Scanner;
//@
//@public class LanguageMenu extends MenuItem {
//@	private Menu menu;
//@	
//@	public LanguageMenu(Scanner scan) {
//@		label = (String) bundle.getObject("select_language");
//@		
//@		menu = new Menu(scan);
//@		
		// #if German
//@		menu.add(new German());
		// #endif
		// #if English
//@		menu.add(new English());
		// #endif
		// #if French
//@		menu.add(new French());
		// #endif
//@		requiresAuth = false;
//@	}
//@	
//@	public boolean open() {
//@		menu.show();
//@		return true;
//@	}
//@}
//@
//#endif
