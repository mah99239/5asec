 package com.example.a5asec.utility;

public class Preconditions
    {
    public static <T> T checkNotNull(T reference)
        {
        if (reference == null)
            {
            throw new NullPointerException();
            } else
            {
            return reference;
            }
        }

    public static <T> T checkNotNull(T reference, String message)
        {
        if (reference == null)
            {
            throw new NullPointerException(message);
            } else
            {
            return reference;
            }
        }
    }
