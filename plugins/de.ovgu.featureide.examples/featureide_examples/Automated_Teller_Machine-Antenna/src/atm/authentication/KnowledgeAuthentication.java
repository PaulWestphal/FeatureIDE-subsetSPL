// #if Password || PIN
//@package atm.authentication;
//@
//@import java.util.Scanner;
//@
//@public abstract class KnowledgeAuthentication extends Authentication{
//@	String PASSWORD;
//@	String instruction;
//@	private int tries;
//@	Scanner scan;
//@	
//@	public KnowledgeAuthentication(Scanner scan) {
//@		this.scan = scan;
//@	}
//@	
//@	public boolean authenticate() {
//@		tries = 3;
//@		System.out.println(instruction);
//@		scan.nextLine();
//@		String input = scan.nextLine();
//@		while (!input.equals(PASSWORD)) {
//@			tries--;
//@			System.out.println(bundle.getObject("authenticate_failed"));
//@			System.out.println(tries + (String)bundle.getObject("authenticate_tries_left") + (String)bundle.getObject("authenticate_try_again"));
//@			if (tries == 0) {
//@				return false;
//@			} else {
//@				input = scan.nextLine();
//@			}
//@		}
//@		return true;
//@	}
//@}
// #endif
