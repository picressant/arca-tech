package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

	private static DatabaseConnection instance = null;
	
	private Connection currentConnection;

	public static DatabaseConnection getInstance() {
		if (instance == null) {
			instance = new DatabaseConnection();
			instance.connect();
		}

		return DatabaseConnection.instance;
	}

	public void connect() {	
		try {
			// db parameters
			String url = "jdbc:sqlite:D:\\Work\\exoTechnique\\Database\\arca.db";
			// create a connection to the database
			this.setCurrentConnection(DriverManager.getConnection(url));

			System.out.println("Connection to SQLite has been established.");

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public Connection getCurrentConnection() {
		return currentConnection;
	}

	public void setCurrentConnection(Connection currentConnection) {
		this.currentConnection = currentConnection;
	}
	
	public void disconnect() {
		try {
			this.getCurrentConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
