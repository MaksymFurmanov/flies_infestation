package sk.tuke.vmir.fliesinfestation;

import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

interface CustomImagesCheckListener {
    void onShowedImageCheck(boolean isCustom);

    void onCrushedImageCheck(boolean isCrushed);
}

public class Levels extends AppCompatActivity {
    TextView customImageStatus;

    String imageType = "fly";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        ImageView backButton = findViewById(R.id.levels_to_main);
        backButton.setOnClickListener(v ->
                startActivity(new Intent(this, Menu.class)));

        Button lvl1 = this.findViewById(R.id.lvl_1);
        Button lvl2 = this.findViewById(R.id.lvl_2);
        Button lvl3 = this.findViewById(R.id.lvl_3);

        lvl1.setOnClickListener(v -> start(1));
        lvl2.setOnClickListener(v -> start(2));
        lvl3.setOnClickListener(v -> start(3));

        MaterialButton flies = findViewById(R.id.flies_btn);
        MaterialButton mosquitoes = findViewById(R.id.mosquitoes_btn);
        MaterialButton custom = findViewById(R.id.custom_btn);

        customImageStatus = findViewById(R.id.custom_status);
        customImageStatus.setOnClickListener(view -> {
            startActivity(new Intent(Levels.this, CustomPictures.class));
        });

        checkCustomImages(new CustomImagesCheckListener() {
            private boolean isCustom = false;
            private boolean isCrushed = false;

            @Override
            public void onShowedImageCheck(boolean customResult) {
                isCustom = customResult;
                updateCustomImageStatus();
            }

            @Override
            public void onCrushedImageCheck(boolean crushedResult) {
                isCrushed = crushedResult;
                updateCustomImageStatus();
            }

            private void updateCustomImageStatus() {
                customImageStatus.post(() -> {
                    if (isCustom && isCrushed) {
                        customImageStatus.setText("CHANGE IMAGES");
                    } else {
                        customImageStatus.setText("SET IMAGES");
                    }
                });
            }
        });

        custom.setOnClickListener(view -> {
            custom.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.stroke_width));
            mosquitoes.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.no_width));
            flies.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.no_width));

            imageType = "custom";
        });

        flies.setOnClickListener(view -> {
            flies.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.stroke_width));
            mosquitoes.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.no_width));
            custom.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.no_width));

            imageType = "fly";
        });

        mosquitoes.setOnClickListener(view -> {
            mosquitoes.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.stroke_width));
            flies.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.no_width));
            flies.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.no_width));
            custom.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.no_width));

            imageType = "mosquito";
        });
    }

    protected void start(int level) {
        Intent intent;
        switch (level) {
            case 2:
                intent = new Intent(this, Level_2.class);
                break;
            case 3:
                intent = new Intent(this, Level_3.class);
                break;
            default:
                intent = new Intent(this, Level_1.class);
        }

        intent.putExtra("imageType", imageType);
        if(!imageType.equals("custom") || customImageStatus.getText().equals("CHANGE IMAGES"))
            startActivity(intent);
        else Toast.makeText(Levels.this, "Custom images aren't set",
                    Toast.LENGTH_LONG).show();
    }

    protected void checkCustomImages(CustomImagesCheckListener listener) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        StorageReference refCustomPic = FirebaseStorage.getInstance().getReference()
                .child("users").child(user.getUid()).child("showed");
        StorageReference refCrushedPic = FirebaseStorage.getInstance().getReference()
                .child("users").child(user.getUid()).child("crushed");

        Task<StorageMetadata> customTask = refCustomPic.getMetadata().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listener.onShowedImageCheck(task.getResult() != null);
            } else {
                listener.onShowedImageCheck(false);
            }
        });

        Task<StorageMetadata> crushedTask = refCrushedPic.getMetadata().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listener.onCrushedImageCheck(task.getResult() != null);
            } else {
                listener.onCrushedImageCheck(false);
            }
        });

        Tasks.whenAllComplete(customTask, crushedTask);
    }
}