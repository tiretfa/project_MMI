package com.example.lilia.myapplication;

/**
 * Created by lilia on 22/11/17.
 */
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import org.opencv.imgcodecs.Imgcodecs;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;


public class MainActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static int descriptorHist = DescriptorExtractor.BRISK;

    private static final String TAG = "OCVSample::Activity";
    private int w, h;
    private CameraBridgeViewBase mOpenCvCameraView;
    TextView tvName;
    Scalar RED = new Scalar(255, 0, 0);
    Scalar GREEN = new Scalar(0, 255, 0);
    FeatureDetector detector;
    DescriptorExtractor descriptor;
    DescriptorMatcher matcher;
    Mat descriptors2,descriptors1;
    Mat img1;
    MatOfKeyPoint keypoints1,keypoints2;

    private static InputStream istr;
    private static Button start;

    static {
        if (!OpenCVLoader.initDebug())
            Log.d("ERROR", "Unable to load OpenCV");
        else
            Log.d("SUCCESS", "OpenCV loaded");
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                    try {
                        initializeOpenCVDependencies();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    private void initializeOpenCVDependencies() throws IOException {
        mOpenCvCameraView.enableView();
        detector = FeatureDetector.create(FeatureDetector.ORB);
        //detector = FeatureDetector.create(FeatureDetector.PYRAMID_FAST);
        descriptor = DescriptorExtractor.create(DescriptorExtractor.ORB);
        //descriptor = DescriptorExtractor.create(descriptorHist);
        matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
        img1 = new Mat();
        AssetManager assetManager = getAssets();
        istr = assetManager.open("aa.jpg");


        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        Utils.bitmapToMat(bitmap, img1);
        Imgproc.cvtColor(img1, img1, Imgproc.COLOR_RGB2GRAY);
        //Log.d("lala3", "lala2type::"+ img1.type());
                //img1.convertTo(img1, 0);
                //converting the image to match with the type of the cameras image
        //Log.d("lala3", "lala2typeconvert::"+ img1.type());

        //Imgproc.cvtColor(img1, img1, Imgproc.COLOR_BGR2RGB);

        descriptors1 = new Mat();
        keypoints1 = new MatOfKeyPoint();
        detector.detect(img1, keypoints1);
      //  Log.d("hello","hellooo3");
        descriptor.compute(img1, keypoints1, descriptors1);
       // Log.d("lala", "lala"+descriptors1.height() + " " + descriptors1.width());


    }


    public MainActivity() {

        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.layout);
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        tvName = (TextView) findViewById(R.id.text1);
        start = (Button) MainActivity.this.findViewById(R.id.start);



    }



    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");

            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
        w = width;
        h = height;
    }
    public void onCameraViewStopped() {
    }

    public Mat recognize(Mat aInputFrame) {

        Imgproc.cvtColor(aInputFrame, aInputFrame, Imgproc.COLOR_RGB2GRAY);
        descriptors2 = new Mat();
        keypoints2 = new MatOfKeyPoint();
        detector.detect(aInputFrame, keypoints2);
        descriptor.compute(aInputFrame, keypoints2, descriptors2);
        //Log.wtf("lala", "lala2: type: " + aInputFrame.type());
        // Matching
        MatOfDMatch matches = new MatOfDMatch();
        if (img1.type() == aInputFrame.type() && !keypoints1.empty() && !keypoints2.empty()) {
        //if (img1.type() == aInputFrame.type()){
            Log.wtf("Match","img1==inputframe");
            matcher.match(descriptors1, descriptors2, matches);
        } else {
            //Log.d("Match","img1!==inputframe");
            return aInputFrame;
        }
        List<DMatch> matchesList = matches.toList();

        Double max_dist = 0.0;
        Double min_dist = 100.0;

        for (int i = 0; i < matchesList.size(); i++) {
            Double dist = (double) matchesList.get(i).distance;
            if (dist < min_dist)
                min_dist = dist;
            if (dist > max_dist)
                max_dist = dist;
        }

        LinkedList<DMatch> good_matches = new LinkedList<DMatch>();
        for (int i = 0; i < matchesList.size(); i++) {
            if (matchesList.get(i).distance <= (1.5 * min_dist))
                good_matches.addLast(matchesList.get(i));
        }

        MatOfDMatch goodMatches = new MatOfDMatch();
        goodMatches.fromList(good_matches);
        Mat outputImg = new Mat();
        MatOfByte drawnMatches = new MatOfByte();
        if (aInputFrame.empty() || aInputFrame.cols() < 1 || aInputFrame.rows() < 1) {
            return aInputFrame;
        }
        Features2d.drawMatches(img1, keypoints1, aInputFrame, keypoints2, goodMatches, outputImg, GREEN, RED, drawnMatches, Features2d.NOT_DRAW_SINGLE_POINTS);
        Imgproc.resize(outputImg, outputImg, aInputFrame.size());
        //Log.d("mat ", "mat" +outputImg.toString());
        return outputImg;
    }

    public void run(final Mat atInputFrame){


        Bitmap bmpimg1 = BitmapFactory.decodeStream(istr);
        Bitmap bmpimg2 = Bitmap.createScaledBitmap(bmpimg2, 100, 100, true);




        start.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {


                    Mat img1 = new Mat();
                    Utils.bitmapToMat(bmpimg1, img1);
                    Mat img2 = atInputFrame;
                    Utils.bitmapToMat(bmpimg2, img2);
                    Imgproc.cvtColor(img1, img1, Imgproc.COLOR_RGBA2GRAY);
                    Imgproc.cvtColor(img2, img2, Imgproc.COLOR_RGBA2GRAY);
                    img1.convertTo(img1, CvType.CV_32F);
                    img2.convertTo(img2, CvType.CV_32F);
                    //Log.d("ImageComparator", "img1:"+img1.rows()+"x"+img1.cols()+" img2:"+img2.rows()+"x"+img2.cols());
                    Mat hist1 = new Mat();
                    Mat hist2 = new Mat();
                    MatOfInt histSize = new MatOfInt(180);
                    MatOfInt channels = new MatOfInt(0);
                    ArrayList<Mat> bgr_planes1= new ArrayList<Mat>();
                    ArrayList<Mat> bgr_planes2= new ArrayList<Mat>();
                    Core.split(img1, bgr_planes1);
                    Core.split(img2, bgr_planes2);
                    MatOfFloat histRanges = new MatOfFloat (0f, 180f);
                    boolean accumulate = false;
                    Imgproc.calcHist(bgr_planes1, channels, new Mat(), hist1, histSize, histRanges, accumulate);
                    Core.normalize(hist1, hist1, 0, hist1.rows(), Core.NORM_MINMAX, -1, new Mat());
                    Imgproc.calcHist(bgr_planes2, channels, new Mat(), hist2, histSize, histRanges, accumulate);
                    Core.normalize(hist2, hist2, 0, hist2.rows(), Core.NORM_MINMAX, -1, new Mat());
                    img1.convertTo(img1, CvType.CV_32F);
                    img2.convertTo(img2, CvType.CV_32F);
                    hist1.convertTo(hist1, CvType.CV_32F);
                    hist2.convertTo(hist2, CvType.CV_32F);

                    double compare = Imgproc.compareHist(hist1, hist2, Imgproc.CV_COMP_CHISQR);
                    Log.d("ImageComparator", "compare: "+compare);
                    if(compare>0 && compare<1500) {
                        Toast.makeText(MainActivity.this, "Images may be possible duplicates, verifying", Toast.LENGTH_LONG).show();
                        new asyncTask(MainActivity.this).execute();
                    }
                    else if(compare==0)
                        Toast.makeText(MainActivity.this, "Images are exact duplicates", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(MainActivity.this, "Images are not duplicates", Toast.LENGTH_LONG).show();

                    startTime = System.currentTimeMillis();
                } else
                    Toast.makeText(MainActivity.this,
                            "You haven't selected images.", Toast.LENGTH_LONG)
                            .show();
            }
        });
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        return recognize(inputFrame.rgba());

    }
}