package function;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhuchao on 7/14/15.
 */
public class NetworkFunction {
    public static String TAG="NetworkFunction";
    /**
     * provide function to connect server and get return value from server.
     * @param keys
     * @param parameters
     * @param url
     * @return
     * Created by LMZ 7/14/15
     */
    public static String ConnectServer(String url,String keys[],String parameters[]){
        String result="";
        //利用HttpClient实现向url发送Post请求
        //建立一个NameValuePair数组，用于存储欲传送的参数
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        for (int i = 0; i < keys.length; i++) {
            params.add(new BasicNameValuePair(keys[i],parameters[i]));//添加参数
        }
        try{
            HttpClient httpClient = new DefaultHttpClient();//创建HttpClient对象
            HttpPost httpPost = new HttpPost(url);//创建HttpPost对象
            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));//设置编码
            HttpResponse httpResponse = httpClient.execute(httpPost);//执行HttpClient请求
            StringBuilder builder = new StringBuilder();
            //获取已获得读取内容的输入流对象
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            //通过循环逐步读取输入流中的内容
            for (String s = reader.readLine(); s != null; s = reader.readLine()){
                builder.append(s);//添加数据流
            }
            result = builder.toString();
            Log.d(TAG, result);
            result = result.replace("\ufeff","");//去掉UTF-8中的BOM(Byte Order Mark)的不可见字符
            if (result.startsWith("<html")) throw new Exception("Post fail for error webPage");
            return result;
        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, e.toString()+"ConnectServer UnsupportedEncoding error");
        } catch (ClientProtocolException e) {
            Log.d(TAG, e.toString() + "ConnectServer ClientProtocol error");
        } catch (IOException e) {
            Log.d(TAG, e.toString() + "ConnectServer IO error");
        } catch (Exception e) {
            Log.d(TAG, e.toString() + "ConnectServer error");
        }
        return null;
    }

    /**
     * download image from server.
     * @param url
     * @return
     * Created by LMZ on 7/14/15
     */
    public static InputStream DownloadImage(String url){
        try{
            URL url1 = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url1.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            if (inputStream != null){
                return inputStream;
            }else {
                Log.d(TAG, "inputStream is null");
            }
        } catch (MalformedURLException e) {
            Log.d(TAG, e.toString() + "DownloadImage MalformedURL error");
        } catch (IOException e) {
            Log.d(TAG, e.toString() + "DownloadImage IO  error");
        }
        return null;
    }

    /**
     * You shouldn't have to write this function before I created socket structure.
     * @param fileName
     */
    public static void UploadFile(String fileName){

    }
}
