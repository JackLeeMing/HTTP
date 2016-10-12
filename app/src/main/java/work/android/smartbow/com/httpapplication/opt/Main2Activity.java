package work.android.smartbow.com.httpapplication.opt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import work.android.smartbow.com.httpapplication.R;
import work.android.smartbow.com.httpapplication.widget.ProgressWebView;

public class Main2Activity extends AppCompatActivity {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main2);

    final ProgressWebView webView = (ProgressWebView) findViewById(R.id.js_webView);
    webView.loadUrl("file:///android_asset/www/index_fall.html");

  }

  @Override
  protected void onStop() {
    super.onStop();
  }
}
