package kr.co.irskorea.separator;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * Created by cetauri on 16. 5. 16..
 */
public class SeparatorTest {

    @Test
    public void classifyTest() throws Exception{

        File f = new File("/Users/cetauri/temp/apache.issue.txt");

        Separator separator = new Separator("/Users/cetauri/playground/separator/model/7b0d6551-35ef-4dbd-8145-804bf4692244/");
        Map<String, Double> map = separator.classify(f);

        String bestName = "";
        double bestScore = -Double.MAX_VALUE;
        for( String key : map.keySet() ){
            String label = key;
            double score = map.get(key);
            System.out.println( String.format("%s : %s", key, map.get(key)) );
            if (score > bestScore) {
                bestScore = score;
                bestName = label;
            }
        }
        System.out.println(bestName);
    }

    @Test
    public void test() throws Exception{
        File f = new File("/Users/cetauri/playground/separator/data");

        Separator separator = new Separator("/Users/cetauri/playground/separator/model/7b0d6551-35ef-4dbd-8145-804bf4692244");
        separator.seqDirectory(f);
        separator.seq2sparse();
        separator.splitDataSet();
        separator.trainNaiveBayes();

        separator.testingForTrain();
        separator.testingForTest();
    }

    String uuid = UUID.randomUUID().toString();
    public String getPath (){
        File f = new File("model", uuid);
        if (!f.exists()) {
            f.mkdir();
        }
        return f.getAbsolutePath();
    }
    @Test
    public void a1_seqdirectory() throws Exception {
        File f = new File("/Users/cetauri/playground/separator/data");

        Separator separator = new Separator(getPath());
        separator.seqDirectory(f);
//        String output = getPath() + "/model/data-seq";
//
//        FileUtils.deleteQuietly(new File(output));
//
//        SequenceFilesFromDirectory sequenceFilesFromDirectory = new SequenceFilesFromDirectory();
//        String[] argsOptions = {"-i", input, "-o", output};
//        sequenceFilesFromDirectory.run(argsOptions );

//        mahout seqDirectory -i ../data/ -o ../model/data-seq
    }

    @Test
    public void a2_seq2sparse() throws Exception {

        Separator separator = new Separator("/Users/cetauri/playground/separator/model/7b0d6551-35ef-4dbd-8145-804bf4692244");
        separator.seq2sparse();
//
//        String seqPath = getPath() + "/model/data-seq";
//        String vectorPath = getPath() + "/model/data-vectors";
//
//        String[] argsOptions = {"-i", seqPath, "-o", vectorPath, "-lnorm", "-nv", "-wt", "tfidf"};
//
//        SparseVectorsFromSequenceFiles sparseVectorsFromSequenceFiles = new SparseVectorsFromSequenceFiles();
//        sparseVectorsFromSequenceFiles.run(argsOptions );
    }

    @Test
    public void a3_split() throws Exception{
        Separator separator = new Separator("/Users/cetauri/playground/separator/model/7b0d6551-35ef-4dbd-8145-804bf4692244");
        separator.splitDataSet();
//        String input = getPath() + "/model/data-vectors";
//        String train = getPath() + "/model/data-train-vectors";
//        String test = getPath() + "/model/data-test-vectors";
//
//        String[] argsOptions = {"-i", input + "/tfidf-vectors",
//                                "-tr", train,
//                                "-te", test,
//                                "-rp", "20",
//                                "-ow", "-seq",
//                                "-xm", "sequential"};
//
//        SplitInput splitInput = new SplitInput();
//        splitInput.run(argsOptions);
    }

    @Test
    public void a4_trainnb() throws Exception{
        Separator separator = new Separator("/Users/cetauri/playground/separator/model/7b0d6551-35ef-4dbd-8145-804bf4692244");
        separator.trainNaiveBayes();
//        String train = getPath() + "/model/data-train-vectors";
//        String model = getPath() + "/model/model";
//        String labelindex = getPath() + "/model/labelindex";
//
//
//        String[] argsOptions = {"-i", train,
//                                "-o", model,
//                                "-li", labelindex,
//                                "-ow", "-c",
//                                "--tempDir", "temp1"
//        };
//
//
////        TrainNaiveBayesJob trainNaiveBayesJob = new TrainNaiveBayesJob();
//        ToolRunner.run(new Configuration(), new TrainNaiveBayesJob(), argsOptions);
    }

//    @Test
//    public void a5_test_with_train() throws Exception{
//        test("train");
//    }
//
//    @Test
//    public void a6_test_with_test() throws Exception{
//        test("test");
//    }

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

//    public void test(String name) throws Exception{
//        String input = getPath() + "/model/data-" + name + "-vectors";
//        String output = getPath() + "/" + UUID.randomUUID().toString() + "/" + "data-testing-" + name;
//        String model = getPath() + "/model/model";
//        String labelindex = getPath() + "/model/labelindex";
//
//
//        String[] argsOptions = {
//                "-i", input,
//                "-m", model,
//                "-l", labelindex,
//                "-o", output,
//                "-ow", "-c",
////                "--tempDir", UUID.randomUUID().toString()
//        };
//
//
//        ToolRunner.run(new Configuration(), new TestNaiveBayesDriver(), argsOptions);
//
//    }
}

