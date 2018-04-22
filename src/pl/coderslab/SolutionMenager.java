package pl.coderslab;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Scanner;

public class SolutionMenager {

	private static String dataBaseName = "warsztaty_02";

	public static void main(String[] args) {
		while (true) {
			try (Connection conn = MySQLHelper.getConnection(dataBaseName)) {
				Scanner scan = new Scanner(System.in);
				String option;

				System.out.println("|Wybierz jedną z opcji:\n"
					+ "|\tadd - przypisywanie zadań do użytkowników\n"
					+ "|\tview - przeglądanie rozwiązań danego użytkownika\n"
					+ "|\tquit - zakończenie programu");
				option = scan.next();
				if (option.equals("add")) {
					add();
				} else if (option.equals("view")) {
					view();
				} else if (option.equals("quit")) {
					System.out.println("Program zakończył działanie.");
					System.exit(0);
				} else {
					System.out.print("Nie wpisałeś poprawnej odpowiedzi. \n");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				System.out.println("Brak zadania w bazie o podanym numerze.");
			}
		}
	}

	/*
	 * add - wyświetli listę wszystkich użytkowników, odpyta o id, następnie
	 * wyświetli listę wszystkich zadań i odpyta o id, utworzy i zapisz obiekt typu
	 * Solution
	 */
	public static void add() {
		try (Connection conn = MySQLHelper.getConnection(dataBaseName)) {
			// wyświetli listę wszystkich użytkowników
			System.out.println(Arrays.toString(User.loadAllUsers(conn)));
			// podaj id użytkownika
			int userId = ScannerHelper.nextInt("Podaj numer użytkownika: ");
			// wyświetli listę wszystkich zadań
			Exercise.loadAllExercises(conn);
			// podaj numer zadania
			int exerciseId = ScannerHelper.nextInt("Podaj numer zadania: ");
			// utworzy obiekt typu Solution
			Solution newSolution = new Solution();
			// pobranie daty i czasu
			String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
			newSolution.setCreated(timeStamp);
			newSolution.setExerciseId(exerciseId);
			newSolution.setUsersId(userId);
			newSolution.saveToDB(conn);
		} catch (SQLException e) {
		}
	}

	public static void view() {
		try (Connection conn = MySQLHelper.getConnection(dataBaseName)) {
			int userId = ScannerHelper.nextInt("Podaj numer użytkownika: ");
			Solution.loadAllByUserId(conn, userId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}