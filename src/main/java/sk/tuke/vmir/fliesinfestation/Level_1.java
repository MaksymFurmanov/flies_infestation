package sk.tuke.vmir.fliesinfestation;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import java.util.Random;

public class Level_1 extends GameLevel {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_level);

        spawnRate = 3;
        disappearRate = 5;
        maxGenerate = 15;
        maxFail = 6;

        parentLayout = this.findViewById(R.id.parentLayout);
        random = new Random();
        handler = new Handler(Looper.getMainLooper());

        generateCounterView = this.findViewById(R.id.generate_counter);
        String newGenerateCounter = "Flies left: " + maxGenerate;
        generateCounterView.setText(newGenerateCounter);

        failCounterView = this.findViewById(R.id.fail_counter);
        String newFailCounter = "Missed: " + failCounter + "/" + maxFail;
        failCounterView.setText(newFailCounter);

        generateRandomImagePeriodically();
    }

    @Override
    protected String getCurrentLevel(){
        return "Easy";
    }
}