package com.coris.example.maildispatcher.network.converter;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Optional;

public class LocalDateConverter implements GsonConverter<LocalDate> {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Optional<String> toJson(LocalDate obj) {
        if (Objects.isNull(obj))
            return Optional.empty();
        return Optional.of(obj.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }

    @Override
    public Optional<LocalDate> fromJson(String obj) {
        if (Strings.isEmpty(obj))
            return Optional.empty();

        try {
            final var p = LocalDate.parse(obj);
        }catch (Exception ex){
            if (ex instanceof DateTimeParseException){
                logger.error("Step 1 parsing failed with {}. Trying manually", ex.getMessage());
                return Optional.of(of(obj));
            }
            return Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    public Class<LocalDate> supports() {
        return LocalDate.class;
    }

    private LocalDate of(String time){
        final var s1 = time.replaceAll("[^0-9]", ",");
        final var split = s1.split(",");

        final var c = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            var temp = split[i];
            c[i] = Integer.parseInt(temp);
        }
        return LocalDate.of(c[0], c[1],c[2]);
    }
}
