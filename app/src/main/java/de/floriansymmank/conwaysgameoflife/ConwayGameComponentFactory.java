package de.floriansymmank.conwaysgameoflife;

import net.sharksystem.SharkComponent;
import net.sharksystem.SharkComponentFactory;

public class ConwayGameComponentFactory implements SharkComponentFactory {

    private final String defaultDirectory;
    private ConwayGameComponent instance = null;

    public ConwayGameComponentFactory(String defaultDirectory) {
        this.defaultDirectory = defaultDirectory;
    }

    @Override
    public SharkComponent getComponent() {

        if (this.instance == null) {
            this.instance = new ConwayGameComponent(defaultDirectory);
        }
        return this.instance;
    }
}
