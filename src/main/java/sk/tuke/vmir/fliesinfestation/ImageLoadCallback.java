package sk.tuke.vmir.fliesinfestation;
import android.graphics.drawable.Drawable;

public interface ImageLoadCallback {
    void onImagesLoaded(Drawable appeared, Drawable crushed);
}
