package function;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;
@SuppressLint("SdCardPath")
public class ImageProcess {
   public static enum FileType_Image{MovieImage,HeadImage};//Type of image
   public static String TAG="ImageProcess";
   @SuppressLint("SdCardPath")
public static boolean InputImage(Bitmap bitmap,FileType_Image fImage,String filename){
   try{
	   if(isSDExit()){
	        String fileFolderPath="";
	        String parentPath="/sdcard/FreeTime/";
	        if(fImage==FileType_Image.MovieImage){
		      fileFolderPath=parentPath+"MovieImages/";
	          }else if (fImage==FileType_Image.HeadImage) {
		      fileFolderPath=parentPath+"HeadImages/";
	           }
	            File parentFile=new File(parentPath);
	            File fileFolder=new File(fileFolderPath);
	            File file=new File(fileFolderPath+filename);
	            if(!parentFile.exists())
	            	parentFile.mkdir();
	            if(!fileFolder.exists()){
		           fileFolder.mkdir();
	            }
	            if(!file.exists()){
	            	file.createNewFile();
	            }else{
	            	return true;
	            }
	            FileOutputStream fOutputStream=new FileOutputStream(file);
	            InputStream iStream=Bitmap2IS(bitmap);
	            int temp = 0;
				byte[] data = new byte[1024];
				while ((temp = iStream.read(data)) != -1) {
					fOutputStream.write(data, 0, temp);
				}
				fOutputStream.close();
				return true;
	      }else{
	    	  return false;
	      }
	   }catch(IOException exception){
		   Log.d(TAG,exception.toString()+"");
		   return false;
	   }
   }
   public static Bitmap OutputImage(FileType_Image fImage,String filename){
	   String fileFolderPath="";
	   Bitmap bitmap;
	   String parentPath="/sdcard/FreeTime/";
	   if(fImage==FileType_Image.MovieImage){
		   fileFolderPath=parentPath+"MovieImages/";
	   }else if (fImage==FileType_Image.HeadImage) {
		   fileFolderPath=parentPath+"HeadImages/";
	   }
	    try {
		  if(isSDExit()){
			  File parentFile=new File(parentPath);
			  File fileFolder=new File(fileFolderPath);
			  File file=new File(fileFolderPath+filename);
			  if(!parentFile.exists())
	            	parentFile.mkdir();
			  if(!fileFolder.exists()){
				  fileFolder.mkdir();
			  }
			  if(!file.exists()){
				  return null;
			  }
			  FileInputStream oStream=new FileInputStream(file);
			  bitmap=getBitmap(oStream);//ת��
			  oStream.close();
			  return bitmap;
		  }
	    } catch (Exception e) {
	    	Log.d(TAG,e.toString()+"");
			   return null;
	    }
	   return null;
   }
   public static boolean SearchImage(FileType_Image fImage,String filename){
	   String fileFolderPath="";//�ļ��е�ַ
	   String parentPath="/sdcard/FreeTime/";
	   if(fImage==FileType_Image.MovieImage){
		   fileFolderPath=parentPath+"GoodImages/";
	   }else if (fImage==FileType_Image.HeadImage) {
		   fileFolderPath=parentPath+"HeadImages/";
	   }
	   if(isSDExit()){
		   File file=new File(fileFolderPath+filename);
		   if(file.exists()){
				  return true;
		  }
	   }
	   return false;
   }
   public static boolean DeleteImage(){
	   String parentPathString="/sdcard/SmallFreeTime/MovieImages";
	   if(isSDExit()){
		   File file=new File(parentPathString);
		   if(file.exists()){
			   File[]files=file.listFiles();
			   if(files!=null||files.length!=0){
				   for(File f:files)
					   f.delete();
				   return true;
			   }
		   }
	   }
	   return false;
   }
    //��ȡ�ļ��д�С    
   public static long getFolderSize(){
	   String parentPathString="/sdcard/FreeTime/MovieImages";
	   if(isSDExit()){
	       File file=new File(parentPathString);
	       return getFolderSize(file);
	   }
	   return 0;
   }
   public static long getFolderSize(File file){  
       long size = 0;  
       try {
			File[] fileList = file.listFiles();   
			for (int i = 0; i < fileList.length; i++)   
			{   
			    if (fileList[i].isDirectory())   
			    {   
			        size = size + getFolderSize(fileList[i]);  
			    }else{   
			        size = size + fileList[i].length();  
			    }   
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
       return size/1048576;
   }  
   public static boolean isSDExit(){
	   String state=Environment.getExternalStorageState();
	   if(state.equals(Environment.MEDIA_MOUNTED)){
		   return true;
	   }else{
		   return false;
	   }
   }
   private static InputStream  Bitmap2IS(Bitmap bm){
           ByteArrayOutputStream baos = new ByteArrayOutputStream();  
           bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
           InputStream sbs = new ByteArrayInputStream(baos.toByteArray());    
           return sbs;  
       }
   public static Bitmap getBitmap(InputStream in){
		Bitmap bitmap=null;
		if(in!=null){
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565;   
	    options.inPurgeable = true;  
		options.inInputShareable = true; 
		options.inJustDecodeBounds=false;
		bitmap=BitmapFactory.decodeStream(in,null,options);
		return bitmap;
		}else{
		return null;
		}
	}//BitMap��ת������
   public static Bitmap zoomImage(Bitmap bitmap,int nwidth,int nheight){
	   float bili=((float)nwidth)/nheight;
	   int width=bitmap.getWidth();
	   int height=bitmap.getHeight();
	   float afterwidth=height*bili;
	   float afterheight=width/bili;
	   if(afterheight<=height)
		   height=(int) afterheight;
	   if(afterwidth<=width)
		   width=(int) afterwidth;  
	   //Bitmap bitmap1=Bitmap.createBitmap(bitmap,(bitmap.getWidth()-width)/2,(bitmap.getHeight()-height)/2, width, height);//������ؼ���ͬ������ͼ
	   float scaleWidth=((float)nwidth)/width;
	   float scaleHeight=((float)nheight)/height;
	   Matrix matrix=new Matrix();
	   matrix.postScale(scaleWidth,scaleHeight);
       return Bitmap.createBitmap(bitmap, (bitmap.getWidth()-width)/2,(bitmap.getHeight()-height)/2, width, height, matrix, true);//�����µ�λͼ
   }
}
