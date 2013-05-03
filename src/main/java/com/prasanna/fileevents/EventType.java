package com.prasanna.fileevents;

public enum EventType
{
    ADD,
    DEL;

    public static EventType getEnum(String value)
    {
        EventType eventType = null;

        if (value != null)
            eventType = valueOf(value.toUpperCase());

        return eventType;
    }
}
