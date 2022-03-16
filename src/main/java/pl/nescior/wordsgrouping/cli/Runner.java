package pl.nescior.wordsgrouping.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import pl.nescior.wordsgrouping.Group;
import pl.nescior.wordsgrouping.WordsGrouper;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.Callable;

import static java.util.stream.Collectors.toList;

@Command(
        name = "words-grouping",
        description = "tool for listing most common words from a file with given tolerance for each group (using Levenshtein distance)",
        sortOptions = false
)
class Runner implements Callable<Integer> {

    @Option(
            names = {"-f", "--file"},
            required = true,
            description = {"input file to read data from", "words will be read from each line in the file, splitted by a space or any special character"}
    )
    private File inputFile;

    @Option(names = {"-o", "--output"}, description = "file where the CSV result will be written to")
    private File outputFile;

    @Option(names = {"-p", "--print"}, description = "whether the results should be printed to console")
    private boolean printToConsole;

    @Option(names = "--max-distance", description = "max Levenshtein distance for a single words group")
    private double maxDistance = 2.5;

    @Option(names = {"-h", "--help"}, usageHelp = true, description = "display help message")
    private boolean helpRequested = false;

    private static final WordsGrouper wordsGrouper = new WordsGrouper();
    private static final CsvPrinter csvPrinter = new CsvPrinter();

    @Override
    public Integer call() throws Exception {
        List<String> strings = Files.readAllLines(inputFile.toPath());
        List<Group> groups = wordsGrouper.analyze(strings, maxDistance);
        if (printToConsole) {
            printFormattedGroups(groups);
        }
        if (outputFile != null) {
            csvPrinter.printGroups(groups, outputFile);
        }
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Runner()).execute(args);
        System.exit(exitCode);
    }

    private static void printFormattedGroups(List<Group> groups) {
        groups.stream()
                .map(Runner::formatGroup)
                .forEachOrdered(System.out::println);
    }

    private static String formatGroup(Group group) {
        List<String> wordsWithCount = group.getWords().stream()
                .sorted()
                .map(word -> String.format("%s %d", word.get(), word.getCount()))
                .collect(toList());
        return String.format("[%s]: %d", String.join(", ", wordsWithCount), group.getCount());
    }
}
