package ravensproject;

// Uncomment these lines to access image processing.

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Your Agent for solving Raven's Progressive Matrices. You MUST modify this
 * file.
 * <p/>
 * You may also create and submit new files in addition to modifying this file.
 * <p/>
 * Make sure your file retains methods with the signatures:
 * public Agent()
 * public char Solve(RavensProblem problem)
 * <p/>
 * These methods will be necessary for the project's main method to run.
 */
public class Agent {
  /**
   * The default constructor for your Agent. Make sure to execute any
   * processing necessary before your Agent starts solving problems here.
   * <p/>
   * Do not add any variables to this signature; they will not be used by
   * main().
   */
  public Agent() {

  }

  /**
   * The primary method for solving incoming Raven's Progressive Matrices.
   * For each problem, your Agent's Solve() method will be called. At the
   * conclusion of Solve(), your Agent should return a String representing its
   * answer to the question: "1", "2", "3", "4", "5", or "6". These Strings
   * are also the Names of the individual RavensFigures, obtained through
   * RavensFigure.getName().
   * <p/>
   * In addition to returning your answer at the end of the method, your Agent
   * may also call problem.checkAnswer(String givenAnswer). The parameter
   * passed to checkAnswer should be your Agent's current guess for the
   * problem; checkAnswer will return the correct answer to the problem. This
   * allows your Agent to check its answer. Note, however, that after your
   * agent has called checkAnswer, it will *not* be able to change its answer.
   * checkAnswer is used to allow your Agent to learn from its incorrect
   * answers; however, your Agent cannot change the answer to a question it
   * has already answered.
   * <p/>
   * If your Agent calls checkAnswer during execution of Solve, the answer it
   * returns will be ignored; otherwise, the answer returned at the end of
   * Solve will be taken as your Agent's answer to this problem.
   *
   * @param problem the RavensProblem your agent should solve
   * @return your Agent's answer to this problem
   */
  public int Solve(RavensProblem problem) {
    return -1;
  }

  public void initialization(RavensProblem problem) {
    Map<String, BufferedImage> images = new HashMap<>();
    Map<String, BufferedImage> answerImages = new HashMap<>();
    Map<Integer, List<String>> completedAnalogies = new HashMap<>();
    Map<Integer, List<String>> partialAnalogies = new HashMap<>();

    // Open all images for problem
    for (Map.Entry<String, RavensFigure> figure : problem.getFigures().entrySet()) {
      if (!Character.isDigit(figure.getKey().charAt(0)))
        images.put(
            figure.getKey(),
            convertToGrayscale(
                openImage(problem.getFigures().get(figure.getKey()).getVisual())
            )
        );
    }

    // Open all answer images for problem
    for (Map.Entry<String, RavensFigure> figure : problem.getFigures().entrySet()) {
      if (Character.isDigit(figure.getKey().charAt(0)))
        answerImages.put(
            figure.getKey(),
            convertToGrayscale(
                openImage(problem.getFigures().get(figure.getKey()).getVisual())
            )
        );
    }

    // Define completed analogies
    completedAnalogies.put(1, Arrays.asList("A", "B", "C"));
    completedAnalogies.put(2, Arrays.asList("D", "E", "F"));
    completedAnalogies.put(3, Arrays.asList("A", "D", "G"));
    completedAnalogies.put(4, Arrays.asList("B", "E", "H"));

    // Define parallel partial analogies
    partialAnalogies.put(1, Arrays.asList("G", "H"));
    partialAnalogies.put(2, Arrays.asList("G", "H"));
    partialAnalogies.put(3, Arrays.asList("C", "F"));
    partialAnalogies.put(4, Arrays.asList("C", "F"));
  }

  public void transformationInduction(Map<Integer, List<String>> analogies, Map<String, BufferedImage> images) {
    for (Map.Entry<Integer, List<String>> entry : analogies.entrySet()) {

    }
  }

  public void answerSelection() {

  }

