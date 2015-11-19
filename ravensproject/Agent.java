package ravensproject;

// Uncomment these lines to access image processing.

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * Your Agent for solving Raven's Progressive Matrices. You MUST modify this
 * file.
 * <p/>
 * You may also create and submit new files in addition to modifying this file.
 * <p/>
 * Make sure your file retains methods with the signatures: public Agent()
 * public char Solve(RavensProblem problem)
 * <p/>
 * These methods will be necessary for the project's main method to run.
 */
public class Agent {
  /**
   * The default constructor for your Agent. Make sure to execute any processing
   * necessary before your Agent starts solving problems here.
   * <p/>
   * Do not add any variables to this signature; they will not be used by
   * main().
   */
  public Agent() {

  }

  /**
   * The primary method for solving incoming Raven's Progressive Matrices. For
   * each problem, your Agent's Solve() method will be called. At the conclusion
   * of Solve(), your Agent should return a String representing its answer to
   * the question: "1", "2", "3", "4", "5", or "6". These Strings are also the
   * Names of the individual RavensFigures, obtained through
   * RavensFigure.getName().
   * <p/>
   * In addition to returning your answer at the end of the method, your Agent
   * may also call problem.checkAnswer(String givenAnswer). The parameter passed
   * to checkAnswer should be your Agent's current guess for the problem;
   * checkAnswer will return the correct answer to the problem. This allows your
   * Agent to check its answer. Note, however, that after your agent has called
   * checkAnswer, it will *not* be able to change its answer. checkAnswer is
   * used to allow your Agent to learn from its incorrect answers; however, your
   * Agent cannot change the answer to a question it has already answered.
   * <p/>
   * If your Agent calls checkAnswer during execution of Solve, the answer it
   * returns will be ignored; otherwise, the answer returned at the end of Solve
   * will be taken as your Agent's answer to this problem.
   *
   * @param problem
   *          the RavensProblem your agent should solve
   * @return your Agent's answer to this problem
   */
  public int Solve(RavensProblem problem) {
    if (problem.getProblemType().equals("3x3"))
      return initialization(problem);
    else
      return -1;
  }

