package pl.nescior.wordsgrouping.runner;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import pl.nescior.wordsgrouping.Group;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

class CsvPrinter {
    private static final String OUTPUT_DIRECTORY = "data";
    private static final String OUTPUT_FILENAME = "result.csv";

    void printGroups(List<Group> groups) throws IOException {
        File outputFile = createOutputFile(OUTPUT_FILENAME);
        CSVPrinter printer = CSVFormat.DEFAULT
                .withHeader("weight", "word", "color", "url")
                .withQuoteMode(QuoteMode.ALL)
                .print(new BufferedWriter(new FileWriter(outputFile)));
        for (Group group : groups) {
            printer.printRecord(group.getCount(), group.getMostPopularWord().get(), "", "");
        }
        printer.close(true);
    }

    private static File createOutputFile(String fileName) throws IOException {
        Path outputFilePath = Paths.get(OUTPUT_DIRECTORY, fileName);
        File outputFile = outputFilePath.toFile();
        if (outputFile.exists()) {
            if (!outputFile.delete()) {
                throw new IOException("unable to delete existing file " + outputFile);
            }
        } else {
            Files.createDirectories(outputFilePath.getParent());
        }
        if (!outputFile.createNewFile()) {
            throw new IOException("unable to create file " + outputFile);
        }
        return outputFile;
    }
}
