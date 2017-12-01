package rfxlab.com.demo.deeplearning;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.tensorflow.DataType;
import org.tensorflow.Graph;
import org.tensorflow.Output;
import org.tensorflow.Session;
import org.tensorflow.Tensor;

// Download the pre-trained inception model from here: https://storage.googleapis.com/download.tensorflow.org/models/inception_dec_2015.zip
public class PetRecognition {

	private String modelpath;
	private String imagepath;	
	private byte[] graphDef;
	private List<String> labels;
		

	private static float[] executeInceptionGraph(byte[] graphDef, Tensor image) {
		try (Graph g = new Graph()) {
			g.importGraphDef(graphDef);
			try (Session s = new Session(g);
					Tensor result = s.runner().feed("DecodeJpeg/contents", image).fetch("softmax").run().get(0)) {
				final long[] rshape = result.shape();
				if (result.numDimensions() != 2 || rshape[0] != 1) {
					throw new RuntimeException(String.format(
							"Expected model to produce a [1 N] shaped tensor where N is the number of labels, instead it produced one with shape %s",
							Arrays.toString(rshape)));
				}
				int nlabels = (int) rshape[1];
				return result.copyTo(new float[1][nlabels])[0];
			}
		}
	}

	private static int maxIndex(float[] probabilities) {
		int best = 0;
		for (int i = 1; i < probabilities.length; ++i) {
			if (probabilities[i] > probabilities[best]) {
				best = i;
			}
		}
		return best;
	}

	private static byte[] readAllBytesOrExit(Path path) {
		try {
			return Files.readAllBytes(path);
		} catch (IOException e) {
			System.err.println("Failed to read [" + path + "]: " + e.getMessage());
			System.exit(1);
		}
		return null;
	}

	private static List<String> readAllLinesOrExit(Path path) {
		try {
			return Files.readAllLines(path, Charset.forName("UTF-8"));
		} catch (IOException e) {
			System.err.println("Failed to read [" + path + "]: " + e.getMessage());
			System.exit(0);
		}
		return null;
	}

	public PetRecognition(String modelpath) {
		graphDef = readAllBytesOrExit(Paths.get(modelpath, "tensorflow_inception_graph.pb"));
        labels = readAllLinesOrExit(Paths.get(modelpath, "imagenet_comp_graph_label_strings.txt"));
		
	}
	
	

	public LabeledData detect(String imagepath) {
        byte[] imageBytes = readAllBytesOrExit(Paths.get(imagepath));
		try (Tensor image = Tensor.create(imageBytes)) {
			float[] labelProbabilities = executeInceptionGraph(graphDef, image);
			int bestLabelIdx = maxIndex(labelProbabilities);
			return new LabeledData(labels.get(bestLabelIdx), labelProbabilities[bestLabelIdx] * 100f) ;
		}
	}
	
	public static void main(String[] args) {
		String modelpath = "/home/trieu/data/tensorflow-models/";		
		PetRecognition petRecognition = new PetRecognition(modelpath);
		
		LabeledData labeledDataDog = petRecognition.detect("data/dog.jpg");
		String result1 = String.format("BEST MATCH: %s (%.2f%% likely)", labeledDataDog.getLabelData(), labeledDataDog.getProbabilities());
		System.out.println(result1); 
		
		LabeledData labeledDataCat = petRecognition.detect("data/cat.jpg");
		String result2 = String.format("BEST MATCH: %s (%.2f%% likely)", labeledDataCat.getLabelData(), labeledDataCat.getProbabilities());
		System.out.println(result2);
	}

}
