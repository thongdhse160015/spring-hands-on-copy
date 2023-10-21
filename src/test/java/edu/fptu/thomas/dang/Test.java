package edu.fptu.thomas.dang;

import edu.fptu.thomas.dang.repository.csv.CSVDataMiner;

public class Test
{
    public static void main(String[] args) {
        CSVDataMiner.getInstance().openFile("./import/companies.csv");
        CSVDataMiner.getInstance().startMonitoring();
    }
}
