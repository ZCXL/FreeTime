package utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import function.ImageProcess;
import function.Network;
import function.NetworkFunction;

/**
 * Created by zhuchao on 7/21/15.
 */
public class DownLoadFile {
    private long file_size;

    private long downloadFileSize;

    private OnDownloadListener downloadListener;

    private OnErrorListener errorListener;

    private Context context;

    public OnDownloadListener getDownloadListener() {
        return downloadListener;
    }

    public OnErrorListener getErrorListener() {
        return errorListener;
    }

    public void setDownloadListener(OnDownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    public void setErrorListener(OnErrorListener errorListener) {
        this.errorListener = errorListener;
    }

    public DownLoadFile(Context context){
        this.context=context;
    }
    public void startDownload(final String download_url){
        if(Network.checkNetWorkState(context)){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        NetworkFunction.FileInfo fileInfo =NetworkFunction.GetFile(download_url);
                        //get file info;
                        InputStream inputStream=fileInfo.inputStream;
                        file_size=fileInfo.fileSize;
                        if(file_size<0){
                            if(errorListener!=null)
                                errorListener.onSizeError();
                        }

                        if(inputStream==null){
                            if(errorListener!=null)
                                errorListener.onStreamError();
                        }

                        if(isSDExit()){
                            String pathName="sdcard/FreeTime/";
                            String moviePath=pathName+"Movies/";
                            File pathNameFile=new File(pathName);
                            File moviePathFile=new File(moviePath);
                            File movie=new File(moviePath+download_url.substring(download_url.lastIndexOf("/")+1));
                            if(!pathNameFile.exists())
                                pathNameFile.mkdir();
                            if(!moviePathFile.exists())
                                moviePathFile.mkdir();
                            if(!movie.exists())
                                movie.createNewFile();

                            FileOutputStream fos=new FileOutputStream(movie);

                            downloadFileSize=0;
                            byte buf[]=new byte[1024];
                            long dotPercent=file_size/1000;
                            long nowPercent=dotPercent;

                            while(downloadFileSize<file_size){
                                if(downloadFileSize>nowPercent){
                                    if(downloadListener!=null)
                                        downloadListener.onValueChange(1000*downloadFileSize/file_size);
                                    nowPercent+=dotPercent;
                                }
                                int numRead=inputStream.read(buf);
                                if(numRead==-1)
                                    break;
                                fos.write(buf,0,numRead);
                                downloadFileSize+=numRead;
                            }
                            fos.close();
                            inputStream.close();
                        }else{
                            if(errorListener!=null)
                                errorListener.onSDCardError();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        if(errorListener!=null)
                            errorListener.onDownloadError();
                    }
                }
            }).start();

        }
    }

    public static boolean isSDExit(){
        String state= Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }else{
            return false;
        }
    }

    public interface OnDownloadListener{
        /**
         * change percent of download
         * @param value
         */
        void onValueChange(float value);
    }
    public interface OnErrorListener{
        /**
         * get file size error
         */
        void onSizeError();

        /**
         * get stream error
         */
        void onStreamError();

        /**
         * no sd card
         */
        void onSDCardError();

        /**
         * download error
         */
        void onDownloadError();
    }
}
