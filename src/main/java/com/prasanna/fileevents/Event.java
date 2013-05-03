package com.prasanna.fileevents;

import java.io.File;
import java.util.StringTokenizer;

public class Event
{
    private EventType eventType;
    private Long timestamp;
    private String path;
    private String contentHash;

    public EventType getEventType()
    {
        return eventType;
    }

    public void setEventType(EventType eventType)
    {
        this.eventType = eventType;
    }

    public Long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Long timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getContentHash()
    {
        return contentHash;
    }

    public void setContentHash(String contentHash)
    {
        this.contentHash = contentHash;
    }

    public boolean isDirectoryEvent()
    {
        if (contentHash != null && contentHash.equals("-"))
            return true;

        return false;
    }

    public String getParentPath()
    {
        File file = new File(path);
        return file.getParent();
    }

    public String getFileType()
    {
        return isDirectoryEvent() ? "dir" : "file";
    }

    public boolean isUnderParent(String parentPath)
    {
        boolean isUnderParent = false;

        if (parentPath != null && path.length() > parentPath.length())
        {
            StringTokenizer tokenizer = new StringTokenizer(parentPath, "/");
            StringTokenizer tokenizerForMyPath = new StringTokenizer(path, "/");

            while (tokenizer.hasMoreTokens())
            {
                String parentPathPart = tokenizer.nextToken();
                String myPathPart = tokenizerForMyPath.nextToken();
                if (myPathPart == null || !myPathPart.equals(parentPathPart))
                {
                    isUnderParent = false;
                    break;
                }

                isUnderParent = true;
            }
        }

        return isUnderParent;
    }

    public String getFileName()
    {
        if (path != null)
        {
            File file = new File(path);
            return file.getName();
        }

        return null;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((contentHash == null) ? 0 : contentHash.hashCode());
        result = prime * result + ((eventType == null) ? 0 : eventType.hashCode());
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Event other = (Event) obj;
        if (contentHash == null)
        {
            if (other.contentHash != null)
                return false;
        }
        else if (!contentHash.equals(other.contentHash))
            return false;
        if (eventType != other.eventType)
            return false;
        if (path == null)
        {
            if (other.path != null)
                return false;
        }
        else if (!path.equals(other.path))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "Event [eventType=" + eventType + ", timeStamp=" + timestamp + ", path=" + path + ", contentHash="
                        + contentHash + "]";
    }

}
