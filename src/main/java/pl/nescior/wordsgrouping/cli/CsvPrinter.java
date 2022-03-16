package pl.nescior.wordsgrouping.cli;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import pl.nescior.wordsgrouping.Group;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toCollection;

class CsvPrinter {
    void printGroups(List<Group> groups, File outputFile) throws IOException {
        createOrReplace(outputFile);
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

    private static void createOrReplace(File file) throws IOException {
        if (file.exists()) {
            if (!file.delete()) {
                throw new IOException("unable to delete existing file " + file);
            }
        } else if (file.toPath().getParent() != null) {
            Files.createDirectories(file.toPath().getParent());
        }
        if (!file.createNewFile()) {
            throw new IOException("unable to create file " + file);
        }
    }
}
