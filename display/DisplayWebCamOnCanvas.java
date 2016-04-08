package display;

import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import opencv.MyException;
import opencv.OpencvFactory;

//this class will be used in DetectorThread class to read frames from webcam and paint them on the canvasses
public class DisplayWebCamOnCanvas {
	
	Canvas canvasCroppedFace;
	Canvas canvas;
	Canvas canvasCroppedEye;
	GraphicsContext gc;
	GraphicsContext canvasCroppedEyeGc;
	GraphicsContext canvasCroppedFaceGc;
	OpencvFactory factory;
	int canvasFacesWidth;
	int canvasFacesHeight;
	int numberOfDetectedEyes;
	int numberOfDetectedFaces;
	int canvasEyeHeight;
	int canvasEyeWidth;
	int canvasFaceHeight;
	int canvasFaceWidth;
	int eyeRation;
	int faceRation; 
	int eyeStartingPosition;
	int faceStartingPosition;
	
	public DisplayWebCamOnCanvas(OpencvFactory factory,Canvas canvas, Canvas canvasCroppedFace,Canvas canvasEye) {
		this.factory = factory;
		this.canvas = canvas;
		this.canvasCroppedFace = canvasCroppedFace;
		this.gc = canvas.getGraphicsContext2D();
		this.canvasCroppedFaceGc = canvasCroppedFace.getGraphicsContext2D();
		this.canvasCroppedEye = canvasEye;
		this.canvasCroppedEyeGc = canvasEye.getGraphicsContext2D();
		canvasFacesWidth = (int) canvas.getWidth();
		canvasFacesHeight = (int) canvas.getHeight();
	}
	
	public void putFacesOnCanvas(){
		try {
			factory.getFaceDetector().detectFaceFromWebCam();
			//draw the main image ( center)
			gc.drawImage(factory.getFaceDetector().getImage(), 0, 0,200,200);
			//draw the faces 
			this.drawFaces();
			
		} catch (MyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void putEyesOnCanvas(){
		this.getEyes();
		this.drawEyes();
	}
	private void getEyes(){
		factory.getFaceDetector().associateEyes(factory.getEyeDetector());
	}
	private void drawEyes(){
		//create array with "eyes"
		ArrayList<Image> eyesToBeDisplayed = new ArrayList<Image>(this.getCroppedEyes());

		this.deletePreviousEyes();
		this.setNumberOfDetectedEyes(eyesToBeDisplayed);
		
		//skip the drawing part if the array is empty
		if( numberOfDetectedEyes > 0){
			this.initiateEyeParameters();
			System.out.println("n>0");
			this.displayEyes(eyesToBeDisplayed);

		}
	}

	private void displayEyes(ArrayList<Image> eyesToBeDisplayed){
		for(Image eye : eyesToBeDisplayed){
			canvasCroppedEyeGc.drawImage(eye, eyeStartingPosition, 0,eyeRation,canvasEyeHeight/4);
			eyeStartingPosition += eyeRation;
		}
	}
	private void initiateEyeParameters(){
		eyeRation = (int) canvasCroppedEye.getWidth()/numberOfDetectedEyes;
		canvasEyeHeight = (int) canvasCroppedEye.getHeight();
		if( eyeRation > 50){
			eyeRation = 50;
		}
		eyeStartingPosition = 0;

	}
	private void setNumberOfDetectedEyes(ArrayList<Image> eyesToBeDisplayed){
		numberOfDetectedEyes = eyesToBeDisplayed.size();
		System.out.println(numberOfDetectedEyes);
	}
	private void deletePreviousEyes(){
		canvasCroppedEyeGc.clearRect(0,0,600,400);
	}

	private ArrayList<Image> getCroppedEyes() {

		return factory.getEyeDetector().getCroppedEyes();
	}
	
	
	
	private void drawFaces(){
		//create array with "faces"
		ArrayList<Image> facesToBeDisplayed = new ArrayList<Image>(this.getCroppedFaces());

		this.deletePreviousFaces();
		this.setNumberOfDetectedFaces(facesToBeDisplayed);
		
		//skip the drawing part if the array is empty
		
		if( numberOfDetectedFaces > 0){
			this.initiateFaceParameters();
			System.out.println("n>0");
			this.displayFaces(facesToBeDisplayed);

		}
	}

	private void displayFaces(ArrayList<Image> facesToBeDisplayed){
		for(Image face : facesToBeDisplayed){
			canvasCroppedFaceGc.drawImage(face, faceStartingPosition, 0,faceRation,canvasFaceHeight);
			faceStartingPosition += faceRation;
			System.out.println("DISPLAYED");
		}
	}
	private void initiateFaceParameters(){
		faceRation = (int) canvasCroppedFace.getWidth()/numberOfDetectedFaces;
		canvasFaceHeight = (int) canvasCroppedFace.getHeight();
		if( faceRation > 100){
			faceRation = 100;
		}
		faceStartingPosition = 0;

	}
	private void setNumberOfDetectedFaces(ArrayList<Image> facesToBeDisplayed){
		numberOfDetectedFaces = facesToBeDisplayed.size();
		System.out.println(numberOfDetectedFaces);
	}
	private void deletePreviousFaces(){
		canvasCroppedFaceGc.clearRect(0,0,600,100);
	}

	private ArrayList<Image> getCroppedFaces() {

		return factory.getFaceDetector().getCroppedFaces();
	}
}
