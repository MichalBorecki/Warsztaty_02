package pl.coderslab;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class GroupMenager {

	private static String dataBaseName = "warsztaty_02";

	public static void main(String[] args) {
		try (Connection conn = MySQLHelper.getConnection(dataBaseName)) {
			Scanner scan = new Scanner(System.in);
			String option;
			while (true) {
				System.out.println("|Wybierz jedną z opcji:\n"
					+ "|\tadd - dodanie grupy\n"
					+ "|\tedit - edycja grupy\n"
					+ "|\tdelete - usunięcie grupy\n"
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
			System.out.println("Brak grupy w bazie o podanym numerze.");
		}
	}

	public static void add() {
		try (Connection conn = MySQLHelper.getConnection(dataBaseName)) {
			Group newGroup = new Group();
			String name = ScannerHelper.nextString("Podaj nazwę grupy: ");
			newGroup.setName(name);
			newGroup.saveToDB(conn);
			newGroup = Group.loadById(conn, newGroup.getId());
			newGroup.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void edit() {
		Group group = new Group();
		try (Connection conn = MySQLHelper.getConnection(dataBaseName)) {
			int groupId = ScannerHelper.nextInt("Podaj numer grupy: ");
			group = Group.loadById(conn, groupId);
			group.toString();
			String name;
			String message = "\". Czy chcesz edytować te dane [t/n]?";
			while (true) {
				System.out.print("Nazwa grupy: \"" + group.getName());
				if (ScannerHelper.yesNo(message)) {
					name = ScannerHelper.nextString("Podaj nową nazwę grupy: ");
				} else {
					name = group.getName();
				}
				System.out.print(
					String.format("Nowe dane: nazwa grupy: %s", name));
				if (!ScannerHelper.yesNo(". \nCzy chcesz ponownie edytować dane [t/n]?")) {
					break;
				}
			}
			group.setName(name);
			group.saveToDB(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void delete() {
		String message = "Czy napewno chcesz usunąć tą grupę [t/n]?";
		Group group = new Group();
		try (Connection conn = MySQLHelper.getConnection(dataBaseName)) {
			int groupId = ScannerHelper.nextInt("Podaj numer grupy: ");
			group = Group.loadById(conn, groupId);
			group.toString();
			if (ScannerHelper.yesNo(message)) {
				group.delete(conn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}