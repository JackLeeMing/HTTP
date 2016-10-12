package work.android.smartbow.com.httpapplication.video;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import work.android.smartbow.com.httpapplication.App;
import work.android.smartbow.com.httpapplication.net.TLog;

public class FileServer extends NanoHttp {
    
    public static final int DEFAULT_SERVER_PORT = 8989;
    public static final String TAG = FileServer.class.getSimpleName();
    
    private static final String REQUEST_ROOT = "/";       
    
    private String mVideoFilePath;

    public FileServer(String filepath, int width, int height, int port) {
        super(port);
        mVideoFilePath = filepath;
    }
    
    @Override
    public Response serve(IHTTPSession session) {        
        TLog.error("OnRequest: "+session.getUri());

        if(REQUEST_ROOT.equals(session.getUri())) {
            try {
                return responseRootPage(session);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(mVideoFilePath.equals(session.getUri())) {
            try {
                return responseVideoStream(session);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response404(session,session.getUri());
    }

    public Response responseRootPage(IHTTPSession session) throws IOException {
        File file = new File(mVideoFilePath);
        if(!file.exists()) {
            return response404(session,mVideoFilePath);
        }
        try {
            InputStream reader = new FileInputStream(file);
            TLog.error(""+reader.available()+"===="+ file.length());
            return new Response(Response.Status.OK,"text/html",reader,reader.available());
        }catch (Exception e){
            TLog.error(e.getMessage());
        }

        InputStream in  = App.getAppContext().getResources().getAssets().open("www/index.html");
        int lenght = in.available();
        TLog.error("=="+lenght);
        return new Response(Response.Status.OK,"text/html",in,lenght);
    }
    
    public Response responseVideoStream(IHTTPSession session) throws IOException {
        try {
            File file = new File(mVideoFilePath);
            FileInputStream fis = new FileInputStream(file);
            TLog.error(""+fis.available()+"===="+file.length());
            Response response = new Response(Response.Status.OK,"text/html",fis,fis.available());
            return response;
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
        InputStream in  = null;
        try {
            in = App.getAppContext().getResources().getAssets().open("www/index.html");
            int lenght = in.available();
            return new Response(Response.Status.OK,"text/html",in,lenght);
        } catch (IOException e) {
            e.printStackTrace();
        }
       return null;
    }
    
    

    private String getFromAssets(){
        try {
            InputStreamReader reader = new InputStreamReader(App.getAppContext().getResources().getAssets().open("www/index.html"));
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line ;
            StringBuilder result = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null){
                result.append(line);
            }

            return result.toString();
        }catch (Exception e){
            TLog.error(e.getMessage());
        }

        return null;
    }
}
