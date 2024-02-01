package sk.tuke.vmir.fliesinfestation;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public abstract class GameLevel extends AppCompatActivity {
    public RelativeLayout parentLayout;

    FirebaseUser user;

    String imageType;
    Drawable appeared;
    Drawable crushed;

    public Random random;
    public Handler handler;

    public float spawnRate;
    public float disappearRate;
    public int generateCounter = 0;
    public int maxGenerate;
    public TextView generateCounterView;

    public int failCounter = 0;
    public int maxFail;
    public TextView failCounterView;

    public int score = 0;
    private int result = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageType = getIntent().getStringExtra("imageType");
        user = FirebaseAuth.getInstance().getCurrentUser();
        switch (Objects.requireNonNull(imageType)) {
            case "mosquito":
                appeared = getResources().getDrawable(R.drawable.mosquito);
                crushed = getResources().getDrawable(R.drawable.crushed_mosquito);
                break;
            case "custom":
                getCustomImages();
                break;
            default:
                appeared = getResources().getDrawable(R.drawable.fly);
                crushed = getResources().getDrawable(R.drawable.crushed_fly_removebg_preview);
                break;
        }
    }

    public void generateRandomImagePeriodically() {
        if (result == -1) {
            handler.postDelayed(() -> {
                if (generateCounter < maxGenerate) {
                    DrawView randomDrawView;
                    randomDrawView = new DrawView(this, appeared);

                    RelativeLayout.LayoutParams layoutParams =
                            new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT
                            );
                    int maxWidth = parentLayout.getWidth();
                    int maxHeight = parentLayout.getHeight();
                    int minWidth = 150;
                    int maxWidthImage = 300;
                    int maxHeightImage = 300;

                    int[] randomPosition =
                            findPosition(maxWidth, maxHeight, maxWidthImage, maxHeightImage);

                    if (randomPosition != null) {
                        int randomSize = random.nextInt(maxWidthImage - minWidth) + minWidth;
                        layoutParams.width = randomSize;
                        layoutParams.height = randomSize;
                        layoutParams.leftMargin = randomPosition[0];
                        layoutParams.topMargin = randomPosition[1];
                        randomDrawView.setLayoutParams(layoutParams);

                        randomDrawView.setOnTouchListener((view, motionEvent) -> {
                            float x = motionEvent.getX();
                            float y = motionEvent.getY();

                            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                                if (view instanceof DrawView) {
                                    DrawView drawView = (DrawView) view;
                                    drawView.setOnTouchListener(null);
                                    if (drawView.isIntersecting(x, y)) {
                                        drawView.setDrawable(crushed);

                                        if (drawView.getParent() != null
                                                && drawView.getVisibility() == View.VISIBLE) {
                                            drawView.performClick();
                                            score++;

                                            dissolveView(drawView);
                                            handler.postDelayed(() -> {
                                                if (randomDrawView.getParent() != null) {
                                                    ((ViewGroup) randomDrawView.getParent())
                                                            .removeView(randomDrawView);
                                                }
                                            }, 1500);

                                            if(generateCounter > maxGenerate){
                                                result = 1;
                                                handleGameEnd();
                                            }
                                        }
                                    }
                                }
                            }
                            return true;
                        });

                        parentLayout.addView(randomDrawView, layoutParams);
                        generateCounter++;
                        String newCounterText = "Left: " + (maxGenerate - generateCounter);
                        generateCounterView.setText(newCounterText);

                        handler.postDelayed(() -> {
                            randomDrawView.setOnTouchListener(null);
                            if (randomDrawView.getParent() != null
                                    && randomDrawView.getVisibility() == View.VISIBLE) {
                                dissolveView(randomDrawView);
                                handler.postDelayed(() -> {
                                    if (randomDrawView.getParent() != null) {
                                        ((ViewGroup) randomDrawView.getParent())
                                                .removeView(randomDrawView);
                                        failCounter++;
                                    }
                                }, 1500);

                                String newFailCounter = "Missed: " + failCounter + "/" + maxFail;
                                failCounterView.setText(newFailCounter);

                                if (maxFail == failCounter) {
                                    result = 0;
                                    handleGameEnd();
                                }
                            }
                        }, (int) disappearRate * 1000);
                        generateRandomImagePeriodically();
                    }
                } else {
                    result = 1;
                    handleGameEnd();
                }
            }, (int) spawnRate * 1000);
        } else {
            handleGameEnd();
        }
    }

    public void handleGameEnd() {
        Intent intent = new Intent(this, GameEnd.class);
        intent.putExtra("result", result != 0);
        intent.putExtra("score", score);
        intent.putExtra("level", getCurrentLevel());
        intent.putExtra("imageType", imageType);

        Date dateTime = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        Results results = new Results(format.format(dateTime), getCurrentLevel(),
                score, maxGenerate, result != 0);
        DbPutData putData = new DbPutData();
        putData.execute(results);

        startActivity(intent);
        finish();
    }

    private int @Nullable [] findPosition(int maxWidth, int maxHeight,
                                          int maxWidthImage, int maxHeightImage) {
        Random random = new Random();
        boolean collision;
        int randomX, randomY;

        for (int attempts = 0; attempts < 100; attempts++) {
            collision = false;
            randomX = random.nextInt(maxWidth - maxWidthImage);
            randomY = random.nextInt(maxHeight - maxHeightImage);

            for (int i = 0; i < parentLayout.getChildCount(); i++) {
                View view = parentLayout.getChildAt(i);
                int viewX = (int) view.getX();
                int viewY = (int) view.getY();

                int existingWidth = view.getWidth();
                int existingHeight = view.getHeight();

                if (randomX < viewX + existingWidth &&
                        randomX + maxWidthImage > viewX &&
                        randomY < viewY + existingHeight &&
                        randomY + maxHeightImage > viewY) {
                    collision = true;
                    break;
                }
            }

            if (!collision) {
                return new int[]{randomX, randomY};
            }
        }
        return null;
    }

    private void dissolveView(View view) {
        Fade fadeOut = new Fade();
        fadeOut.setDuration(1500);
        fadeOut.addTarget(view);

        TransitionManager.beginDelayedTransition((ViewGroup) view.getParent(), fadeOut);

        view.setVisibility(View.GONE);
    }

    protected abstract String getCurrentLevel();

    class DbPutData extends AsyncTask<Results, Integer, List<Results>> {
        @Override
        protected List<Results> doInBackground(Results... results) {
            DbTools.getDbContext(new WeakReference<>(GameLevel.this))
                    .resultsDao().insertResults(results);
            return null;
        }

        @Override
        protected void onPostExecute(List<Results> results) {
            super.onPostExecute(results);
        }
    }

    protected void getCustomImages() {
        StorageReference refCustomPic = FirebaseStorage.getInstance().getReference()
                .child("users").child(user.getUid()).child("showed");
        StorageReference refCrushedPic = FirebaseStorage.getInstance().getReference()
                .child("users").child(user.getUid()).child("crushed");

        refCustomPic.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(GameLevel.this).load(uri).into(new CustomTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource,
                                            @Nullable Transition<? super Drawable> transition) {
                    appeared = resource;
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {
                }
            });
        });

        refCrushedPic.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(GameLevel.this).load(uri).into(new CustomTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource,
                                            @Nullable Transition<? super Drawable> transition) {
                    crushed = resource;
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {
                }
            });
        });
    }
}