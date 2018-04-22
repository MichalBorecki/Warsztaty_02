package pl.coderslab;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Scanner;

public class UserPanel {

	private static String dataBaseName = "warsztaty_02";
	private static int id;

	public static void main(String[] args) {
		while (true) {
			try (Connection conn = MySQLHelper.getConnection(dataBaseName)) {
				User user = new User();
				Scanner scan = new Scanner(System.in);
				String option;

				while (true) {
					id = ScannerHelper.nextInt("|Podaj swój numer identyfikacyjny lub wpisz '0' jeśli chesz zakończyć: ");
					if (User.loadUserById(conn, id) != null) {
						System.out.println();
						System.out.println("|Wybierz jedną z opcji:\n"
							+ "|  add - dodaj rozwiązanie\n"
							+ "|  quit - zakończ");
						option = scan.next();
						if (option.equals("add")) {
							add();
						} else if (option.equals("quit")) {
							System.out.println("Program zakończył działanie.");
							System.exit(0);
						} else {
							System.out.print("Nie wpisałeś poprawnej odpowiedzi. ");
						}
					} else if (id == 0){
						System.out.println("Program zakończył działanie.");
						System.exit(0);
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				System.out.println("Niepoprawne dane");
			}
		}

	}

	public static void add() {
		try (Connection conn = MySQLHelper.getConnection(dataBaseName)) {
			
			// wyświetli listę zadań do których użytkownik nie dodał jeszcze rozwiązania
			Solution newSolution = new Solution();
			int exerciseNo;
			while(true) {
				System.out.println("Zadania do których brakuje rozwiązań:\n" + Arrays.toString(Solution.loadAllByUserId(conn, id)));
				
				exerciseNo = ScannerHelper.nextInt("Do którego numeru zadania chcesz dodać rozwiązanie: ");
				
				newSolution = Solution.loadByExerciseIdAndUserId(conn, id, exerciseNo);
				
				if(newSolution.getDescription() == null) { 
					break;
				} else {
					System.out.println("To zadanie posiada już rozwiązanie! \n");
				}
			}
			Exercise exercise = new Exercise();
			exercise = Exercise.loadById(conn, exerciseNo);
			
			// pobranie rozwiązanie
			String description = ScannerHelper.nextString("Podaj rozwiązanie: ");
			// pobranie daty i czasu
			String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
			newSolution.setUpdated(timeStamp);
			newSolution.setDescription(description);
			newSolution.saveToDB(conn);
			newSolution = Solution.loadById(conn, newSolution.getId());
			newSolution.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}