package work.android.smartbow.com.httpapplication.video;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import work.android.smartbow.com.httpapplication.net.NanoHTTPD;
import work.android.smartbow.com.httpapplication.net.TLog;

public class VideoServer extends NanoHTTPD {
    
    public static final int DEFAULT_SERVER_PORT = 8080;
    public static final String TAG = FileServer.class.getSimpleName();
    
    private static final String REQUEST_ROOT = "/";       
    
    private String mVideoFilePath;
    private int mVideoWidth  = 0;
    private int mVideoHeight = 0;

    public VideoServer(String filepath, int width, int height, int port) {
        super(port);
        mVideoFilePath = filepath;
        mVideoWidth  = width;
        mVideoHeight = height;
    }
    
    @Override
    public Response serve(IHTTPSession session) {

        if (session == null){
            return null;
        }

        TLog.error(session.getQueryParameterString());
        TLog.error(session.getUri());
        TLog.error(session.getCookies().toString());
        TLog.error(session.getHeaders().toString());
        TLog.error(session.getParms().toString());

        Method method = session.getMethod();
        TLog.error(method.name());
        if (Method.GET.equals(method)){
            TLog.error("A Get request !");

        }

        if (Method.POST.equals(method)){
            return new Response(Response.Status.OK,"text/plain","收到请求");
        }
      TLog.error("OnRequest: "+session.getUri());
        if(REQUEST_ROOT.equals(session.getUri())) {
            TLog.error(REQUEST_ROOT);
            return responseRootPage(session);
        }
        else if(mVideoFilePath.equals(session.getUri())) {
            TLog.error(mVideoFilePath);
            return responseVideoStream(session);
        }


        return response404(session,session.getUri());
    }

    public Response responseRootPage(IHTTPSession session) {
        File file = new File(mVideoFilePath);
        if(!file.exists()) {
            return response404(session,mVideoFilePath);
        }
        StringBuilder builder = new StringBuilder();

       // data = {'name': 'zhang', 'age': 10}
       // Template("index.html", data);


        builder.append("<!DOCTYPE html><html><body>");
        builder.append("<img");
        builder.append("width="+getQuotaStr(String.valueOf(mVideoWidth))+" ");
        builder.append("height="+getQuotaStr(String.valueOf(mVideoHeight))+" ");
        builder.append("controls>");
        builder.append("<source src="+getQuotaStr(mVideoFilePath)+" ");
        builder.append("type="+getQuotaStr("image/jpeg")+">");
        builder.append("Your browser doestn't support HTML5");
        builder.append("</img>");
        builder.append("</body></html>\n");
        return new Response(builder.toString());
    }
    
    public Response responseVideoStream(IHTTPSession session) {
        try {
            FileInputStream fis = new FileInputStream(mVideoFilePath);
            return new Response(Response.Status.OK, "image/jpeg", fis);
        } 
        catch (FileNotFoundException e) {        
            e.printStackTrace();
            return response404(session,mVideoFilePath);
        } 
    }
    
    public Response response404(IHTTPSession session,String url) {
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html><html><body>");        
        builder.append("Sorry, Can't Found "+url + " !");        
        builder.append("</body></html>\n");
        return new Response(builder.toString());
    }
    
    
    protected String getQuotaStr(String text) {
        return "\"" + text + "\"";
    }
    
}
