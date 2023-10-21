package edu.fptu.thomas.dang;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import edu.fptu.thomas.dang.service.CompanyService;

public class Application {
    private static final Scanner scanner = new Scanner(System.in);
    private static String csvFile = "./import/companies.csv";

    public static void main(String[] args) {
        System.out.println("Welcome to the company data importer!");
        boolean isContinue = true;
        while (isContinue) {
            //1. Import file
            System.out.println("Default file to import: " + csvFile);
            System.out.println("----------------------------------------------");
            System.out.println("FUNCTION 1: Import file");
            System.out.println("Start reading the file...");
            CompanyService service = new CompanyService(csvFile);
            System.out.println("File read successfully!");
            System.out.println("----------------------------------------------");
            //2. Output to the console the total capital of headquarters located in “CH”.
            System.out.println("FUNCTION 2: Output to the console the total capital of headquarters located in “CH”");
            long totalCapital = service.calculateTotalCapitalForCHCompanies();
            System.out.println("total capital of headquarters located in “CH” = " + totalCapital);
            System.out.println("----------------------------------------------");

            //3. Output to the console the name of companies that the country is in “CH”.
            // The list is sorted descending by capital.
            System.out.println("FUNCTION 3: Output to the console the name of companies that the country is in “CH”" +
                    " descending by capital:");
            System.out.println("name of companies that the country is in “CH” descending by capital:");
            service.outputCHCompaniesByCapitalDescending().forEach(System.out::println);
            System.out.println("----------------------------------------------");

            //4. Modify your program to monitor a predefined folder “import” for changes.
            // If your program is able to process the file, reimport the file and print out the results in feature #2 and #3.


            //5. Use your program to re-import the following zip file (companies_big_data.zip).
            // Unzip the file with Windows before importing, your program only needs to handle csv file for now.
            System.out.print("If you want to change the file to import, please put the file in the folder " +
                    "\"import\" in the project and enter \"y\". Otherwise, enter others to continue:");
            isContinue = getYesNo();
            if (isContinue) {
                System.out.println("FUNCTION 5: Re-import the file");
                printFilesInFolder();
                System.out.print("Enter the file name: ");
                csvFile = "./import/" + getFileName();
            }

            System.out.println("----------------------------------------------");
        }
    }

    public static boolean getYesNo() {
        return scanner.nextLine().trim().equalsIgnoreCase("y");
    }

    public static String getFileName() {
        return scanner.nextLine().trim();
    }

    public static void printFilesInFolder() {
        Path directory = Paths.get("./import/");
        System.out.println("Files in folder " + directory.toAbsolutePath() + ":");
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory)) {
            for (Path file : directoryStream) {
                if (Files.isRegularFile(file)) {
                    System.out.println(file.getFileName());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
