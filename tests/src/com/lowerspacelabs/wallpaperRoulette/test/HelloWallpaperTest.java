package com.lowerspacelabs.wallpaperRoulette.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import com.lowerspacelabs.wallpaperRoulette.HelloWallpaper;

public class HelloWallpaperTest extends ActivityInstrumentationTestCase2<HelloWallpaper> {
	private HelloWallpaper mActivity;
    private Button mView;
    private String resourceString;

	public HelloWallpaperTest() {
	      super("com.lowerspacelabs.wallpaperRoulette", HelloWallpaper.class);
	}

	@Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = this.getActivity();
        mView = (Button) mActivity.findViewById(com.lowerspacelabs.wallpaperRoulette.R.id.searchButton);
        resourceString = mActivity.getString(com.lowerspacelabs.wallpaperRoulette.R.string.searchButtonText);
    }

	public void testPreconditions() {
	      assertNotNull(mView);
	}

	public void testText() {
	      assertEquals(resourceString,(String)mView.getText());
	}
}
