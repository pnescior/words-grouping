package pl.nescior.wordsgrouping;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.util.stream.Collectors.toList;

class StopWords {
    List<String> getFor(Language language) {
        try {
            return Files.readAllLines(Paths.get(language.fileName))
                    .stream()
                    .map(StringUtils::stripAccents)
                    .collect(toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    enum Language {
        PL("./stopwords.pl.txt");

        private final String fileName;

        Language(String fileName) {
            this.fileName = fileName;
        }
    }
}
