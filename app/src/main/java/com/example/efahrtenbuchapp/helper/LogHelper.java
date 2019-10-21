package com.example.efahrtenbuchapp.helper;


import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import utils.DateUtils;

public class LogHelper {
	private Logger logger = Logger.getLogger(getClass().getName());
	//No arg constructor
	public LogHelper() {}
	/*
	 * Loggt den Text als Info und mit Datum und Uhrzeit [TT.MM.YYYY | HH:MM:SS:Millis] als Prefix
	 */
	public  void log(String text) {
		LocalDateTime ldt = LocalDateTime.now();
		String prefix = generatePrefix(ldt);
		
		this.logger.info(prefix + text);
	}
	/**
	 * Macht das Selbe Wie log(text) nur man kann entscheiden ob man den Prefix will oder nicht
	 * @param text Text der zu loggenden Message
	 * @param useDateTimePrefix true = Mit Prefix; false = ohne prefix;
	 */
	public void log(String text, boolean useDateTimePrefix)
	{
		if(useDateTimePrefix)
		{
			LocalDateTime ldt = LocalDateTime.now();
			String prefix = generatePrefix(ldt);
			this.logger.info(prefix + text);
		}
		else
			this.logger.info(text);
	}
	/**
	 * Erstellt Datums/Uhrzeit Prefix siehe {@link log(String text)}
	 */
	public static String generatePrefix(LocalDateTime ldt)
	{
		String datum = DateUtils.getDate(ldt);
		String uhrzeit = DateUtils.getTime(ldt);
		
		return "[" + datum + "|" + uhrzeit + "]: ";
	}
}