package sk.tuke.vmir.fliesinfestation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    private EditText email_input;
    private EditText password_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email_input = findViewById(R.id.sign_up_email);
        password_input = findViewById(R.id.sign_up_password);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.sign_up_btn).setOnClickListener(view -> signUp());
    }

    private void signUp() {
        mAuth.createUserWithEmailAndPassword(email_input.getText().toString(),
                        password_input.getText().toString())
                .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(SignUp.this, MoreUserInfo.class));
                            } else {
                                Toast.makeText(SignUp.this, "Authentication failed. "
                                                + task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                );
    }
}