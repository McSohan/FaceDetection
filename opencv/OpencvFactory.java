package opencv;

public class OpencvFactory {

	private FaceDetector faceDetector;
	private EyeDetector eyeDetector;
	
	public void createFactory(Cascades cascade){
		this.setFaceDetector(cascade);
		this.setEyeDetector(cascade);
		
	}
	
	public void setFaceDetector(Cascades cascade){
		faceDetector = cascade.detectFace();
	}
	public void setFaceDetector(Cascades cascade,String filePath){
		faceDetector = cascade.detectFace(filePath);
	}
	public void setEyeDetector(Cascades cascade){
		eyeDetector = cascade.detectEye();
	}
	
	public FaceDetector getFaceDetector(){
		return faceDetector;
	}
	
	public EyeDetector getEyeDetector(){
		return eyeDetector;
	}
	
	public void release(){
		faceDetector.release();
	}
	
	public void open(int cameraNr){
		faceDetector.open(cameraNr);
	}


}
