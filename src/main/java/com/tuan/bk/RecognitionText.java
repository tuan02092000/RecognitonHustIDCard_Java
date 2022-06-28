package com.tuan.bk;

import org.opencv.core.*;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class RecognitionText {
    public static Mat loadImage(String imagePath) {
        Imgcodecs imageCodecs = new Imgcodecs();
        return imageCodecs.imread(imagePath);
    }
    public static void saveImage(Mat imageMatrix, String targetPath) {
        Imgcodecs imgcodecs = new Imgcodecs();
        imgcodecs.imwrite(targetPath, imageMatrix);
    }
    public static ArrayList<Mat> getInformationImage(String sourcePath)
    {
        ArrayList<Mat> arrMat = new ArrayList<>();
        Mat matSrc = loadImage(sourcePath);
        // Creating an empty matrix to store the image
        Mat mat = new Mat();
        // Creating the Size object
        Size size = new Size(2048, 1290);
        // Scaling the Image
        Imgproc.resize(matSrc, mat, size, 0, 0, Imgproc.INTER_AREA);
        // Name
        Mat mat_name = new Mat(mat, new Rect(50, 440, 650, 130));
        arrMat.add(mat_name);
        // MSSV
        Mat mat_id = new Mat(mat, new Rect(1500, 1150, 400, 80));
        arrMat.add(mat_id);
        // Birthday
        Mat mat_birthday = new Mat(mat, new Rect(50, 665, 390, 85));
        arrMat.add(mat_birthday);
        // Class
        Mat mat_class = new Mat(mat, new Rect(50, 730, 1150, 120));
        arrMat.add(mat_class);
        // Expiration date
        Mat mat_exdate = new Mat(mat, new Rect(70, 1160, 260, 70));
        arrMat.add(mat_exdate);
        return arrMat;
    }
    public static BufferedImage getImg(Mat mat) throws IOException {
        // Change Image To Gray Scale
        Mat gray = new Mat();
        Imgproc.cvtColor(mat, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(gray, gray, 0,255, Imgproc.THRESH_BINARY_INV + Imgproc.THRESH_OTSU);

        // Preparing the kernel matrix object
        //  Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new  Size(5, 5));
        int morph_size = 2;
        Mat kernel = Imgproc.getStructuringElement(
                Imgproc.MORPH_RECT, new Size(2 * morph_size,
                        2 * morph_size),
                new Point(morph_size, morph_size));

        // Applying erode on the Image
        Imgproc.erode(gray, gray, kernel);

        // Show Image
        // JFrame frame = new JFrame();
        // frame.getContentPane().add(new JLabel(new ImageIcon(matToBufferedImage(gray))));
        // frame.pack();
        // frame.setTitle("Examples JavaCV");
        // frame.setResizable(false);
        // frame.setLocationRelativeTo(null);
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setVisible(true);

        // Convert To Buffer Image
        MatOfByte mof = new MatOfByte();
        byte imageByte[];
        Imgcodecs.imencode(".png", gray, mof);
        imageByte = mof.toArray();

        return ImageIO.read(new ByteArrayInputStream(imageByte));
    }
    private static BufferedImage matToBufferedImage(Mat matrix) {
        try {
            MatOfByte mob = new MatOfByte();
            Imgcodecs.imencode(".jpg", matrix, mob);
            byte ba[] = mob.toArray();
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(ba));
            return bufferedImage;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args) throws IOException {
        // Load OpenCV
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Tesseract
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:\\Users\\AD\\Documents\\JAVA\\src\\main\\java\\com\\tuan\\bk\\Tess4J\\tessdata");
        tesseract.setLanguage("vie");

        // Process Image
        String sourcePath = "C:\\Users\\AD\\Documents\\JAVA\\src\\main\\java\\com\\tuan\\bk\\images\\nguyenbavinh.png";
        ArrayList<Mat> arrMat = getInformationImage(sourcePath);
        String str[] = {"HỌ và tên: ", "MSSV: ", "Ngày sinh: ", "Lớp: ", "Ngày hết hạn: "};

        // Process File
        String file = "C:\\Users\\AD\\Documents\\JAVA\\src\\main\\java\\com\\tuan\\bk\\results\\result2.txt";
        FileWriter fstream = new FileWriter(file);
        BufferedWriter out = new BufferedWriter(fstream);
        for(int i=0; i < arrMat.size(); i++)
        {
            try {
                String text = tesseract.doOCR(getImg(arrMat.get(i)));
                text = str[i] + text.replaceAll("[\\_\\;\\~\\,\\(\\)\\!\\@\\#\\$\\%\\^\\&\\^\\&\\*\\'\\?\\:\\=]", "").trim();
                System.out.println(text);
                out.write(text);
            } catch (TesseractException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        out.close();
    }
}
