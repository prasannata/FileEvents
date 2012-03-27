package com.prasanna.fileevents;

import java.util.List;

public class FileEventHandler
{
	public static void main(String[] args)
	{
		EventReader reader = new EventReader(System.in);
		List<Event> events = reader.read();

		EventInterpreter eventInterpreter = new EventInterpreter();
		eventInterpreter.interpret(events);
	}
}