  public int initialization(RavensProblem problem) {
    Map<String, BufferedImage> images = new HashMap<>();
    Map<String, BufferedImage> answerImages = new HashMap<>();

    Map<Integer, List<String>> completedAnalogyTriplets = new HashMap<>();
    Map<Integer, List<String>> partialAnalogyTriplets = new HashMap<>();
    List<List<Integer>> completedParallelAnalogyTriplets = new ArrayList<>();

    Map<Integer, Pair<String, String>> completedAnalogyPairs = new HashMap<>();
    Map<Integer, Pair<String, String>> partialAnalogyPairs = new HashMap<>();
    List<List<Integer>> completedParallelAnalogyPairs = new ArrayList<>();

    // Open all images for problem
    for (Map.Entry<String, RavensFigure> figure : problem.getFigures().entrySet()) {
      if (!Character.isDigit(figure.getKey().charAt(0)))
        images.put(figure.getKey(), convertToGrayscale(openImage(problem.getFigures().get(figure.getKey()).getVisual())));
    }

    // Open all answer images for problem
    for (Map.Entry<String, RavensFigure> figure : problem.getFigures().entrySet()) {
      if (Character.isDigit(figure.getKey().charAt(0)))
        answerImages.put(figure.getKey(), convertToGrayscale(openImage(problem.getFigures().get(figure.getKey()).getVisual())));
    }

    // Define completed analogies
    completedAnalogyTriplets.put(1, Arrays.asList("A", "B", "C"));
    completedAnalogyTriplets.put(2, Arrays.asList("D", "E", "F"));
    completedAnalogyTriplets.put(3, Arrays.asList("A", "D", "G"));
    completedAnalogyTriplets.put(4, Arrays.asList("B", "E", "H"));
    completedAnalogyTriplets.put(5, Arrays.asList("F", "G", "B"));
    completedAnalogyTriplets.put(6, Arrays.asList("H", "C", "D"));
    completedAnalogyTriplets.put(7, Arrays.asList("F", "H", "A"));
    completedAnalogyTriplets.put(8, Arrays.asList("G", "C", "E"));

    // Define parallel partial analogies
    partialAnalogyTriplets.put(1, Arrays.asList("G", "H", "?"));
    partialAnalogyTriplets.put(2, Arrays.asList("G", "H", "?"));
    partialAnalogyTriplets.put(3, Arrays.asList("C", "F", "?"));
    partialAnalogyTriplets.put(4, Arrays.asList("C", "F", "?"));
    partialAnalogyTriplets.put(5, Arrays.asList("A", "E", "?"));
    partialAnalogyTriplets.put(6, Arrays.asList("A", "E", "?"));
    partialAnalogyTriplets.put(7, Arrays.asList("B", "D", "?"));
    partialAnalogyTriplets.put(8, Arrays.asList("B", "D", "?"));

    // Define completed parallel analogies
    completedParallelAnalogyTriplets.add(Arrays.asList(1, 2));
    completedParallelAnalogyTriplets.add(Arrays.asList(3, 4));
    completedParallelAnalogyTriplets.add(Arrays.asList(5, 6));
    completedParallelAnalogyTriplets.add(Arrays.asList(7, 8));

    // Define completed analogy pairs
    completedAnalogyPairs.put(1, new Pair<>("A", "B"));
    completedAnalogyPairs.put(2, new Pair<>("B", "C"));
    completedAnalogyPairs.put(3, new Pair<>("D", "E"));
    completedAnalogyPairs.put(4, new Pair<>("E", "F"));
    completedAnalogyPairs.put(5, new Pair<>("G", "H"));
    completedAnalogyPairs.put(6, new Pair<>("A", "C"));
    completedAnalogyPairs.put(7, new Pair<>("D", "F"));
    completedAnalogyPairs.put(8, new Pair<>("A", "D"));
    completedAnalogyPairs.put(9, new Pair<>("D", "G"));
    completedAnalogyPairs.put(10, new Pair<>("B", "E"));
    completedAnalogyPairs.put(11, new Pair<>("E", "H"));
    completedAnalogyPairs.put(12, new Pair<>("C", "F"));
    completedAnalogyPairs.put(13, new Pair<>("A", "G"));
    completedAnalogyPairs.put(14, new Pair<>("B", "H"));
    completedAnalogyPairs.put(15, new Pair<>("F", "G"));
    completedAnalogyPairs.put(16, new Pair<>("G", "B"));
    completedAnalogyPairs.put(17, new Pair<>("H", "C"));
    completedAnalogyPairs.put(18, new Pair<>("C", "D"));
    completedAnalogyPairs.put(19, new Pair<>("A", "E"));
    completedAnalogyPairs.put(20, new Pair<>("F", "B"));
    completedAnalogyPairs.put(21, new Pair<>("H", "D"));
    completedAnalogyPairs.put(22, new Pair<>("F", "H"));
    completedAnalogyPairs.put(23, new Pair<>("H", "A"));
    completedAnalogyPairs.put(24, new Pair<>("G", "C"));
    completedAnalogyPairs.put(25, new Pair<>("C", "E"));
    completedAnalogyPairs.put(26, new Pair<>("B", "D"));
    completedAnalogyPairs.put(27, new Pair<>("F", "A"));
    completedAnalogyPairs.put(28, new Pair<>("G", "E"));

    // Define parallel partial analogy pairs
    partialAnalogyPairs.put(1, new Pair<>("H", "?"));
    partialAnalogyPairs.put(2, new Pair<>("H", "?"));
    partialAnalogyPairs.put(3, new Pair<>("H", "?"));
    partialAnalogyPairs.put(4, new Pair<>("H", "?"));
    partialAnalogyPairs.put(5, new Pair<>("H", "?"));
    partialAnalogyPairs.put(6, new Pair<>("G", "?"));
    partialAnalogyPairs.put(7, new Pair<>("G", "?"));
    partialAnalogyPairs.put(8, new Pair<>("F", "?"));
    partialAnalogyPairs.put(9, new Pair<>("F", "?"));
    partialAnalogyPairs.put(10, new Pair<>("F", "?"));
    partialAnalogyPairs.put(11, new Pair<>("F", "?"));
    partialAnalogyPairs.put(12, new Pair<>("F", "?"));
    partialAnalogyPairs.put(13, new Pair<>("C", "?"));
    partialAnalogyPairs.put(14, new Pair<>("C", "?"));
    partialAnalogyPairs.put(15, new Pair<>("E", "?"));
    partialAnalogyPairs.put(16, new Pair<>("E", "?"));
    partialAnalogyPairs.put(17, new Pair<>("E", "?"));
    partialAnalogyPairs.put(18, new Pair<>("E", "?"));
    partialAnalogyPairs.put(19, new Pair<>("E", "?"));
    partialAnalogyPairs.put(20, new Pair<>("A", "?"));
    partialAnalogyPairs.put(21, new Pair<>("A", "?"));
    partialAnalogyPairs.put(22, new Pair<>("D", "?"));
    partialAnalogyPairs.put(23, new Pair<>("D", "?"));
    partialAnalogyPairs.put(24, new Pair<>("D", "?"));
    partialAnalogyPairs.put(25, new Pair<>("D", "?"));
    partialAnalogyPairs.put(26, new Pair<>("D", "?"));
    partialAnalogyPairs.put(27, new Pair<>("B", "?"));
    partialAnalogyPairs.put(28, new Pair<>("B", "?"));

    // Define completed parallel analogy pairs
    completedParallelAnalogyPairs.add(Arrays.asList(1, 3, 5));
    completedParallelAnalogyPairs.add(Arrays.asList(2, 4));
    completedParallelAnalogyPairs.add(Arrays.asList(6, 7));
    completedParallelAnalogyPairs.add(Arrays.asList(8, 10, 12));
    completedParallelAnalogyPairs.add(Arrays.asList(9, 11));
    completedParallelAnalogyPairs.add(Arrays.asList(13, 14));
    completedParallelAnalogyPairs.add(Arrays.asList(15, 17, 19));
    completedParallelAnalogyPairs.add(Arrays.asList(16, 18));
    completedParallelAnalogyPairs.add(Arrays.asList(20, 21));
    completedParallelAnalogyPairs.add(Arrays.asList(22, 24, 26));
    completedParallelAnalogyPairs.add(Arrays.asList(23, 25));
    completedParallelAnalogyPairs.add(Arrays.asList(27, 28));

    // Get final transforms
    Map.Entry<Integer, Pair<String, Double>> finalBinaryTransform = binaryTransformationInduction(completedAnalogyTriplets, images);
    Map.Entry<Integer, Pair<String, Double>> finalUnaryTransform = unaryTransformationInduction(completedAnalogyPairs, images);

    if(finalBinaryTransform.getValue().getElement1() > finalUnaryTransform.getValue().getElement1())
      return Integer.valueOf(
          binaryAnswerSelection(partialAnalogyTriplets, finalBinaryTransform, images, answerImages).getKey()
      );
    else
      return Integer.valueOf(
          unaryAnswerSelection(partialAnalogyPairs, finalUnaryTransform, images, answerImages).getKey()
      );
  }

