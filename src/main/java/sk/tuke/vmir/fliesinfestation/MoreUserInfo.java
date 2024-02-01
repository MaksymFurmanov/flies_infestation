package sk.tuke.vmir.fliesinfestation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MoreUserInfo extends AppCompatActivity {

    EditText username;
    FirebaseDatabase database;
    DatabaseReference ref;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_user_info);

        username = findViewById(R.id.Username);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        findViewById(R.id.save_username_btn).setOnClickListener(view -> setUsername());
    }

    protected void setUsername() {
        if (username != null) {
            FirebaseUser user = mAuth.getCurrentUser();

            HashMap<String, Object> map = new HashMap<>();
            map.put("id", user.getUid());
            map.put("name", username.getText().toString());
            map.put("email", user.getEmail());

            database.getReference().child("users").child(user.getUid()).setValue(map);
            startActivity(new Intent(MoreUserInfo.this, Menu.class));
        } else {
            Toast.makeText(MoreUserInfo.this,
                    "You need to set username",
                    Toast.LENGTH_SHORT).show();
        }
    }
}