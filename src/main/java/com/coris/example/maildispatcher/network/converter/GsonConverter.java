package com.coris.example.maildispatcher.network.converter;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface GsonConverter <F>{

    Optional<String> toJson(F obj);

    Optional<F> fromJson(String obj);

    Class<F> supports();

    default double getNumeric(@NotNull F obj) throws NoSuchMethodException {
        throw new NoSuchMethodException("Not implemented");
    }

    class EmptyGsonConverter implements GsonConverter<Object>{

        @Override
        public Optional<String> toJson(Object obj) {
            return Optional.empty();
        }

        @Override
        public Optional<Object> fromJson(String obj) {
            return Optional.empty();
        }

        @Override
        public Class<Object> supports() {
            return Object.class;
        }
    }

}
