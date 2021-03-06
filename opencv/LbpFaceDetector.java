package opencv;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;

import javafx.scene.image.Image;

public abstract class LbpFaceDetector implements FaceDetector {

	private int MIN_FACE_SIZE;
	private String path;
	private Mat grayFrame;
	private Mat blurFrame;
	private Mat cannyFrame;
	private Mat initialFrame;
	private Mat equalizedFrame;
	private MatOfRect faces;
	private VideoCapture videoCapture;
	private CascadeClassifier faceCascade;
	private ArrayList<Mat> croppedFacesMat;
	private ArrayList<Image> croppedFacesImg;

	/*webcam constructor */
	public LbpFaceDetector() {
		this.init();
	}
	/* read image from file. */
	public LbpFaceDetector(String path){
		this.init();
		this.path = path;
		System.out.println("LbpDetector status:ok");
	}

	private void init(){
		this.videoCapture = new VideoCapture();
		this.initialFrame = new Mat();
		this.grayFrame = new Mat();
		this.equalizedFrame = new Mat();
		this.cannyFrame = new Mat();
		this.blurFrame = new Mat();
		this.croppedFacesImg = new ArrayList<Image>();
		this.croppedFacesMat = new ArrayList<Mat>();
		this.faces = new MatOfRect();
		this.faceCascade = new CascadeClassifier("E:/3UBB/java/projectopencv/openfx/lbpcascade_frontalcatface.xml");
		this.MIN_FACE_SIZE = 80;
	}

	/* get the image from the given path -> initialFrame
	 * initialFrame -> grayFrame -> equalizedFrame
	 * apply haar cascade on the equalizedFrame
	 */
	@Override
	public void readAndProcessPhoto() {
		initialFrame = Imgcodecs.imread(path);
		this.getGrayFrame();
		this.getEqualizedFrame();
		this.getBlurFrame();
		this.getCannyFrame();
		this.detectFace();
		System.out.println(path);
	}
	/*Open the camera, if it is not possible then throw an exception
	 *
	 */
	@Override
	public void openCamera(int cameraNr) throws MyException{
		if(videoCapture.isOpened() == false){
			videoCapture.open(cameraNr);

			if(videoCapture.isOpened() == false){
				throw new MyException("HaarDetector: Failed to open camera "+cameraNr);
			}
		}
	}

	/*Return the initialFrame as an image*/ 
	@Override
	public Image getImage() {
		return this.mat2Image(initialFrame);
	}

	/* step 1  step2   step3     step4    step 5
	 * read -> gray ->equalize->detect -> draw
	 */
	public void detectFaceFromWebCam()throws MyException{
		//open camera if it's closed
		this.openCamera(0);
		try {
			this.getFrame(0);
			this.getGrayFrame();
			this.getEqualizedFrame();
		} catch (MyException e) {
			// TODO Auto-generated catch block
			throw e;
		}
		this.detectFace();

	}
	
	//close the camera connection
	public void release(){
		videoCapture.release();
	}

	//can't display the Mat type, so it need's to be converted to Image type
	private Image mat2Image(Mat frame)
	{
		MatOfByte buffer = new MatOfByte();
		Imgcodecs.imencode(".jpg", frame, buffer);
		return new Image(new ByteArrayInputStream(buffer.toArray()));
	}

	@Override
	public void open(int cameraNr){
		if( videoCapture.isOpened() == false){
			videoCapture.open(cameraNr);
		}
	}
	
	//return arraylist with faces
	public ArrayList<Image> getCroppedFaces(){
		for( Mat faces : croppedFacesMat){
			croppedFacesImg.add(this.mat2Image(faces));
		}

		return croppedFacesImg;
	}

	private Mat getFrame(int cameraNr) throws MyException{
		if(videoCapture.read(initialFrame)){
			return initialFrame;
		}
		else
			throw new MyException("HaarDetector: Failed to get stream from device nr "+cameraNr);
	}

	private void getGrayFrame(){
		Imgproc.cvtColor(initialFrame,grayFrame, Imgproc.COLOR_BGR2GRAY);

	}
	private void getBlurFrame(){
		Imgproc.blur(equalizedFrame, blurFrame, new Size(3,3));

	}

	private void getCannyFrame(){
		Imgproc.Canny(grayFrame, cannyFrame, 30, 100);

	}

	private void getEqualizedFrame() {
		Imgproc.equalizeHist(grayFrame,equalizedFrame);

	}
	
	/* step 4 and step 5
	 * detect and draw
	 */
	private void detectFace(){
		faceCascade.detectMultiScale(equalizedFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE, new Size(MIN_FACE_SIZE,MIN_FACE_SIZE), new Size());
		System.out.println(MIN_FACE_SIZE);

		for(Rect rect : faces.toArray()){
			Imgproc.rectangle(initialFrame,new Point(rect.x,rect.y),new Point(rect.x+rect.width,rect.y+rect.height), new Scalar(255,255,255));

			croppedFacesMat.add(new Mat(initialFrame,rect));
		}
	}
	
	@Override
	public void associateEyes(EyeDetector eyeDetector){
		
		eyeDetector.findEyes(initialFrame);
	}

}
