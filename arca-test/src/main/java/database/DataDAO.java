package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import models.Data;
import models.FilterAttribute;

public class DataDAO {
		
	public void saveData(Data iData) {
		String sql = "INSERT INTO ARCA_DATA VALUES(?,?,?)";

		try (PreparedStatement lStatement = DatabaseConnection.getInstance().getCurrentConnection()
				.prepareStatement(sql)) {
			lStatement.setLong(1, iData.getDate().getTime());
			lStatement.setInt(2, iData.getValue());
			lStatement.setString(3, iData.getOrigin());
			lStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void clear() {
		String sql = "Delete From ARCA_DATA";

		try (PreparedStatement lStatement = DatabaseConnection.getInstance().getCurrentConnection()
				.prepareStatement(sql)) {
			lStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public Map<String, Integer> getValueByOrigin(FilterAttribute iFilter) {
		HashMap<String, Integer> lValues = new HashMap<String, Integer>();

		String lSQL = "select origin, sum(val) countVal from ARCA_DATA ";
		
		if (iFilter.startingDate != null && iFilter.endingDate != null) {				
			lSQL += " where TimeStamp >= " + iFilter.startingDate.getTime() + " and TimeStamp <= " + iFilter.endingDate.getTime();
		}		
				
		lSQL += " group by origin";
		try (PreparedStatement lStatement = DatabaseConnection.getInstance().getCurrentConnection()
				.prepareStatement(lSQL)) {
			ResultSet lRes = lStatement.executeQuery();
			while (lRes.next())
				lValues.put(lRes.getString("origin"), lRes.getInt("countVal"));
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return lValues;
	}
	
	public Map<Date, Integer> getSumByDay(int iYear) {
		TreeMap<Date, Integer> lValues = new TreeMap<Date, Integer>();
		SimpleDateFormat lDateFormatter = new SimpleDateFormat("dd-MM-yyyy");
		
		String lSQL = "select DATE_TEXT, SUM_VAL from (select strftime('%d-%m-%Y', TimeStamp/1000,'unixepoch') DATE_TEXT, sum(Val) SUM_VAL from ARCA_DATA group by DATE_TEXT order by TimeStamp ) where DATE_TEXT like '%-" + iYear + "'";
		try (PreparedStatement lStatement = DatabaseConnection.getInstance().getCurrentConnection()
				.prepareStatement(lSQL)) {
			ResultSet lRes = lStatement.executeQuery();
			while (lRes.next()) {				
				lValues.put((lDateFormatter.parse(lRes.getString("DATE_TEXT"))), lRes.getInt("SUM_VAL"));
			}
		} catch (SQLException | ParseException e) {
			System.out.println(e.getMessage());
		}
		
		return lValues;
		
	}
	
	public Map<String, Integer> getSumBy(FilterAttribute iFilter) {
		TreeMap<String, Integer> lValues = new TreeMap<String, Integer>();		
		
		String lSQL = this._buildChartSQL(iFilter);
		try (PreparedStatement lStatement = DatabaseConnection.getInstance().getCurrentConnection()
				.prepareStatement(lSQL)) {
			ResultSet lRes = lStatement.executeQuery();
			while (lRes.next()) {				
				lValues.put(lRes.getString("MY_DATE"), lRes.getInt("SUM_VAL"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return lValues;
		
	}
	
	private String _buildChartSQL(FilterAttribute iFilter) {
		String lVar = "";
		String lSousSelect = "";
		
		switch(iFilter.granularity) {
		case 1 :
			lVar = "DAY_TEXT";
			lSousSelect = " select strftime('%d-%m-%Y', TimeStamp/1000,'unixepoch') " + lVar;
			break;
		case 2 :
			lVar = "WEEK_TEXT";
			lSousSelect = " select strftime('%W-%Y', TimeStamp/1000,'unixepoch') " + lVar;
			break;
		case 3 :
			lVar = "MONTH_TEXT";
			lSousSelect = " select strftime('%m-%Y', TimeStamp/1000,'unixepoch') " + lVar;
			break;
		case 4 :
			lVar = "YEAR_TEXT";
			lSousSelect = " select strftime('%Y', TimeStamp/1000,'unixepoch') " + lVar;
			break;
		}
		
		String lSQL = "select " + lVar + " MY_DATE, SUM_VAL from ( ";
		lSQL += lSousSelect + ", sum(Val) SUM_VAL from ARCA_DATA ";
		
		boolean isWhere = false;
		
		if (iFilter.startingDate != null && iFilter.endingDate != null) {	
			isWhere = true;
			lSQL += " where TimeStamp >= " + iFilter.startingDate.getTime() + " and TimeStamp <= " + iFilter.endingDate.getTime();
		}		
		
		if (! iFilter.source.equals("")) {
			if (! isWhere)
				lSQL += " where ";
			else
				lSQL += " and ";
			lSQL += " Origin = '" + iFilter.source + "' ";
		}
		
		lSQL += " group by " + lVar + " order by TimeStamp ) ";
		
		if (iFilter.year >= 0 && (iFilter.startingDate == null || iFilter.endingDate == null)) {
			if (iFilter.granularity == 4)
				lSQL += " where " + lVar + "=" + iFilter.year;
			else
				lSQL += " where " + lVar + " like '%-" + iFilter.year + "'";
		}		
		return lSQL;
	}
}
