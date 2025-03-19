package com.woozy.carrentals.bdd.helper;

import io.cucumber.java.Scenario;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileReader;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AttachmentsHelper {
    @SneakyThrows
    public static void attachLogToReport(Scenario scenario, String logPath) {
        File logFile = new File(logPath);

        if (logFile.exists()) {
            String logs = FileCopyUtils.copyToString(new FileReader(logFile));
            scenario.attach(logs, "text/plain", "Test logs");
        }
    }
}