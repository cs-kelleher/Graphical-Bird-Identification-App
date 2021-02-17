package kellehj1.FYP.birdID;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.View.OnTouchListener;

import kellehj1.FYP.birdID.R;

import java.util.LinkedList;
import java.util.Queue;


public class FillActivity extends AppCompatActivity implements OnTouchListener {

    private DataBaseHelper dbHelper;
    private ImageView imageView;
    private Canvas canvas;
    private Bitmap mask, original, coloured;
    private int replacementColour;

    int screenWidth  = Resources.getSystem().getDisplayMetrics().widthPixels;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill);
        //findViewById(R.id.imageView1).setOnTouchListener(this);
        imageView = (ImageView) findViewById(R.id.imageView1);
        imageView.setOnTouchListener(this);

        dbHelper = new DataBaseHelper(FillActivity.this, "TIT_TABLE", "tits.json");

        mask = BitmapFactory.decodeResource(getResources(), R.drawable.tit_mask); // Mask Image
        mask = Bitmap.createScaledBitmap(mask, screenWidth, screenWidth, true);
        original = BitmapFactory.decodeResource(getResources(), R.drawable.tit_outline); // Original Image Without Color
        original = Bitmap.createScaledBitmap(original, screenWidth, screenWidth, true);
        coloured = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Config.ARGB_8888);
        coloured = Bitmap.createScaledBitmap(coloured, screenWidth, screenWidth, true);

        canvas = new Canvas(coloured);
        canvas.drawBitmap(original, 0,0, null);
        imageView.setImageBitmap(original);
    }

    public boolean onTouch(View arg0, MotionEvent arg1) {
        int selectedPixel = mask.getPixel((int)arg1.getX(),(int)arg1.getY());

        int currentColour = coloured.getPixel((int) arg1.getX(), (int) arg1.getY());
        int maskColour = mask.getPixel((int) arg1.getX(), (int) arg1.getY());

        if (currentColour != Color.BLACK && maskColour != Color.BLACK && currentColour != replacementColour) {
            Point point = new Point((int) arg1.getX(), (int) arg1.getY());
            coloured = FloodFill(coloured, point, currentColour, replacementColour);
            imageView.setImageBitmap(coloured);
            imageView.invalidate();
        }

        String section = dbHelper.getColouredSection(maskColour);

        return true;
    }

    private Bitmap FloodFill(Bitmap bmp, Point pt, int targetColor, int replacementColour) {
        Queue<Point> q = new LinkedList<Point>();
        q.add(pt);
        while (q.size() > 0) {
            Point n = q.poll();
            if (bmp.getPixel(n.x, n.y) != targetColor)
                continue;

            Point w = n, e = new Point(n.x + 1, n.y);
            while ((w.x > 0) && (bmp.getPixel(w.x, w.y) == targetColor)) {
                bmp.setPixel(w.x, w.y, replacementColour);
                if ((w.y > 0) && (bmp.getPixel(w.x, w.y - 1) == targetColor))
                    q.add(new Point(w.x, w.y - 1));
                if ((w.y < bmp.getHeight() - 1)
                        && (bmp.getPixel(w.x, w.y + 1) == targetColor))
                    q.add(new Point(w.x, w.y + 1));
                w.x--;
            }
            while ((e.x < bmp.getWidth() - 1)
                    && (bmp.getPixel(e.x, e.y) == targetColor)) {
                bmp.setPixel(e.x, e.y, replacementColour);

                if ((e.y > 0) && (bmp.getPixel(e.x, e.y - 1) == targetColor))
                    q.add(new Point(e.x, e.y - 1));
                if ((e.y < bmp.getHeight() - 1)
                        && (bmp.getPixel(e.x, e.y + 1) == targetColor))
                    q.add(new Point(e.x, e.y + 1));
                e.x++;
            }
        }
        return bmp;
    }

    //colour functions
    public void setColourBlack(View view) {
        replacementColour = getResources().getColor(R.color.btn_black);
    }
    public void setColourGrey(View view) {
        replacementColour = getResources().getColor(R.color.btn_grey);
    }
    public void setColourWhite(View view) {
        replacementColour = getResources().getColor(R.color.btn_white);
    }
    public void setColourBrown(View view) {
        replacementColour = getResources().getColor(R.color.btn_brown);
    }
    public void setColourRed(View view) {
        replacementColour = getResources().getColor(R.color.btn_red);
    }
    public void setColourYellow(View view) {

        replacementColour = getResources().getColor(R.color.btn_yellow);
    }
    public void setColourBlue(View view) {
        replacementColour = getResources().getColor(R.color.btn_blue);
    }
    public void setColourOrange(View view) {

        replacementColour = getResources().getColor(R.color.btn_orange);
    }
    public void setColourGreen(View view) {

        replacementColour = getResources().getColor(R.color.btn_green);
    }

}