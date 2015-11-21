package ravensproject;

/**
 * Created by Caleb on 11/19/2015.
 */
public class SimiltudeTransform {

  private Integer analogyKey;
  private String transform;
  private Pair<Integer, Integer> translation;
  private String compositionOperand;
  private Double similarityRatio;
  private int[][] compositionResult;

  public SimiltudeTransform(
      Integer analogyKey,
      String transform,
      Pair<Integer, Integer> translation,
      String compositionOperand,
      Double similarityRatio) {
    this.analogyKey = analogyKey;
    this.transform = transform;
    this.translation = translation;
    this.compositionOperand = compositionOperand;
    this.similarityRatio = similarityRatio;
  }

  public Integer getAnalogyKey() {
    return analogyKey;
  }

  public String getTransform() {
    return transform;
  }

  public Pair<Integer, Integer> getTranslation() {
    return translation;
  }

  public String getCompositionOperand() {
    return compositionOperand;
  }

  public Double getSimilarityRatio() {
    return similarityRatio;
  }

  public int[][] getCompositionResult() {
    return compositionResult;
  }

  public void setCompositionResult(int[][] compositionResult) {
    this.compositionResult = compositionResult;
  }
}
