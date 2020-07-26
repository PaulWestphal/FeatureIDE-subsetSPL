// #if English
//@package resources;
//@
//@import java.util.ListResourceBundle;
//@
//@public class MyClassBundle_en extends ListResourceBundle {
//@
//@	@Override
//@	protected Object[][] getContents() {
//@		return contents;
//@	}
//@
//@	private Object[][] contents = {
//@			{ "welcome", "Welcome! What do you want to do?" },
//@			{ "invalid_input", "Invalid input! Please try again!" },
//@			{ "exit", "Exit" },
//@			
			// #if (English && German) || (German && French) || (English && French)
//@			{ "select_language", "Select language" },
			// #endif
//@			
			// #if Bank_Statement
//@			{ "balance_label", "Account Balance" },
//@			{ "balance_your_balance_is", "Your balance is " },
			// #if Print_Statement
//@			{ "balance_print", "Printing bank statement ... " },
			// #endif
			// #endif
//@			
			// #if Deposit
//@			{ "deposit", "Deposit money " },
//@			{ "deposit_accepted_1", "Your deposit of " },
//@			{ "deposit_accepted_2", " has been accepted." },
			// #endif
			// #if Deposit_Cash
//@			{ "deposit_cash", "Cash" },
//@			{ "deposit_cash_how_much", "Please deposit your bank notes... hint: enter any number" },
			// #endif
			// #if Deposit_Cheques
//@			{ "deposit_cheques", "Cheque" },
//@			{ "deposit_accepted_cheque", "Your cheque deposit of " },
			// #endif
//@			
			// #if Withdraw_Cash
//@			{ "withdraw", "Withdraw money " },
//@			{ "withdraw_how_much", "Please enter the amount that you want to withdraw. " },
//@			{ "withdraw_insufficient_balance", "Account balance is insufficient. " },
//@			{ "withdraw_you_withdrew", "You withdrew " },
			// #endif
//@			
//@			// if Authentication
//@			{ "authenticate_success", "Authentication successful!"},
//@			{ "authenticate_failed", "Authentication failed."},
			// #if PIN || Password
//@			{ "authenticate_try_again", "Please try again." },
//@			{ "authenticate_tries_left", " tries left." },
//@			// endif
			// #if Facial_Recognition
//@			{ "authenticate_facial_recognition", "Scanning face..."},
			// #elif Iris_Scan
//@			{ "authenticate_iris_scan", "Scanning iris..."},
			// #elif PIN
//@			{ "authenticate_enter_pin", "Please enter your PIN... hint: your PIN is: 1445" },
			// #elif Password
//@			{ "authenticate_enter_password", "Please enter your password... hint: your password is: vL@6HdOm-" }	
			// #endif
			// #endif
//@	};
//@}
// #endif
