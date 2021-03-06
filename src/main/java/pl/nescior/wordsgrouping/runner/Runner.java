package pl.nescior.wordsgrouping.runner;

import pl.nescior.wordsgrouping.Group;
import pl.nescior.wordsgrouping.WordsGrouper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static pl.nescior.wordsgrouping.runner.ProgramArguments.FILE;
import static pl.nescior.wordsgrouping.runner.ProgramArguments.MAX_GROUP_DISTANCE;

class Runner {

    private static final WordsGrouper wordsGrouper = new WordsGrouper();
    private static final CsvPrinter csvPrinter = new CsvPrinter();
    private static final double DEFAULT_MAX_GROUP_DISTANCE = 2.5;

    public static void main(String[] args) throws IOException {
        Map<String, String> arguments = parseArguments(args);
        validateArguments(arguments);
        List<String> strings = Files.readAllLines(Paths.get(arguments.get(FILE)));
        List<Group> groups = wordsGrouper.analyze(strings, getMaxGroupDistance(arguments));
        printFormattedGroups(groups);
        csvPrinter.printGroups(groups);
    }

    private static double getMaxGroupDistance(Map<String, String> arguments) {
        if (arguments.containsKey(MAX_GROUP_DISTANCE)) {
            return Double.parseDouble(arguments.get(MAX_GROUP_DISTANCE));
        } else {
            return DEFAULT_MAX_GROUP_DISTANCE;
        }
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

    private static Map<String, String> parseArguments(String[] args) {
        Map<String, String> result = new HashMap<>();
        if (args.length == 0) {
            return result;
        }
        for (int i = 0; i < args.length; i++) {
            String key = args[i];
            if (key.startsWith("--") && i + 1 < args.length) {
                result.put(key.replaceFirst("--", ""), args[i + 1]);
                i++;
            }
        }
        return result;
    }

    private static void validateArguments(Map<String, String> arguments) {
        if (!arguments.containsKey(FILE)) {
            throw new IllegalArgumentException("file argument must be present");
        }
        if (arguments.containsKey(MAX_GROUP_DISTANCE)) {
            try {
                Double.valueOf(arguments.get(MAX_GROUP_DISTANCE));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("invalid max-group-distance parameter");
            }
        }
    }
}
