package com.example.efahrtenbuchapp.helper;

public class DbHelper {
	
	private String tableName;
	
	//Statische Methoden brauchen immer den Tablenamen;
	/**
	 * @param from tablename
	 * @return Query als String die alles selektiert
	 */
	public static String easySelect(String from)
	{
		notNull(from);
		return selectWithColumns("*", from);
	}
	/**
	 * 
	 * @param columns auszuw�hlende spalten
	 * @param from tablename
	 * @return Query als String
	 */
	public static String selectWithColumns(String columns, String from)
	{
		notNull(from); notNull(columns);
		return "SELECT " + columns + " FROM " + from;
	}

	public static String where(String column, String value){
		return " " + column + " = '" + value + "' ";
	}

	public static String where(String column, Number value){
		return " " + column + " = " + value + " ";
	}

	/**
	 * @param columns auszuw�hlende spalten
	 * @param from tablename
	 * @param whereclause WHERE Bedingung
	 * @return Query als String
	 */
	public static String selectColumnsWhere(String columns, String from, String whereclause)
	{
		notNull(from); notNull(columns); notNull(whereclause);
		return "SELECT " + columns + " FROM " + from + " WHERE " + whereclause;
	}

	public static String easyInsert(String from, String columns, String values)
	{
		notNull(from); notNull(columns); notNull(values);
		return "INSERT INTO " + from + " (" + columns + ") VALUES (" + ");"; 
	}
	//Konstruktor
	public DbHelper(String tableName)
	{
		notNull(tableName);
		this.tableName = tableName;
		
	}
	//Klassenmethoden greifen auf das Attribut (tableName) zu
	/**
	 * Siehe {@link easySelect(..)}
	 * Ist zwar kein guter Weg, alle Spalten per * anzugeben, reicht aber f�r die Anwendung 
	 * vollkommen aus.
	 */
	public String easySelect()
	{
		tableNameNotNull();
		return selectWithColumns("*");
	}
	/**
	 * Siehe {@link selectWithColumns(..)}
	 */
	public String selectWithColumns(String columns)
	{
		tableNameNotNull();
		return "SELECT " + columns + " FROM " + this.tableName;
	}
	/**
	 * Siehe {@link selectColumnsWhere(..)}
	 */
	public String selectColumnsWhere(String columns, String whereclause)
	{
		tableNameNotNull();
		return "SELECT " + columns + " FROM " + this.tableName + "WHERE " + whereclause;
	}
	public String easyInsert(String columns, String values)
	{
		return "INSERT INTO " + this.tableName + " (" + columns + ") VALUES (" + ");"; 
	}
	private void tableNameNotNull() throws NullPointerException
	{
		notNull(this.tableName);
	}
	private static void notNull(String text) {

		if(text == null) {throw new NullPointerException(text + "(Tabelle) darf nicht null sein!");}
		else return;
	}
	public String getFrom()
	{
		return this.tableName;
	}
}
