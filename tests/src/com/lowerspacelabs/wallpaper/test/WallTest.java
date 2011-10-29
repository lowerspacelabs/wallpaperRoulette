package com.lowerspacelabs.wallpaper.WallpaperRoulette.test;

import junit.framework.Assert;
import android.test.ServiceTestCase;

import java.net.URL;
import java.util.List;
import android.content.Intent;
import com.lowerspacelabs.wallpaper.WallpaperRouletteService;
import com.lowerspacelabs.wallpaper.WallpaperRouletteService.RouletteEngine;

public class WallTest extends ServiceTestCase<WallpaperRouletteService> { 

   public WallTest() { super(WallpaperRouletteService.class); }

   private RouletteEngine mainEngine;
   private RouletteEngine previewEngine;

   protected void setUp() throws Exception { super.setUp();
      this.startService(
            new Intent("android.service.wallpaper.WallpaperService"));
      mainEngine = (RouletteEngine) this.getService().onCreateEngine();
      previewEngine = (RouletteEngine) this.getService().onCreateEngine();
   }

   public void testWallpaper() {
      Assert.assertNotNull(mainEngine);
      Assert.assertNotNull(previewEngine);
      Assert.assertEquals(mainEngine.currentUrl(), previewEngine.currentUrl());
   }

   public void testPaperSearch() {
      List<URL> testList = mainEngine.searchForImagesOf("lightning");
      Assert.assertFalse(testList.size() == 0);
      URL randomImageUrl = mainEngine.selectRandomWallpaper();
      Assert.assertTrue(testList.contains(randomImageUrl));
   }
}