  public Map.Entry<Integer, Pair<String, Double>> binaryTransformationInduction(
      Map<Integer, List<String>> completedAnalogyTriplets,
      Map<String, BufferedImage> images) {
    Map<Integer, Pair<String, Double>> tripletAnalogyTransforms = new HashMap<>();

    for (Map.Entry<Integer, List<String>> entry : completedAnalogyTriplets.entrySet()) {
      BufferedImage image1 = images.get(entry.getValue().get(0));
      BufferedImage image2 = images.get(entry.getValue().get(1));
      BufferedImage image3 = images.get(entry.getValue().get(2));
      int[][] pixelMatrix1 = getGrayscalePixelMatrix(image1);
      int[][] pixelMatrix2 = getGrayscalePixelMatrix(image2);
      int[][] pixelMatrix3 = getGrayscalePixelMatrix(image3);
      Map<String, Double> transformations = new HashMap<>();

      /* Binary transformations */
      // Union
      transformations.put("union", calculateTverskyRatio(pixelMatrix3, unionTransform(pixelMatrix1, pixelMatrix2)));
      // Intersection
      transformations.put("intersect", calculateTverskyRatio(pixelMatrix3, intersectTransform(pixelMatrix1, pixelMatrix2)));
      // Subtraction
      transformations.put("subtract", calculateTverskyRatio(pixelMatrix3, subtractTransform(pixelMatrix1, pixelMatrix2)));
      // Back-subtraction
      transformations.put("backSubtract", calculateTverskyRatio(pixelMatrix3, backSubtractTransform(pixelMatrix1, pixelMatrix2)));
      // Exclusive-or
      transformations.put("exclusiveOr", calculateTverskyRatio(pixelMatrix3, exclusiveOrTransform(pixelMatrix1, pixelMatrix2)));

      // Select the best fit transform by highest similarity
      Map.Entry<String, Double> bestTransform = null;
      for (Map.Entry<String, Double> transform : transformations.entrySet()) {
        if (bestTransform == null || bestTransform.getValue() < transform.getValue()) {
          bestTransform = transform;
        }
      }

      // Assign transform to analogy
      tripletAnalogyTransforms.put(entry.getKey(), new Pair<>(bestTransform.getKey(), bestTransform.getValue()));
    }

    // Select the best fit transform across triplet analogies
    Map.Entry<Integer, Pair<String, Double>> finalTransform = null;
    for (Map.Entry<Integer, Pair<String, Double>> transform : tripletAnalogyTransforms.entrySet()) {
      if (finalTransform == null || finalTransform.getValue().getElement1() < transform.getValue().getElement1()) {
        finalTransform = transform;
      }
    }

    return finalTransform;
  }

