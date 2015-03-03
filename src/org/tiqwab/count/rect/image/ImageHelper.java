package org.tiqwab.count.rect.image;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import processing.core.PApplet;
import processing.core.PImage;

public class ImageHelper {

	public static Mat getImage(PApplet parent, Uri uri) {
		InputStream is = null;
		Bitmap bm;
		Mat mat = new Mat();
		
		try{
			is = parent.getContentResolver().openInputStream(uri);
			bm = BitmapFactory.decodeStream(is);
			Utils.bitmapToMat(bm, mat);
		} catch(IOException e){
			e.printStackTrace();
		} finally {
			if (is != null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return mat;
	}
	
	
	public static Mat getImageFromAssets(PApplet parent, String fileName) {
		InputStream is = null;
		Bitmap bm;
		Mat mat = new Mat();
		
		try{
			is = parent.getResources().getAssets().open(fileName);
			bm = BitmapFactory.decodeStream(is);
			Utils.bitmapToMat(bm, mat);
		} catch(IOException e){
			e.printStackTrace();
		} finally {
			if (is != null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return mat;
	}
	
	
	public static Mat getGrayImage(Mat src) {
		Mat matgr = src.clone();
		Imgproc.cvtColor(src, matgr, Imgproc.COLOR_BGRA2GRAY, 4);		
		return matgr;
	}
	
	
	public static Mat resizeMatWithSameRate(Mat src, double w, double h) {
		Mat mat = new Mat();
		
		//Determine size
		Size matSize = src.size();
		double width = matSize.width;
		double height = matSize.height;
		double scaleW = w / width;
		double scaleH = h / height;
		double scale = 1;
		if (scaleW < 1 || scaleH < 1) {
			scale = Math.min(scaleW, scaleH);
		}
		
		Imgproc.resize(src, mat, new Size((width * scale), (height * scale)));
		return mat;
	}
	
	
	public static void showImage(PApplet parent, Mat mat, double x, double y, double w, double h) {
		Bitmap src = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888);
		Utils.matToBitmap(mat, src);
		Bitmap bm = Bitmap.createScaledBitmap(src, (int)w, (int)h, false);
		
		//How to show images.
		PImage im = new PImage(bm.getWidth(), bm.getHeight());
		bm.getPixels(im.pixels, 0, bm.getWidth(), 0, 0, bm.getWidth(), bm.getHeight());
		
		parent.image(im, (float)x, (float)y);
	}
	
	
	public static PImage showImageToCenter(PApplet parent, Mat mat, double centerX, double centerY) {
		Bitmap bm = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888);
		Utils.matToBitmap(mat, bm);
		
		PImage im = new PImage(bm.getWidth(), bm.getHeight());
		bm.getPixels(im.pixels, 0, bm.getWidth(), 0, 0, bm.getWidth(), bm.getHeight());
		
		//Determine x, y
		double x = centerX - (bm.getWidth() / 2);
		double y = centerY - (bm.getHeight() / 2);
		
		parent.image(im, (float)x, (float)y);
		return im;
	}
	
	
	public static void showImageToCenterWithSameRate(PApplet parent, Mat mat, double centerX, double centerY, double w, double h) {
		Bitmap src = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888);
		Utils.matToBitmap(mat, src);
		
		//Determine size
		double width = src.getWidth();
		double height = src.getHeight();
		double scaleW = w / width;
		double scaleH = h / height;
		double scale = 1;
		if (scaleW < 1 || scaleH < 1) {
			scale = Math.min(scaleW, scaleH);
		}
		Bitmap bm = Bitmap.createScaledBitmap(src, (int)(width * scale), (int)(height * scale), false);
		
		//Determine x, y
		double x = centerX - (bm.getWidth() / 2);
		double y = centerY - (bm.getHeight() / 2);
		
		//How to show images.
		PImage im = new PImage(bm.getWidth(), bm.getHeight());
		bm.getPixels(im.pixels, 0, bm.getWidth(), 0, 0, bm.getWidth(), bm.getHeight());
		
		parent.image(im, (float)x, (float)y);
	}
	
	
	public static Mat getHoughLines(Mat matgr) {
		Mat lines = new Mat();
		Mat matli = matgr.clone();
		Imgproc.Canny(matgr, matli, 100, 200);			//cannot set ksize...
		ImgConfigManager.HoughConfig hc = new ImgConfigManager.HoughConfig();
		Imgproc.HoughLinesP(matli, lines, 
				hc.getRho(), hc.getTheta(), hc.getThreshold(), hc.getLength(), hc.getGap()); /* Initial was ( 50, 50, 10) */
		return lines;
	}
	
	
	public static void drawHVLines(Mat mat, Set<Line> Hlines, Set<Line> Vlines) {
		/* Scalar shows color as BGRA */
		Scalar scalar = new Scalar(0, 0, 255, 255);
		for (Line line : Hlines) {
			Core.line(mat, 
					  new org.opencv.core.Point(line.start.x, line.start.y), 
					  new org.opencv.core.Point(line.end.x, line.end.y), 
					  scalar,
					  1);
		}
		
		scalar = new Scalar(255, 0, 0, 255);
		for (Line line : Vlines) {
			Core.line(mat, 
					  new org.opencv.core.Point(line.start.x, line.start.y), 
					  new org.opencv.core.Point(line.end.x, line.end.y), 
					  scalar,
					  1);
		}		
	}
	
	
	public static void drawHVDLines(Mat mat, Set<Line> Hlines, Set<Line> Vlines, Set<Line> Dlines) {
		/* Scalar shows color as BGRA */
		Scalar scalar = new Scalar(0, 0, 255, 255);
		for (Line line : Hlines) {
			Core.line(mat, 
					  new org.opencv.core.Point(line.start.x, line.start.y), 
					  new org.opencv.core.Point(line.end.x, line.end.y), 
					  scalar,
					  1);
		}
		
		scalar = new Scalar(255, 0, 0, 255);
		for (Line line : Vlines) {
			Core.line(mat, 
					  new org.opencv.core.Point(line.start.x, line.start.y), 
					  new org.opencv.core.Point(line.end.x, line.end.y), 
					  scalar,
					  1);
		}		
		
		scalar = new Scalar(0, 255, 0, 255);
		for (Line line : Dlines) {
			Core.line(mat, 
					  new org.opencv.core.Point(line.start.x, line.start.y), 
					  new org.opencv.core.Point(line.end.x, line.end.y), 
					  scalar,
					  1);
		}	
	}
}

class ImageReadCaller implements Callable<Mat> {

	PApplet p;
	Uri open_file;
	
	ImageReadCaller(PApplet p, Uri open_file) {
		super();
		this.p = p;
		this.open_file = open_file;
	}

	@Override
	public Mat call() throws Exception {
		Mat targetMat = ImageHelper.getImage(p, open_file);
		return targetMat;
	}
	
}