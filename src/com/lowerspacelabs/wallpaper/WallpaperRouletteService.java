package com.lowerspacelabs.wallpaper;

import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.app.WallpaperManager;
import android.widget.Toast;
import android.content.Context;
import java.util.Scanner;
import java.net.URL;
import java.io.*;
import java.util.*;

/* This animated wallpaper draws a random image taken from the web.  */
public class WallpaperRouletteService extends WallpaperService {

   private static URL currentImageUrl = null;
   private static List<URL> imageList;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Engine onCreateEngine() { return new RouletteEngine(this); }

    public class RouletteEngine extends Engine {

        private long mStartTime;
        private boolean mVisible;
        private Context ctx;

        RouletteEngine(Context c) {
            ctx = c;
            mStartTime = SystemClock.elapsedRealtime();
            if(currentImageUrl == null){
               imageList = searchForImagesOf("lightning");
               currentImageUrl = selectRandomWallpaper();
            }
            drawAsWallpaper(currentImageUrl);
            setTouchEventsEnabled(true);
        }

        public URL currentUrl() { return currentImageUrl; }

        public URL selectRandomWallpaper() {
            Log.v("Roulette", "Setting random wallpaper.");
            int randomIndex = new Random().nextInt(imageList.size());
            URL randomUrl = imageList.get(randomIndex);
            Toast.makeText(ctx, 
                           "Selecting random wallpaper: " + randomUrl, 
                           Toast.LENGTH_SHORT).show();
            return randomUrl;
        }

        public List<URL> searchForImagesOf(String topic) {
           ArrayList<URL> pList = new ArrayList<URL>();
           try {
              URL sUrl = new URL("http://www.google.com/images?q=" + topic);
              String regexToMatch = "imgurl=(http.*?jpg)";
              InputStream sUrlStream = sUrl.openStream();
              Scanner imageScanner = new Scanner(sUrlStream);
              while(imageScanner.hasNext()) {
                 String token = imageScanner.findInLine(regexToMatch); 
                 if(token != null) { 
                    token = imageScanner.match().group(1);
                    Log.v("Roulette", "Found: " + token); 
                    pList.add(new URL(token));
                 }
                 imageScanner.next();
              }
              Log.v("Roulette", "No more matches.");
              sUrlStream.close();
           } catch(Exception e) {
               Toast.makeText(ctx, "Exception: " + e, Toast.LENGTH_LONG).show();
           }
           return pList;
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            // By default we don't get touch events, so enable them.
            setTouchEventsEnabled(true);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, 
                                     int format, 
                                     int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mVisible = false;
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset,
                float xStep, float yStep, int xPixels, int yPixels) {
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            Log.v("Roulette", "Touch event: " + event);
            currentImageUrl = selectRandomWallpaper();
            drawAsWallpaper(currentImageUrl);
            super.onTouchEvent(event);
        }

        void drawAsWallpaper(URL imageUrl) {
            final SurfaceHolder holder = getSurfaceHolder();
            long now = SystemClock.elapsedRealtime();
            Log.v("Roulette", "In drawAsWallpaper with " + imageUrl);
            Canvas c = null;
            try {
               InputStream bitmapStream = imageUrl.openStream();
               Bitmap image = BitmapFactory.decodeStream(bitmapStream);
               bitmapStream.close();
               c = holder.lockCanvas();
               if (c != null) { c.drawBitmap(image, 0, 0, null); }
            } catch(Exception e) {
               Toast etoast = Toast.makeText(ctx, 
                                             "Exception: " + e, 
                                             Toast.LENGTH_LONG);
               etoast.show();
            } finally {
                if (c != null) holder.unlockCanvasAndPost(c);
            }
        }
    }
}
