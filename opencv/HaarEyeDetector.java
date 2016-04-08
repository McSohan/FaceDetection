package opencv;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;
import javafx.scene.image.Image;

public class HaarEyeDetector implements EyeDetector {

	CascadeClassifier eyeCascade;
	Mat initialFrame;
	ArrayList<Mat> croppedEyes;
	ArrayList<Image> croppedEyesImg;
	MatOfRect eyesMat;
	Path currentPath;
	
	public HaarEyeDetector() {
		this.currentPath = Paths.get("");
		
		String filePath = currentPath.toAbsolutePath().toString();
		
		eyeCascade = new CascadeClassifier(filePath+"/haarcascade_eye.xml");
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
