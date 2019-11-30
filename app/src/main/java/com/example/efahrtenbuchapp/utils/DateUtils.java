package com.example.efahrtenbuchapp.utils;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

public class DateUtils {
	private static final int HOUR_IN_MILLIS = 1000 * 60 * 60;

	/**
	 * @return Aktuelles Datum als String im Format: TT.MM.YYYY (DOY)
	 */
	public static String getDate(java.util.Date date){
		return date.getDay() + "." + (date.getMonth() + 1) + "." + date.getYear();
	}

	public static String getTime(java.util.Date time){
		return time.getHours() + ":" + time.getMinutes() + ":" + time.getSeconds();
	}

	/**
	 * @return Aktuelles Datum als String im Format: TT.MM.YYYY (DOY)
	 * (DOY = Day Of Year)
	 */
	public static String getDate(LocalDateTime ldt)
	{
		return ldt.getDayOfMonth() + "." + ldt.getMonthValue() + "." + ldt.getYear() + " (" + ldt.getDayOfYear() + ")";
	}
	
	/**
	 * Aktuelle Uhrzeit als String im Format HH:MM:SS:Millis
	 * @return
	 */
	public static String getTime(LocalDateTime ldt)
	{
		return ldt.getHour() + ":" + ldt.getMinute() + ":" + ldt.getSecond() + "," + ldt.getNano()/1000000;
	}
	
	/**
	 * String zu Date
	 * @param dateString
	 * @return
	 * @throws ParseException
	 */
	public static Date simpleDateConverter(String dateString) throws ParseException
	{
		if(dateString == null) return null;
		return (Date) DateFormat.getDateInstance().parse(dateString);
	}
	
	/**
	 * String zu Date
	 * @param dateString
	 * @param addMillis
	 * @return
	 * @throws ParseException
	 */
	public static Date simpleDateConverter(String dateString, int addMillis) throws ParseException
	{
		return new Date(new SimpleDateFormat("dd.MM.yyyy").parse((dateString)).getTime() + addMillis);
	}
	
	/**
	 * String zu Time
	 * @param time
	 * @return
	 * @throws ParseException
	 */
	public static Time simpleTimeConverter(String time) throws ParseException
	{
		return (Time) DateFormat.getDateInstance().parse(time);
	}
	
	public static java.util.Date aktuellesDatum()
	{
		java.util.Date local = new java.util.Date();
		//Aus irgendeinem Grund ist die Uhrzeit eine Stunde zur�ck deswegen m�ssen wir eine Stunde Drauf rechnen
		local.setTime(local.getTime() + HOUR_IN_MILLIS);
		return local;
	}
	
	/**
	 * Konvertiert ein Date in einen MySQL String <b>ohne</b> Zeit
	 * @param date
	 * @return
	 */
	public static String dateToSqlString(java.util.Date date)
	{
		return (date.getYear() + 1900) + "-" + (date.getMonth() + 1) +"-"+ date.getDate();
	}
	
	/**
	 * Konvertiert ein Date in einen MySQL String <b>mit</b> Zeit
	 * @param time
	 * @return
	 */
	public static String timeToSqlString(java.util.Date time)
	{
		//Aus irgendeinem Grund ist die Uhrzeit eine Stunde vor deswegen m�ssen wir eine Stunde zur�ck rechnen
		time = new java.util.Date(time.getTime() - HOUR_IN_MILLIS);
		return time.getHours() + ":" + time.getMinutes() + ":" + time.getSeconds();
	}
	
	/**
	 * Berechnet die Differenz zwischen den Daten in der angegeben TimeUnit
	 * @param date1
	 * @param date2
	 * @param timeUnit
	 * @return differenz 
	 */
	public static long getDateDiff(java.util.Date date1, java.util.Date date2, TimeUnit timeUnit) {
	    long diffInMillies = date2.getTime() - date1.getTime();
	    return timeUnit.convert(diffInMillies, timeUnit);
	}
	
	/**
	 * 
	 * @param date Datum zu dem addiert werden soll
	 * @param hours Anzahl an Stunden
	 * @return date 
	 */
	public static java.util.Date plusHours(java.util.Date date, int hours)
	{
		return new java.util.Date(date.getTime() + HOUR_IN_MILLIS * hours);
	}

	/**
	 * Siehe {@link DateUtils#plusHours(java.util.Date, int)}
	 */
	public static java.util.Date minusHours(java.util.Date date, int hours)
	{
		return new java.util.Date(date.getTime() - HOUR_IN_MILLIS * hours);
	}

	public static java.util.Date of(int day, int month, int year){
		return Date.from(LocalDateTime.of(year, month, day, 0, 0).atZone(ZoneId.systemDefault()).toInstant());
	}
	public static java.util.Date of(java.util.Date date, int hour, int minute){
		return Date.from(LocalDateTime.of(date.getYear(), date.getMonth(), date.getDay(), hour, minute).atZone(ZoneId.systemDefault()).toInstant());
	}
}
