package opencv;

public interface Cascades {

	public FaceDetector detectFace();
	public EyeDetector detectEye();
	public FaceDetector detectFace(String filePath);
}
