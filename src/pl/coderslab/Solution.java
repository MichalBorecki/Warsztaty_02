package pl.coderslab;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import org.mindrot.jbcrypt.BCrypt;

public class Solution {

	public Solution() {
		super();
	}

	public Solution(int id, String created, String updated, String description, int exerciseId, int usersId) {
		super();
		this.id = id;
		this.created = created;
		this.updated = updated;
		this.description = description;
		this.exerciseId = exerciseId;
		this.usersId = usersId;
	}

	private int id;
	String created;
	String updated;
	String description;
	int exerciseId;
	int usersId;

	public void saveToDB(Connection conn) {
		try {
			if (this.id == 0) {
				String sql = "INSERT INTO solution (created, updated, description, exercise_id, users_id) VALUES (?, ?, ?, ?, ?)";
				String generatedColumns[] = { "id" };
				PreparedStatement preparedStatement;
				preparedStatement = conn.prepareStatement(sql, generatedColumns);
				preparedStatement.setString(1, this.created);
				preparedStatement.setString(2, this.updated);
				preparedStatement.setString(3, this.description);
				preparedStatement.setInt(4, this.exerciseId);
				preparedStatement.setInt(5, this.usersId);
				preparedStatement.executeUpdate();
				ResultSet rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					this.id = rs.getInt(1);
				}
			} else {
				String sql = "UPDATE solution SET created=?, updated=?, description=?, exercise_id=?, users_id=? WHERE id=?";
				PreparedStatement preparedStatement;
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1, this.created);
				preparedStatement.setString(2, this.updated);
				preparedStatement.setString(3, this.description);
				preparedStatement.setInt(4, this.exerciseId);
				preparedStatement.setInt(5, this.usersId);
				preparedStatement.setInt(6, this.id);
				preparedStatement.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println("Niepoprawne dane");
		}
	}

	public void delete(Connection conn) throws SQLException {
		if (this.id != 0) {
			String sql = "DELETE FROM solution WHERE id=?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, this.id);
			preparedStatement.executeUpdate();
			System.out.println("^^ Rozwiązanie zostało usunięte.\n");
			this.id = 0;
		}
	}

	static public Solution loadById(Connection conn, int id) throws SQLException {
		String sql = "SELECT * FROM solution WHERE id=?";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			Solution loadedSolution = new Solution();
			loadedSolution.id = resultSet.getInt("id");
			loadedSolution.created = resultSet.getString("created");
			loadedSolution.updated = resultSet.getString("updated");
			loadedSolution.description = resultSet.getString("description");
			loadedSolution.exerciseId = resultSet.getInt("exercise_id");
			loadedSolution.usersId = resultSet.getInt("users_id");
			System.out.println(loadedSolution);
			return loadedSolution;
		}
		return null;
	}
	
	static public Solution loadByExerciseIdAndUserId(Connection conn, int userId, int exerciseId) throws SQLException {
		String sql = "SELECT * FROM solution JOIN exercise ON exercise.id = solution.exercise_id WHERE solution.users_id=? AND solution.exercise_id=?";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, userId);
		preparedStatement.setInt(2, exerciseId);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			Solution loadedSolution = new Solution();
			loadedSolution.created = resultSet.getString("created");
			loadedSolution.updated = resultSet.getString("updated");
			loadedSolution.description = resultSet.getString("description");
			loadedSolution.exerciseId = resultSet.getInt("exercise_id");
			loadedSolution.usersId = resultSet.getInt("users_id");
			return loadedSolution;
		}
		return null;
	}

	static public Solution[] loadAllSolutions(Connection conn) throws SQLException {
		ArrayList<Solution> solutions = new ArrayList<Solution>();
		String sql = "SELECT * FROM solution";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();

		while (resultSet.next()) {
			Solution loadedSolution = new Solution();
			loadedSolution.id = resultSet.getInt("id");
			loadedSolution.created = resultSet.getString("created");
			loadedSolution.updated = resultSet.getString("updated");
			loadedSolution.description = resultSet.getString("description");
			loadedSolution.exerciseId = resultSet.getInt("exercise_id");
			loadedSolution.usersId = resultSet.getInt("users_id");
			solutions.add(loadedSolution);
		}
		Solution[] uArray = new Solution[solutions.size()];
		uArray = solutions.toArray(uArray);
		return checkIfExist(solutions, "Brak rozwiązań w bazie");
	}

	static public Solution[] loadAllByUserId(Connection conn, int id) throws SQLException {
		ArrayList<Solution> solutions = new ArrayList<Solution>();
		String sql = "SELECT * FROM solution WHERE users_id=? AND description IS NULL;";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			Solution loadedSolution = new Solution();
			loadedSolution.id = resultSet.getInt("id");
			loadedSolution.created = resultSet.getString("created");
			loadedSolution.updated = resultSet.getString("updated");
			loadedSolution.description = resultSet.getString("description");
			loadedSolution.exerciseId = resultSet.getInt("exercise_id");
			loadedSolution.usersId = resultSet.getInt("users_id");
			solutions.add(loadedSolution);
		}
		return checkIfExist(solutions, "Brak takiego użytkownika w bazie danych");
	}
	
	static public Solution[] loadAllEmptyByUserId(Connection conn, int id) throws SQLException {
		ArrayList<Solution> solutions = new ArrayList<Solution>();
		String sql = "SELECT * FROM solution WHERE updated=NULL AND WHERE users_id=?";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			Solution loadedSolution = new Solution();
			loadedSolution.id = resultSet.getInt("id");
			loadedSolution.created = resultSet.getString("created");
			loadedSolution.updated = resultSet.getString("updated");
			loadedSolution.description = resultSet.getString("description");
			loadedSolution.exerciseId = resultSet.getInt("exercise_id");
			loadedSolution.usersId = resultSet.getInt("users_id");
			solutions.add(loadedSolution);
		}
		return checkIfExist(solutions, "Brak takiego użytkownika w bazie danych");
	}

	static public Solution[] loadAllByExerciseId(Connection conn, int exerciseId) throws SQLException {
		ArrayList<Solution> solutions = new ArrayList<Solution>();
		String sql = "SELECT * FROM solution WHERE exercise_id=?";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, exerciseId);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			Solution loadedSolution = new Solution();
			loadedSolution.id = resultSet.getInt("id");
			loadedSolution.created = resultSet.getString("created");
			loadedSolution.updated = resultSet.getString("updated");
			loadedSolution.description = resultSet.getString("description");
			loadedSolution.exerciseId = resultSet.getInt("exercise_id");
			loadedSolution.usersId = resultSet.getInt("users_id");
			solutions.add(loadedSolution);
		}
		Solution[] uArray = new Solution[solutions.size()];
		uArray = solutions.toArray(uArray);
		return checkIfExist(solutions, "Brak takiego numeru rozwiązania");
	}

	public static Solution[] checkIfExist(ArrayList<Solution> solutions, String message) {
		Solution[] uArray = new Solution[solutions.size()];
		if (solutions.size() == 0) {
			System.out.println(message);
			return null;
		}
		uArray = solutions.toArray(uArray);
		return uArray;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getExerciseId() {
		return exerciseId;
	}

	public void setExerciseId(int exerciseId) {
		this.exerciseId = exerciseId;
	}

	public int getUsersId() {
		return usersId;
	}

	public void setUsersId(int usersId) {
		this.usersId = usersId;
	}

	@Override
	public String toString() {
		return "\n-------------------------------------------------\n"
			+ "| Numer zadania: \t" + exerciseId + "\n"
			+ "| data utworzenia: \t" + created + "\n"
			+ "| data motyfikacji: \t" + updated + "\n"
			+ "| rozwiązanie: \t\t" + description + "\n"
			+ "-------------------------------------------------\n";
	}
}
