package com.gamedev.dreamteam.scriptureengine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class GameEngine extends Activity {
    private boolean run;
    private Context context;
    private InputSystem inputSystemystem;
    private Log log;
    private SoundSystem soundSystemystem;
    private DataBase dataBasease;
    private GraficalEngine;
    private GLSurfaceView view;

     GameEngine(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        run = true;
    }

    public void onPause(){}
    public void onResume(){}

    public Context getContext(){return context;}
    public void changeContex(new Context cont){}

    public boolean checkSystem(){}

    public boolean isRun (){
        return run;
    }

    public void exit( int flag){}

}
