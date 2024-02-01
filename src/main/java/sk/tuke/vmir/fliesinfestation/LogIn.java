package sk.tuke.vmir.fliesinfestation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText email_input;
    private EditText password_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        email_input = findViewById(R.id.login);
        password_input = findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.login_btn).setOnClickListener(view -> signIn());
    }

    private void signIn() {
        mAuth.signInWithEmailAndPassword(email_input.getText().toString(), password_input.getText().toString())
                .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(LogIn.this, Menu.class));
                            } else {
                                Toast.makeText(LogIn.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                );
    }
}