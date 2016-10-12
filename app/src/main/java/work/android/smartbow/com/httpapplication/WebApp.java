package work.android.smartbow.com.httpapplication;

import android.os.Bundle;

import com.thefinestartist.finestwebview.FinestWebViewActivity;

/**
 * This file was created by hellomac on 2016/10/12.
 * name: HttpApplication.
 */

public class WebApp extends FinestWebViewActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    webView.loadUrl("file:///android_asset/www/index_fall.html");
  }

}
