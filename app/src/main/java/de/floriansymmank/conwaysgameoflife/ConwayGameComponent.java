package de.floriansymmank.conwaysgameoflife;

import net.sharksystem.ASAPFormats;
import net.sharksystem.SharkComponent;
import net.sharksystem.SharkException;
import net.sharksystem.asap.ASAPPeer;

import ConwayGameEngine.ConwayGameEngineFacadeImpl;

@ASAPFormats(formats = {ConwayGameComponent.FINAL_SCORE_URI})
public class ConwayGameComponent extends ConwayGameEngineFacadeImpl implements SharkComponent {

    public static final String APP_NAME = "application/x-ConwaysGameOfLife";
    public static final String FINAL_SCORE_URI = "fs://finalScore";

    public ConwayGameComponent(String defaultDirectory) {
        super(defaultDirectory);
    }

    @Override
    public void onStart(ASAPPeer asapPeer) throws SharkException {
        asapPeer.addASAPMessageReceivedListener(APP_NAME, new FinalScoreMessageReceivedListener());
    }
}
