package sk.tuke.vmir.fliesinfestation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CustomPictures extends AppCompatActivity {

    ImageView customPic;
    ImageView crushedPic;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_pictures);

        ImageView backButton = findViewById(R.id.customPictures_to_levels);
        backButton.setOnClickListener(v ->
                startActivity(new Intent(this, Levels.class)));

        customPic = findViewById(R.id.custom_pic);
        crushedPic = findViewById(R.id.custom_crushed_pic);
        user = FirebaseAuth.getInstance().getCurrentUser();

        getImages();

        customPic.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        });

        crushedPic.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, 2);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1) {
            saveImageToStorage(data, 1);
        }
        if (requestCode == 2) {
            saveImageToStorage(data, 2);
        }
    }

    protected void saveImageToStorage(Intent data, int type) {
        String imgType;
        switch (type) {
            case 1:
                imgType = "showed";
                break;
            case 2:
                imgType = "crushed";
                break;
            default:
                imgType = "";
        }

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("users").child(user.getUid())
                .child(imgType);


        UploadTask uploadTask = imageRef.putFile(data.getData());

        uploadTask.addOnSuccessListener(taskSnapshot -> {
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                database.child("users").child(user.getUid()).child(imgType)
                        .setValue(uri.toString());
                getImages();
            });
        }).addOnFailureListener(exception -> {
        });
    }

    private void getImages() {

        StorageReference refCustomPic = FirebaseStorage.getInstance().getReference()
                .child("users").child(user.getUid()).child("showed");
        StorageReference refCrushedPic = FirebaseStorage.getInstance().getReference()
                .child("users").child(user.getUid()).child("crushed");

        refCustomPic.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(CustomPictures.this).load(uri).into(customPic);
        });

        refCrushedPic.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(CustomPictures.this).load(uri).into(crushedPic);
        });
    }

}
