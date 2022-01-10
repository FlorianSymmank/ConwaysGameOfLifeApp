package de.floriansymmank.conwaysgameoflife;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import de.floriansymmank.conwaysgameoflife.activities.GameActivity;
import de.floriansymmank.conwaysgameoflife.activities.SavedGamesListActivity;

public class NormalDrawer implements Drawer.OnDrawerItemClickListener {

    private final Activity activity;
    private Drawer drawer;

    public NormalDrawer(Activity activity) {
        this.activity = activity;
    }

    public static Drawer createNormalDrawer(Activity activity) {

        NormalDrawer nd = new NormalDrawer(activity);

        PrimaryDrawerItem i = new PrimaryDrawerItem();
        SecondaryDrawerItem item1 = new SecondaryDrawerItem().withIdentifier(1).withName("New Conway's Game of Life");
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName("Saved Games");
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(3).withName("Saved Sores");

        Drawer drawer = new DrawerBuilder()
                .withActivity(activity)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(false)
                .addDrawerItems(i, item1, item2, item3)
                .withOnDrawerItemClickListener(nd).build();

        nd.setDrawer(drawer);

        return drawer;
    }

    private void setDrawer(Drawer drawer) {
        this.drawer = drawer;
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

        Intent intent;
        switch ((int) drawerItem.getIdentifier()) {
            case 1:
                if (activity.getClass() == GameActivity.class)
                    drawer.closeDrawer();
                else
                    activity.startActivity(new Intent(activity.getApplicationContext(), GameActivity.class));
                return true;
            case 2:
                if (activity.getClass() == SavedGamesListActivity.class)
                    drawer.closeDrawer();
                else
                    activity.startActivity(new Intent(activity.getApplicationContext(), SavedGamesListActivity.class));
                break;
            default:
                return false;

        }
        return true;
    }
}

