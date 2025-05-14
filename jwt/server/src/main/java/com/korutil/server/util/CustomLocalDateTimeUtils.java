package com.korutil.server.util;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class CustomLocalDateTimeUtils {

    private CustomLocalDateTimeUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static LocalDateTime getNow()    {
        return LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }
}
