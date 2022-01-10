package de.floriansymmank.conwaysgameoflife.asap;

import android.util.Log;

import net.sharksystem.asap.ASAPHop;
import net.sharksystem.asap.ASAPMessageReceivedListener;
import net.sharksystem.asap.ASAPMessages;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import ConwayGameEngine.FinalScore;

public class FinalScoreMessageReceivedListener implements ASAPMessageReceivedListener {
    @Override
    public void asapMessagesReceived(ASAPMessages messages, String s, List<ASAPHop> list) throws IOException {

        CharSequence format = messages.getFormat();
        CharSequence uri = messages.getURI();

        // discard wrong messages?
        if (!format.equals(ConwayGameComponent.APP_NAME) && uri.equals(ConwayGameComponent.FINAL_SCORE_URI))
            return;

        Iterator<byte[]> msgIterator = messages.getMessages();
        while (msgIterator.hasNext()) {
            try {
                FinalScore fs = ConwayGameEngine.Util.Serialize.finalScoreDeserializer(msgIterator.next());
                ConwayGameApp.getConwayGameApp().getConwayGameEngineFacade().saveScore(fs);
                Log.println(Log.DEBUG, "FinalScoreMessageReceivedListener asapMessagesReceived", "Final Score erhalten, yay!");
            } catch (ClassNotFoundException e) {
                Log.println(Log.DEBUG, "FinalScoreMessageReceivedListener asapMessagesReceived", "Deserializer Class Not Found " + e.getMessage());
            }
        }
    }
}