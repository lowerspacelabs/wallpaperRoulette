package com.lowerspacelabs.wallpaperRoulette.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.lowerspacelabs.wallpaperRoulette.HelloWallpaper;

public class HelloWallpaperTest extends ActivityInstrumentationTestCase2<HelloWallpaper> {
	private HelloWallpaper mActivity;
    private TextView mView;
    private String resourceString;

	public HelloWallpaperTest() {
	      super("com.lowerspacelabs.wallpaperRoulette", HelloWallpaper.class);
	}

	@Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = this.getActivity();
        mView = (TextView) mActivity.findViewById(com.lowerspacelabs.wallpaperRoulette.R.id.textview);
        resourceString = mActivity.getString(com.lowerspacelabs.wallpaperRoulette.R.string.hello);
    }

	public void testPreconditions() {
	      assertNotNull(mView);
	}

	public void testText() {
	      assertEquals(resourceString,(String)mView.getText());
	}
}
