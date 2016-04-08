package opencv;

public class LbpCascadesImpl implements Cascades {

	@Override
	public FaceDetector detectFace() {
		// TODO Auto-generated method stub
		return new HaarFaceDetector();
	}
	@Override
	public FaceDetector detectFace(String filePath) {
		// TODO Auto-generated method stub
		return new HaarFaceDetector(filePath);
	}
	@Override
	public EyeDetector detectEye() {
		// TODO Auto-generated method stub
		return new LbpEyeDetector() {
		};
	}

	

}
