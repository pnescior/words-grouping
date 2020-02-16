package pl.nescior.wordsgrouping.runner;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import pl.nescior.wordsgrouping.Group;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

class CsvPrinter {

    void printTo(String filename, List<Group> groups) throws IOException {
        FileWriter writer = new FileWriter(filename);
        CSVPrinter printer = CSVFormat.DEFAULT
                .withHeader("weight", "word", "color", "url")
                .withQuoteMode(QuoteMode.ALL)
                .print(writer);
        for (Group group : groups) {
            printer.printRecord(group.getCount(), group.getWords().iterator().next(), "", "");
        }
        printer.close(true);
    }
}
