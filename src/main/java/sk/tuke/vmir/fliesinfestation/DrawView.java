package sk.tuke.vmir.fliesinfestation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

public class DrawView extends View {
    private Drawable drawable;
    private final Rect rect;

    public DrawView(Context context) {
        super(context);
        rect = new Rect();
    }
    public DrawView(Context context, Drawable drawableObject) {
        super(context);
        drawable = drawableObject;
        rect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        rect.set(0, 0, getWidth(), getHeight());
        drawable.setBounds(rect);
        drawable.draw(canvas);
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    public boolean isIntersecting(float x, float y) {
        return rect.contains((int) x, (int) y);
    }

    public void setDrawable(int objectId) {
        drawable = getResources().getDrawable(objectId);
    }

    public void setDrawable(Drawable drawableObject) {
        drawable = drawableObject;
    }
}