package com.tuan.bk;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class GUI_Recognition_Card extends JFrame{
    private JPanel mainPanel;
    private JLabel imgLabel;
    private JLabel infoLabel;
    private JButton selectBtn;
    private JTextField nameTextField;
    private JTextField idTextField;
    private JLabel nameLabel;
    private JLabel idLabel;
    private JTextField classTextField;
    private JLabel labelClass;
    private JTextField birthTextField;
    private JLabel birthLabel;
    private JTextField exdateTextField;
    private JLabel exdateLabel;
    private JButton saveBtn;
    private JButton clearBtn;
    private JButton readBtn;
    private JPanel secondPanel;
    private JLabel image;
    private JLabel mess;

    File f = null;
    String path = null;
    private ImageIcon format = null;
    public static ArrayList<Mat> arrMat = null;
    public static Tesseract tesseract = null;
    String nameText = null;
    String idText = null;
    String birthdayText = null;
    String classText = null;
    String exdateText = null;
    public static String[] stringConcat = {"HỌ và tên: ", "MSSV: ", "Ngày sinh: ", "Lớp: ", "Ngày hết hạn: "};

    public GUI_Recognition_Card()
    {
        setContentPane(mainPanel);
        setTitle("Recognition ID Card");
        setSize(2000, 1000);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        selectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter fnwf = new FileNameExtensionFilter("PNG JPG AND JPEG", "png", "jpeg", "jpg");
                fileChooser.addChoosableFileFilter(fnwf);
                int load = fileChooser.showOpenDialog(null);
                if(load == fileChooser.APPROVE_OPTION)
                {
                    f = fileChooser.getSelectedFile();
                    path = f.getAbsolutePath();
                    ImageIcon ii = new ImageIcon(path);
                    Image img = ii.getImage().getScaledInstance(800, 600, Image.SCALE_SMOOTH);
                    image.setIcon(new ImageIcon(img));
                    arrMat = getInformationImage(path);
                }
            }
        });
        readBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    nameText = tesseract.doOCR(getImg(arrMat.get(0)));
                    nameText = nameText.replaceAll("[\\_\\;\\~\\,\\(\\)\\!\\@\\#\\$\\%\\^\\&\\^\\&\\*\\'\\?\\:\\=]", "").trim();
                    nameTextField.setText(nameText);

                    idText = tesseract.doOCR(getImg(arrMat.get(1)));
                    idText = idText.replaceAll("[\\_\\;\\~\\,\\(\\)\\!\\@\\#\\$\\%\\^\\&\\^\\&\\*\\'\\?\\:\\=]", "").trim();
                    idTextField.setText(idText);

                    birthdayText = tesseract.doOCR(getImg(arrMat.get(2)));
                    birthdayText = birthdayText.replaceAll("[\\_\\;\\~\\,\\(\\)\\!\\@\\#\\$\\%\\^\\&\\^\\&\\*\\'\\?\\:\\=]", "").trim();
                    birthTextField.setText(birthdayText);

                    classText = tesseract.doOCR(getImg(arrMat.get(3)));
                    classText = classText.replaceAll("[\\_\\;\\~\\,\\(\\)\\!\\@\\#\\$\\%\\^\\&\\^\\&\\*\\'\\?\\:\\=]", "").trim();
                    classTextField.setText(classText);

                    exdateText = tesseract.doOCR(getImg(arrMat.get(4)));
                    exdateText = exdateText.replaceAll("[\\_\\;\\~\\,\\(\\)\\!\\@\\#\\$\\%\\^\\&\\^\\&\\*\\'\\?\\:\\=]", "").trim();
                    exdateTextField.setText(exdateText);

                    mess.setText("Read Image Successfully!");
                } catch (TesseractException tesseractException) {
                    tesseractException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nameTextField.setText("");
                idTextField.setText("");
                birthTextField.setText("");
                classTextField.setText("");
                exdateTextField.setText("");
                mess.setText("");
            }
        });
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String file = "C:\\Users\\AD\\Documents\\JAVA\\src\\main\\java\\com\\tuan\\bk\\results\\" + idText + ".txt";
                System.out.println(file);
                FileWriter fstream = null;
                try {
                    fstream = new FileWriter(file);
                    BufferedWriter out = new BufferedWriter(fstream);
                    // Name
                    String nameTextFile = stringConcat[0] + nameText;
                    out.write(nameTextFile);
                    // ID
                    String idTextFile = stringConcat[1] + idText;
                    out.newLine();
                    out.write(idTextFile);
                    // Birthday
                    String birthTextFile = stringConcat[2] + birthdayText;
                    out.newLine();
                    out.write(birthTextFile);
                    // Class
                    String classTextFile = stringConcat[3] + classText;
                    out.newLine();
                    out.write(classTextFile);
                    // Expiration date
                    String exdateTextFile = stringConcat[4] + exdateText;
                    out.newLine();
                    out.write(exdateTextFile);
                    // Close file
                    out.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                mess.setText("Save done!");
            }
        });
    }
    public static Mat loadImage(String imagePath) {
        Imgcodecs imageCodecs = new Imgcodecs();
        return imageCodecs.imread(imagePath);
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
        // Imgproc.threshold(gray, gray, 125,255, Imgproc.THRESH_BINARY_INV + Imgproc.THRESH_OTSU);
        Imgproc.threshold(gray, gray, 0,255, Imgproc.THRESH_BINARY_INV + Imgproc.THRESH_OTSU);

        // Preparing the kernel matrix object
        // Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new  Size(5, 5));
        int morph_size = 2;
        Mat kernel = Imgproc.getStructuringElement(
                Imgproc.MORPH_RECT, new Size(2 * morph_size + 1,
                        2 * morph_size + 1),
                new Point(morph_size, morph_size));

        // Applying erode on the Image
        Imgproc.erode(gray, gray, kernel);

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
    public static void main(String[] args) {
        // Load OpenCV
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Tesseract
        tesseract = new Tesseract();
        tesseract.setDatapath("C:\\Users\\AD\\Documents\\JAVA\\src\\main\\java\\com\\tuan\\bk\\Tess4J\\tessdata");
        tesseract.setLanguage("vie");

        new GUI_Recognition_Card();
    }
}
