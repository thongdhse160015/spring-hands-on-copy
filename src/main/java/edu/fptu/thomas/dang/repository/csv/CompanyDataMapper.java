package edu.fptu.thomas.dang.repository.csv;

import org.apache.commons.csv.CSVRecord;

import edu.fptu.thomas.dang.dto.CompanyDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CompanyDataMapper implements DataMapper<CompanyDTO, CSVRecord> {
    private static final List<DateTimeFormatter> formatters = new ArrayList<>();

    public CompanyDataMapper() {
        formatters.add(DateTimeFormatter.ofPattern("yyyy"));
        formatters.add(DateTimeFormatter.ofPattern("MM/yyyy"));
        formatters.add(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    @Override
    public CompanyDTO mapToObject(CSVRecord record) {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(Integer.parseInt(record.get("ID")));
        companyDTO.setName(record.get("Name"));
        companyDTO.setCountry(record.get("Country"));

        String foundationDateStr = record.get("Foundation Date");
        if (!foundationDateStr.isEmpty()) {
            LocalDate date = null;
            boolean isYearOnly = false;

            // Check if the string contains only digits
            if (foundationDateStr.matches("\\d+")) {
                try {
                    int year = Integer.parseInt(foundationDateStr);
                    date = LocalDate.of(year, 1, 1);
                    isYearOnly = true;
                } catch (NumberFormatException e) {
                    // Parsing failed for year-only date
                }
            }

            if (!isYearOnly) {
                for (DateTimeFormatter formatter : formatters) {
                    try {
                        date = LocalDate.parse(foundationDateStr, formatter);
                        break; // Successful parse, exit the loop
                    } catch (Exception e) {
                        // Parsing failed, try the next formatter
                    }
                }
            }
            if (date != null) {
                companyDTO.setFoundationDate(date);
            }
        }

        String capitalStr = record.get("Capital");
        if (!capitalStr.isEmpty()) {
            companyDTO.setCapital(Integer.parseInt(capitalStr));
        }

        String isHeadQuarterStr = record.get("isHeadQuarter");
        if (!isHeadQuarterStr.isEmpty()) {
            companyDTO.setHeadQuarter(Integer.parseInt(isHeadQuarterStr) == 1);
        }

        return companyDTO;
    }
}
