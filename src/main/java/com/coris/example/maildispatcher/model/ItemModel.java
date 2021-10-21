package com.coris.example.maildispatcher.model;

import com.coris.example.maildispatcher.network.converter.BigDecimalConverter;
import com.coris.example.maildispatcher.network.converter.LocalDateConverter;
import com.coris.example.maildispatcher.network.util.JsonNode;
import lombok.Data;
import org.apache.poi.ss.usermodel.CellType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ItemModel {

    @JsonNode(autoPopulate = true,
            writerIgnore = true)
    private String uuid;

    @JsonNode(nodeName = "date",
            converter = LocalDateConverter.class)
    private LocalDate date;

    @JsonNode(nodeName = "bankacct")
    private String account;

    @JsonNode(nodeName = "bankreference")
    private String bankReference;

    @JsonNode(nodeName = "nhif_reference")
    private String nhifReference;

    @JsonNode(nodeName = "trx_date",
            converter = LocalDateConverter.class)
    private LocalDate trxDate;

    @JsonNode(nodeName = "trx_type")
    private String trxType;

    @JsonNode(nodeName = "details")
    private String details;

    @JsonNode(nodeName = "db_amount", celltype = CellType.NUMERIC,
            converter = BigDecimalConverter.class)
    private BigDecimal dbAmount;

    @JsonNode(nodeName = "cr_amount",celltype = CellType.NUMERIC,
            converter = BigDecimalConverter.class)
    private BigDecimal crAmount;

    @JsonNode(nodeName = "balance",celltype = CellType.NUMERIC,
            converter = BigDecimalConverter.class)
    private BigDecimal balance;

}