  public Map.Entry<Integer, Pair<String, Double>> unaryTransformationInduction(
      Map<Integer, Pair<String, String>> completedAnalogyPairs,
      Map<String, BufferedImage> images) {
    Map<Integer, Pair<String, Double>> pairAnalogyTransforms = new HashMap<>();

    for (Map.Entry<Integer, Pair<String, String>> entry : completedAnalogyPairs.entrySet()) {
      BufferedImage image1 = images.get(entry.getValue().getElement0());
      BufferedImage image2 = images.get(entry.getValue().getElement1());
      BufferedImage transformedImage;
      int[][] pixelMatrix1 = getGrayscalePixelMatrix(image1);
      int[][] pixelMatrix2 = getGrayscalePixelMatrix(image2);
      Map<String, Double> transformations = new HashMap<>();

      /* Unary transformations */
      // Identity
      transformations.put("identity", calculateTverskyRatio(pixelMatrix2, pixelMatrix1));
      // Rotate 90
      transformedImage = rotateImage(image1, Math.PI / 2);
      transformations.put("rotate90", calculateTverskyRatio(pixelMatrix2, getGrayscalePixelMatrix(transformedImage)));
      // Rotate 180
      transformedImage = rotateImage(image1, Math.PI);
      transformations.put("rotate180", calculateTverskyRatio(pixelMatrix2, getGrayscalePixelMatrix(transformedImage)));
      // Rotate 270
      transformedImage = rotateImage(image1, 3 * Math.PI / 2);
      transformations.put("rotate270", calculateTverskyRatio(pixelMatrix2, getGrayscalePixelMatrix(transformedImage)));
      // Identity flip
      transformedImage = verticalFlip(image1);
      transformations.put("identityFlip", calculateTverskyRatio(pixelMatrix2, getGrayscalePixelMatrix(transformedImage)));
      // Rotate 90 flip
      transformedImage = rotate90Flip(image1);
      transformations.put("rotate90Flip", calculateTverskyRatio(pixelMatrix2, getGrayscalePixelMatrix(transformedImage)));
      // Rotate 180 flip
      transformedImage = horizontalFlip(image1);
      transformations.put("rotate180Flip", calculateTverskyRatio(pixelMatrix2, getGrayscalePixelMatrix(transformedImage)));
      // Rotate 270 flip
      transformedImage = rotate270Flip(image1);
      transformations.put("rotate270Flip", calculateTverskyRatio(pixelMatrix2, getGrayscalePixelMatrix(transformedImage)));

      // Select the best fit transform by highest similarity
      Map.Entry<String, Double> bestTransform = null;
      for (Map.Entry<String, Double> transform : transformations.entrySet()) {
        if (bestTransform == null || bestTransform.getValue() < transform.getValue()) {
          bestTransform = transform;
        }
      }

      // Assign transform to analogy
      pairAnalogyTransforms.put(entry.getKey(), new Pair<>(bestTransform.getKey(), bestTransform.getValue()));
    }

    // Select the best fit transform across pair analogies
    Map.Entry<Integer, Pair<String, Double>> finalTransform = null;
    for (Map.Entry<Integer, Pair<String, Double>> transform : pairAnalogyTransforms.entrySet()) {
      if (finalTransform == null || finalTransform.getValue().getElement1() < transform.getValue().getElement1()) {
        finalTransform = transform;
      }
    }

    return finalTransform;
  }

