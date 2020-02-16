package pl.nescior.wordsgrouping;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static java.lang.Integer.compare;
import static java.util.stream.Collectors.toSet;

public class Group implements Comparable<Group> {
    private final Set<Word> words = new HashSet<>();

    Group(String firstWord) {
        words.add(new Word(firstWord));
    }

    boolean isSuitable(String newWord, DistanceCalculator distanceCalculator, double maxDistance) {
        Set<String> stringWords = words.stream()
                .map(Word::get)
                .collect(toSet());
        double distance = distanceCalculator.meanDistanceFromGroup(stringWords, newWord);
        return distance <= maxDistance;
    }

    @Override
    public int compareTo(Group other) {
        return compare(other.getCount(), getCount());
    }

    void add(String word) {
        for (Word existingWord : words) {
            if (existingWord.equals(word)) {
                existingWord.add();
                return;
            }
        }
        words.add(new Word(word));
    }

    public Set<Word> getWords() {
        return words;
    }

    public Word getMostPopularWord() {
        return words.stream()
                .sorted()
                .findFirst()
                .orElse(null);
    }

    public int getCount() {
        return words.stream()
                .map(Word::getCount)
                .reduce(0, Integer::sum);
    }

    public static class Word implements Comparable<Word> {
        private final String word;
        private int count;

        private Word(String word) {
            this.word = word;
            this.count = 1;
        }

        public String get() {
            return word;
        }

        public int getCount() {
            return count;
        }

        private void add() {
            this.count++;
        }

        @Override
        public int compareTo(Word o) {
            return Integer.compare(o.count, count);
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (other == null) return false;
            if (other instanceof Word) {
                Word word1 = (Word) other;
                return Objects.equals(word, word1.word);
            } else if (other instanceof String) {
                return Objects.equals(word, other);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(word);
        }
    }
}
