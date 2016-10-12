package work.android.smartbow.com.httpapplication;

import android.app.Application;

/**
 * This file was created by hellomac on 2016/10/11.
 * name: HttpApplication.
 */

public class App extends Application {
  private static App appContext;

  @Override
  public void onCreate() {
    super.onCreate();
    appContext = this;
  }

  public static App getAppContext(){
    return appContext;
  }
}