  public Map.Entry<String, Double> binaryAnswerSelection(
      Map<Integer, List<String>> partialAnalogyTriplets,
      Map.Entry<Integer, Pair<String, Double>> finalBinaryTransform,
      Map<String, BufferedImage> images, 
      Map<String, BufferedImage> answerImages) {
    String transform = finalBinaryTransform.getValue().getElement0();
    List<String> analogySequence = partialAnalogyTriplets.get(finalBinaryTransform.getKey());
    BufferedImage image1 = images.get(analogySequence.get(0));
    BufferedImage image2 = images.get(analogySequence.get(0));
    int[][] pixelMatrix1 = getGrayscalePixelMatrix(image1);
    int[][] pixelMatrix2 = getGrayscalePixelMatrix(image2);
    int[][] candidateMatrix = new int[pixelMatrix1.length][pixelMatrix1.length];
    Map<String, Double> answers = new HashMap<>();

    if (transform.equals("union"))
      candidateMatrix = unionTransform(pixelMatrix1, pixelMatrix2);
    else if (transform.equals("intersect"))
      candidateMatrix = intersectTransform(pixelMatrix1, pixelMatrix2);
    else if (transform.equals("subtract"))
      candidateMatrix = subtractTransform(pixelMatrix1, pixelMatrix2);
    else if (transform.equals("backSubtract"))
      candidateMatrix = backSubtractTransform(pixelMatrix1, pixelMatrix2);
    else if (transform.equals("exclusiveOr"))
      candidateMatrix = exclusiveOrTransform(pixelMatrix1, pixelMatrix2);

    // Get similarity value with each answer against the binary transformation predicted answer
    for (Map.Entry<String, BufferedImage> entry : answerImages.entrySet()) {
      int[][] pixelMatrix3 = getGrayscalePixelMatrix(entry.getValue());

      answers.put(entry.getKey(), calculateTverskyRatio(candidateMatrix, pixelMatrix3));
    }

    // Select the answer with the highest similarity with candidate answer
    Map.Entry<String, Double> answer = null;
    for (Map.Entry<String, Double> entry : answers.entrySet()) {
      if (answer == null || answer.getValue() < entry.getValue()) {
        answer = entry;
      }
    }

    return answer;
  }

