package work.android.smartbow.com.httpapplication.net;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.EntityUtils;

import java.net.Socket;

/**
 * This file was created by hellomac on 2016/10/11.
 * name: HttpApplication.
 */

public class HttpGet {

  public static void mainT() throws Exception {


    BasicHttpProcessor httpproc = new BasicHttpProcessor();

    httpproc.addInterceptor(new RequestContent());
    httpproc.addInterceptor(new RequestTargetHost());
    httpproc.addInterceptor(new RequestConnControl());
    httpproc.addInterceptor(new RequestUserAgent());
//    httpproc.addInterceptor(new RequestExpectContinue(true));


    HttpRequestExecutor httpexecutor = new HttpRequestExecutor();

    HttpCoreContext coreContext = HttpCoreContext.create();
    HttpHost host = new HttpHost("localhost", 8080);
    coreContext.setTargetHost(host);


    DefaultBHttpClientConnection conn = new DefaultBHttpClientConnection(8 * 1024);
    ConnectionReuseStrategy connStrategy = DefaultConnectionReuseStrategy.INSTANCE;

    try {

      String[] targets = {
          "/",
          "/servlets-examples/servlet/RequestInfoExample",
          "/somewhere%20in%20pampa"};

      for (int i = 0; i < targets.length; i++) {
        if (!conn.isOpen()) {
          Socket socket = new Socket(host.getHostName(), host.getPort());
          conn.bind(socket);
        }
        BasicHttpRequest request = new BasicHttpRequest("GET", targets[i]);
        System.out.println(">> Request URI: " + request.getRequestLine().getUri());

        httpexecutor.preProcess(request, httpproc, coreContext);
        HttpResponse response = httpexecutor.execute(request, conn, coreContext);
        httpexecutor.postProcess(response, httpproc, coreContext);

        System.out.println("<< Response: " + response.getStatusLine());
        System.out.println(EntityUtils.toString(response.getEntity()));
        System.out.println("==============");
        if (!connStrategy.keepAlive(response, coreContext)) {
          conn.close();
        } else {
          System.out.println("Connection kept alive...");
        }
      }
    } finally {

      conn.close();
    }
  }
}
