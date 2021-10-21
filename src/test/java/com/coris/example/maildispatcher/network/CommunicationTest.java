package com.coris.example.maildispatcher.network;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;


@SpringBootTest
public class CommunicationTest {

    @Test
    public void testDate(){
        final String time = "2021—04—30";

        final var s1 = time.replaceAll("[^0-9]", ",");
        final var split = s1.split(",");

        final var c = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            var temp = split[i];
            c[i] = Integer.parseInt(temp);
        }

        final var date = LocalDate.of(c[0], c[1],c[2]);

        final var of = LocalDate.of(2021, 4, 30);
        Assertions.assertThat(date).isAfterOrEqualTo(of);
    }
}