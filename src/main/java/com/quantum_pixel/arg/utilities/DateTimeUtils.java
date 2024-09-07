package com.quantum_pixel.arg.utilities;

import jakarta.validation.constraints.NotNull;

import java.time.*;

public class DateTimeUtils {
    public static final ZoneId TIRANE_ZONE_ID = ZoneId.of("Europe/Tirane");

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
    public static LocalDateTime toLocalDateTimeTirane(OffsetDateTime dateTime) {
        return dateTime.atZoneSameInstant(TIRANE_ZONE_ID).toLocalDateTime();
    }

}
