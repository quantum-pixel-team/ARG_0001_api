package com.quantum_pixel.arg.common.web.mapper;

import com.quantum_pixel.arg.utilities.DateTimeUtils;

import java.time.*;
import java.util.Optional;

public interface DateTimeMapper {
    default LocalTime toLocalTime(Optional<OffsetTime> offsetTime) {
        return offsetTime.map(DateTimeUtils::toLocalTimeUtc).orElse(null);
    }

    default LocalDateTime toLocalDateTime(Optional<OffsetDateTime> dateTime) {
        return dateTime.map(DateTimeUtils::toLocalDateTimeUtc).orElse(null);
    }

    default LocalDateTime toLocalDateTime(OffsetDateTime value) {
        return DateTimeUtils.toLocalDateTimeUtc(value);
    }

    default OffsetDateTime toOffsetDateTime(LocalDateTime value) {
        return value.atOffset(ZoneOffset.UTC);
    }

    default OffsetTime toOffsetTime(LocalTime value) {
        return DateTimeUtils.toOffsetTimeUtc(value);
    }


}
