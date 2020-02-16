package pl.nescior.wordsgrouping;

import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.Collection;

class DistanceCalculator {
    private final LevenshteinDistance distanceCalculator = LevenshteinDistance.getDefaultInstance();

    double meanDistanceFromGroup(Collection<String> wordsGroup, String wordToCheck) {
        double distance = 0;
        for (String word : wordsGroup) {
            distance += calculate(word, wordToCheck);
        }
        return distance / wordsGroup.size();
    }

    private int calculate(String word1, String word2) {
        return distanceCalculator.apply(word1, word2);
    }
}
