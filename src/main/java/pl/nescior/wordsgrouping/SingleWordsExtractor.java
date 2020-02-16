package pl.nescior.wordsgrouping;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

class SingleWordsExtractor {
    private static final int MIN_LENGTH = 4;

    public List<String> extract(List<String> strings) {
        return strings.stream()
                .flatMap(string -> stream(string.split(" ")))
                .map(this::normalize)
                .filter(s -> !s.isBlank())
                .filter(this::isLongEnough)
                .collect(toList());
    }

    private boolean isLongEnough(String string) {
        return string.length() >= MIN_LENGTH;
    }

    private String normalize(String string) {
        String result = string.toLowerCase();
        result = StringUtils.stripAccents(result);
        result = result.replaceAll("[^a-z]", "");
        return result;
    }
}
