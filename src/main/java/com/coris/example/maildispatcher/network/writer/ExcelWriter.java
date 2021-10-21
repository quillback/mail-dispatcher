package com.coris.example.maildispatcher.network.writer;

import com.coris.example.maildispatcher.network.converter.BigDecimalConverter;
import com.coris.example.maildispatcher.network.converter.GsonConverter;
import com.coris.example.maildispatcher.network.converter.LocalDateConverter;
import com.coris.example.maildispatcher.network.util.JsonNode;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class ExcelWriter<T> implements Function<List<T>, Path> {
    private final Path path;
    private final BiMap<Integer, PairEntry> fields;

    public ExcelWriter(final Path path,
                       final Class<T> klazz) {
        this.path = path;
        this.fields = HashBiMap.create();

        this.getColumns(klazz);
    }

    private void getColumns(final Class<T> klazz){
        final var fields = klazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            var field = fields[i];
            final var builder = PairEntry.builder();

            if (field.isAnnotationPresent(JsonNode.class)) {
                final var node = field.getAnnotation(JsonNode.class);
                if (!node.writerIgnore())
                    builder.jsonField(Strings.isNotEmpty(node.nodeName()) ? node.nodeName() :
                        field.getName());
            }
            builder.fieldName(field.getName());
            this.fields.put(i, builder.build());
        }
    }

    @Override
    public Path apply(final List<T> ts) {
        final HSSFWorkbook workbook = new HSSFWorkbook();
        final var sheet = workbook.createSheet();

        this.addHeaders(sheet.createRow(0));

        int rowNumber = 1;
        for (final T t : ts)
            this.createCells(sheet.createRow(rowNumber++), t);

        try {
            final var stream = Files.newOutputStream(this.path);
            workbook.write(stream);
            stream.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this.path;
    }

    private void addHeaders(final HSSFRow row){
        this.fields.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getKey))
                .forEach(x -> {
                    final var cell = row.createCell(x.getKey(), CellType.STRING);
                    cell.setCellValue(x.getValue().jsonField);
                });
    }

    private void createCells(final HSSFRow row, final T t){
        final var set = this.fields.keySet().stream()
                .sorted(Integer::compareTo).collect(Collectors.toCollection(LinkedHashSet::new));

        final Class<?> klazz = t.getClass();
        for (int a : set) {
            final var entry = this.fields.get(a);
            try {
                final var df = klazz.getDeclaredField(entry.fieldName);
                df.setAccessible(true);
                final var jNode = df.getAnnotation(JsonNode.class);
                if (jNode.writerIgnore()) continue;

                final var cell = row.createCell(a);
                final var o = df.get(t);

                if (jNode.converter() == GsonConverter.EmptyGsonConverter.class){
                    cell.setCellValue(String.valueOf(o));
                }

                if (jNode.converter() == LocalDateConverter.class){
                    final var s = ((LocalDate) o).toString();
                    cell.setCellValue(s);
                    cell.setCellType(CellType.STRING);
                }

                if (jNode.converter() == BigDecimalConverter.class){
                    final var v = ((BigDecimal) o).doubleValue();
                    cell.setCellValue(v);
                    cell.setCellType(jNode.celltype());
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    @EqualsAndHashCode
    public static class PairEntry{
        private final String fieldName;
        private final String jsonField;

        @Builder
        public PairEntry(@NotNull String fieldName, String jsonField) {
            this.fieldName = fieldName;
            this.jsonField = jsonField;
        }
    }
}
