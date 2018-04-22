package pl.coderslab;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class ExerciseMenager {

	private static String dataBaseName = "warsztaty_02";

	public static void main(String[] args) {
		while (true) {
			try (Connection conn = MySQLHelper.getConnection(dataBaseName)) {
				Scanner scan = new Scanner(System.in);
				String option;

				System.out.println("|Wybierz jedną z opcji:\n"
					+ "|\tadd - dodanie zadania\n"
					+ "|\tedit - edycja zadania\n"
					+ "|\tdelete - usunięcie zadania\n"
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
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				System.out.println("Brak zadania w bazie o podanym numerze.");
			}
		}
	}

	public static void add() {
		try (Connection conn = MySQLHelper.getConnection(dataBaseName)) {
			Exercise newExercise = new Exercise();
			String description = ScannerHelper.nextString("Podaj opis: ");
			String title = ScannerHelper.nextString("Podaj tytuł: ");
			newExercise.setDescription(description);
			newExercise.setTitle(title);
			newExercise.saveToDB(conn);
			newExercise = Exercise.loadById(conn, newExercise.getId());
			newExercise.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void edit() {
		Exercise exercise = new Exercise();
		try (Connection conn = MySQLHelper.getConnection(dataBaseName)) {
			int exerciseId = ScannerHelper.nextInt("Podaj numer zadania: ");
			exercise = Exercise.loadById(conn, exerciseId);
			exercise.toString();
			String description;
			String title;
			String message = "\". Czy chcesz edytować te dane [t/n]?";
			while (true) {
				System.out.print("Opis zadania: \"" + exercise.getDescription());
				if (ScannerHelper.yesNo(message)) {
					description = ScannerHelper.nextString("Podaj nowy opis zadania lub wpisz poprzedni: ");
				} else {
					description = exercise.getDescription();
				}
				System.out.print("Tytuł: \"" + exercise.getTitle());
				if (ScannerHelper.yesNo(message)) {
					title = ScannerHelper.nextString("Podaj nowy tytuł: ");
				} else {
					title = exercise.getTitle();
				}
				System.out.print(
					String.format("Nowe dane: opis zadania: %s, tytuł: %s", description, title));
				if (!ScannerHelper.yesNo(". \nCzy chcesz ponownie edytować dane [t/n]?")) {
					break;
				}
			}
			exercise.setDescription(description);
			exercise.setTitle(title);
			exercise.saveToDB(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void delete() {
		String message = "Czy napewno chcesz usunąć to zadanie [t/n]?";
		Exercise exercise = new Exercise();
		try (Connection conn = MySQLHelper.getConnection(dataBaseName)) {
			int exerciseId = ScannerHelper.nextInt("Podaj numer zadania: ");
			exercise = Exercise.loadById(conn, exerciseId);
			exercise.toString();
			if (ScannerHelper.yesNo(message)) {
				exercise.delete(conn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}