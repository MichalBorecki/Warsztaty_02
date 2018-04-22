package pl.coderslab;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class UserMenager {

	private static String dataBaseName = "warsztaty_02";

	public static void main(String[] args) {
		try (Connection conn = MySQLHelper.getConnection(dataBaseName)) {
			Scanner scan = new Scanner(System.in);
			String option;
			while (true) {
				System.out.println("|Wybierz jedną z opcji:\n"
					+ "|\tadd - dodanie użytkownika\n"
					+ "|\tedit - edycja użytkownika\n"
					+ "|\tdelete - usunięcie użytkownika\n"
					+ "|\tquit - zakończenie programu");
				option = scan.next();
				if (option.equals("add")) {
					add();
				} else if (option.equals("edit")) {
					edit();
				} else if (option.equals("delete")) {
					delete();
				} else if (option.equals("quit")) {
					System.out.println("Program zakończył działanie.");
					System.exit(0);
				} else {
					System.out.print("Nie wpisałeś poprawnej odpowiedzi. ");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("Brak użytkownika w bazie o podanym nr id");
		}
	}

	public static void add() {
		try (Connection conn = MySQLHelper.getConnection(dataBaseName)) {
			User newUser = new User();
			String username = ScannerHelper.nextString("Podaj nazwę użytkownika: ");
			String email = ScannerHelper.nextString("Podaj email: ");
			String password = ScannerHelper.nextString("Podaj hasło: ");
			int userGroupId = ScannerHelper.nextInt("Podaj nr grupy: ");
			newUser.setUsername(username);
			newUser.setEmail(email);
			newUser.setPassword(password);
			newUser.setUserGroupId(userGroupId);
			newUser.saveToDB(conn);
			newUser = User.loadUserById(conn, newUser.getId());
			newUser.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void edit() {
		User user = new User();
		try (Connection conn = MySQLHelper.getConnection(dataBaseName)) {
			int userId = ScannerHelper.nextInt("Podaj id użytkownika: ");
			user = User.loadUserById(conn, userId);
			user.toString();
			String username;
			String email;
			String password;
			int userGroupId;
			String message = "\". Czy chcesz edytować te dane [t/n]?";
			while (true) {
				System.out.print("Nazwa użytkownika: \"" + user.getUsername());
				if (ScannerHelper.yesNo(message)) {
					username = ScannerHelper.nextString("Podaj nową nazwę użytkownika lub wpisz poprzednią: ");
				} else {
					username = user.getUsername();
				}
				System.out.print("Email: \"" + user.getEmail());
				if (ScannerHelper.yesNo(message)) {
					email = ScannerHelper.nextString("Podaj nowy adres email: ");
				} else {
					email = user.getEmail();
				}
				System.out.print("Hasło: \"" + user.getPassword());
				if (ScannerHelper.yesNo(message)) {
					password = ScannerHelper.nextString("Podaj nowe hasło: ");
				} else {
					password = user.getPassword();
				}
				System.out.print("Numer grupy: \"" + user.getUserGroupId());
				if (ScannerHelper.yesNo(message)) {
					userGroupId = ScannerHelper.nextInt("Podaj nowy numer grupy: ");
				} else {
					userGroupId = user.getUserGroupId();
				}
				System.out.print(
					String.format("Nowe dane: nazwa używkownika: %s, email: %s, hasło: %s, numer grupy: %s", username,
						email, password, userGroupId));
				if (!ScannerHelper.yesNo(". \nCzy chcesz ponownie edytować dane [t/n]?")) {
					break;
				}
			}
			user.setUsername(username);
			user.setEmail(email);
			user.setPassword(password);
			user.setUserGroupId(userGroupId);
			user.saveToDB(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void delete() {
		String message = "Czy napewno chcesz usunąć tego użytkownika [t/n]?";
		User user = new User();
		try (Connection conn = MySQLHelper.getConnection(dataBaseName)) {
			int userId = ScannerHelper.nextInt("Podaj id użytkownika: ");
			user = User.loadUserById(conn, userId);
			user.toString();
			if (ScannerHelper.yesNo(message)) {
				user.delete(conn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}