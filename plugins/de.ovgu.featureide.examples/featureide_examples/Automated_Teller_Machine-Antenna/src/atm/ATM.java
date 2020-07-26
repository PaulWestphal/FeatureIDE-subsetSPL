package atm;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

import atm.authentication.Authentication;

// #if Password
//@import atm.authentication.PasswordAuthentication;
// #elif PIN
//@import atm.authentication.PINAuthentication;
// #elif Facial_Recognition
//@import atm.authentication.FacialRecognition;
// #elif Iris_Scan
//@import atm.authentication.IrisScan;
// #endif

// #if Bank_Statement
import atm.menu.CurrentBalance;
//#endif
//#if (English && German) || (German && French) || (English && French)
//@import atm.menu.LanguageMenu;
// #endif
import atm.menu.Menu;

// #if Withdraw_Cash
//@import atm.menu.Withdraw;
// #endif
// #if Deposit
import atm.menu.Deposit;
// #endif
import atm.menu.Exit;


public class ATM {
	
	private static ATM atm = null;
	
	private Locale currentLocale;
	
	private Menu mainMenu;
	
	private Authentication auth;
	
	private Account acc;
	
	private ResourceBundle bundle;
	
	private Scanner scan = new Scanner(System.in);

	
	private ATM() {

	}
	
	private void initialize(boolean fullReset) {
		// if there are multiple languages, default to english
		if (fullReset) {
			// #if English
//@			currentLocale = Locale.ENGLISH;
			// #elif German
//@			currentLocale = Locale.GERMAN;
			// #elif French
//@			currentLocale = Locale.FRENCH;
			// #endif
		}

		// #if Password
//@		auth = new PasswordAuthentication(scan);
		// #elif PIN
//@		auth = new PINAuthentication(scan);
		// #elif Iris_Scan
//@		auth = new IrisScan();
		// #elif Facial_Recognition
//@		auth = new FacialRecognition();
		// #endif
		
		acc = new Account();
	}
	
	public static ATM getInstance() {
		if (atm == null) {
			atm = new ATM();
		}
		return atm;
	}

	public static void main(String[] args) {
		getInstance();
		atm.initialize(true);
		atm.buildMainMenu();
		atm.showMainMenu();
	}

	private void buildMainMenu() {
		bundle = ResourceBundle.getBundle("resources.MyClassBundle", currentLocale);
		System.out.println(bundle.getObject("welcome") );
		
		mainMenu = new Menu(scan);
		// #if Bank_Statement
		mainMenu.add(new CurrentBalance(acc));
		// #endif
		
		// If there is more than one language available
		// #if (English && German) || (German && French) || (English && French)
//@		mainMenu.add(new LanguageMenu(scan));
		// #endif
		
		// #if Withdraw_Cash
//@		mainMenu.add(new Withdraw(scan, acc));
		// #endif
		
		// #if Deposit
		mainMenu.add(new Deposit(scan));
		// #endif
		
		mainMenu.add(new Exit());
	}

	private void showMainMenu() {
		if(mainMenu.show())	showMainMenu();
	}
	
	// #if (English && German) || (German && French) || (English && French)
//@	public void setLanguage(Locale locale) {
//@		if (!currentLocale.equals(locale)) {
//@			currentLocale = locale;
//@			initialize(false);
//@			buildMainMenu();
//@		}
//@	}
	// #endif
	
	public Locale getCurrentLocale() {
		return currentLocale;
	}

	public boolean authenticate() {
		return auth.authenticate();
	}
	
	// #if Deposit
	public void deposit(double number) {
		acc.deposit(number);
	}
	// #endif
}
