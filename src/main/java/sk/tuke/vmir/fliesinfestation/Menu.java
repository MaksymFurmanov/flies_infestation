package sk.tuke.vmir.fliesinfestation;

import android.content.Intent;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Menu extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(v -> startActivity(new Intent(this,
                Levels.class)));

        Button history_button = findViewById(R.id.history_button);
        history_button.setOnClickListener(v -> startActivity(new Intent(this,
                GameHistory.class)));

        Button sign_out_button = findViewById(R.id.sign_out_btn);
        sign_out_button.setOnClickListener(v -> sign_out());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        database.getReference().child("users").child(user.getUid()).child("name")
                .get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    } else {
                        ((TextView) findViewById(R.id.display_username))
                                .setText(String.valueOf(task.getResult().getValue()));
                    }
                });
    }

    protected void sign_out() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this,
                MainActivity.class));
    }
}
