package opencv;

import java.util.ArrayList;

import javafx.scene.image.Image;

public interface FaceDetector {

	
	
	public void openCamera(int cameraNr) throws MyException;
	public void detectFaceFromWebCam() throws MyException;
	public void release();
	public void open(int cameraNr);
	public void readAndProcessPhoto();
	public Image getImage();
	public ArrayList<Image> getCroppedFaces();
	public void setMinFace(int i);
	public void associateEyes(EyeDetector eyeDetector);
	
	
	
}
