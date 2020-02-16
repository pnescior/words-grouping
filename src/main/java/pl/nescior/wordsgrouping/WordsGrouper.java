package pl.nescior.wordsgrouping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class WordsGrouper {
    private final SingleWordsExtractor singleWordsExtractor = new SingleWordsExtractor();
    private final DistanceCalculator distanceCalculator = new DistanceCalculator();
    private final List<String> polishStopWords = new StopWords().getFor(StopWords.Language.PL);

    public List<Group> analyze(List<String> sentences, double maxGroupDistance) {
        List<String> singleWords = singleWordsExtractor.extract(sentences)
                .stream()
                .filter(this::isNotAStopWord)
                .collect(toList());
        List<Group> groups = new ArrayList<>();
        for (String word : singleWords) {
            if (!addToSuitableGroup(groups, word, maxGroupDistance)) {
                groups.add(new Group(word));
            }
        }
        Collections.sort(groups);
        return groups;
    }

    private boolean isNotAStopWord(String word) {
        return !polishStopWords.contains(word);
    }

    private boolean addToSuitableGroup(List<Group> groups, String word, double maxGroupDistance) {
        for (Group group : groups) {
            if (group.isSuitable(word, distanceCalculator, maxGroupDistance)) {
                group.add(word);
                return true;
            }
        }
        return false;
    }
}
