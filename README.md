# Apache Mahout prepare20newsgroups in version .7
http://www.feeny.org/apache-mahout-prepare20newsgroups-in-version-7/

## 1. Generate sequence files (of Text) from a directory
	mahout seqdirectory -i 20news-bydate-train -o 20news-seq

## 2. Sparse Vector generation from Text sequence files

	mahout seq2sparse -i 20news-seq -o 20news-vectors  -lnorm -nv  -wt tfidf
	


 Option  |  Desc.
------------- | ------------- 
--namedVector (-nv)    | (Optional) Whether output vectors should be NamedVectors. If set true else false    
--logNormalize (-lnorm)    | (Optional) Whether output vectors should be logNormalize. If set true else false    
--weight (-wt) weight |            The kind of weight to use. Currently TF or TFIDF. Default: TFIDF





## 3. Split data into train and test sets with 20% of the data being used for test and 80% for train:

	mahout split -i 20news-vectors/tfidf-vectors -tr 20news-train-vectors -te 20news-test-vectors -rp 20 -ow -seq -xm sequential
	

 Option  |  Desc.
------------- | ------------- 
--trainingOutput (-tr)     | The training data output directory    
--testOutput (-te)    | The test data output directory
--randomSelectionPct (-rp) | Percentage of items to be randomly selected as test   data when using mapreduce mode
  --sequenceFiles (-seq) |Set if the input files are sequence files. Default is false                   
  --method (-xm)  | The execution method to use: sequential or mapreduce. Default is mapreduce                  
  --overwrite (-ow)  | If present, overwrite the  output directory before running job                

## 4. Build the model:

	mahout trainnb -i 20news-train-vectors  -o model -li labelindex -ow -c
 
 Option  |  Desc.
------------- | ------------- 
  --trainComplementary (-c)        |Train complementary? Default is false.                        
  --labelIndex (-li)     |The path to store the label index in         
  --overwrite (-ow)                |If present, overwrite the output directory before running job                           

## 5. You can test the model against the training set:
	mahout testnb -i 20news-train-vectors -m model -l labelindex -ow -o 20news-testing-train -c

## 참고
- [Apache Mahout prepare20newsgroups in version .7](http://www.feeny.org/apache-mahout-prepare20newsgroups-in-version-7/)