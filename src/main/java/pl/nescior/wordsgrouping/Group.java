package pl.nescior.wordsgrouping;

import java.util.HashSet;
import java.util.Set;

import static java.lang.Integer.compare;

public class Group implements Comparable<Group> {
    private final Set<String> words = new HashSet<>();
    private int count;

    Group(String firstWord) {
        words.add(firstWord);
        count = 1;
    }

    boolean isSuitable(String newWord, DistanceCalculator distanceCalculator, double maxDistance) {
        double distance = distanceCalculator.meanDistanceFromGroup(words, newWord);
        return distance <= maxDistance;
    }

    @Override
    public int compareTo(Group other) {
        return compare(other.count, count);
    }

    void add(String word) {
        words.add(word);
        count++;
    }

    public Set<String> getWords() {
        return words;
    }

    public int getCount() {
        return count;
    }
}