  /**
   * This method flips an image horizontally. Credit goes to Byron Kiourtzoglou
   * for the original code:
   * <p/>
   * http://examples.javacodegeeks.com/desktop-java/awt/image/flipping-a-buffered-image/
   *
   * @param image
   * @return
   */
  public BufferedImage horizontalFlip(BufferedImage image) {
    BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

    AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
    tx.translate(-image.getWidth(null), 0);
    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
    op.filter(image, result);

    return result;
  }

  /**
   * This method flips an image vertically. Credit goes to Byron Kiourtzoglou
   * for the original code:
   * <p/>
   * http://examples.javacodegeeks.com/desktop-java/awt/image/flipping-a-buffered-image/
   *
   * @param image
   * @return
   */
  public BufferedImage verticalFlip(BufferedImage image) {
    BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

    AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
    tx.translate(0, -image.getHeight(null));
    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
    op.filter(image, result);

    return result;
  }

  /**
   * This method rotates an image about its center. Theta is a radian value:
   * <p/>
   * 90 degrees = pi / 2
   * 180 degrees = pi
   * 270 degrees = 3 * pi / 2
   *
   * @return The result of the transformation
   * @image The image to rotate
   * @theta A radian value for the rotation.
   */
  public BufferedImage rotateImage(BufferedImage image, double theta) {
    BufferedImage result = new BufferedImage(image.getHeight(), image.getWidth(), image.getType());

		/* Transformations are stacked then applied so the last transformation is the first to happen */
    AffineTransform tx = new AffineTransform();

    tx.translate(image.getHeight() / 2, image.getWidth() / 2);
    tx.rotate(theta);
    tx.translate(-image.getWidth() / 2, -image.getHeight() / 2);

    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
    op.filter(image, result);

    return result;
  }

  /**
   * This method rotates an image 90 degrees then reflects the image over the
   * x axis.
   *
   * @param image
   * @return
   */
  public BufferedImage rotate90flip(BufferedImage image) {
    BufferedImage result = null;

    // Rotate 90 degrees
    result = rotateImage(image, Math.PI / 2);
    // Reflect image over x axis
    result = verticalFlip(result);

    return result;
  }

  /**
   * This method rotates an image 270 degrees then reflects the image over the
   * x axis.
   *
   * @param image
   * @return
   */
  public BufferedImage rotate270flip(BufferedImage image) {
    BufferedImage result = null;

    // Rotate 270 degrees
    result = rotateImage(image, 3 * Math.PI / 2);
    // Reflect image over x axis
    result = verticalFlip(result);

    return result;
  }

  public double [][] unionTransform(double [][] pixelArray1, double [][] pixelArray2) {
    double [][] resultPixelArray = new double [pixelArray1.length][pixelArray1.length];

    for (int i = 0; i < pixelArray1.length; i++) {
      for (int j = 0; j < pixelArray1[i].length; j++) {
        resultPixelArray[i][j] = Math.max(pixelArray1[i][j], pixelArray2[i][j]);
      }
    }

    return resultPixelArray;
  }

  public double [][] intersectionTransform(double [][] pixelArray1, double [][] pixelArray2) {
    double [][] resultPixelArray = new double [pixelArray1.length][pixelArray1.length];

    for (int i = 0; i < pixelArray1.length; i++) {
      for (int j = 0; j < pixelArray1[i].length; j++) {
        resultPixelArray[i][j] = Math.min(pixelArray1[i][j], pixelArray2[i][j]);
      }
    }

    return resultPixelArray;
  }

  public double [][] subtractionTransform(double [][] pixelArray1, double [][] pixelArray2) {
    double [][] resultPixelArray = new double [pixelArray1.length][pixelArray1.length];

    for (int i = 0; i < pixelArray1.length; i++) {
      for (int j = 0; j < pixelArray1[i].length; j++) {
        resultPixelArray[i][j] = pixelArray1[i][j] - pixelArray2[i][j];
      }
    }

    return resultPixelArray;
  }

