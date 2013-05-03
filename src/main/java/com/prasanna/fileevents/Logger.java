package com.prasanna.fileevents;

import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger
{
    private static int ACTION_COLUMN_WIDTH = 9;
    private static int TYPE_COLUMN_WIDTH = 9;
    private static int TIME_COLUMN_WIDTH = 26;
    private static int DETAILS_COLUMN_WIDTH = 51;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy HH:mm:ss:SSS");
    private PrintStream outputStream = System.out;

    public Logger()
    {
        if (outputStream == null)
        {
            throw new IllegalArgumentException("Cannot initialize logger with null output stream.");
        }

    }

    public static String padRight(String s, int padding)
    {
        return String.format("%1$-" + padding + "s", s);
    }

    private String getDate(Long timestamp)
    {
        Date date = new Date(timestamp);
        return dateFormat.format(date);
    }

    public void printHeader()
    {
        printBoundary();

        outputStream.format("|%1$-" + TIME_COLUMN_WIDTH + "s|%2$-" + ACTION_COLUMN_WIDTH + "s|%3$-" + TYPE_COLUMN_WIDTH
                        + "s|%4$-" + DETAILS_COLUMN_WIDTH + "s%5$s", "Occurence", "Event", "Type", "Details", "|");
        outputStream.println();

        printBoundary();
    }

    private void printBoundary()
    {
        for (int i = 0; i < 100; i++)
            outputStream.print("-");

        outputStream.println();
    }

    public void prettyLog(Event event, Action action, String text)
    {
        if (text != null)
        {
            if (text.length() > DETAILS_COLUMN_WIDTH)
                printMultiRowStatement(event, action, text);
            else
            {
                logLine("|" + padRight(getDate(event.getTimestamp()), TIME_COLUMN_WIDTH) + "|"
                                + padRight(action.getName(), ACTION_COLUMN_WIDTH) + "|"
                                + padRight(event.getFileType(), TYPE_COLUMN_WIDTH) + "|"
                                + padRight(text, DETAILS_COLUMN_WIDTH) + "|");
            }
        }
        printBoundary();
    }

    private void printMultiRowStatement(Event event, Action action, String text)
    {
        int numRows = text.length() / DETAILS_COLUMN_WIDTH;
        String firstRowText = text.substring(0, DETAILS_COLUMN_WIDTH);

        logLine("|" + padRight(getDate(event.getTimestamp()), TIME_COLUMN_WIDTH) + "|"
                        + padRight(action.getName(), ACTION_COLUMN_WIDTH) + "|"
                        + padRight(event.getFileType(), TYPE_COLUMN_WIDTH) + "|"
                        + padRight(firstRowText, DETAILS_COLUMN_WIDTH) + "|");

        int offset = DETAILS_COLUMN_WIDTH;
        int end = DETAILS_COLUMN_WIDTH;
        for (int i = 1; i <= numRows; i++)
        {
            if (offset + end > text.length())
                end = text.length();

            String rowText = text.substring(offset, end);
            logLine("|" + padRight("", TIME_COLUMN_WIDTH) + "|" + padRight("", ACTION_COLUMN_WIDTH) + "|"
                            + padRight("", TYPE_COLUMN_WIDTH) + "|" + padRight(rowText, DETAILS_COLUMN_WIDTH) + "|");
            offset += DETAILS_COLUMN_WIDTH;
        }
    }

    public void logLine(String text)
    {
        if (text != null)
        {
            try
            {
                outputStream.write(text.getBytes());
                outputStream.write("\n".getBytes());
            }
            catch (IOException e)
            {
                System.out.println("Cannot log to output stream: " + e.getMessage());
            }
        }
    }

    @Override
    public void finalize()
    {
        if (outputStream != null)
            outputStream.close();
    }
}
