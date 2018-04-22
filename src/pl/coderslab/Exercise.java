package pl.coderslab;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import org.mindrot.jbcrypt.BCrypt;

public class Exercise {

	public Exercise() {
		super();
	}

	public Exercise(int id, String description, String title) {
		super();
		this.id = id;
		this.description = description;
		this.title = title;
	}

	private int id;
	String description;
	String title;

	public void saveToDB(Connection conn) throws SQLException {
		try {
			if (this.id == 0) {
				String sql = "INSERT INTO exercise (description, title) VALUES (?, ?)";
				String generatedColumns[] = { "id" };
				PreparedStatement preparedStatement;
				preparedStatement = conn.prepareStatement(sql, generatedColumns);
				preparedStatement.setString(1, this.description);
				preparedStatement.setString(2, this.title);
				preparedStatement.executeUpdate();
				ResultSet rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					this.id = rs.getInt(1);
				}
			} else {
				String sql = "UPDATE exercise SET description=?, title=? WHERE id=?";
				PreparedStatement preparedStatement;
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1, this.description);
				preparedStatement.setString(2, this.title);
				preparedStatement.setInt(3, this.id);
				preparedStatement.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println("Niepoprawne dane.");
		}
	}

	public void delete(Connection conn) throws SQLException {
		if (this.id != 0) {
			String sql = "DELETE FROM exercise WHERE id=?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, this.id);
			preparedStatement.executeUpdate();
			System.out.println("^^ Zadanie zostało usunięte.\n");
			this.id = 0;
		}
	}

	static public Exercise loadById (Connection conn, int id) {
		try {
			String sql = "SELECT * FROM exercise WHERE id=?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				Exercise loadedExercise = new Exercise();
				loadedExercise.id = resultSet.getInt("id");
				loadedExercise.description = resultSet.getString("description");
				loadedExercise.title = resultSet.getString("title");
				System.out.println(loadedExercise);
				return loadedExercise;
			}

		} catch (SQLException e) {
			System.out.println("Podaj poprawny numer zadania");
		}
		return null;
	}

	static public void loadAllExercises(Connection conn) throws SQLException {
		ArrayList<Exercise> exercises = new ArrayList<Exercise>();
		String sql = "SELECT * FROM exercise";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();

		while (resultSet.next()) {
			Exercise loadedExercise = new Exercise();
			loadedExercise.id = resultSet.getInt("id");
			loadedExercise.description = resultSet.getString("description");
			loadedExercise.title = resultSet.getString("title");
			exercises.add(loadedExercise);
		}
		Exercise[] uArray = new Exercise[exercises.size()];
		uArray = exercises.toArray(uArray);
		System.out.println("Lista zadań: ");
		System.out.println(Arrays.toString(uArray));
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "-------------------------------------------------\n"
			+ "| Informacje o zadaniu:\n"
			+ "| tytuł: \t" + title + "\n"
			+ "| opis: \t" + description + "\n"
			+ "=================================================";
	}
}
