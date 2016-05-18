package kr.co.irskorea.separator;

import kr.co.irskorea.separator.classifier.naivebayes.test.TestNaiveBayesDriver;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.classifier.naivebayes.training.TrainNaiveBayesJob;
import org.apache.mahout.text.SequenceFilesFromDirectory;
import org.apache.mahout.utils.SplitInput;
import org.apache.mahout.vectorizer.SparseVectorsFromSequenceFiles;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;

/**
 * Created by cetauri on 16. 5. 18..
 */
public class Separator {

    private enum DataTarget { TEST, TRAIN }

    private final String PATH_DATA_SEQ = "data-seq";
    private final String PATH_DATA_VECTORS = "data-vectors";

    private final String PATH_DATA_TRAIN_VECTORS = "data-train-vectors";
    private final String PATH_DATA_TEST_VECTORS = "data-test-vectors";

//    private final String PATH_DATA_TESTING_TRAIN = "data-testing-train";
//    private final String PATH_DATA_TESTING_TEST = "data-testing-test";

    private final String PATH_MODEL = "model";
    private final String PATH_LABEL_INDEX = "labelindex";
    private final String PATH_TEMP = "temp";

    private String prefix = "model";

    public Separator(String prefix){
        this.prefix = prefix;
    }

    public File getTemp(){
        return new File(prefix, PATH_TEMP);
    }

    public File getDataModel(){
        return new File(prefix, PATH_MODEL);
    }

    public File getDataLabelIndex(){
        return new File(prefix, PATH_LABEL_INDEX);
    }

    public File getDataTrainVectorsFile(){
        return new File(prefix, PATH_DATA_TRAIN_VECTORS);
    }

    public File getDataTestVectorsFile(){
        return new File(prefix, PATH_DATA_TEST_VECTORS);
    }

    public File getDataSeqFile(){
        return new File(prefix, PATH_DATA_SEQ);
    }

    public File getDataVectorsFile(){
        return new File(prefix, PATH_DATA_VECTORS);
    }

    private File getDataFreqCountFile() {
        return new File(getDataVectorsFile(), "df-count");
    }

    public void seqDirectory(File input) throws Exception {

        if(!input.exists() || !input.isDirectory()) throw new FileNotFoundException(input.getAbsolutePath());

        File output = getDataSeqFile();

        FileUtils.deleteQuietly(output);

        SequenceFilesFromDirectory sequenceFilesFromDirectory = new SequenceFilesFromDirectory();
        String[] argsOptions = {
                "-i", input.getAbsolutePath(),
                "-o", output.getAbsolutePath()
        };

        sequenceFilesFromDirectory.run(argsOptions);

    }

    public void seq2sparse() throws Exception {

        File seqFile = getDataSeqFile();
        File vectorsFile = getDataVectorsFile();

        String[] argsOptions = {
                "-i", seqFile.getAbsolutePath(),
                "-o", vectorsFile.getAbsolutePath(),
                "-lnorm", "-nv",
                "-wt", "tfidf"
        };

        SparseVectorsFromSequenceFiles sparseVectorsFromSequenceFiles = new SparseVectorsFromSequenceFiles();
        sparseVectorsFromSequenceFiles.run(argsOptions);
    }

    public void splitDataSet() throws Exception{
        File dataVectorsFile = getDataVectorsFile();
        File tfidfVectorsFile = new File(dataVectorsFile, "tfidf-vectors");

        String input = tfidfVectorsFile.getAbsolutePath();
        String train = getDataTrainVectorsFile().getAbsolutePath();
        String test = getDataTestVectorsFile().getAbsolutePath();

        String[] argsOptions = {
                "-i", input,
                "-tr", train,
                "-te", test,
                "-rp", "20",
                "-ow", "-seq",
                "-xm", "sequential"};

        SplitInput splitInput = new SplitInput();
        splitInput.run(argsOptions);
    }

    public void trainNaiveBayes() throws Exception{
        String train = getDataTrainVectorsFile().getAbsolutePath();
        String model = getDataModel().getAbsolutePath();
        String labelIndex = getDataLabelIndex().getAbsolutePath();
        String temp = getTemp().getAbsolutePath();

        String[] argsOptions = {
                "-i", train,
                "-o", model,
                "-li", labelIndex,
                "--tempDir", temp,
                "-ow", "-c",
        };

        ToolRunner.run(new Configuration(), new TrainNaiveBayesJob(), argsOptions);
    }

    public void testingForTrain() throws Exception{
        testing(DataTarget.TRAIN);
    }

    public void testingForTest() throws Exception{
        testing(DataTarget.TEST);
    }

    private void testing(DataTarget target) throws Exception{
        String name = null;
        String data_vectors_input = null;

        switch (target){
            case TEST:
                data_vectors_input = getDataTestVectorsFile().getAbsolutePath();
                name = "test";
                break;
            case TRAIN:
            default:
                data_vectors_input = getDataTrainVectorsFile().getAbsolutePath();
                name = "train";
        }

        String output = prefix + File.separator  + "data-testing-" + name;
        String model = getDataModel().getAbsolutePath();
        String labelindex = getDataLabelIndex().getAbsolutePath();


        String[] argsOptions = {
                "-i", data_vectors_input,
                "-m", model,
                "-l", labelindex,
                "-o", output,
                "-ow", "-c",
        };

        ToolRunner.run(new Configuration(), new TestNaiveBayesDriver(), argsOptions);
    }

    public Map<String, Double> classify(File f) throws IOException {

        String model = getDataModel().getAbsolutePath();
        String labelIndex = getDataLabelIndex().getAbsolutePath();
        String dictionary = getDataVectorsFile().getAbsolutePath() + File.separator + "dictionary.file-0";
        String dfCount = getDataFreqCountFile().getAbsolutePath() + File.separator + "part-r-00000";

        Classifier classifier = new Classifier();
        return classifier.run(model, labelIndex, dictionary, dfCount, f.getAbsolutePath());

    }

}
