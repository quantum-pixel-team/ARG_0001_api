package com.quantum_pixel.arg.utilities;

import jakarta.validation.constraints.NotNull;

import java.time.*;

public class DateTimeUtils {
    private DateTimeUtils() {
    }

    public static LocalDateTime toLocalDateTimeUtc(@NotNull OffsetDateTime dateTime) {
        return dateTime.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }


    public static LocalTime toLocalTimeUtc(@NotNull OffsetDateTime dateTime) {
        return dateTime.withOffsetSameLocal(ZoneOffset.UTC).toLocalTime();
    }

    public static LocalTime toLocalTimeUtc(@NotNull OffsetTime dateTime) {
        return dateTime.withOffsetSameLocal(ZoneOffset.UTC).toLocalTime();
    }

    public static OffsetTime toOffsetTimeUtc(LocalTime value) {
        return value.atOffset(ZoneOffset.UTC);
    }
}
