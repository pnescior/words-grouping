package pl.nescior.wordsgrouping.cli;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import pl.nescior.wordsgrouping.Group;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toCollection;

class CsvPrinter {
    private static final String OUTPUT_DIRECTORY = "data";
    private static final String OUTPUT_FILENAME = "result.csv";

    void printGroups(List<Group> groups) throws IOException {
        File outputFile = createOutputFile(OUTPUT_FILENAME);
        CSVPrinter printer = CSVFormat.DEFAULT
                .withHeader("weight", "words")
                .print(new BufferedWriter(new FileWriter(outputFile)));
        for (Group group : groups) {
            LinkedList<String> entries = group.getWords().stream().map(Group.Word::get).collect(toCollection(LinkedList::new));
            entries.addFirst(String.valueOf(group.getCount()));
            printer.printRecord(entries);
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
