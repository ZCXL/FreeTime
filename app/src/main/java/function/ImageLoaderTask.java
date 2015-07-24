package function;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;


import function.ImageProcess.FileType_Image;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.zhuchao.freetime.R;

public class ImageLoaderTask extends AsyncTask<String, Void, Bitmap> {

	private String imageUrl;
	private Context context;
	private final WeakReference<ImageView> imageViewReference; // 防止内存溢出

	private static HashMap<String,SoftReference<Bitmap>> ImageCache=new HashMap<String, SoftReference<Bitmap>>();

	private FileType_Image fileType_Image;
	public ImageLoaderTask(ImageView imageView,Context context,FileType_Image fImage) {
		imageViewReference = new WeakReference<ImageView>(imageView);
        this.context=context;
        fileType_Image=fImage;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		imageUrl = params[0];
		return loadImageFile(imageUrl);
	}

	private Bitmap loadImageFile(String filename) {
		Bitmap bitmap;
		if(filename.equals(""))
     		filename="http://coon-moonlord.stor.sinaapp.com/GoodsImage/noimage.jpg";
		String temp=new String(filename);
		//temp=temp.substring(temp.lastIndexOf("/")+1);
		if(ImageCache.containsKey(filename)){
			Log.d("image","reuse");
			SoftReference<Bitmap>reference=ImageCache.get(filename);
			bitmap=reference.get();
			if(bitmap!=null)
				return bitmap;
		}
		if(ImageProcess.SearchImage(fileType_Image,temp)){
			bitmap=ImageProcess.OutputImage(fileType_Image,temp);
			bitmap=ImageProcess.compressImage(bitmap);
			if(bitmap!=null){
				ImageCache.put(filename,new SoftReference<Bitmap>(bitmap));
				return bitmap;
			}
		}else{
	     	if(Network.checkNetWorkState(context)){
	         	InputStream iStream=NetworkFunction.DownloadImage(filename);
	         	bitmap=ImageProcess.getBitmap(iStream);
				bitmap=ImageProcess.compressImage(bitmap);
	         	ImageProcess.InputImage(bitmap, fileType_Image, filename.substring(filename.lastIndexOf("/")+1));
				try {
					iStream.close();
				    } catch (IOException e) {
					// TODO: handle exception
				    }
				ImageCache.put(filename,new SoftReference<Bitmap>(bitmap));
		        return bitmap;
			}else{
	        		return null;
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		if (isCancelled()) {
			bitmap = null;
		}
		if (imageViewReference != null) {
			ImageView imageView = imageViewReference.get();
			if (imageView != null) {
				if (bitmap != null) {
					if(imageView.getLayoutParams().width>0){
					    imageView.setImageBitmap(ImageProcess.zoomImage(bitmap, imageView.getLayoutParams().width, imageView.getLayoutParams().height));
					}else if(imageView.getWidth()>0){
						imageView.setImageBitmap(ImageProcess.zoomImage(bitmap, imageView.getWidth(), imageView.getHeight()));					
					}else{
						imageView.setImageBitmap(bitmap);
					}
				}
			}
		}
	}
}