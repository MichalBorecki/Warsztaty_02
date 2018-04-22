package pl.coderslab;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordTest {

	public static void main(String[] args) {
		String password = "abc";
		// Hash a password for the first time
		String hashed = BCrypt.hashpw(password, BCrypt.gensalt());

		System.out.println("Orginalne hasło: " + password);
		System.out.println("Hash hasła: " + hashed);

		// gensalt's log_rounds parameter determines the complexity
		// the work factor is 2**log_rounds, and the default is 10
		hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));

		System.out.println(hashed.length()); // sprawdzamy jak długie jest zahashowane hasło by stworzyć int o dł 60 w
												// tabeli

		String candidate = "abc";
		// Check that an unencrypted password matches one that has
		// previously been hashed
		if (BCrypt.checkpw(candidate, hashed)) {// checpw sprawdza czy wprowadzone hasło zgadza się z tym zahashowanym
												// hashed
			System.out.println("It matches");
		} else {
			System.out.println("It does not match");
		}
	}

}
