package kr.co.irskorea.separator;

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
    }

    @Test
    public void a2_seq2sparse() throws Exception {

        Separator separator = new Separator("/Users/cetauri/playground/separator/model/7b0d6551-35ef-4dbd-8145-804bf4692244");
        separator.seq2sparse();
    }

    @Test
    public void a3_split() throws Exception{
        Separator separator = new Separator("/Users/cetauri/playground/separator/model/7b0d6551-35ef-4dbd-8145-804bf4692244");
        separator.splitDataSet();
    }

    @Test
    public void a4_trainnb() throws Exception{
        Separator separator = new Separator("/Users/cetauri/playground/separator/model/7b0d6551-35ef-4dbd-8145-804bf4692244");
        separator.trainNaiveBayes();
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

}

