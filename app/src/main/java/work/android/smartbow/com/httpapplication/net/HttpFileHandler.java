package work.android.smartbow.com.httpapplication.net;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 * This file was created by hellomac on 2016/10/11.
 * name: HttpApplication.
 */

public class HttpFileHandler implements HttpRequestHandler {

  private final String docRoot;

  public HttpFileHandler(String docRoot) {
    this.docRoot = docRoot;
  }

  @Override
  public void handle(final HttpRequest request,final HttpResponse response,
      final HttpContext context) throws HttpException, IOException {

    String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
    if (!method.equals("GET") && !method.equals("HEAD") && !method.equals("POST")) {
      throw new MethodNotSupportedException(method + " method not supported");
    }
    String target = request.getRequestLine().getUri();

    if (request instanceof HttpEntityEnclosingRequest) {
      HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
      byte[] entityContent = EntityUtils.toByteArray(entity);
      TLog.error("Incoming entity content (bytes): " + entityContent.length);
    }

    final File file = new File(this.docRoot, URLDecoder.decode(target, "UTF-8"));
    if (!file.exists()) {

      response.setStatusCode(HttpStatus.SC_NOT_FOUND);
      StringEntity entity = new StringEntity(
          "<html><body><h1>File" + file.getPath() +
              " not found</h1></body></html>",
          ContentType.create("text/html", "UTF-8"));
      response.setEntity(entity);
      TLog.error("File " + file.getPath() + " not found");

    } else if (!file.canRead() || file.isDirectory()) {

      response.setStatusCode(HttpStatus.SC_FORBIDDEN);
      StringEntity entity = new StringEntity(
          "<html><body><h1>Access denied</h1></body></html>",
          ContentType.create("text/html", "UTF-8"));
      response.setEntity(entity);
      TLog.error("Cannot read file " + file.getPath());

    } else {

      response.setStatusCode(HttpStatus.SC_OK);
      FileEntity body = new FileEntity(file, ContentType.create("text/html", (Charset) null));
      response.setEntity(body);
      TLog.error("Serving file " + file.getPath());
    }
  }
}
