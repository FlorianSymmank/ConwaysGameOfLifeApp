package de.floriansymmank.conwaysgameoflife.asap;

import android.util.Log;

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
        // register message receiver
        Log.println(Log.DEBUG, "ConwayGameComponent onStart", "ASAPPeer created");
        asapPeer.addASAPMessageReceivedListener(APP_NAME, new FinalScoreMessageReceivedListener());
    }
}
