package opencv;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;

import javafx.scene.image.Image;

public abstract class LbpEyeDetector implements EyeDetector {

	/* Opencv has no lbp classifier for eye detection */
	/* For the sake of abstract factory's structure, I will implement it using haar cascades for eye detection */

	CascadeClassifier eyeCascade;
	Mat initialFrame;
	ArrayList<Mat> croppedEyes;
	ArrayList<Image> croppedEyesImg;
	MatOfRect eyesMat;
	
	public LbpEyeDetector() {
		eyeCascade = new CascadeClassifier("E:/3UBB/java/projectopencv/openfx/haarcascade_eye.xml");
		initialFrame = new Mat();
		croppedEyes = new ArrayList<Mat>();
		croppedEyesImg = new ArrayList<Image>();
		eyesMat = new MatOfRect();
	}
	
	public void findEyes(Mat frame){
		croppedEyes.clear();
		croppedEyesImg.clear();
		initialFrame = frame;
		croppedEyes.clear();
		this.detectEyes();
		System.out.println("ok");
		
		
	}
	
	private void detectEyes(){
		eyeCascade.detectMultiScale(initialFrame, eyesMat);
		for(Rect eye : eyesMat.toArray()){
			
			eye.x+=20;
			eye.y+=20;
			eye.width-=20;
			eye.height-=20;
			croppedEyes.add(new Mat(initialFrame,eye));
			System.out.println("eye");
		}

	}
	
	public ArrayList<Image> getCroppedEyes(){
		this.matList2ImageList();
		return croppedEyesImg;
	}
	private void matList2ImageList(){
		for(Mat eye :croppedEyes){
			croppedEyesImg.add(this.mat2Image(eye));
		}
	}

	private Image mat2Image(Mat frame)
	{
		MatOfByte buffer = new MatOfByte();
		Imgcodecs.imencode(".jpg", frame, buffer);
		return new Image(new ByteArrayInputStream(buffer.toArray()));
	}
}
