package de.floriansymmank.conwaysgameoflife.models;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ScoreHolder {
    Map<String, BigDecimal> scores = new HashMap<>();

    public void addScore(String key, double value) {
        addScore(key, BigDecimal.valueOf(value));
    }

    public void addScore(String key, int value) {
        addScore(key, BigDecimal.valueOf(value));
    }

    public void addScore(String key, BigDecimal value) {
        if (scores.containsKey(key))
            scores.put(key, scores.get(key).add(value));
        else
            scores.put(key, value);
    }

    public BigDecimal getScore(String key) throws ScoreNotFoundException {
        if (!scores.containsKey(key))
            throw new ScoreNotFoundException(key);

        return scores.get(key);
    }

    public Set<String> getAllKeys() {
        return scores.keySet();
    }

    public void reset() {
        scores = new HashMap<>();
    }

    public void removeScore(String key) {
        scores.remove(key);
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, BigDecimal> entry : scores.entrySet()) {
            sb.append(entry.getKey() + " " + entry.getValue() + " " + System.lineSeparator());
        }

        return sb.toString();
    }
}
