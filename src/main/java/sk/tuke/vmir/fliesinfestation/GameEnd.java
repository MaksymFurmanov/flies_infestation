package sk.tuke.vmir.fliesinfestation;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class GameEnd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_end);

        Intent intent = getIntent();
        boolean result = intent.getBooleanExtra("result", false);
        int score = intent.getIntExtra("score", 0);
        String level = intent.getStringExtra("level");
        String imageType = intent.getStringExtra("imageType");

        TextView resultView = findViewById(R.id.resultView);
        TextView scoreView = findViewById(R.id.scoreView);
        Button tryAgain = findViewById(R.id.try_again_button);
        Button levels = findViewById(R.id.levels_button);
        Button mainMenu = findViewById(R.id.main_menu_button);

        String resultText = result ? "You won!" : "You lose";
        resultView.setText(resultText);

        String scoreText = "You crushed " + score;
        scoreView.setText(scoreText);

        tryAgain.setOnClickListener(v -> {
            Intent newIntent;
            switch (level) {
                case "Easy":
                    newIntent = new Intent(this, Level_1.class);
                    break;
                case "Normal":
                    newIntent = new Intent(this, Level_2.class);
                    break;
                case "Hard":
                    newIntent = new Intent(this, Level_3.class);
                    break;
                default:
                    newIntent = new Intent(this, Menu.class);
            }

            newIntent.putExtra("imageType", imageType);
            startActivity(newIntent);
        });

        levels.setOnClickListener(v -> {
            startActivity(new Intent(this, Levels.class));
        });

        mainMenu.setOnClickListener(v -> {
            startActivity(new Intent(this, Menu.class));
        });
    }
}