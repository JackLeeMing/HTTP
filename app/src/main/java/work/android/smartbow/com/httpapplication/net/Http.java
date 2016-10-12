package work.android.smartbow.com.httpapplication.net;

import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;

import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;

/**
 * This file was created by hellomac on 2016/10/11.
 * name: HttpApplication.
 */

public class Http {


  public static void Start(String args[]){
    if (args.length < 1) {
      System.err.println("Please specify document root directory");
      System.exit(1);
    }
    // Document root directory
    String docRoot = args[0];
    int port = 8080;
    if (args.length >= 2) {
      port = Integer.parseInt(args[1]);
    }

    try {
      createServer(port,docRoot);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }


  public static void createServer(int port,String docRoot) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {


    BasicHttpProcessor httpproc = new BasicHttpProcessor();

    httpproc.addInterceptor(new ResponseDate());
    httpproc.addInterceptor(new ResponseServer());
    httpproc.addInterceptor(new ResponseContent());
    httpproc.addInterceptor(new RequestConnControl());

    //set up request handler

    //set up the HTTP service
    HttpService httpService = new HttpService(httpproc,
        new DefaultConnectionReuseStrategy(),
        new DefaultHttpResponseFactory());

    HttpRequestHandlerRegistry reqistry = new HttpRequestHandlerRegistry();
    // 增加HTTP请求执行器
    reqistry.register("*", new HttpFileHandler(docRoot));

    // 设置HTTP请求执行器
    httpService.setHandlerResolver(reqistry);

    SSLServerSocketFactory sf = null;
    if (port == 8443){
      ClassLoader cl = Http.class.getClassLoader();
      URL url = cl.getResource("my.keystore");
      if (url == null){
        TLog.error("keystore not found");
        System.exit(1);
      }

      KeyStore keyStore = KeyStore.getInstance("jks");
      keyStore.load(url.openStream(),"secret".toCharArray());
      KeyManagerFactory keyManagerFactory
          = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
      keyManagerFactory.init(keyStore,"secret".toCharArray());
      KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();
      SSLContext sslContext = SSLContext.getInstance("TLS");
      sslContext.init(keyManagers,null,null);
      sf = sslContext.getServerSocketFactory();
    }

    Thread thread = new RequestListenerThread(port,httpService,sf);
    thread.setDaemon(true);
    thread.start();
  }

}
