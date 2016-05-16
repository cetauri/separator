package kr.co.irskorea.separator;

import kr.co.irskorea.separator.classifier.naivebayes.test.TestNaiveBayesDriver;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.classifier.naivebayes.training.TrainNaiveBayesJob;
import org.apache.mahout.text.SequenceFilesFromDirectory;
import org.apache.mahout.utils.SplitInput;
import org.apache.mahout.vectorizer.SparseVectorsFromSequenceFiles;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by cetauri on 16. 5. 16..
 */
public class SeparatorTest {

    public String getPath (){
        File f = new File("./");
        return f.getAbsolutePath();
    }
    @Test
    public void a1_seqdirectory() throws Exception {

        String input = getPath() + "/data/";
        String output = getPath() + "/model/data-seq";

        FileUtils.deleteQuietly(new File(output));

        SequenceFilesFromDirectory sequenceFilesFromDirectory = new SequenceFilesFromDirectory();
        String[] argsOptions = {"-i", input, "-o", output};
        sequenceFilesFromDirectory.run(argsOptions );

//        mahout seqdirectory -i ../data/ -o ../model/data-seq
    }

    @Test
    public void a2_seq2sparse() throws Exception {

        String seqPath = getPath() + "/model/data-seq";
        String vectorPath = getPath() + "/model/data-vectors";

        String[] argsOptions = {"-i", seqPath, "-o", vectorPath, "-lnorm", "-nv", "-wt", "tfidf"};

        SparseVectorsFromSequenceFiles sparseVectorsFromSequenceFiles = new SparseVectorsFromSequenceFiles();
        sparseVectorsFromSequenceFiles.run(argsOptions );
    }

    @Test
    public void a3_split() throws Exception{
        String input = getPath() + "/model/data-vectors";
        String train = getPath() + "/model/data-train-vectors";
        String test = getPath() + "/model/data-test-vectors";

        String[] argsOptions = {"-i", input + "/tfidf-vectors",
                                "-tr", train,
                                "-te", test,
                                "-rp", "20",
                                "-ow", "-seq",
                                "-xm", "sequential"};

        SplitInput splitInput = new SplitInput();
        splitInput.run(argsOptions);
    }

    @Test
    public void a4_trainnb() throws Exception{
        String train = getPath() + "/model/data-train-vectors";
        String model = getPath() + "/model/model";
        String labelindex = getPath() + "/model/labelindex";


        String[] argsOptions = {"-i", train,
                                "-o", model,
                                "-li", labelindex,
                                "-ow", "-c"};


//        TrainNaiveBayesJob trainNaiveBayesJob = new TrainNaiveBayesJob();
        ToolRunner.run(new Configuration(), new TrainNaiveBayesJob(), argsOptions);


    }

    @Test
    public void a5_test_with_train() throws Exception{
        test("train");
    }

    @Test
    public void a6_test_with_test() throws Exception{
        test("test");
    }

    @Test
    public void a7_test() throws IOException{
        String model = getPath() + "/model/model";
        String labelindex = getPath() + "/model/labelindex";
        String dictionary = getPath() + "/model/data-vectors/dictionary.file-0";
        String dfCount = getPath() + "/model/data-vectors/df-count/part-r-00000";
        String newDocument = "/Users/cetauri/temp/sport.new.txt";

        Classifier classifier = new Classifier();
        classifier.run(model, labelindex, dictionary, dfCount, newDocument);
    }

    public void test(String name) throws Exception{
        String input = getPath() + "/model/data-" + name + "-vectors";
        String output = getPath() + "data-testing-" + name;
        String model = getPath() + "/model/model";
        String labelindex = getPath() + "/model/labelindex";


        String[] argsOptions = {
                "-i", input,
                "-m", model,
                "-l", labelindex,
                "-o", output,
                "-ow", "-c"};


        ToolRunner.run(new Configuration(), new TestNaiveBayesDriver(), argsOptions);

    }
}

