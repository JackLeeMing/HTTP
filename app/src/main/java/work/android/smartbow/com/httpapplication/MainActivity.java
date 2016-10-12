package work.android.smartbow.com.httpapplication;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{


  ImageView imageView;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

   imageView = (ImageView)findViewById(R.id.imageView);

    findViewById(R.id.btn_click_01).setOnClickListener(this);
    findViewById(R.id.btn_click_02).setOnClickListener(this);
    findViewById(R.id.btn_click_03).setOnClickListener(this);

  }

  private Handler handler = new Handler(new Handler.Callback() {
    @Override
    public boolean handleMessage(Message msg) {

      if (msg != null && msg.what == 1){
        Bitmap bitmap = (Bitmap) msg.obj;

        imageView.setImageBitmap(bitmap);
      }
      return false;
    }
  });

  private void getBitMap() {

    String urlStr = "http://192.168.31.92:8080/";
    HttpURLConnection httpURLConnection = null;
    try {
      //String pam = URLEncoder.encode(urlStr,"UTF-8");
      try {
        URL url = new URL(urlStr);
        try {
          httpURLConnection = (HttpURLConnection)url.openConnection();
          httpURLConnection.setDoInput(true);
          httpURLConnection.setDoInput(true);
          httpURLConnection.setRequestMethod("POST");
          httpURLConnection.setConnectTimeout(8000);
          httpURLConnection.setReadTimeout(8000);

          String strs = "Hello 你好，在忙啥呢?";
          byte[] re = strs.getBytes("UTF-8");

          httpURLConnection.setRequestProperty("Content-Type", "application/octet-stream");
          httpURLConnection.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
          httpURLConnection.setRequestProperty("Charset", "UTF-8");
//
          String name = URLEncoder.encode("明怀", "utf-8");
          httpURLConnection.setRequestProperty("NAME", name);

          OutputStream outputStream = httpURLConnection.getOutputStream();
          outputStream.write(re);
          outputStream.flush();
          outputStream.close();


          int status_code = httpURLConnection.getResponseCode();
          if (status_code == 200){
            StringBuffer sb = new StringBuffer();
            String readLine;
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"utf-8"));
            while ((readLine = reader.readLine()) != null){
              sb.append(readLine).append("\n");
            }

            reader.close();
            Message message = Message.obtain();
            message.what = 1;
            message.obj = sb.toString();
            handler.removeMessages(1);

            handler.sendMessage(message);

          }


        } catch (IOException e) {
          e.printStackTrace();
        }
      } catch (MalformedURLException e) {
        e.printStackTrace();
      }
    } finally {
      if (httpURLConnection != null){
        httpURLConnection.disconnect();
      }
    }
  }

  private void getBit(){


    ExecutorService service = Executors.newCachedThreadPool();
    service.submit(new Runnable() {
      @Override
      public void run() {
        getBitMap();
      }
    });

    service.shutdown();

  }



  public void onClick(View view){
    switch (view.getId()){
      case R.id.btn_click_01 :
        getBit();
        break;

      case R.id.btn_click_02:
        break;

      case R.id.btn_click_03:
        break;
    }
  }
}
