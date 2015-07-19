package com.zhuchao.freetime;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import view_rewrite.PlayerViewTest;


public class Test extends Activity {

    private PlayerViewTest playerViewTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.test);

        playerViewTest=(PlayerViewTest)findViewById(R.id.play_video);

        playerViewTest.setUrl("http://2b2726fb22467.cdn.sohucs.com/56ac66d690ffc98e77f321d91bd45b85.mp4");

    }
}