  public double [][] backSubtractionTransform(double [][] pixelArray1, double [][] pixelArray2) {
    double [][] resultPixelArray = new double [pixelArray1.length][pixelArray1.length];

    for (int i = 0; i < pixelArray2.length; i++) {
      for (int j = 0; j < pixelArray2[i].length; j++) {
        resultPixelArray[i][j] = pixelArray2[i][j] - pixelArray1[i][j];
      }
    }

    return resultPixelArray;
  }

  public double [][] exclusiveOrTransform(double [][] pixelArray1, double [][] pixelArray2) {
    double [][] resultPixelArray = new double [pixelArray1.length][pixelArray1.length];

    for (int i = 0; i < pixelArray1.length; i++) {
      for (int j = 0; j < pixelArray1[i].length; j++) {
        resultPixelArray[i][j] = Math.max(pixelArray1[i][j], pixelArray2[i][j]) - Math.min(pixelArray1[i][j], pixelArray2[i][j]);
      }
    }

    return resultPixelArray;
  }

  public double calculateTverskyRatio(double [][] pixelArray1, double [][] pixelArray2) {
    double alpha = 2.0;
    double beta = 1.0;
    double intersection = intersect2dArrays(pixelArray1, pixelArray2);
    double difference = difference2dArrays(pixelArray1, pixelArray2);
    double difference2 = difference2dArrays(pixelArray2, pixelArray1);

    return intersection / (intersection + alpha * difference + beta * difference2);
  }

  public double calculateTverskyRatio2(double [][] pixelArray1, double [][] pixelArray2) {
    return intersect2dArrays(pixelArray1, pixelArray2) / union2dArrays(pixelArray1, pixelArray2);
  }

  public double union2dArrays(double[][] array1, double[][] array2) {
    double count = 0;

    for (int i = 0; i < array1.length; i++) {
      for (int j = 0; j < array1[i].length; j++) {
        if (array1[i][j] != 0 || array2[i][j] != 0)
          count++;
      }
    }

    return count;
  }

  public double intersect2dArrays(double[][] array1, double[][] array2) {
    double count = 0;

    for (int i = 0; i < array1.length; i++) {
      for (int j = 0; j < array1[i].length; j++) {
        if (array1[i][j] != 0 && array1[i][j] == array2[i][j])
          count++;
      }
    }

    return count;
  }

  public double difference2dArrays(double[][] array1, double[][] array2) {
    double count = 0;

    for (int i = 0; i < array1.length; i++) {
      for (int j = 0; j < array1[i].length; j++) {
        if (array1[i][j] != 0 && array1[i][j] != array2[i][j])
          count++;
      }
    }

    return count;
  }

  /**
   * This method opens an image from a file.
   *
   * @param path
   * @return
   */
  public BufferedImage openImage(String path) {
    // Open image
    try {
      return ImageIO.read(new File(path));
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Converts an RGB color image to a grayscale image.
   *
   * Original code by vulkanino:
   * http://stackoverflow.com/questions/9131678/convert-a-rgb-image-to-grayscale-image-reducing-the-memory-in-java
   */
  public BufferedImage convertToGrayscale(BufferedImage image) {
    BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
    Graphics graphics = image.getGraphics();

    graphics.drawImage(image, 0, 0, null);
    graphics.dispose();

    return result;
  }

  /**
   * TODO could be int[][]?
   *
   * Original code by blackSmith:
   * http://stackoverflow.com/questions/17278829/grayscale-bitmap-into-2d-array
   */
  public double[][] getGrayscalePixelMatrix(BufferedImage image) {
    int width = image.getWidth();
    int height = image.getHeight();
    double[][] matrix = new double[width][height];

    Raster raster = image.getData();
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        matrix[i][j] = raster.getSample(i, j, 0);
      }
    }

    return matrix;
  }
}
