package pl.coderslab;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.mindrot.jbcrypt.BCrypt;

public class Group {

	public Group() {
		super();
	}

	public Group(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	private int id;
	String name;

	public void saveToDB(Connection conn) throws SQLException {
		try {
			if (this.id == 0) {
				String sql = "INSERT INTO user_group (name) VALUES (?)";
				String generatedColumns[] = { "id" };
				PreparedStatement preparedStatement;
				preparedStatement = conn.prepareStatement(sql, generatedColumns);
				preparedStatement.setString(1, this.name);
				preparedStatement.executeUpdate();
				ResultSet rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					this.id = rs.getInt(1);
				}
			} else {
				String sql = "UPDATE user_group SET name=? WHERE id=?";
				PreparedStatement preparedStatement;
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1, this.name);
				preparedStatement.setInt(3, this.id);
				preparedStatement.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println("Niepoprawne dane.");
		}
	}

	public void delete(Connection conn) throws SQLException {
		if (this.id != 0) {
			String sql = "DELETE FROM user_group WHERE id=?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, this.id);
			preparedStatement.executeUpdate();
			System.out.println("^^ Zadanie zostało usunięte.\n");
			this.id = 0;
		}
	}

	static public Group loadById(Connection conn, int id) throws SQLException {
		String sql = "SELECT * FROM user_group WHERE id=?";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			Group loadedGroup = new Group();
			loadedGroup.id = resultSet.getInt("id");
			loadedGroup.name = resultSet.getString("name");
			System.out.println(loadedGroup);
			return loadedGroup;
		}
		return null;
	}

	static public Group[] loadAllGroups(Connection conn) throws SQLException {
		ArrayList<Group> groups = new ArrayList<Group>();
		String sql = "SELECT * FROM user_group";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();

		while (resultSet.next()) {
			Group loadedGroup = new Group();
			loadedGroup.id = resultSet.getInt("id");
			loadedGroup.name = resultSet.getString("name");
		}
		Group[] uArray = new Group[groups.size()];
		uArray = groups.toArray(uArray);
		return uArray;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "-------------------------------------------------\n"
			+ "| Informacje o grupie:\n"
			+ "| numer grupy: \t" + id + "\n"
			+ "| nazwa grupy: \t" + name + "\n"
			+ "-------------------------------------------------";
	}
}
