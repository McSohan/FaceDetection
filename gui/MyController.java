package gui;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;

import display.DisplayPathOnCanvas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import opencv.Cascades;
import opencv.HaarCascadesImpl;
import opencv.LbpCascadesImpl;
import opencv.OpencvFactory;
import thread.DetectorThread;



public class MyController {

	@FXML Label haarLabel;
	@FXML MenuItem loadMenuItem;
	@FXML MenuBar menuBar;
	@FXML Canvas canvas;
	@FXML Canvas canvasCroppedFace;
	@FXML Canvas canvasEye;
	@FXML Button haarDetectButton;
	@FXML Button haarStopDetectButton;
	@FXML Button lbpDetectButton;
	@FXML Button lbpStopDetectButton;
	@FXML Slider slider;
	FileChooser fileChooser;
	DetectorThread thread;
	DisplayPathOnCanvas draw;
	OpencvFactory factory;
	boolean cascadeIsRunning;

	public MyController() {
		cascadeIsRunning = false;
		fileChooser = new FileChooser();
	}

	private void initHaar(){
		this.createFactory();
		Cascades haar = new HaarCascadesImpl();
		factory.setFaceDetector(haar);
		factory.setEyeDetector(haar);
		thread = new DetectorThread(factory,canvas,canvasCroppedFace,canvasEye);

	}
	private void initLbp(){
		this.createFactory();
		Cascades lbp = new LbpCascadesImpl();
		factory.setFaceDetector(lbp);
		factory.setEyeDetector(lbp);
		thread = new DetectorThread(factory,canvas,canvasCroppedFace,canvasEye);

	}
	private void createFactory(){
		factory = new OpencvFactory();
	}

	private void initLbp(String filePath){

		factory = new OpencvFactory();
		Cascades lbp = new LbpCascadesImpl();
		factory.setEyeDetector(lbp);
		factory.setFaceDetector(lbp,filePath);

	}
	@FXML
	public void haarDetectOnAction(){
		if(cascadeIsRunning == false){
			System.out.println("REC");
			this.initHaar();
			thread.start();
			cascadeIsRunning = true;
		}

	}

	@FXML
	public void haarStopDetectOnAction(){
		if( cascadeIsRunning == true){
			thread.stopThread();
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			factory.release();
			cascadeIsRunning = false;

		}
	}

	// load file with filechooser
	@FXML 
	public void loadAction(){
		if(cascadeIsRunning){
			this.haarStopDetectOnAction();
		}

		fileChooser.setTitle("Load file");

		File file = fileChooser.showOpenDialog(null);
		if( file!= null){
			String filePath=file.getAbsolutePath().replaceAll("\\\\", Matcher.quoteReplacement("/"));
			
			draw = new DisplayPathOnCanvas(factory, canvas, canvasCroppedFace, canvasEye);
			draw.putEyesAndFacesOnCanvas(filePath);
		}

	}
	

	@FXML 
	public void lbpStopDetectOnAction(ActionEvent e){
		this.haarStopDetectOnAction();
	}
	@FXML
	public void lbpDetectOnAction(){
		if(cascadeIsRunning == false){
			System.out.println("REC");
			
			this.initLbp();
			thread.start();
			cascadeIsRunning = true;
		}
	}

}
