package pl.coderslab;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ScannerHelper {

	static Scanner scan = new Scanner(System.in);

	public static int nextInt(String message) {
		Scanner scan = new Scanner(System.in);

		if (message != null && !"".equals(message)) {// wstawiamy tego if aby nie wyświetlał pustej linii jeśli
														// użytkownik nic nie wpisze
			System.out.println(message);
		}
		while (!scan.hasNextInt()) {
			scan.nextLine();
			System.out.println("Nieprawidłowe dane. Podaj jeszcze raz:");

		}
		return scan.nextInt();
	}

	public static double nextDouble(String message) {

		if (message != null && !"".equals(message)) {
			System.out.println(message);
		}
		while (!scan.hasNextDouble()) {
			scan.next();
			System.out.print("Nieprawidłowe dane. Podaj jeszcze raz:");
		}
		return scan.nextDouble();
	}
	
	public static String nextString(String message) {
			if (message != null && !"".equals(message)) {
				System.out.println(message);
			}
			while (!scan.hasNextLine()) {
				scan.next();
				System.out.print("Nieprawidłowe dane. Podaj jeszcze raz:");
			}
			return scan.nextLine();
	}
	
	public static boolean yesNo(String message) {
		while (true) {
			String reply = ScannerHelper.nextString(message);
			if (reply.equals("t")) {
				return true;
			} else if (reply.equals("n")) {
				return false;
			} else {
				System.out.println("Odpowiedz [t/n]");
			}
		}
	}
}
