package de.floriansymmank.conwaysgameoflife.models;

public class ScoreNotFoundException extends Exception {
    public ScoreNotFoundException(String key) {
        super("Key : " + key + " not found.");
    }
}
