package com.coris.example.maildispatcher.network.converter;

import org.apache.logging.log4j.util.Strings;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Optional;

public class BigDecimalConverter implements GsonConverter<BigDecimal>{
    @Override
    public Optional<String> toJson(BigDecimal obj) {
        return Optional.of(obj.toString());
    }

    @Override
    public Optional<BigDecimal> fromJson(String obj) {
        if (Strings.isEmpty(obj))
            return Optional.empty();

        final var s = obj.replaceAll("[^\\d.]+", "");
        final var bD = new BigDecimal(s);
        return Optional.of(bD);
    }

    @Override
    public double getNumeric(@NotNull BigDecimal obj) throws NoSuchMethodException {
        return obj.doubleValue();
    }

    @Override
    public Class<BigDecimal> supports() {
        return BigDecimal.class;
    }
}