  public Map.Entry<String, Double> unaryAnswerSelection(
      Map<Integer, Pair<String, String>> partialAnalogyPairs,
      Map.Entry<Integer, Pair<String, Double>> finalUnaryTransform,
      Map<String, BufferedImage> images,
      Map<String, BufferedImage> answerImages) {
    String transform = finalUnaryTransform.getValue().getElement0();
    Pair<String, String> analogyPair = partialAnalogyPairs.get(finalUnaryTransform.getKey());
    BufferedImage image1 = images.get(analogyPair.getElement0());
    int[][] pixelMatrix1 = getGrayscalePixelMatrix(image1);
    int[][] candidateMatrix = new int[pixelMatrix1.length][pixelMatrix1.length];
    Map<String, Double> answers = new HashMap<>();

    if (transform.equals("identity"))
      candidateMatrix = pixelMatrix1;
    else if (transform.equals("rotate90"))
      candidateMatrix = getGrayscalePixelMatrix(rotateImage(image1, Math.PI / 2));
    else if (transform.equals("rotate180"))
      candidateMatrix = getGrayscalePixelMatrix(rotateImage(image1, Math.PI));
    else if (transform.equals("rotate270"))
      candidateMatrix = getGrayscalePixelMatrix(rotateImage(image1, 3 * Math.PI / 2));
    else if (transform.equals("identityFlip"))
      candidateMatrix = getGrayscalePixelMatrix(verticalFlip(image1));
    else if (transform.equals("rotate90Flip"))
      candidateMatrix = getGrayscalePixelMatrix(rotate90Flip(image1));
    else if (transform.equals("rotate180Flip"))
      candidateMatrix = getGrayscalePixelMatrix(horizontalFlip(image1));
    else if (transform.equals("rotate270Flip"))
      candidateMatrix = getGrayscalePixelMatrix(rotate270Flip(image1));

    // Get similarity value with each answer against the binary transformation predicted answer
    for (Map.Entry<String, BufferedImage> entry : answerImages.entrySet()) {
      int[][] pixelMatrix3 = getGrayscalePixelMatrix(entry.getValue());

      answers.put(entry.getKey(), calculateTverskyRatio(candidateMatrix, pixelMatrix3));
    }

    // Select the answer with the highest similarity with candidate answer
    Map.Entry<String, Double> answer = null;
    for (Map.Entry<String, Double> entry : answers.entrySet()) {
      if (answer == null || answer.getValue() < entry.getValue()) {
        answer = entry;
      }
    }

    return answer;
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

    /*
     * Transformations are stacked then applied so the last transformation is
     * the first to happen
     */
    AffineTransform tx = new AffineTransform();

    tx.translate(image.getHeight() / 2, image.getWidth() / 2);
    tx.rotate(theta);
    tx.translate(-image.getWidth() / 2, -image.getHeight() / 2);

    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
    op.filter(image, result);

    return result;
  }

  /**
   * This method rotates an image 90 degrees then reflects the image over the x
   * axis.
   *
   * @param image
   * @return
   */
  public BufferedImage rotate90Flip(BufferedImage image) {
    BufferedImage result = null;

    // Rotate 90 degrees
    result = rotateImage(image, Math.PI / 2);
    // Reflect image over x axis
    result = verticalFlip(result);

    return result;
  }

  /**
   * This method rotates an image 270 degrees then reflects the image over the x
   * axis.
   *
   * @param image
   * @return
   */
  public BufferedImage rotate270Flip(BufferedImage image) {
    BufferedImage result = null;

    // Rotate 270 degrees
    result = rotateImage(image, 3 * Math.PI / 2);
    // Reflect image over x axis
    result = verticalFlip(result);

    return result;
  }

  public int[][] unionTransform(int[][] matrix1, int[][] matrix2) {
    int[][] resultPixelArray = new int[matrix1.length][matrix1.length];

    for (int i = 0; i < matrix1.length; i++) {
      for (int j = 0; j < matrix1[i].length; j++) {
        resultPixelArray[i][j] = Math.max(matrix1[i][j], matrix2[i][j]);
      }
    }

    return resultPixelArray;
  }

