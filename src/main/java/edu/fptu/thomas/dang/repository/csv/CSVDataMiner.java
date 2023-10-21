package edu.fptu.thomas.dang.repository.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import edu.fptu.thomas.dang.dto.CompanyDTO;
import edu.fptu.thomas.dang.repository.DataMiner;
import edu.fptu.thomas.dang.utils.MyLogger;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class CSVDataMiner extends DataMiner {

    private static CSVDataMiner instance; // Singleton instance
    private final DataMapper<CompanyDTO, CSVRecord> mapper;
    private boolean shouldStopMonitoring = false;

    private CSVDataMiner() {
        mapper = new CompanyDataMapper();
    }

    public static synchronized CSVDataMiner getInstance() {
        if (instance == null) {
            instance = new CSVDataMiner();
        }
        return instance;
    }

    @Override
    public Map<Integer, CompanyDTO> parseData() {
        Map<Integer, CompanyDTO> companyList = null;
        MyLogger.getInstance()
                .info("Reading file: ".concat(this.filePath.toString()));

        // Measure the time before reading the file
        Instant start = Instant.now();
        try (CSVParser csvParser = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .parse(new FileReader(this.filePath.toString()))) {
            companyList = new HashMap<>();
            for (CSVRecord record : csvParser) {
                CompanyDTO companyDTO = mapper.mapToObject(record);
                companyList.put(companyDTO.getId(), companyDTO);
            }
        } catch (IOException e) {
            MyLogger.getInstance().warning(e.getMessage());
        }

        // Measure the time after reading the file
        Instant end = Instant.now();

        // Calculate the duration
        Duration duration = Duration.between(start, end);
        MyLogger.getInstance()
                .info("Reading the file took " + duration.toMillis() + " milliseconds");

        return companyList;
    }

    public void startMonitoring() {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path directory = Paths.get(this.filePath.toString()).getParent();
            directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY);

            MyLogger.getInstance()
                .info("Monitoring directory: " + directory);

            while (!shouldStopMonitoring) {
                WatchKey key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    Path filePath = (Path) event.context();

                    if (kind == StandardWatchEventKinds.ENTRY_CREATE || kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        if (filePath.toString().endsWith(".csv")) {
                            String fullPath = directory.resolve(filePath).toString();
                            processFile(fullPath);
                        }
                    }
                }
                key.reset();
            }
        } catch (IOException e) {
            MyLogger.getInstance().warning(e.getMessage());
        } catch (InterruptedException e) {
            MyLogger.getInstance().warning(e.getMessage());
            // Restore interrupted state...
            Thread.currentThread().interrupt();
        }
    }

    private void processFile(String filePath) {
        // Implement file processing logic here
        // This method will be called when a new file is created or modified
    }

    public void stopMonitoring() {
        shouldStopMonitoring = true;
    }
}
