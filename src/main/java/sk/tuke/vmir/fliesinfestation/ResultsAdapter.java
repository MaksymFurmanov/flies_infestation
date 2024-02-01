package sk.tuke.vmir.fliesinfestation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.List;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsHolder> {
    private List<Results> _data;
    private final WeakReference<Context> _context;

    public ResultsAdapter(List<Results> data, WeakReference<Context> context){
        _data = data;
        _context = context;
    }

    public void refreshData(List<Results> data){
        _data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ResultsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(_context.get())
                .inflate(R.layout.activity_history_item, viewGroup,false);
        return new ResultsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultsHolder resultsHolder, int i) {
        resultsHolder.gameDate.setText(_data.get(i).getGameDate());
        resultsHolder.result.setText(_data.get(i).getResult() ? "Won" : "Lost");

        resultsHolder.itemView.setOnClickListener(v -> {
            moreInfo(_data.get(i));
        });
    }

    @Override
    public int getItemCount() {
        if(_data!=null)return _data.size();
        return 0;
    }

    public void moreInfo(Results results){
        Intent intent = new Intent(_context.get(), ResultsInfoActivity.class);

        intent.putExtra("Date", results.getGameDate());
        intent.putExtra("Level", results.getLevel());
        intent.putExtra("Killed", String.valueOf(results.getKilled()));
        intent.putExtra("Max", String.valueOf(results.getMax()));
        intent.putExtra("Result", results.getResult() ? "Won" : "Lost");

        _context.get().startActivity(intent);
    }
}
