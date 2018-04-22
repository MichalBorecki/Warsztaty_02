package pl.coderslab;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class MySQLHelper {
	public static Connection getConnection(String dataBaseName) throws SQLException {
		String dataBaseConnection = String.format(
				"jdbc:mysql://localhost:3306/%s?useSSL=false&useUnicode=true&characterEncoding=utf-8", dataBaseName); // wklejony jest kod (useSSL=false&useUnicode=true&characterEncoding=utf-8) który koduje polskie znaki
		Connection con = DriverManager.getConnection(dataBaseConnection, "root", "coderslab");
		return con;
	}

	public static ResultSet executeQuery(Connection con, String sql, String... conditions) throws SQLException {
		PreparedStatement stm = con.prepareStatement(sql);
		for (int i = 1; i <= conditions.length; i++) {
			stm.setString(i, conditions[i - 1]);
		}
		ResultSet rs = stm.executeQuery();
		return rs;
	}

	public static void executeUpdate(Connection con, String sql, String... conditions) throws SQLException {
		PreparedStatement stm = con.prepareStatement(sql);
		for (int i = 1; i <= conditions.length; i++) {
			stm.setString(i, conditions[i - 1]);
		}
		stm.executeUpdate();
	}

	public static void printResultSet(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		while (rs.next()) {
			for (int i = 1; i <= meta.getColumnCount(); i++) {
				System.out.print(String.format("%s: %s, ", meta.getColumnName(i), rs.getString(i)));
			}
			System.out.println();
		}
	}

	/*
	 * wywołuje zapytania które są wpisane w jednen string przedzielone ':', arg:
	 * nazwa bazy danych i wspomniany string
	 */
	public static void executeUpdateToDataBase(String dataBaseName, String sqlQuerry) {
		try (Connection con = getConnection(dataBaseName)) {
			for (String string : sqlQuerry.split(";")) {
				executeUpdate(con, string);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
