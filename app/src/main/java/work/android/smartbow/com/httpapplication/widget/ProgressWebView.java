package work.android.smartbow.com.httpapplication.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

import work.android.smartbow.com.httpapplication.R;

/**
 * This file was created by hellomac on 2016/10/12.
 * name: HttpApplication.
 */

public class ProgressWebView extends WebView {

  private ProgressBar progressBar;
  public ProgressWebView(Context context) {
    super(context);
    init(context,null);
  }

  public ProgressWebView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context,attrs);
  }

  public ProgressWebView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context,attrs);
  }

  @TargetApi(21)
  public ProgressWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(context,attrs);
  }

  private void init(Context context,AttributeSet attrs){
    progressBar = new ProgressBar(context,attrs,android.R.attr.progressBarStyleHorizontal);
    progressBar.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int) getResources().getDimension(R.dimen.progress_height),0,0));
    Drawable drawable = context.getResources().getDrawable(R.drawable.progress_bar_states);
    progressBar.setProgressDrawable(drawable);
    addView(progressBar);
    setWebChromeClient(new WebChromClient());
    getSettings().setSupportZoom(true);
    getSettings().setBuiltInZoomControls(true);
    getSettings().setJavaScriptEnabled(true);

  }

  public class WebChromClient extends android.webkit.WebChromeClient
  {
    @Override
    public void onProgressChanged(WebView view, int newProgress) {

      if (newProgress == 100){
        progressBar.setVisibility(GONE);
      }else{
        if (progressBar.getVisibility() == GONE) {
          progressBar.setVisibility(VISIBLE);
        }
        progressBar.setProgress(newProgress);
      }
      super.onProgressChanged(view, newProgress);
    }
  }

  @Override
  protected void onScrollChanged(int l, int t, int oldl, int oldt) {

    LayoutParams lp = (LayoutParams)progressBar.getLayoutParams();
    lp.x = l;
    lp.y = t;
    progressBar.setLayoutParams(lp);
    super.onScrollChanged(l, t, oldl, oldt);
  }
}
