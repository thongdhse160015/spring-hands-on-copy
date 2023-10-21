package edu.fptu.thomas.dang.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.fptu.thomas.dang.dto.CompanyDTO;
import edu.fptu.thomas.dang.repository.csv.CSVDataMiner;

public class CompanyService {

    public CompanyService(String path) {
        CSVDataMiner.getInstance().openFile(path);
    }

    public long calculateTotalCapitalForCHCompanies() {
        long totalCapital = 0;
        Map<Integer, CompanyDTO> result = CSVDataMiner.getInstance().parseData();
        totalCapital = result.values()
                .stream()
                .filter(company -> (company).getCountry().equals("CH") && (company).isHeadQuarter())
                .mapToInt(CompanyDTO::getCapital)
                .sum();
        return totalCapital;
    }

    public List<String> outputCHCompaniesByCapitalDescending() {
        Map<Integer, CompanyDTO> companies = CSVDataMiner.getInstance().parseData();
        return companies.values()
                .stream()
                .filter(company -> (company).getCountry().equals("CH"))
                .sorted((o1, o2) -> o2.getCapital() - o1.getCapital())
                .map(CompanyDTO::getName)
                .collect(Collectors.toList());
    }
}
