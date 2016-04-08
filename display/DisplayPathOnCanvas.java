package display;

import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import opencv.Cascades;
import opencv.HaarCascadesImpl;
import opencv.OpencvFactory;
//this class will be used in MyController class to display a given image 
public class DisplayPathOnCanvas {

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
	String path;

	public DisplayPathOnCanvas(OpencvFactory factory,Canvas canvas, Canvas canvasCroppedFace,Canvas canvasEye) {

		this.canvas = canvas;
		this.canvasCroppedFace = canvasCroppedFace;
		this.gc = canvas.getGraphicsContext2D();
		this.canvasCroppedFaceGc = canvasCroppedFace.getGraphicsContext2D();
		this.canvasCroppedEye = canvasEye;
		this.canvasCroppedEyeGc = canvasEye.getGraphicsContext2D();
		canvasFacesWidth = (int) canvas.getWidth();
		canvasFacesHeight = (int) canvas.getHeight();
	}

	public void putEyesAndFacesOnCanvas(String path){
		this.path = path;
		this.putFacesOnCanvas(path);
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

		if( numberOfDetectedEyes < 5){
			for(Image eye : eyesToBeDisplayed){
				canvasCroppedEyeGc.drawImage(eye, eyeStartingPosition, 0,eyeRation,canvasEyeHeight/4);
				eyeStartingPosition += eyeRation;
			}
		}
		else
		{
			// trying to create enough space for all images
			int counter = 0;
			int y = 0;
			int canvasEyeHeightDividedBy4 = canvasEyeHeight/4;
			for(Image eye : eyesToBeDisplayed){
				counter++;
				if( counter == 6){
					counter = 1;
					//x coord.
					eyeStartingPosition = 0;
					//y coord.
					y += canvasEyeHeightDividedBy4;
				}
				canvasCroppedEyeGc.drawImage(eye, eyeStartingPosition, y,eyeRation,canvasEyeHeightDividedBy4);
				eyeStartingPosition += eyeRation;
			}
		}
	}
	private void initiateEyeParameters(){
		eyeRation = (int) canvasCroppedEye.getWidth()/5;
		canvasEyeHeight = (int) canvasCroppedEye.getHeight();
		

		eyeStartingPosition = 0;

	}
	private void setNumberOfDetectedEyes(ArrayList<Image> eyesToBeDisplayed){
		numberOfDetectedEyes = eyesToBeDisplayed.size();
		System.out.println("N OF D E =" +numberOfDetectedEyes);
	}
	private void deletePreviousEyes(){
		canvasCroppedEyeGc.clearRect(0,0,600,400);
	}

	private ArrayList<Image> getCroppedEyes() {

		return factory.getEyeDetector().getCroppedEyes();
	}

	public void  putFacesOnCanvas(String path){
		this.path = path;
		this.initiateFactory();
		this.loadPhotoAndDetectFaces();
		// main canvas (center)
		gc.drawImage(factory.getFaceDetector().getImage(), 0, 0,200,200);
		//secondary canvas (bottom)
		this.drawFaces();
		//secondary canvas ( left )
	}
	private void initiateFactory(){
		factory = new OpencvFactory();
		Cascades haar = new HaarCascadesImpl();
		factory.setFaceDetector(haar,path);
		factory.setEyeDetector(haar);
		factory.getFaceDetector().setMinFace(5);
	}
	private void drawImage(){

		factory.getFaceDetector().setMinFace(5);
		this.loadPhotoAndDetectFaces();
		gc.drawImage(factory.getFaceDetector().getImage(), 0, 0,200,200);
		this.drawFaces();

	}

	private void loadPhotoAndDetectFaces(){
		factory.getFaceDetector().readAndProcessPhoto();
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
