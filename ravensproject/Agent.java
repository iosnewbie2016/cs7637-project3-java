package ravensproject;

// Uncomment these lines to access image processing.

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.*;
import java.util.List;

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

  public RavensProblem theProblem;

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
    theProblem = problem;

    if (problem.getProblemType().equals("3x3"))
      return initialization(problem);
    else
      return -1;
  }

  public int initialization(RavensProblem problem) {
    System.out.println(problem.getName());
    Map<String, BufferedImage> images = new HashMap<>();
    Map<String, BufferedImage> answerImages = new HashMap<>();

    Map<Integer, List<String>> completedAnalogyTriplets = new TreeMap<>();
    Map<Integer, List<String>> partialAnalogyTriplets = new TreeMap<>();
    List<List<Integer>> completedParallelAnalogyTriplets = new ArrayList<>();

    Map<Integer, Pair<String, String>> completedAnalogyPairs = new TreeMap<>();
    Map<Integer, Pair<String, String>> partialAnalogyPairs = new TreeMap<>();
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

    // Define completed analogy triplets

    // Rows
    completedAnalogyTriplets.put(1, Arrays.asList("A", "B", "C"));
    completedAnalogyTriplets.put(2, Arrays.asList("D", "E", "F"));
    // Columns
    completedAnalogyTriplets.put(3, Arrays.asList("A", "D", "G"));
    completedAnalogyTriplets.put(4, Arrays.asList("B", "E", "H"));
    // Diagonals
    completedAnalogyTriplets.put(5, Arrays.asList("F", "G", "B"));
    completedAnalogyTriplets.put(6, Arrays.asList("H", "C", "D"));
    completedAnalogyTriplets.put(7, Arrays.asList("F", "H", "A"));
    completedAnalogyTriplets.put(8, Arrays.asList("G", "C", "E"));

    // Define parallel partial analogies

    // Rows
    partialAnalogyTriplets.put(1, Arrays.asList("G", "H", "?"));
    partialAnalogyTriplets.put(2, Arrays.asList("G", "H", "?"));
    // Columns
    partialAnalogyTriplets.put(3, Arrays.asList("C", "F", "?"));
    partialAnalogyTriplets.put(4, Arrays.asList("C", "F", "?"));
    // Diagonals
    partialAnalogyTriplets.put(5, Arrays.asList("A", "E", "?"));
    partialAnalogyTriplets.put(6, Arrays.asList("A", "E", "?"));
    partialAnalogyTriplets.put(7, Arrays.asList("B", "D", "?"));
    partialAnalogyTriplets.put(8, Arrays.asList("B", "D", "?"));

    // Define completed parallel analogies

    // Rows
    completedParallelAnalogyTriplets.add(Arrays.asList(1, 2));
    // Columns
    completedParallelAnalogyTriplets.add(Arrays.asList(3, 4));
    // Diagonals
    completedParallelAnalogyTriplets.add(Arrays.asList(5, 6));
    completedParallelAnalogyTriplets.add(Arrays.asList(7, 8));

    // Define completed analogy pairs

    // Rows
    completedAnalogyPairs.put(1, new Pair<>("A", "B"));
    completedAnalogyPairs.put(2, new Pair<>("B", "C"));
    completedAnalogyPairs.put(3, new Pair<>("D", "E"));
    completedAnalogyPairs.put(4, new Pair<>("E", "F"));
    completedAnalogyPairs.put(5, new Pair<>("G", "H"));
    completedAnalogyPairs.put(6, new Pair<>("A", "C"));
    completedAnalogyPairs.put(7, new Pair<>("D", "F"));
    // Columns
    completedAnalogyPairs.put(8, new Pair<>("A", "D"));
    completedAnalogyPairs.put(9, new Pair<>("D", "G"));
    completedAnalogyPairs.put(10, new Pair<>("B", "E"));
    completedAnalogyPairs.put(11, new Pair<>("E", "H"));
    completedAnalogyPairs.put(12, new Pair<>("C", "F"));
    completedAnalogyPairs.put(13, new Pair<>("A", "G"));
    completedAnalogyPairs.put(14, new Pair<>("B", "H"));
    // Diagonals
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

    // Rows
    partialAnalogyPairs.put(1, new Pair<>("H", "?"));
    partialAnalogyPairs.put(2, new Pair<>("H", "?"));
    partialAnalogyPairs.put(3, new Pair<>("H", "?"));
    partialAnalogyPairs.put(4, new Pair<>("H", "?"));
    partialAnalogyPairs.put(5, new Pair<>("H", "?"));
    partialAnalogyPairs.put(6, new Pair<>("G", "?"));
    partialAnalogyPairs.put(7, new Pair<>("G", "?"));
    // Columns
    partialAnalogyPairs.put(8, new Pair<>("F", "?"));
    partialAnalogyPairs.put(9, new Pair<>("F", "?"));
    partialAnalogyPairs.put(10, new Pair<>("F", "?"));
    partialAnalogyPairs.put(11, new Pair<>("F", "?"));
    partialAnalogyPairs.put(12, new Pair<>("F", "?"));
    partialAnalogyPairs.put(13, new Pair<>("C", "?"));
    partialAnalogyPairs.put(14, new Pair<>("C", "?"));
    // Diagonals
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

    // Rows
    completedParallelAnalogyPairs.add(Arrays.asList(1, 3, 5));
    completedParallelAnalogyPairs.add(Arrays.asList(2, 4));
    completedParallelAnalogyPairs.add(Arrays.asList(6, 7));
    // Columns
    completedParallelAnalogyPairs.add(Arrays.asList(8, 10, 12));
    completedParallelAnalogyPairs.add(Arrays.asList(9, 11));
    completedParallelAnalogyPairs.add(Arrays.asList(13, 14));
    // Diagonals
    completedParallelAnalogyPairs.add(Arrays.asList(15, 17, 19));
    completedParallelAnalogyPairs.add(Arrays.asList(16, 18));
    completedParallelAnalogyPairs.add(Arrays.asList(20, 21));
    completedParallelAnalogyPairs.add(Arrays.asList(22, 24, 26));
    completedParallelAnalogyPairs.add(Arrays.asList(23, 25));
    completedParallelAnalogyPairs.add(Arrays.asList(27, 28));

    // Get final transforms
    SimiltudeTransform finalBinaryTransform = binaryTransformationInduction(completedAnalogyTriplets, images);
    SimiltudeTransform finalUnaryTransform = unaryTransformationInduction(completedAnalogyPairs, images);
    SimiltudeTransform finalUnaryTransform2 = unaryTransformationInduction2(completedAnalogyPairs, images);

    int answer = -1;
//    if(finalBinaryTransform.getSimilarityRatio() > finalUnaryTransform.getSimilarityRatio())
//      answer = binaryAnswerSelection(partialAnalogyTriplets, finalBinaryTransform, images, answerImages);
//    else
//      answer = unaryAnswerSelection(partialAnalogyPairs, finalUnaryTransform, images, answerImages);

    answer = unaryAnswerSelection2(partialAnalogyPairs, finalUnaryTransform2, images, answerImages);

    System.out.println(answer);

    return answer;
  }

  public SimiltudeTransform binaryTransformationInduction(
      Map<Integer, List<String>> completedAnalogyTriplets,
      Map<String, BufferedImage> images) {
    Map<Integer, Tuple<String, String, Double>> bestAnalogyTransforms = new TreeMap<>();
    Map<Integer, Map<String, Double>> analogyTransforms = new TreeMap<>();

    for (Map.Entry<Integer, List<String>> entry : completedAnalogyTriplets.entrySet()) {
      BufferedImage image1 = images.get(entry.getValue().get(0));
      BufferedImage image2 = images.get(entry.getValue().get(1));
      BufferedImage image3 = images.get(entry.getValue().get(2));
      int[][] pixelMatrix1 = getGrayscalePixelMatrix(image1);
      int[][] pixelMatrix2 = getGrayscalePixelMatrix(image2);
      int[][] pixelMatrix3 = getGrayscalePixelMatrix(image3);
      // Tuple is transform name, composition operand, similarity ratio
      List<Tuple<String, String, Double>> transformations = new ArrayList<>();
      String compositionOperand;
      int[][] transformMatrix;
      int[][] compositionResult;

      /* Binary transformations */
      // Union
      transformMatrix = unionTransform(pixelMatrix1, pixelMatrix2);
      // Find composition operand
      compositionOperand = findImageComposition(transformMatrix, pixelMatrix3);
      compositionResult = performImageComposition(transformMatrix, pixelMatrix3, compositionOperand);
      transformations.add(new Tuple<>(
          "union",
          compositionOperand,
          calculateTverskyRatio(pixelMatrix3, compositionResult, 2, 1)
      ));
      // Intersection
      transformMatrix = intersectTransform(pixelMatrix1, pixelMatrix2);
      // Find composition operand
      compositionOperand = findImageComposition(transformMatrix, pixelMatrix3);
      compositionResult = performImageComposition(transformMatrix, pixelMatrix3, compositionOperand);
      transformations.add(new Tuple<>(
          "intersect",
          compositionOperand,
          calculateTverskyRatio(pixelMatrix3, compositionResult, 2, 1)
      ));

      // Subtraction
      transformMatrix = subtractTransform(pixelMatrix1, pixelMatrix2);
      // Find composition operand
      compositionOperand = findImageComposition(transformMatrix, pixelMatrix3);
      compositionResult = performImageComposition(transformMatrix, pixelMatrix3, compositionOperand);
      transformations.add(new Tuple<>(
          "subtract",
          compositionOperand,
          calculateTverskyRatio(pixelMatrix3, compositionResult, 2, 1)
      ));
      // Back-subtraction
      transformMatrix = backSubtractTransform(pixelMatrix1, pixelMatrix2);
      // Find composition operand
      compositionOperand = findImageComposition(transformMatrix, pixelMatrix3);
      compositionResult = performImageComposition(transformMatrix, pixelMatrix3, compositionOperand);
      transformations.add(new Tuple<>(
          "backSubtract",
          compositionOperand,
          calculateTverskyRatio(pixelMatrix3, compositionResult, 2, 1)
      ));
      // Exclusive-or
      transformMatrix = exclusiveOrTransform(pixelMatrix1, pixelMatrix2);
      // Find composition operand
      compositionOperand = findImageComposition(transformMatrix, pixelMatrix3);
      compositionResult = performImageComposition(transformMatrix, pixelMatrix3, compositionOperand);
      transformations.add(new Tuple<>(
          "exclusiveOr",
          compositionOperand,
          calculateTverskyRatio(pixelMatrix3, compositionResult, 2, 1)
      ));

      // Select the best fit transform by highest similarity
      Tuple<String, String, Double> bestTransform = null;
      for (Tuple<String, String, Double> transform : transformations) {
        if (bestTransform == null || bestTransform.getElement2() < transform.getElement2()) {
          bestTransform = transform;
        }
      }

      // Assign best transform to analogy
      bestAnalogyTransforms.put(
          entry.getKey(),
          new Tuple<>(
              bestTransform.getElement0(),
              bestTransform.getElement1(),
              bestTransform.getElement2()
          )
      );
      // Store other transform values for analogy
      Map<String, Double> transformsMap = new TreeMap<>();
      for (Tuple<String, String, Double> tuple : transformations) {
        transformsMap.put(tuple.getElement0(), tuple.getElement2());
      }
      analogyTransforms.put(entry.getKey(), transformsMap);
    }

    // Choose the best candidate transform
    Map.Entry<Integer, Tuple<String, String, Double>> finalTransform = null;
    for (Map.Entry<Integer, Tuple<String, String, Double>> transform : bestAnalogyTransforms.entrySet()) {
      if (finalTransform == null || finalTransform.getValue().getElement2() < transform.getValue().getElement2()) {
        finalTransform = transform;
      }
    }


    return new SimiltudeTransform(
        finalTransform.getKey(),
        finalTransform.getValue().getElement0(),
        new Pair<>(0, 0),
        finalTransform.getValue().getElement1(),
        finalTransform.getValue().getElement2()
    );
  }

  public SimiltudeTransform unaryTransformationInduction(
      Map<Integer, Pair<String, String>> completedAnalogyPairs,
      Map<String, BufferedImage> images) {
    Map<Integer, Tuple<String, String, Double>> bestAnalogyTransforms = new TreeMap<>();

    for (Map.Entry<Integer, Pair<String, String>> entry : completedAnalogyPairs.entrySet()) {
      BufferedImage image1 = images.get(entry.getValue().getElement0());
      BufferedImage image2 = images.get(entry.getValue().getElement1());
      BufferedImage transformedImage;
      int[][] pixelMatrix1 = getGrayscalePixelMatrix(image1);
      int[][] pixelMatrix2 = getGrayscalePixelMatrix(image2);
      // Tuple is transform name, composition operand, similarity ratio
      List<Tuple<String, String, Double>> transformations = new ArrayList<>();
      String compositionOperand;
      int[][] transformMatrix;
      int[][] compositionResult;

      /* Unary transformations */
      // Identity
      compositionOperand = findImageComposition(pixelMatrix1, pixelMatrix2);
      compositionResult = performImageComposition(pixelMatrix1, pixelMatrix2, compositionOperand);
      transformations.add(new Tuple<>(
          "identity",
          compositionOperand,
          calculateTverskyRatio2(pixelMatrix2, compositionResult)
      ));

      // Rotate 90
      transformedImage = rotateImage(image1, Math.PI / 2);
      // Find composition operand
      transformMatrix = getGrayscalePixelMatrix(transformedImage);
      compositionOperand = findImageComposition(transformMatrix, pixelMatrix2);
      compositionResult = performImageComposition(transformMatrix, pixelMatrix2, compositionOperand);
      transformations.add(new Tuple<>(
          "rotate90",
          compositionOperand,
          calculateTverskyRatio2(pixelMatrix2, compositionResult)
      ));

      // Rotate 180
      transformedImage = rotateImage(image1, Math.PI);
      // Find composition operand
      transformMatrix = getGrayscalePixelMatrix(transformedImage);
      compositionOperand = findImageComposition(transformMatrix, pixelMatrix2);
      compositionResult = performImageComposition(transformMatrix, pixelMatrix2, compositionOperand);
      transformations.add(new Tuple<>(
          "rotate180",
          compositionOperand,
          calculateTverskyRatio2(pixelMatrix2, compositionResult)
      ));

      // Rotate 270
      transformedImage = rotateImage(image1, 3 * Math.PI / 2);
      // Find composition operand
      transformMatrix = getGrayscalePixelMatrix(transformedImage);
      compositionOperand = findImageComposition(transformMatrix, pixelMatrix2);
      compositionResult = performImageComposition(transformMatrix, pixelMatrix2, compositionOperand);
      transformations.add(new Tuple<>(
          "rotate270",
          compositionOperand,
          calculateTverskyRatio2(pixelMatrix2, compositionResult)
      ));

      // Identity flip
      transformedImage = verticalFlip(image1);
      // Find composition operand
      transformMatrix = getGrayscalePixelMatrix(transformedImage);
      compositionOperand = findImageComposition(transformMatrix, pixelMatrix2);
      compositionResult = performImageComposition(transformMatrix, pixelMatrix2, compositionOperand);
      transformations.add(new Tuple<>(
          "identityFlip",
          compositionOperand,
          calculateTverskyRatio2(pixelMatrix2, compositionResult)
      ));

      // Rotate 90 flip
      transformedImage = rotate90Flip(image1);
      // Find composition operand
      transformMatrix = getGrayscalePixelMatrix(transformedImage);
      compositionOperand = findImageComposition(transformMatrix, pixelMatrix2);
      compositionResult = performImageComposition(transformMatrix, pixelMatrix2, compositionOperand);
      transformations.add(new Tuple<>(
          "rotate90Flip",
          compositionOperand,
          calculateTverskyRatio2(pixelMatrix2, compositionResult)
      ));

      // Rotate 180 flip
      transformedImage = horizontalFlip(image1);
      // Find composition operand
      transformMatrix = getGrayscalePixelMatrix(transformedImage);
      compositionOperand = findImageComposition(transformMatrix, pixelMatrix2);
      compositionResult = performImageComposition(transformMatrix, pixelMatrix2, compositionOperand);
      transformations.add(new Tuple<>(
          "rotate180Flip",
          compositionOperand,
          calculateTverskyRatio2(pixelMatrix2, compositionResult)
      ));

      // Rotate 270 flip
      transformedImage = rotate270Flip(image1);
      // Find composition operand
      transformMatrix = getGrayscalePixelMatrix(transformedImage);
      compositionOperand = findImageComposition(transformMatrix, pixelMatrix2);
      compositionResult = performImageComposition(transformMatrix, pixelMatrix2, compositionOperand);
      transformations.add(new Tuple<>(
          "rotate270Flip",
          compositionOperand,
          calculateTverskyRatio2(pixelMatrix2, compositionResult)
      ));

      // Select the best fit transform by highest similarity
      Tuple<String, String, Double> bestTransform = null;
      for (Tuple<String, String, Double> transform : transformations) {
        if (bestTransform == null || bestTransform.getElement2() < transform.getElement2()) {
          bestTransform = transform;
        }
      }

      // Assign best transform to analogy
      bestAnalogyTransforms.put(
          entry.getKey(),
          new Tuple<>(
              bestTransform.getElement0(),
              bestTransform.getElement1(),
              bestTransform.getElement2()
          )
      );
    }

    // Choose the best candidate transform
    Map.Entry<Integer, Tuple<String, String, Double>> finalTransform = null;
    for (Map.Entry<Integer, Tuple<String, String, Double>> transform : bestAnalogyTransforms.entrySet()) {
      if (finalTransform == null || finalTransform.getValue().getElement2() < transform.getValue().getElement2()) {
        finalTransform = transform;
      }
    }

    return new SimiltudeTransform(
        finalTransform.getKey(),
        finalTransform.getValue().getElement0(),
        new Pair<>(0, 0),
        finalTransform.getValue().getElement1(),
        finalTransform.getValue().getElement2()
    );
  }

  public SimiltudeTransform unaryTransformationInduction2(
      Map<Integer, Pair<String, String>> completedAnalogyPairs,
      Map<String, BufferedImage> images) {
    Map<Integer, Pair<String, Double>> bestAnalogyTransforms = new HashMap<>();

    for (Map.Entry<Integer, Pair<String, String>> entry : completedAnalogyPairs.entrySet()) {
      BufferedImage image1 = images.get(entry.getValue().getElement0());
      BufferedImage image2 = images.get(entry.getValue().getElement1());
      BufferedImage transformedImage;
      int[][] pixelMatrix1 = getGrayscalePixelMatrix(image1);
      int[][] pixelMatrix2 = getGrayscalePixelMatrix(image2);
      List<Pair<String, Double>> transformations = new ArrayList<>();

      /* Unary transformations */
      // Identity
      // Find best translation for transform
      transformations.add(new Pair<>(
          "identity",
          calculateTverskyRatio(pixelMatrix2, pixelMatrix1, 2, 1)
      ));

      // Rotate 90
      transformedImage = rotateImage(image1, Math.PI / 2);
      transformations.add(new Pair<>(
          "rotate90",
          calculateTverskyRatio(pixelMatrix2, getGrayscalePixelMatrix(transformedImage), 2, 1)
      ));

      // Rotate 180
      transformedImage = rotateImage(image1, Math.PI);
      transformations.add(new Pair<>(
          "rotate180",
          calculateTverskyRatio(pixelMatrix2, getGrayscalePixelMatrix(transformedImage), 2, 1)
      ));

      // Rotate 270
      transformedImage = rotateImage(image1, 3 * Math.PI / 2);
      transformations.add(new Pair<>(
          "rotate270",
          calculateTverskyRatio(pixelMatrix2, getGrayscalePixelMatrix(transformedImage), 2, 1)
      ));

      // Identity flip
      transformedImage = verticalFlip(image1);
      transformations.add(new Pair<>(
          "identityFlip",
          calculateTverskyRatio(pixelMatrix2, getGrayscalePixelMatrix(transformedImage), 2, 1)
      ));

      // Rotate 90 flip
      transformedImage = rotate90Flip(image1);
      transformations.add(new Pair<>(
          "rotate90Flip",
          calculateTverskyRatio(pixelMatrix2, getGrayscalePixelMatrix(transformedImage), 2, 1)
      ));

      // Rotate 180 flip
      transformedImage = horizontalFlip(image1);
      transformations.add(new Pair<>(
          "rotate180Flip",
          calculateTverskyRatio(pixelMatrix2, getGrayscalePixelMatrix(transformedImage), 2, 1)
      ));
      // Rotate 270 flip
      transformedImage = rotate270Flip(image1);
      transformations.add(new Pair<>(
          "rotate270Flip",
          calculateTverskyRatio(pixelMatrix2, getGrayscalePixelMatrix(transformedImage), 2, 1)
      ));

      // Select the best fit transform by highest similarity
      Pair<String, Double> bestTransform = null;
      for (Pair<String, Double> transform : transformations) {
        if (bestTransform == null || bestTransform.getElement1() < transform.getElement1()) {
          bestTransform = transform;
        }
      }

      // Assign best transform to analogy
      bestAnalogyTransforms.put(
          entry.getKey(),
          new Pair<>(
              bestTransform.getElement0(),
              bestTransform.getElement1()
          )
      );
    }

    // Choose the best candidate transform
    Map.Entry<Integer, Pair<String, Double>> finalTransform = null;
    for (Map.Entry<Integer, Pair<String, Double>> transform : bestAnalogyTransforms.entrySet()) {
      if (finalTransform == null || finalTransform.getValue().getElement1() < transform.getValue().getElement1()) {
        finalTransform = transform;
      }
    }

    // Find image composition operand
    int[][] pixelMatrix1 = getGrayscalePixelMatrix(images.get(completedAnalogyPairs.get(finalTransform.getKey()).getElement0()));
    int[][] pixelMatrix2 = getGrayscalePixelMatrix(images.get(completedAnalogyPairs.get(finalTransform.getKey()).getElement1()));
    String compositionOperand = findImageComposition(pixelMatrix1, pixelMatrix2);

    // Perform composition
    int[][] compositionResult = new int[pixelMatrix1.length][pixelMatrix1.length];
    if(compositionOperand.equals("addition"))
      compositionResult = differenceMatrices2(pixelMatrix2, pixelMatrix1);
    else if(compositionOperand.equals("subtraction"))
      compositionResult = differenceMatrices2(pixelMatrix1, pixelMatrix2);

    SimiltudeTransform similtudeTransform = new SimiltudeTransform(
        finalTransform.getKey(),
        finalTransform.getValue().getElement0(),
        new Pair<>(0, 0),
        compositionOperand,
        finalTransform.getValue().getElement1()
    );
    similtudeTransform.setCompositionResult(compositionResult);

    return similtudeTransform;
  }


  public int binaryAnswerSelection(
      Map<Integer, List<String>> partialAnalogyTriplets,
      SimiltudeTransform finalBinaryTransform,
      Map<String, BufferedImage> images, 
      Map<String, BufferedImage> answerImages) {
    String transform = finalBinaryTransform.getTransform();
    List<String> analogySequence = partialAnalogyTriplets.get(finalBinaryTransform.getAnalogyKey());
    BufferedImage image1 = images.get(analogySequence.get(0));
    BufferedImage image2 = images.get(analogySequence.get(1));
    int[][] pixelMatrix1 = getGrayscalePixelMatrix(image1);
    int[][] pixelMatrix2 = getGrayscalePixelMatrix(image2);
    int[][] candidateMatrix = new int[pixelMatrix1.length][pixelMatrix1.length];
    Map<String, Pair<Double, int[][]>> answers = new HashMap<>();

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
      int[][] answerMatrix = getGrayscalePixelMatrix(entry.getValue());

      // Perform composition
      int[][] compositionResult = performImageComposition(candidateMatrix, answerMatrix, finalBinaryTransform.getCompositionOperand());

      answers.put(entry.getKey(), new Pair<>(calculateTverskyRatio(compositionResult, answerMatrix, 2, 1), compositionResult));
    }

    // Select the answer with the highest similarity with candidate answer
    Map.Entry<String, Pair<Double, int[][]>> answer = null;
    for (Map.Entry<String, Pair<Double, int[][]>> entry : answers.entrySet()) {
      if (answer == null || answer.getValue().getElement0() < entry.getValue().getElement0()) {
        answer = entry;
      }
    }

    // TODO remove after testing
    // Save candidate image
    saveImage(getImage(convertGrayscalePixelMatrix(answer.getValue().getElement1()), images.get("A").getSampleModel()), theProblem.getName() + "-candidateAnswer");

    return Integer.valueOf(answer.getKey());
  }

  public int unaryAnswerSelection(
      Map<Integer, Pair<String, String>> partialAnalogyPairs,
      SimiltudeTransform finalUnaryTransform,
      Map<String, BufferedImage> images,
      Map<String, BufferedImage> answerImages) {
    String transform = finalUnaryTransform.getTransform();
    Pair<String, String> analogyPair = partialAnalogyPairs.get(finalUnaryTransform.getAnalogyKey());
    BufferedImage image1 = images.get(analogyPair.getElement0());
    BufferedImage candidateImage = image1;
    int[][] candidateMatrix;
    Map<String, Pair<Double, int[][]>> answers = new HashMap<>();

    if (transform.equals("identity"))
      candidateImage = image1;
    else if (transform.equals("rotate90"))
      candidateImage = rotateImage(image1, Math.PI / 2);
    else if (transform.equals("rotate180"))
      candidateImage = rotateImage(image1, Math.PI);
    else if (transform.equals("rotate270"))
      candidateImage = rotateImage(image1, 3 * Math.PI / 2);
    else if (transform.equals("identityFlip"))
      candidateImage = verticalFlip(image1);
    else if (transform.equals("rotate90Flip"))
      candidateImage = rotate90Flip(image1);
    else if (transform.equals("rotate180Flip"))
      candidateImage = horizontalFlip(image1);
    else if (transform.equals("rotate270Flip"))
      candidateImage = rotate270Flip(image1);

    // Get pixel matrix for candidate image
    candidateMatrix = getGrayscalePixelMatrix(candidateImage);

    // Get similarity value with each answer against the unary transformation predicted answer
    for (Map.Entry<String, BufferedImage> entry : answerImages.entrySet()) {
      int[][] pixelMatrix3 = getGrayscalePixelMatrix(entry.getValue());

      // Perform composition
      int[][] compositionResult = performImageComposition(candidateMatrix, pixelMatrix3, finalUnaryTransform.getCompositionOperand());

      answers.put(
          entry.getKey(),
          new Pair<>(calculateTverskyRatio2(compositionResult, pixelMatrix3), compositionResult)
      );
    }

    // Select the answer with the highest similarity with candidate answer
    Map.Entry<String, Pair<Double, int[][]>> answer = null;
    for (Map.Entry<String, Pair<Double, int[][]>> entry : answers.entrySet()) {
      if (answer == null || answer.getValue().getElement0() < entry.getValue().getElement0()) {
        answer = entry;
      }
    }

    // TODO remove after testing
    // Save candidate image
    saveImage(getImage(convertGrayscalePixelMatrix(answer.getValue().getElement1()), images.get("A").getSampleModel()), theProblem.getName() + "-candidateAnswer");

    return Integer.valueOf(answer.getKey());
  }

  public int unaryAnswerSelection2(
      Map<Integer, Pair<String, String>> partialAnalogyPairs,
      SimiltudeTransform finalUnaryTransform,
      Map<String, BufferedImage> images,
      Map<String, BufferedImage> answerImages) {
    String transform = finalUnaryTransform.getTransform();
    Pair<String, String> analogyPair = partialAnalogyPairs.get(finalUnaryTransform.getAnalogyKey());
    BufferedImage image1 = images.get(analogyPair.getElement0());
    BufferedImage candidateImage = image1;
    int[][] candidateMatrix;
    Map<String, Double> answers = new HashMap<>();

    if (transform.equals("identity"))
      candidateImage = image1;
    else if (transform.equals("rotate90"))
      candidateImage = rotateImage(image1, Math.PI / 2);
    else if (transform.equals("rotate180"))
      candidateImage = rotateImage(image1, Math.PI);
    else if (transform.equals("rotate270"))
      candidateImage = rotateImage(image1, 3 * Math.PI / 2);
    else if (transform.equals("identityFlip"))
      candidateImage = verticalFlip(image1);
    else if (transform.equals("rotate90Flip"))
      candidateImage = rotate90Flip(image1);
    else if (transform.equals("rotate180Flip"))
      candidateImage = horizontalFlip(image1);
    else if (transform.equals("rotate270Flip"))
      candidateImage = rotate270Flip(image1);

    // Apply translation transform to candidate image
    candidateImage = translateTransform(
        candidateImage,
        finalUnaryTransform.getTranslation().getElement0(),
        finalUnaryTransform.getTranslation().getElement1()
    );

    // Get pixel matrix for candidate image
    candidateMatrix = getGrayscalePixelMatrix(candidateImage);

    // Apply image composition to candidate image
    if(finalUnaryTransform.getCompositionOperand().equals("addition"))
      candidateMatrix = imageAddition(candidateMatrix, finalUnaryTransform.getCompositionResult());
    else if(finalUnaryTransform.getCompositionOperand().equals("subtraction"))
      candidateMatrix = imageSubtraction(candidateMatrix, finalUnaryTransform.getCompositionResult());

    // Get similarity value with each answer against the binary transformation predicted answer
    for (Map.Entry<String, BufferedImage> entry : answerImages.entrySet()) {
      int[][] pixelMatrix3 = getGrayscalePixelMatrix(entry.getValue());

      answers.put(entry.getKey(), calculateTverskyRatio2(candidateMatrix, pixelMatrix3));
    }

    // Select the answer with the highest similarity with candidate answer
    Map.Entry<String, Double> answer = null;
    for (Map.Entry<String, Double> entry : answers.entrySet()) {
      if (answer == null || answer.getValue() < entry.getValue()) {
        answer = entry;
      }
    }

    return Integer.valueOf(answer.getKey());
  }

  public Pair<Double, Pair<Integer, Integer>> getBestTranslation(BufferedImage transformedImage, int[][] pixelMatrix2) {
    // Find best translation for transform
    double maxTversky = 0;
    Pair<Integer, Integer> bestTranslation = new Pair<>(0, 0);

    // Positive translations
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        // Translate image
        transformedImage = translateTransform(transformedImage, i, j);
        // Check if most similar yet
        double tversky = calculateTverskyRatio2(pixelMatrix2, getGrayscalePixelMatrix(transformedImage));
        if (tversky > maxTversky) {
          maxTversky = tversky;
          bestTranslation = new Pair<>(i, j);
        }
      }
    }

    // Negative translations
    for (int i = 0; i > -10; i--) {
      for (int j = 0; j > -10; j--) {
        // Translate image
        transformedImage = translateTransform(transformedImage, i, j);
        // Check if most similar yet
        double tversky = calculateTverskyRatio2(pixelMatrix2, getGrayscalePixelMatrix(transformedImage));
        if (tversky > maxTversky) {
          maxTversky = tversky;
          bestTranslation = new Pair<>(i, j);
        }
      }
    }

    return new Pair<>(maxTversky, bestTranslation);
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

  /**
   * Translates an image to a new location.
   *
   * Original code:
   * http://www.java2s.com/Tutorial/Java/0261__2D-Graphics/TranslatingaBufferedImage.htm
   *
   * @param image
   * @param x
   * @param y
   * @return
   */
  public BufferedImage translateTransform(BufferedImage image, double x, double y) {
    BufferedImage result = new BufferedImage(image.getHeight(), image.getWidth(), image.getType());

    AffineTransform tx = new AffineTransform();
    tx.translate(x, y);

    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
    op.filter(image, result);

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
        int value = matrix1[i][j] - matrix2[i][j];

        if (value < 0)
          resultPixelArray[i][j] = 0;
        else
          resultPixelArray[i][j] = value;
      }
    }

    return resultPixelArray;
  }

  public int[][] backSubtractTransform(int[][] matrix1, int[][] matrix2) {
    int[][] resultPixelArray = new int[matrix1.length][matrix1.length];

    for (int i = 0; i < matrix1.length; i++) {
      for (int j = 0; j < matrix1[i].length; j++) {
        int value = matrix2[i][j] - matrix1[i][j];

        if (value < 0)
          resultPixelArray[i][j] = 0;
        else
          resultPixelArray[i][j] = value;
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

  public String findImageComposition(int[][] matrix1, int[][] matrix2) {
    Map<String, Double> compositions = new HashMap<>();

    compositions.put("", calculateTverskyRatio(matrix1, matrix2, 1.0, 1.0));
    compositions.put("addition", calculateTverskyRatio(matrix1, matrix2, 1.0, 0));
    compositions.put("subtraction", calculateTverskyRatio(matrix1, matrix2, 0, 1.0));

    // Find the best composition
    Map.Entry<String, Double> bestComposition = null;
    for (Map.Entry<String, Double> composition : compositions.entrySet()) {
      if (bestComposition == null || bestComposition.getValue() < composition.getValue()) {
        bestComposition = composition;
      }
    }

    return bestComposition.getKey();
  }

  public int[][] performImageComposition(int[][] matrix1, int[][] matrix2, String operand) {
    int[][] compositionResult;
    int[][] result = new int[matrix1.length][matrix1.length];

    // Perform composition
    if(operand.equals("addition")) {
      compositionResult = differenceMatrices2(matrix2, matrix1);
      result = imageAddition(matrix1, compositionResult);
    }
    else if(operand.equals("subtraction")) {
      compositionResult = differenceMatrices2(matrix1, matrix2);
      result = imageSubtraction(matrix1, compositionResult);
    }

    return result;
  }

  public double calculateTverskyRatio(int[][] matrix1, int[][] matrix2, double alpha, double beta) {
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
        if (matrix1[i][j] == 1 || matrix2[i][j] == 1)
          count++;
      }
    }

    return count;
  }

  public int intersectMatrices(int[][] matrix1, int[][] matrix2) {
    int count = 0;

    for (int i = 0; i < matrix1.length; i++) {
      for (int j = 0; j < matrix1[i].length; j++) {
        if (matrix1[i][j] == 1 && matrix2[i][j] == 1)
          count++;
      }
    }

    return count;
  }

  public int differenceMatrices(int[][] matrix1, int[][] matrix2) {
    int count = 0;

    for (int i = 0; i < matrix1.length; i++) {
      for (int j = 0; j < matrix1[i].length; j++) {
        if (matrix1[i][j] == 1 && matrix2[i][j] == 0)
          count++;
      }
    }

    return count;
  }

  public int[][] differenceMatrices2(int[][] matrix1, int[][] matrix2) {
    int[][] result = new int[matrix1.length][matrix1.length];

    for (int i = 0; i < matrix1.length; i++) {
      for (int j = 0; j < matrix1[i].length; j++) {
        if (matrix1[i][j] == 1 && matrix2[i][j] == 0)
          result[i][j] = 1;
        else
          result[i][j] = 0;
      }
    }

    return result;
  }

  public int[][] imageAddition(int[][] pixelMatrix1, int[][] pixelMatrix2) {
    int[][] result = cloneMatrix(pixelMatrix1);

    for (int i = 0; i < pixelMatrix1.length; i++) {
      for (int j = 0; j < pixelMatrix1[i].length; j++) {
        if (pixelMatrix1[i][j] == 0 && pixelMatrix2[i][j] == 1)
          result[i][j] = 1;
      }
    }

    return result;
  }

  public int[][] imageSubtraction(int[][] pixelMatrix1, int[][] pixelMatrix2) {
    int[][] result = cloneMatrix(pixelMatrix1);

    for (int i = 0; i < pixelMatrix1.length; i++) {
      for (int j = 0; j < pixelMatrix1[i].length; j++) {
        if (pixelMatrix1[i][j] == 1 && pixelMatrix2[i][j] == 1)
          result[i][j] = 0;
      }
    }

    return result;
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
        int value = raster.getSample(i, j, 0);

        // Map gray/black to 1 and white to 0
        if (value == 255)
          matrix[i][j] = 0;
        else
          matrix[i][j] = 1;
      }
    }

    return matrix;
  }

  /**
   * Copies integer matrix to another matrix. Original code can be found here:
   *
   * http://stackoverflow.com/questions/1686425/copy-a-2d-array-in-java
   *
   * @param original
   * @return
   */
  public int[][] cloneMatrix(int[][] original) {
    int[][] copy = new int[original.length][];

    for (int i = 0; i < original.length; i++) {
      int[] aMatrix = original[i];
      int aLength = aMatrix.length;
      copy[i] = new int[aLength];
      System.arraycopy(aMatrix, 0, copy[i], 0, aLength);
    }

    return copy;
  }

  // TODO remove after testing
  /**
   * This method saves an image to a file.
   *
   * @param image
   * @return
   */
  public void saveImage(BufferedImage image, String name) {
    File file = new File(name + ".png");
    try {
      ImageIO.write(image, "PNG", file);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  // TODO remove after testing
  public BufferedImage getImage(int pixels[][], SampleModel sampleModel)
  {
    int w=pixels.length;
    int h=pixels[0].length;
    WritableRaster raster= Raster.createWritableRaster(sampleModel, new Point(0,0));
    for(int i=0;i<w;i++)
    {
      for(int j=0;j<h;j++)
      {
        raster.setSample(i,j,0,pixels[i][j]);
      }
    }

    BufferedImage image=new BufferedImage(w,h,BufferedImage.TYPE_BYTE_GRAY);
    image.setData(raster);

    return image;
  }

  // TODO remove after testing
  public int[][] convertGrayscalePixelMatrix(int[][] pixelMatrix) {
    int[][] matrix = new int[pixelMatrix.length][pixelMatrix.length];

    for (int i = 0; i < pixelMatrix.length; i++) {
      for (int j = 0; j < pixelMatrix.length; j++) {
        int value = pixelMatrix[i][j];

        // Map gray/black to 0 and white to 255
        if (value == 0)
          matrix[i][j] = 255;
        else
          matrix[i][j] = 0;
      }
    }

    return matrix;
  }
}
