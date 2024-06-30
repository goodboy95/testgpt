package com.seekerhut.utils;

public class ConstValues {
    public static final int millisecondsInAnHour = 3600 * 1000;
    public static final long longMin = 0x8000000000000000l;
    public static final long longMax = 0x7fffffffffffffffl;

    public static class RedisKeys {
        public static String PrimaryKeyIdPrefix = "PrimaryKeyId:";
        public static String characterData = "characterData";
    }
}
