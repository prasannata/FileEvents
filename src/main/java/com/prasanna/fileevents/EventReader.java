package com.prasanna.fileevents;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reads a event from the provided input stream. The input event pattern must
 * match [event] [timestamp] [path] [content hash] where valid events are add
 * and del.
 * 
 * @author prasanna
 */
public class EventReader
{
    private final InputStream inputStream;
    private final BufferedReader reader;
    private final static String eventInputPattern = "(^add|del)\\s+(\\d+)\\s+(/[^\\$/\\^\\*%#@!\\(\\);:\\\\<>\\?\\,\\&]+[/[^\\$/\\^\\*%#@!\\(\\);:\\\\<>\\?\\,\\&]*]*)\\s+(\\w{8}|\\-$)";
    private final Pattern pattern;

    public EventReader(InputStream inputStream)
    {
        if (inputStream == null)
            throw new IllegalArgumentException("Cannot initialize reader without input stream");

        this.inputStream = inputStream;
        reader = new BufferedReader(new InputStreamReader(this.inputStream));
        pattern = Pattern.compile(eventInputPattern, Pattern.CASE_INSENSITIVE);
    }

    public List<Event> read()
    {
        List<Event> events = new ArrayList<Event>();
        Event lastEvent = null;
        int numEvents = readNumEvents();

        while (numEvents > 0)
        {
            try
            {
                String inputEvent = reader.readLine();
                Event event = parse(inputEvent);
                if (event != null && (lastEvent == null || isChronological(lastEvent, event)))
                    events.add(event);

                lastEvent = event;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            numEvents--;
        }

        return events;
    }

    private boolean isChronological(Event lastEvent, Event event)
    {
        return lastEvent != null && event.getTimestamp() >= lastEvent.getTimestamp();
    }

    public Event parse(String text)
    {
        Event event = null;
        Matcher matcher = pattern.matcher(text);

        if (matcher.find() && matcher.groupCount() == 4)
        {
            event = new Event();
            event.setEventType(EventType.valueOf(matcher.group(1)));
            event.setTimestamp(Long.valueOf(matcher.group(2)));
            event.setPath(matcher.group(3));
            event.setContentHash(matcher.group(4));
        }

        return event;
    }

    private int readNumEvents()
    {
        String numEventsInStr = null;
        int numEvents = 0;

        try
        {
            numEventsInStr = reader.readLine();
            numEvents = Integer.parseInt(numEventsInStr);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (NumberFormatException e)
        {
        }

        return numEvents;
    }

    @Override
    public void finalize()
    {
        if (reader != null)
        {
            try
            {
                reader.close();
            }
            catch (IOException e)
            {
                System.out.println("Failed to close input stream.");

            }
        }
    }
}
