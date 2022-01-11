package de.floriansymmank.conwaysgameoflife.asap;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import net.sharksystem.SharkException;
import net.sharksystem.SharkPeer;
import net.sharksystem.SharkPeerFS;
import net.sharksystem.asap.android.apps.ASAPActivity;
import net.sharksystem.asap.android.apps.ASAPAndroidPeer;

import java.io.IOException;
import java.util.Random;

import ConwayGameEngine.ConwayGameEngineFacade;
import ConwayGameEngine.ConwayGameEngineFacadeImpl;

public class ConwayGameApp {

    public final static String PREFERENCES_FILE = "ConwayGameAppPref";

    // Shared prefs keys
    public final static String PLAYER_NAME = "PLAYER_NAME";
    public final static String PLAYER_ID = "PLAYER_ID";
    public final static String GAME_WIDTH = "GAME_WIDTH";
    public final static String GAME_HEIGHT = "GAME_HEIGHT";
    public final static String GAME_INTERVAL = "GAME_INTERVAL";

    // default values
    public final static String DEFAULT_PLAYER_NAME = "PlayerOne";
    public final static int DEFAULT_GAME_WIDTH = 50;
    public final static int DEFAULT_GAME_HEIGHT = 50;
    public final static int DEFAULT_GAME_INTERVAL = 300;

    private static ConwayGameApp singleton;

    // current vals
    private final ConwayGameEngineFacade conwayGameEngineFacade;
    private SharkPeer sharkPeer;
    private ASAPAndroidPeer asapAndroidPeer;
    private int playerID = 0;
    private String playerName = "";
    private int width = 0;
    private int height = 0;
    private String defaultDirectory = "";
    private int interval = 0;
    private boolean bluetoothStarted = false;

    private ConwayGameApp(Activity initialActivity) {

        defaultDirectory = initialActivity.getFilesDir().getAbsolutePath();
        conwayGameEngineFacade = new ConwayGameEngineFacadeImpl(defaultDirectory);

        SharedPreferences sharedPref = initialActivity.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);


        // load(default if not set) shared prefs

        // Player Name
        if (sharedPref.contains(PLAYER_NAME))
            this.playerName = sharedPref.getString(PLAYER_NAME, DEFAULT_PLAYER_NAME);
        else
            this.playerName = DEFAULT_PLAYER_NAME;

        // Player ID
        int rnd = new Random().nextInt(1_000_000);
        if (sharedPref.contains(PLAYER_ID))
            this.playerID = sharedPref.getInt(PLAYER_ID, rnd);
        else
            this.playerID = rnd;

        // Game width
        if (sharedPref.contains(GAME_WIDTH))
            this.width = sharedPref.getInt(GAME_WIDTH, DEFAULT_GAME_WIDTH);
        else
            this.width = DEFAULT_GAME_WIDTH;

        // Game width
        if (sharedPref.contains(GAME_INTERVAL))
            this.interval = sharedPref.getInt(GAME_INTERVAL, DEFAULT_GAME_INTERVAL);
        else
            this.interval = DEFAULT_GAME_INTERVAL;

        // Game height
        if (sharedPref.contains(GAME_HEIGHT))
            this.height = sharedPref.getInt(GAME_HEIGHT, DEFAULT_GAME_HEIGHT);
        else
            this.height = DEFAULT_GAME_HEIGHT;
    }

    public static ConwayGameApp initializeConwayGameApp(Activity initialActivity) {
        if (ConwayGameApp.singleton == null) {
            Log.println(Log.DEBUG, "ConwayGameApp initializeConwayGameApp", "creating ConwayApp singleton");
            ConwayGameApp.singleton = new ConwayGameApp(initialActivity);

            try {

                // produce application side shark peer
                ConwayGameApp.singleton.sharkPeer = new SharkPeerFS(
                        getConwayGameApp().getOwnerID(),
                        ConwayGameApp.singleton.getDefaultDirectory()
                );

                // create ConwayGameComponentFactory
                ConwayGameComponentFactory factory =
                        new ConwayGameComponentFactory(ConwayGameApp.singleton.getDefaultDirectory());

                // register this component with shark peer
                ConwayGameApp.singleton.sharkPeer.addComponent(factory, ConwayGameComponent.class);

                // setup android (application side peer)
                ASAPAndroidPeer.initializePeer(
                        getConwayGameApp().getOwnerID(),
                        ConwayGameApp.singleton.sharkPeer.getFormats(),
                        getConwayGameApp().getDefaultDirectory(),
                        initialActivity);

                // launch service side
                ASAPAndroidPeer applicationSideASAPPeer = ASAPAndroidPeer.startPeer(initialActivity);

                // use asap peer proxy for this app side shark peer
                ConwayGameApp.singleton.sharkPeer.start(applicationSideASAPPeer);

                // remember
                ConwayGameApp.singleton.setApplicationSideASAPAndroidPeer(applicationSideASAPPeer);

            } catch (SharkException | IOException e) {
                Log.println(Log.DEBUG, "ConwayGameApp", "error initializeConwayGameApp " + e.toString());
            }
        }

        return ConwayGameApp.singleton;
    }

    public static ConwayGameApp getConwayGameApp() {
        if (ConwayGameApp.singleton == null)
            throw new RuntimeException("ConwayGameApp not initialized yet!");

        return ConwayGameApp.singleton;
    }

    private void setApplicationSideASAPAndroidPeer(ASAPAndroidPeer applicationSideASAPPeer) {
        this.asapAndroidPeer = applicationSideASAPPeer;
    }

    // getters/setters

    public ConwayGameEngineFacade getConwayGameEngineFacade() {
        return conwayGameEngineFacade;
    }

    public int getPlayerID() {
        return playerID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getDefaultDirectory() {
        return defaultDirectory;
    }

    private CharSequence getOwnerID() {
        return "O_ID_" + String.valueOf(getPlayerID());
    }

    public int getInterval() {
        return interval;
    }

    public void setPlayerName(Context ctx, String playerName) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PLAYER_NAME, playerName);
        editor.apply();

        this.playerName = playerName;
    }

    public void setWidth(Context ctx, int width) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(GAME_WIDTH, width);
        editor.apply();

        this.width = width;
    }

    public void setHeight(Context ctx, int height) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(GAME_HEIGHT, height);
        editor.apply();

        this.height = height;
    }

    public void setInterval(Context ctx, int interval) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(GAME_INTERVAL, interval);
        editor.apply();

        this.interval = interval;
    }

    public void resetSharedPreferences(Context ctx) {
        Log.println(Log.DEBUG, "ConwayGameApp resetSharedPreferences", "resetting shared prefs");
        SharedPreferences pref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        pref.edit().clear().commit();
    }

    public void startBluetoothExchanges(ASAPActivity asapActivity) {

        if (!bluetoothStarted) {
            asapActivity.startBluetooth();
            asapActivity.startBluetoothDiscoverable();
            asapActivity.startBluetoothDiscovery();
            bluetoothStarted = true;
        }
    }
}
