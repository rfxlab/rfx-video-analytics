package rfxlab.com.demo.deeplearning;

public class LabeledData {

	String labelData;
	float probabilities;
	public LabeledData(String labelData, float probabilities) {
		super();
		this.labelData = labelData;
		this.probabilities = probabilities;
	}
	public String getLabelData() {
		return labelData;
	}
	public void setLabelData(String labelData) {
		this.labelData = labelData;
	}
	public float getProbabilities() {
		return probabilities;
	}
	public void setProbabilities(float probabilities) {
		this.probabilities = probabilities;
	}
	
	
}
