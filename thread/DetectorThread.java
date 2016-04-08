package thread;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

import display.DisplayWebCamOnCanvas;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import opencv.EyeDetector;
import opencv.FaceDetector;
import opencv.MyException;
import opencv.OpencvFactory;


public class DetectorThread extends Thread {


	DisplayWebCamOnCanvas draw;
	boolean run;

	public DetectorThread(OpencvFactory factory,Canvas canvas, Canvas canvasCroppedFace,Canvas canvasEye) {	
		run = true;
		draw = new DisplayWebCamOnCanvas(factory, canvas, canvasCroppedFace, canvasEye);
	}

	public void stopThread(){
		run = false;
	}
	
	@Override
	public void run() {


		while(run == true){
			try{
			draw.putFacesOnCanvas();
			draw.putEyesOnCanvas();
			}
		catch(Exception e){
			System.out.println("EXCEPTIIIIIIIONNNN---------------------------------------");
		}
		}
	}

}
