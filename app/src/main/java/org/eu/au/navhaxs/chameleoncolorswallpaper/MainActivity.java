package org.eu.au.navhaxs.chameleoncolorswallpaper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.app.WallpaperManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "org.eu.au.navhaxs.chameleoncolorswallpaper.MESSAGE";

    private Window window;
    private String hexcolor;
    private int c;

    View colorView;
    SeekBar skRed;
    SeekBar skGreen;
    SeekBar skBlue;
    TextView lbl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        colorView = (View) findViewById(R.id.colorView);
        skRed = (SeekBar) findViewById(R.id.seekbarRed);
        skGreen = (SeekBar) findViewById(R.id.seekbarGreen);
        skBlue = (SeekBar) findViewById(R.id.seekbarBlue);
        lbl = (TextView) findViewById(R.id.hexcolorLabel);

        c = Color.parseColor("black");

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(c);
            }
            colorView.setBackgroundColor(c);
        } finally {

        }
        OnSeekBarChangeListener listener = new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                hexcolor = "#" + String.format("%02X", skRed.getProgress()) +  String.format("%02X", skGreen.getProgress()) + String.format("%02X", skBlue.getProgress());

                lbl.setText(hexcolor);
                c = Color.parseColor(hexcolor);

                try {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.setStatusBarColor(c);
                    }
                    colorView.setBackgroundColor(c);
                } finally {

                }
            }
        };

        skRed.setOnSeekBarChangeListener(listener);
        skGreen.setOnSeekBarChangeListener(listener);
        skBlue.setOnSeekBarChangeListener(listener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * @param view
     */
    public void sendMessage(View view) {
//        Intent intent = new Intent(this, DisplayMessageActivity.class);


    }

    public void setWallpaper(View v) {



        new Thread(new Runnable() {
            public void run() {
                WallpaperManager wp = WallpaperManager.getInstance(getApplicationContext());
                try {
                    Display display = getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    int width = size.x;
                    int height = size.y;

                    int longside = (width > height) ? width : height;

                    Rect rect = new Rect(0, 0, longside, longside);
                    Bitmap image = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);//
                    Canvas canvas = new Canvas(image);
                    Paint paint = new Paint();
                    paint.setColor(c);
                    canvas.drawRect(rect, paint);

                    wp.setBitmap(image);

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            //Your UI code here
                            Toast.makeText(getApplicationContext(), "Wallpaper set to " + hexcolor, Toast.LENGTH_LONG).show();
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            //Your UI code here
                            Toast.makeText(getApplicationContext(), "Failed to set the wallpaper :(", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        }).start();

    }
}
