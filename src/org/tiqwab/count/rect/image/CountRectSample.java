package org.tiqwab.count.rect.image;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.tiqwab.count.rect.layout.RectCountLayout;
import org.tiqwab.count.rect.layout.RectToolBar;
import org.tiqwab.count.rect.process.JudgeImplHVDS;
import org.tiqwab.count.rect.process.JudgeStr;
import org.tiqwab.count.rect.process.LineDividerManager;
import org.tiqwab.count.rect.process.JudgeImplHV;
import org.tiqwab.count.rect.process.JudgeImplHVD;
import org.tiqwab.count.rect.process.JudgeImplHVS;
import org.tiqwab.count.rect.process.Rect;
import org.tiqwab.count.rect.process.RectCountThread;

import controlP5.Button;
import controlP5.CheckBox;
import controlP5.ControlP5;
import processing.core.PApplet;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class CountRectSample extends PApplet{
	
	//For preparing OpenCV manager
    private boolean doneSetup = false;
    private boolean doOnce = true;
    
    //Main content of the app
    private Content child;
    
    //For UI
    ControlP5 cp5;
    Button fileBtn;
    Button nextBtn;
    Button detectBtn;
    int detect_lv = 0;
    boolean isBtnIgnored = true;
    CheckBox squareCheck;
    
    //For choose image to process
    private static final int OPEN_DOCUMENT_REQUEST = 1;
    private volatile boolean isTargetChosen = false;
    private volatile Mat targetMat = null;
	
	
    public static void main(String args[]) {
        PApplet.main(new String[] { "–present", "org.tiqwab.count.rect.image" });
        
    }
	
    
	private BaseLoaderCallback mOpenCVCallBack = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
		   switch (status) {
		       case LoaderCallbackInterface.SUCCESS:
		       {
		    	   Log.i("managertest", "OpenCV loaded successfully");
		    	   doneSetup = true;
		       } break;
		       default:
		       {
		    	   super.onManagerConnected(status);
		       } break;
		   }
		}
	};
    

    
    public void setup(){
    	cp5 = new ControlP5(this);
		
    	Log.i("managertest", "Trying to load OpenCV library");
        if (!OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_2, this, mOpenCVCallBack))
        {
          Log.e("managertest", "Cannot connect to OpenCV Manager");
        } else {
        	Log.e("managertest", "connect to OpenCV Manager");
        }      
    }
    
    public void draw() {
    	if (doneSetup) {
    		if (doOnce) {
    			child = new Content(this);
    			child.setup();
    			doOnce = false;
    		}  		
    		child.draw();  
    	} 	
    }
    
    
    public synchronized void imagePick() {
    	Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
    	intent.setType("image/*");              //Error occurs when a gif file is chosen.
    	startActivityForResult(intent, OPEN_DOCUMENT_REQUEST);
    }
    
    
    public synchronized void getImage() {
    	imagePick();  
    	while (isTargetChosen == false) {
        	try {
    			wait();
    		} catch (InterruptedException e) {
    		}
    	}   	
    	isTargetChosen = false;
    }
    
    
    @Override
    protected synchronized void onActivityResult(int requestCode, int resultCode, Intent data){
    	if (requestCode == OPEN_DOCUMENT_REQUEST) {
    		isTargetChosen = true;
    		if (resultCode == RESULT_OK) {
                final Uri open_file = data.getData();
                targetMat = ImageHelper.getImage(this, open_file);
    		} else {
    			//when cancelled.
    			targetMat = null;
    		}
    		notifyAll();
    	}
    }
    
    
    /*
    public void keyPressed() {
        if (key == CODED) {
    	    if (keyCode == BACK) {
    	        System.out.println("Back");
    	    } else if (keyCode == MENU) {
    	        // When menu button was pressed.
    	    }
    	}
    }
    */
    

    //For image button
    public void ClickOnReadImage(int theValue) {
    	if (isBtnIgnored) {
    		return;
    	}
    	child.processHVD();
    }
    
    
    //For NextRect button
    public void NextRect(int theValue) {
    	if (isBtnIgnored) {
    		return;
    	}
    	child.layout.nextRect();
    }
    
    
    //For DetectionLevel button
    public void DetectionLevel(int value) {
    	if (isBtnIgnored) {
    		return;
    	}
    	child.tool.doDetect();
    }
    
    
    //For Square checkbox
    //theValues consists of toggle's status.
    //If there are three toggles and only the second is activated, theValues is [0.0, 1.0, 0.0].
    public void SquareCheck(float[] theValues) {
    	if (isBtnIgnored) {
    		return;
    	}
    	if (theValues[0] == 1.0) {
    		child.setStrategy(new JudgeImplHVDS());
    	} else {
    		child.setStrategy(new JudgeImplHVD());
    	}
    }
    
    
    class Content {
    	private PApplet parent;
    	private RectCountLayout layout;
    	private RectToolBar tool;
    	//private RectCounter counter;
    	private JudgeStr strategy;
    	
    	Content(PApplet parent) {
    		this.parent = parent;
    		this.layout = new RectCountLayout(parent);
    		this.tool = new RectToolBar(parent, cp5);
    		this.strategy = new JudgeImplHVD();
    		//counter = new RectCounter();
    	}
    	
    	public void setup() {
    		clear();
    		tool.setFileBtn(fileBtn);
    		tool.setNextBtn(nextBtn);
    		tool.setDetectBtn(detectBtn);
    		tool.setSquareCheck(squareCheck);
    		layout.setupToolBar(tool);
    		isBtnIgnored = false;
    	}
    	
    	
    	public void draw() {
    		layout.draw();
    	}
        
    	
    	public void clear() {
    		layout.clear();
    	}
    	
    	
        public void processHVD() {
    		getImage();
    		if (targetMat == null) {
    			return;
    		}   		
    		clear();
    		Mat mat = targetMat;

    		layout.setBaseMat(mat);
    		layout.showBaseImage();
    		mat = layout.getBaseMat();

    		Mat matgr = ImageHelper.getGrayImage(mat);
    		
    		List<Line> Hlines = new ArrayList<Line>();
    		List<Line> Vlines = new ArrayList<Line>();
    		List<Line> Dlines = new ArrayList<Line>();
    		LineHelper.detectLines(matgr, new Condition(Math.PI / 4, mat.width() / 20), Hlines, Vlines, Dlines);
    		
    		Set<Line> HlineSet = new HashSet<Line>(Hlines);
    		Set<Line> VlineSet = new HashSet<Line>(Vlines);
    		Set<Line> DlineSet = new HashSet<Line>(Dlines);
    		final Set<Line> resultLines = new HashSet<Line>();
    		
    		LineDividerManager.divideLines(HlineSet, VlineSet, DlineSet, resultLines);
    		
    		RectCountThread counterTh = new RectCountThread(new ArrayList<Line>(resultLines), strategy);
    		counterTh.start();
    		layout.setCounter(counterTh);
    		layout.beginDetect();
    		
    		Mat resultMat = Mat.zeros(mat.size(), mat.type()/*CvType.CV_8U*/);
    		ImageHelper.drawHVDLines(resultMat, HlineSet, VlineSet, DlineSet); /* COLOR: BGRA */
    		layout.setResultMat(resultMat);
    		layout.showResultImage();
    		
    		//System.out.println("RectCount: " + resultRects.size());
        }
        
        
        public void setStrategy(JudgeStr str) {
        	this.strategy = str;
        }
    }

}