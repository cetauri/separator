start_time=`date +%s`

mahout seqdirectory -i ../data/ -o ../model/data-seq
mahout seq2sparse -i ../model/data-seq -o ../model/data-vectors  -lnorm -nv  -wt tfidf
mahout split -i ../model/data-vectors/tfidf-vectors -tr ../model/data-train-vectors -te ../model/data-test-vectors -rp 20 -ow -seq -xm sequential
mahout trainnb -i ../model/data-train-vectors  -o ../model/model -li ../model/labelindex -ow -c

# mahout testnb -i ../model/data-train-vectors -m ../model/model -l ../model/labelindex -ow -o ../model/data-testing-train -c
# mahout testnb -i ../model/data-test-vectors -m ../model/model -l ../model/labelindex -ow -o ../model/data-testing-test -c

java -cp ../target/separator-core-1.0-SNAPSHOT-jar-with-dependencies.jar kr.co.irskorea.separator.classifier.naivebayes.test.TestNaiveBayesDriver -i ../model/data-train-vectors -m ../model/model -l ../model/labelindex -ow -o ../model/data-testing-train -c
java -cp ../target/separator-core-1.0-SNAPSHOT-jar-with-dependencies.jar kr.co.irskorea.separator.classifier.naivebayes.test.TestNaiveBayesDriver -i ../model/data-test-vectors -m ../model/model -l ../model/labelindex -ow -o ../model/data-testing-test -c

end_time=`date +%s`
echo execution time was `expr $end_time - $start_time` s.

