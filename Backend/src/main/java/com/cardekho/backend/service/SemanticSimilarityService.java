package com.cardekho.backend.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class SemanticSimilarityService {

    public double score(String prompt, String description) {
        if (prompt == null || description == null || prompt.isBlank() || description.isBlank()) {
            return 0.0;
        }

        Map<String, Integer> promptCounts = tokenize(prompt);
        Map<String, Integer> descriptionCounts = tokenize(description);
        Set<String> vocabulary = new HashSet<>(promptCounts.keySet());
        vocabulary.addAll(descriptionCounts.keySet());

        double dotProduct = 0.0;
        double promptNorm = 0.0;
        double descriptionNorm = 0.0;

        for (String term : vocabulary) {
            int promptCount = promptCounts.getOrDefault(term, 0);
            int descriptionCount = descriptionCounts.getOrDefault(term, 0);
            dotProduct += promptCount * descriptionCount;
            promptNorm += promptCount * promptCount;
            descriptionNorm += descriptionCount * descriptionCount;
        }

        if (promptNorm == 0.0 || descriptionNorm == 0.0) {
            return 0.0;
        }

        return dotProduct / (Math.sqrt(promptNorm) * Math.sqrt(descriptionNorm));
    }

    private Map<String, Integer> tokenize(String text) {
        Map<String, Integer> counts = new HashMap<>();
        Arrays.stream(text.toLowerCase().replaceAll("[^a-z0-9\\s]", "").split("\\s+"))
                .filter(token -> !token.isBlank())
                .forEach(token -> counts.merge(token, 1, Integer::sum));
        return counts;
    }
}
