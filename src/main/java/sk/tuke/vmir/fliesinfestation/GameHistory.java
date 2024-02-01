package sk.tuke.vmir.fliesinfestation;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.List;

public class GameHistory extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_history);

        ImageView backButton = findViewById(R.id.history_to_main);
        backButton.setOnClickListener(v ->
                startActivity(new Intent(this, Menu.class)));

        DbGetData dbGetData = new DbGetData();
        dbGetData.execute();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    class DbGetData extends AsyncTask<Results, Integer, List<Results>> {
        @Override
        protected List<Results> doInBackground(Results... results) {
            List<Results> data = DbTools.getDbContext(new WeakReference<>(GameHistory.this))
                    .resultsDao().getAll();
            if (data.size() == 0) {
                DbTools.getDbContext(new WeakReference<>(GameHistory.this))
                        .resultsDao().insertResults(results);
            }
            return DbTools.getDbContext(new WeakReference<>(GameHistory.this))
                    .resultsDao().getAll();
        }

        @Override
        protected void onPostExecute(List<Results> results) {
            super.onPostExecute(results);

            ResultsAdapter adapter = new ResultsAdapter(results, new WeakReference<>(GameHistory.this));
            recyclerView.setAdapter(adapter);
        }
    }
}