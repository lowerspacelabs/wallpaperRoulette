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
   private static Bitmap currentImage = null;
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
            drawWallpaper(currentImage);
        }

        public URL currentUrl() { return currentImageUrl; }

        public URL selectRandomWallpaper() {
            int randomIndex = new Random().nextInt(imageList.size());
            URL randomUrl = imageList.get(randomIndex);
            Toast.makeText(ctx, 
                           "Selected random wallpaper: " + randomUrl, 
                           Toast.LENGTH_LONG).show();
            try {
               InputStream bitmapStream = randomUrl.openStream();
               currentImage = BitmapFactory.decodeStream(bitmapStream);
               bitmapStream.close();
            } catch(Exception e) {
               Toast.makeText(ctx, 
                                "Exception: " + e, 
                                Toast.LENGTH_LONG).show();
            }
            return randomUrl;
        }

        public List<URL> searchForImagesOf(String topic) {
           ArrayList<URL> pList = new ArrayList<URL>();
           try {
              URL sUrl = new URL("http://www.google.com/images?q=" + topic);
              String regexToMatch = "imgurl=(http.*?(jpg|jpeg|png))";
              InputStream sUrlStream = sUrl.openStream();
              Scanner imageScanner = new Scanner(sUrlStream);
              while(imageScanner.hasNext()) {
                 String token = imageScanner.findInLine(regexToMatch); 
                 if(token != null) { 
                    token = imageScanner.match().group(1);
                    pList.add(new URL(token));
                 }
                 imageScanner.next();
              }
              sUrlStream.close();
           } catch(Exception e) {
               Toast.makeText(ctx, 
                              "Exception: " + e, 
                              Toast.LENGTH_LONG).show();
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
           if(event.getAction() == MotionEvent.ACTION_UP) {
            currentImageUrl = selectRandomWallpaper();
            drawWallpaper(currentImage);
           }
           super.onTouchEvent(event);
        }

        void drawWallpaper(Bitmap wallImage) {
            Canvas c = null;
            final SurfaceHolder holder = getSurfaceHolder();
            try {
               c = holder.lockCanvas();
               if (c != null) { c.drawBitmap(wallImage, 0, 0, null); }
            } finally {
                if (c != null) holder.unlockCanvasAndPost(c);
            }
        }
    }
}
