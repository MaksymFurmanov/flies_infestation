package sk.tuke.vmir.fliesinfestation;

import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.concurrent.atomic.AtomicReference;

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

        boolean isCustom = checkCustomImages();

        customImageStatus = findViewById(R.id.custom_status);
        customImageStatus.setOnClickListener(view -> {
            startActivity(new Intent(this, CustomPictures.class));
        });
        if(isCustom)customImageStatus.setText("CHANGE IMAGES");

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

        custom.setOnClickListener(view -> {
            if(!isCustom){
                startActivity(new Intent(this, CustomPictures.class));
            } else {
                custom.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.stroke_width));
                mosquitoes.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.no_width));
                flies.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.no_width));

                imageType = "custom";
            }
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

        startActivity(intent);
    }

    protected boolean checkCustomImages() {
        AtomicReference<Boolean> result = new AtomicReference<>(true);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        StorageReference refCustomPic = FirebaseStorage.getInstance().getReference()
                .child("users").child(user.getUid()).child("showed");
        StorageReference refCrushedPic = FirebaseStorage.getInstance().getReference()
                .child("users").child(user.getUid()).child("crushed");

        refCustomPic.getDownloadUrl().addOnFailureListener(res -> {
            result.set(false);
        });
        refCrushedPic.getDownloadUrl().addOnFailureListener(res -> {
            result.set(false);
        });

        return result.get();
    }
}