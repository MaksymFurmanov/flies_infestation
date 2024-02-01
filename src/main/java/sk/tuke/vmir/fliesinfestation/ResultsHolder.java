package sk.tuke.vmir.fliesinfestation;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ResultsHolder extends RecyclerView.ViewHolder {
    TextView gameDate;
    TextView result;

    public ResultsHolder(@NonNull View itemView) {
        super(itemView);
        gameDate = itemView.findViewById(R.id.res_date);
        result = itemView.findViewById(R.id.res_result);
    }
}
