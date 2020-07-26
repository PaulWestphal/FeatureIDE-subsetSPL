// #if German
//@package resources;
//@
//@import java.util.ListResourceBundle;
//@
//@public class MyClassBundle_de extends ListResourceBundle {
//@
//@	@Override
//@	protected Object[][] getContents() {
//@		return contents;
//@	}
//@
//@	private Object[][] contents = {
//@			{ "welcome", "Herzlich Wilkommen, was m?chten Sie tun?" },
//@			{ "invalid_input", "Ung?ltige Eingabe, bitten versuchen Sie es noch einmal." },
//@			{ "exit", "Beenden " },
//@			
			// #if (English && German) || (German && French) || (English && French)
//@			{ "select_language", "Sprache w?hlen " },
			// #endif
//@			
			// #if Bank_Statement
//@			{ "balance_label", "Kontostand " },
//@			{ "balance_your_balance_is", "Ihr Kontostand ist " },
			// #if Print_Statement
//@			{ "balance_print", "Drucke Bankauszug ... " },
			// #endif
			// #endif
//@			
			// #if Deposit
//@			{ "deposit", "Geld einzahlen " },
//@			{ "deposit_accepted_1", "Ihre Einzahlung von " },
//@			{ "deposit_accepted_2", " wurde akzeptiert." },
			// #endif
			// #if Deposit_Cash
//@			{ "deposit_cash", "Bargeld" },
//@			{ "deposit_cash_how_much", "Bitte geben Sie Ihre Geldnoten ein... hinweis: irgendeine Zahl eingeben" },
			// #endif
			// #if Deposit_Cheques
//@			{ "deposit_cheques", "Scheck " },
//@			{ "deposit_accepted_cheque", "Ihre Scheckeinzahlung ?ber " },
			// #endif
//@			
			// #if Withdraw_Cash
//@			{ "withdraw", "Geld abheben " },
//@			{ "withdraw_how_much", "Bitte geben Sie ein wie viel Geld Sie abheben m?chten. " },
//@			{ "withdraw_insufficient_balance", "Nicht gen?gend Geld vorhanden. " },
//@			{ "withdraw_you_withdrew", "Sie haben abgehoben: " },
			// #endif
//@			
//@			// if Authentication
//@			{ "authenticate_success", "Authetifizierung erfolgreich!"},
//@			{ "authenticate_failed", "Authentifizierung fehlgeschlagen."},
			// #if PIN || Password
//@			{ "authenticate_try_again", "Bitte versuchen Sie es noch einmal." },
//@			{ "authenticate_tries_left", " Versuche ?brig." },
//@			// endif
			// #if Facial_Recognition
//@			{ "authenticate_facial_recognition", "Gesicht wird gescannt... "},
			// #elif Iris_Scan
//@			{ "authenticate_iris_scan", "Iris wird gescannt... "},
			// #elif PIN
//@			{ "authenticate_enter_pin", "Bitte geben Sie Ihre PIN ein... Hinweis: Ihre PIN ist: 1445" },
			// #elif Password
//@			{ "authenticate_enter_password", "Bitte geben Sie Ihr Passwort ein... Hinweis: Ihr Passwort ist: vL@6HdOm-" }	
			// #endif
			// #endif
//@	};
//@}
// #endif
