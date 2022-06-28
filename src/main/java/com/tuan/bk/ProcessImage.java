package com.tuan.bk;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class ProcessImage {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);


        String sourcePath = "C:\\Users\\AD\\Documents\\JAVA\\src\\main\\java\\com\\tuan\\bk\\images\\nguyenbavinh.png";

        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(sourcePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int height = image.getHeight();
        int width = image.getWidth();

        System.out.println(height);
        System.out.println(width);

        Mat mat = loadImage(sourcePath);
        int h1 = mat.height();
        int w1 = mat.width();
        System.out.println(h1);
        System.out.println(w1);

        // Zoom Image
        // Name
        // Rect rectCrop = new Rect(80, 435, 480, 130);
        Mat mat_name = new Mat(mat, new Rect(50, 435, 550, 130));
        // Birthday
        Mat mat_birthday = new Mat(mat, new Rect(50, 658, 390, 85));
        // Class2
        Mat mat_class = new Mat(mat, new Rect(50, 730, 1150, 120));
        // MSSV
        Mat mat_id = new Mat(mat, new Rect(1500, 1150, 400, 80));
        // Expiration date
        Mat mat_exdate = new Mat(mat, new Rect(70, 1160, 260, 70));


        // Save Image
        // String targetPath = "C:\\Users\\AD\\Documents\\JAVA\\src\\main\\java\\com\\tuan\\bk\\results\\resize.jpg";
        // saveImage(mat, targetPath);

        // Show Image
        JFrame frame = new JFrame();
        frame.getContentPane().add(new JLabel(new ImageIcon(matToBufferedImage(mat_name))));
        frame.pack();
        frame.setTitle("Examples JavaCV");
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
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
    public static Mat loadImage(String imagePath) {
        Imgcodecs imageCodecs = new Imgcodecs();
        return imageCodecs.imread(imagePath);
    }
    public static void saveImage(Mat imageMatrix, String targetPath) {
        Imgcodecs imgcodecs = new Imgcodecs();
        imgcodecs.imwrite(targetPath, imageMatrix);
    }
}