  public int[][] intersectTransform(int[][] matrix1, int[][] matrix2) {
    int[][] resultPixelArray = new int[matrix1.length][matrix1.length];

    for (int i = 0; i < matrix1.length; i++) {
      for (int j = 0; j < matrix1[i].length; j++) {
        resultPixelArray[i][j] = Math.min(matrix1[i][j], matrix2[i][j]);
      }
    }

    return resultPixelArray;
  }

  public int[][] subtractTransform(int[][] matrix1, int[][] matrix2) {
    int[][] resultPixelArray = new int[matrix1.length][matrix1.length];

    for (int i = 0; i < matrix1.length; i++) {
      for (int j = 0; j < matrix1[i].length; j++) {
        resultPixelArray[i][j] = matrix1[i][j] - matrix2[i][j];
      }
    }

    return resultPixelArray;
  }

  public int[][] backSubtractTransform(int[][] matrix1, int[][] matrix2) {
    int[][] resultPixelArray = new int[matrix1.length][matrix1.length];

    for (int i = 0; i < matrix2.length; i++) {
      for (int j = 0; j < matrix2[i].length; j++) {
        resultPixelArray[i][j] = matrix2[i][j] - matrix1[i][j];
      }
    }

    return resultPixelArray;
  }

  public int[][] exclusiveOrTransform(int[][] matrix1, int[][] matrix2) {
    int[][] resultPixelArray = new int[matrix1.length][matrix1.length];

    for (int i = 0; i < matrix1.length; i++) {
      for (int j = 0; j < matrix1[i].length; j++) {
        resultPixelArray[i][j] = Math.max(matrix1[i][j], matrix2[i][j]) - Math.min(matrix1[i][j], matrix2[i][j]);
      }
    }

    return resultPixelArray;
  }

  public double calculateTverskyRatio(int[][] matrix1, int[][] matrix2) {
    double alpha = 2.0;
    double beta = 1.0;
    double intersection = intersectMatrices(matrix1, matrix2);
    double difference = differenceMatrices(matrix1, matrix2);
    double difference2 = differenceMatrices(matrix2, matrix1);

    return intersection / (intersection + alpha * difference + beta * difference2);
  }

  public double calculateTverskyRatio2(int[][] matrix1, int[][] matrix2) {
    return (double) intersectMatrices(matrix1, matrix2) / unionMatrices(matrix1, matrix2);
  }

  public int unionMatrices(int[][] matrix1, int[][] matrix2) {
    int count = 0;

    for (int i = 0; i < matrix1.length; i++) {
      for (int j = 0; j < matrix1[i].length; j++) {
        if (matrix1[i][j] != 255 || matrix2[i][j] != 255)
          count++;
      }
    }

    return count;
  }

  public int intersectMatrices(int[][] matrix1, int[][] matrix2) {
    int count = 0;

    for (int i = 0; i < matrix1.length; i++) {
      for (int j = 0; j < matrix1[i].length; j++) {
        if (matrix1[i][j] != 255 && matrix1[i][j] == matrix2[i][j])
          count++;
      }
    }

    return count;
  }

  public int differenceMatrices(int[][] matrix1, int[][] matrix2) {
    int count = 0;

    for (int i = 0; i < matrix1.length; i++) {
      for (int j = 0; j < matrix1[i].length; j++) {
        if (matrix1[i][j] != 255 && matrix1[i][j] != matrix2[i][j])
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
    } catch (IOException e) {
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
    Graphics graphics = result.getGraphics();

    graphics.drawImage(image, 0, 0, null);
    graphics.dispose();

    return result;
  }

  /**
   * Returns a matrix of pixel values, where 255 equals white and 0 equals black.
   *
   * Original code by blackSmith:
   * http://stackoverflow.com/questions/17278829/grayscale-bitmap-into-2d-array
   */
  public int[][] getGrayscalePixelMatrix(BufferedImage image) {
    int width = image.getWidth();
    int height = image.getHeight();
    int[][] matrix = new int[width][height];

    Raster raster = image.getData();
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        matrix[i][j] = raster.getSample(i, j, 0);
      }
    }

    return matrix;
  }
}
