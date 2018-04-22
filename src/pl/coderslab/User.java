package pl.coderslab;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import org.mindrot.jbcrypt.BCrypt;

public class User {

	public User() {
	}

	public User(int id, String username, String email, String password) {
		this.username = username;
		this.email = email;
		setPassword(password);
	}

	private int id;
	private String username;
	private String email;
	private String password;
	private int userGroupId;

	public void saveToDB(Connection conn) {
		try {
		if (this.id == 0) {
			String sql = "INSERT INTO users(username, email, password, user_group_id) VALUES (?, ?, ?, ?)";
			String generatedColumns[] = { "id" };
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql, generatedColumns);
			preparedStatement.setString(1, this.username);
			preparedStatement.setString(2, this.email);
			preparedStatement.setString(3, this.password);
			preparedStatement.setInt(4, this.userGroupId);
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			if (rs.next()) {
				this.id = rs.getInt(1);// getInt odczytuje z pierwszej kolumny inta
			}
		} else { // Modyfikacja obiektu
			String sql = "UPDATE users SET username=?, email=?, password=?, user_group_id=? WHERE id=?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, this.username); // setString ustawia nam każdy pytajnik danym tekstem
			preparedStatement.setString(2, this.email);
			preparedStatement.setString(3, this.password);
			preparedStatement.setInt(4, this.userGroupId);
			preparedStatement.setInt(5, this.id);
			preparedStatement.executeUpdate();
		}
		} catch (SQLException e) {
			System.out.println("Niepoprawne dane.");
		}
	}

	/*
	 * usuwanie obiektu, id różne od zera - sprawdzamy czy użtkownik jest w bazie,
	 * jeśli ma zero tzn że jeszcze baza nie została zupdatowana i nie musimy tego
	 * usera usuwać
	 */
	public void delete(Connection conn) throws SQLException {
		if (this.id != 0) {
			String sql = "DELETE FROM users WHERE id=?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, this.id);
			preparedStatement.executeUpdate();
			System.out.println("^^ Użytkownik został skasowany.\n");
			this.id = 0;
		}
	}

	static public User loadUserById(Connection conn, int id) throws SQLException { 
		String sql = "SELECT * FROM users WHERE id=?";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {// jest if zamiast while ponieważ potrzebujemy pobrać tylko jednego użytkownika
			User loadedUser = new User();
			loadedUser.id = resultSet.getInt("id");
			loadedUser.username = resultSet.getString("username");
			loadedUser.password = resultSet.getString("password");
			loadedUser.email = resultSet.getString("email");
			loadedUser.userGroupId = resultSet.getInt("user_group_id");
			System.out.println(loadedUser);
			return loadedUser;
		}
		return null;
	}

	static public User[] loadAllUsers(Connection conn) throws SQLException {
		ArrayList<User> users = new ArrayList<User>();
		String sql = "SELECT * FROM users";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();

		while (resultSet.next()) {
			User loadedUser = new User();
			loadedUser.id = resultSet.getInt("id");
			loadedUser.username = resultSet.getString("username");
			loadedUser.password = resultSet.getString("password");
			loadedUser.email = resultSet.getString("email");
			loadedUser.userGroupId = resultSet.getInt("user_group_id");
			users.add(loadedUser);
		}
		User[] uArray = new User[users.size()]; // tworzenie nowej tablicy
		uArray = users.toArray(uArray);// zamiana listy do tablicy
		return checkIfExist(users, "Brak użytkowników w bazie danych");
	}

	static public User[] loadAllByGrupId(Connection conn, int groupId) throws SQLException {
		ArrayList<User> users = new ArrayList<User>();
		String sql = "SELECT * FROM users WHERE user_group_id=?";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, groupId);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			User loadedUser = new User();
			loadedUser.id = resultSet.getInt("id");
			loadedUser.username = resultSet.getString("username");
			loadedUser.password = resultSet.getString("password");
			loadedUser.email = resultSet.getString("email");
			loadedUser.userGroupId = resultSet.getInt("user_group_id");
			users.add(loadedUser);
		}
		User[] uArray = new User[users.size()]; // tworzenie nowej tablicy
		uArray = users.toArray(uArray);// zamiana listy do tablicy
		return checkIfExist(users, "Brak takiej grupy w bazie danych");
	}
	
	public static User[] checkIfExist(ArrayList<User> users, String message) {
		User[] uArray = new User[users.size()];
		if (users.size() == 0) {
			System.out.println(message);
			return null;
		}
		uArray = users.toArray(uArray);
		return uArray;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = BCrypt.hashpw(password, BCrypt.gensalt());
	}

	public int getId() {
		return id;
	}

	public int getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(int userGroupId) {
		this.userGroupId = userGroupId;
	}

	@Override
	public String toString() {
		return "-------------------------------------------------\n"
			+ "| Dane użytkownika:\n"
			+ "| numer: \t" + id + "\n"
			+ "| nazwa: \t" + username + "\n"
			+ "| email: \t" + email + "\n"
			+ "| hasło: \t" + password + "\n"
			+ "| nr grupy: \t" + userGroupId + "\n"
			+ "-------------------------------------------------";
	}
}
