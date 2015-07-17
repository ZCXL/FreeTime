package function;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;


import function.ImageProcess.FileType_Image;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.zhuchao.freetime.R;

public class ImageLoaderTask extends AsyncTask<String, Void, Bitmap> {

	private String imageUrl;
	private Context context;
	private final WeakReference<ImageView> imageViewReference; // 防止内存溢出

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
		if(filename.equals(""))
     		filename="http://coon-moonlord.stor.sinaapp.com/GoodsImage/noimage.jpg";
		String temp=new String(filename);
		temp=temp.substring(temp.lastIndexOf("/")+1);
		if(ImageProcess.SearchImage(fileType_Image,temp)){
		   return ImageProcess.OutputImage(fileType_Image, temp);
		}else{
	     	if(Network.checkNetWorkState(context)){
	         	InputStream iStream=NetworkFunction.DownloadImage(filename);
	         	Bitmap bitmap=ImageProcess.getBitmap(iStream);
	         	ImageProcess.InputImage(bitmap, fileType_Image, filename.substring(filename.lastIndexOf("/")+1));
	         	try {
					iStream.close();
				} catch (IOException e) {
					// TODO: handle exception
				}
		        return bitmap;
	        	}else{
	        		return null;
	        	}
	     	}
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
				}else{
					imageView.setImageResource(R.drawable.logo);//��ʾ����
				}
			}
		}
	}
}