package sk.tuke.vmir.fliesinfestation;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class ResultsInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_info);

        ImageView backButton = findViewById(R.id.info_to_history);

        backButton.setOnClickListener(v ->
                startActivity(new Intent(this, GameHistory.class)));

        TextView date = findViewById(R.id.Date);
        TextView level = findViewById(R.id.Level);
        TextView killed = findViewById(R.id.Killed);
        TextView max = findViewById(R.id.Max);
        TextView result = findViewById(R.id.Result);

        date.setText(getIntent().getStringExtra("Date"));
        level.setText(getIntent().getStringExtra("Level"));
        killed.setText(getIntent().getStringExtra("Killed"));
        max.setText(getIntent().getStringExtra("Max"));
        result.setText(getIntent().getStringExtra("Result"));
    }
}