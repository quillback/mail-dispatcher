package com.coris.example.maildispatcher.network;

import com.coris.example.maildispatcher.model.ItemModel;
import com.coris.example.maildispatcher.network.parser.GsonParser;
import com.coris.example.maildispatcher.network.writer.ExcelWriter;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.List;

@SpringBootTest
public class GsonParserTest {
    private GsonParser gsonParser;
    final Logger logger = LoggerFactory.getLogger(getClass());

    final String json = "{\n" +
            "   \"date\":\"2021—04—30\",\n" +
            "   \"bankacct\":\"1030123\",\n" +
            "   \"bankreference\":\"2023431017226\",\n" +
            "   \"nhif_reference\":\" \",\n" +
            "   \"trx_date\":\"2021—04-25\",\n" +
            "   \"trx_type\":\"Oth\",\n" +
            "   \"details\":\"Monthly Ledger rees\",\n" +
            "   \"db_amount\":\"0.00\",\n" +
            "   \"cr_amount\":\"2,500.00\",\n" +
            "   \"balance\":\"8,765,430.00\"\n" +
            "}";

    @Autowired
    public void setParser(GsonParser parser){
        this.gsonParser = parser;
    }



    @Test
    public void gsonNodes() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final var itemModel = gsonParser.populateKlazz(ItemModel.class, json);
        itemModel.ifPresent(x -> {
            final var writer = new ExcelWriter<>(Path.of("data.xlsx"), ItemModel.class);
            final var apply = writer.apply(List.of(x));
            logger.info(apply.toAbsolutePath().toString());
        });

    }
}