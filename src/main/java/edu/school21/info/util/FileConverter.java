package edu.school21.info.util;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import edu.school21.info.model.dto.BaseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class FileConverter {

    public static <T extends BaseDto> List<T> convertCsvToList(MultipartFile file, Class<T> clazz) {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            CustomMappingStrategy<T> strategy = new CustomMappingStrategy<>();
            strategy.setType(clazz);
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withType(clazz)
                    .withSkipLines(1)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return Collections.emptyList();
    }

    public static <T extends BaseDto> void writeCsvFromBean(Writer writer, List<T> beans, Class<T> clazz) {
        CustomMappingStrategy<T> strategy = new CustomMappingStrategy<>();
        strategy.setType(clazz);

        StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(true)
                .withMappingStrategy(strategy)
                .build();

        try {
            beanToCsv.write(beans);
        } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void writeCsvFromMap(PrintWriter writer, Map<String, List<Object>> map) {
        String keys = map.keySet().toString().replaceAll("[\\[\\]]", "");
        if (keys.isEmpty()) return;
        int size = map.values().stream().findFirst().get().size();
        List<StringBuilder> list = new ArrayList<>(size);
        IntStream.range(0, size).forEach(i -> {
            list.add(new StringBuilder());
            for (List<Object> value : map.values()) {
                list.get(i).append(value.get(i).toString()).append(',');
            }
            list.get(i).deleteCharAt(list.get(i).length() - 1);
        });
        writer.println(keys);
        list.stream().map(StringBuilder::toString).forEach(writer::println);
    }
}
