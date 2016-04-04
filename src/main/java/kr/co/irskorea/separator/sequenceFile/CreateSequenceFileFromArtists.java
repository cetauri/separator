/*
 * .
 */
package kr.co.irskorea.separator.sequenceFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;

import java.io.File;
import java.io.FileNotFoundException;
import org.apache.hadoop.fs.FileSystem;
import java.io.IOException;
import java.util.List;


import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.io.IOUtils;


public class CreateSequenceFileFromArtists {
    public static void main(String[] argsx) throws FileNotFoundException, IOException
    {
        String filename = "/Users/cetauri/playground/irskorea/classfication/data/정치/13880";
        String outputfilename =  "/Users/cetauri/playground/irskorea/classfication/data/정치/13880.seq";
        Path path = new Path(outputfilename);

        //opening file
//        BufferedReader br = new BufferedReader(new FileReader(filename));
        //creating Sequence Writerr
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        SequenceFile.Writer writer = new SequenceFile.Writer(fs,conf,path,LongWritable.class,Text.class);

        List<String> lines = FileUtils.readLines(new File(outputfilename));

        String[] temp;
        String tempvalue = new String();
        String delimiter = " ";
        LongWritable key = new LongWritable();
        Text value = new Text();
        long tempkey = 0;


        for (String line : lines) {
            try{

//                line = br.readLine();
//                temp = line.split(delimiter);
                temp = StringUtils.split(line, delimiter);
                value = new Text();
                tempvalue = "";
                for (int i=1; i< temp.length;i++) {
                    tempvalue +=  temp[i] + delimiter;
                }
                value = new Text(tempvalue);
//                System.out.println("writing key/value  " + key.toString() + "/" + value.toString());

                tempkey++;
                key = new LongWritable(tempkey);
                writer.append(key,value);

            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }

//        while (line != null) {
//            try
//            {
//
////                line = br.readLine();
////                temp = line.split(delimiter);
//                temp = StringUtils.split(line, delimiter);
//                System.out.println("delimiter : "+delimiter);
//                System.out.println("temp : "+temp.toString());
//                value = new Text();
//                tempvalue = "";
//                for (int i=1; i< temp.length;i++) {
//                   tempvalue +=  temp[i] + delimiter;
//                }
//                value = new Text(tempvalue);
//                System.out.println("writing key/value  " + key.toString() + "/" + value.toString());
//
//                tempkey++;
//                key = new LongWritable(tempkey);
//                writer.append(key,value);
//
//            }
//            catch(Exception ex)
//            {
//                ex.printStackTrace();
//            }
//
//        }

        writer.close();
    }
}
