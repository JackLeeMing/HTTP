package work.android.smartbow.com.httpapplication.net;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.net.Socket;

/**
 * This file was created by hellomac on 2016/10/11.
 * name: HttpApplication.
 */

public class HttpPost {
  public static void mainT() throws Exception {


    BasicHttpProcessor httpproc = new BasicHttpProcessor();

    httpproc.addInterceptor(new RequestContent());
    httpproc.addInterceptor(new RequestTargetHost());
    httpproc.addInterceptor(new RequestConnControl());
    httpproc.addInterceptor(new RequestUserAgent());
    httpproc.addInterceptor(new RequestExpectContinue(true));

    HttpRequestExecutor httpexecutor = new HttpRequestExecutor();

    HttpCoreContext coreContext = HttpCoreContext.create();
    HttpHost host = new HttpHost("localhost", 8080);
    coreContext.setTargetHost(host);

    DefaultBHttpClientConnection conn = new DefaultBHttpClientConnection(8 * 1024);
    ConnectionReuseStrategy connStrategy = DefaultConnectionReuseStrategy.INSTANCE;

    try {
      HttpEntity[] requestBodies = {
          new StringEntity(
              "This is the first test request",
              ContentType.create("text/plain", Consts.UTF_8)),
          new ByteArrayEntity(
              "This is the second test request".getBytes("UTF-8"),
              ContentType.APPLICATION_OCTET_STREAM),
          new InputStreamEntity(
              new ByteArrayInputStream(
                  "This is the third test request (will be chunked)"
                      .getBytes("UTF-8")),
              ContentType.APPLICATION_OCTET_STREAM)
      };

      for (int i = 0; i < requestBodies.length; i++) {
        if (!conn.isOpen()) {
          Socket socket = new Socket(host.getHostName(), host.getPort());
          conn.bind(socket);
        }
        BasicHttpEntityEnclosingRequest request = new BasicHttpEntityEnclosingRequest("POST",
            "/servlets-examples/servlet/RequestInfoExample");
        request.setEntity(requestBodies[i]);
        TLog.error(">> Request URI: " + request.getRequestLine().getUri());

        httpexecutor.preProcess(request, httpproc, coreContext);
        HttpResponse response = httpexecutor.execute(request, conn, coreContext);
        httpexecutor.postProcess(response, httpproc, coreContext);

        TLog.error("<< Response: " + response.getStatusLine());
        TLog.error(EntityUtils.toString(response.getEntity()));
        TLog.error("==============");
        if (!connStrategy.keepAlive(response, coreContext)) {
          conn.close();
        } else {
          TLog.error("Connection kept alive...");
        }
      }
    } finally {
      conn.close();
    }
  }
}
