package com.prasanna.fileevents;

public enum Action
{
    ADDED("Added"),
    DELETED("Deleted"),
    RENAMED("Renamed"),
    MOVED("Moved");

    private final String name;

    Action(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}
