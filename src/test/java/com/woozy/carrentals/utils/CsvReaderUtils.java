package com.woozy.carrentals.utils;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.util.Collections;
import java.util.List;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CsvReaderUtils {
    public static <T> List<T> loadObjectList(Class<T> type, String fileName) {
        CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
        CsvMapper mapper = new CsvMapper();

        File file;
        try {
            file = new ClassPathResource(fileName).getFile();
        } catch (Exception e) {
            log.error("Error occurred while accessing file {}", fileName, e);
            return Collections.emptyList();
        }

        try (MappingIterator<T> readValues = mapper.readerFor(type).with(bootstrapSchema).readValues(file)) {
            return readValues.readAll();
        } catch (Exception e) {
            log.error("Error occurred while loading object list from file {}", fileName, e);
            return Collections.emptyList();
        }
    }
}