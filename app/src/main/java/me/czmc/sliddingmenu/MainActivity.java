package me.czmc.sliddingmenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import me.czmc.sliddingmenu.R;
import me.czmc.sliddingmenu.view.SliddingView;


public class MainActivity extends Activity {
    public SliddingView siddingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        siddingView = (SliddingView)findViewById(R.id.sliddingMenu);
    }
    public void toggleMenu(View v){
        siddingView.toggleMenu();
    }
}
