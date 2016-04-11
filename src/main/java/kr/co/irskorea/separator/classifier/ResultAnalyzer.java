/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.co.irskorea.separator.classifier;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.mahout.cf.taste.impl.common.RunningAverageAndStdDev;
import org.apache.mahout.classifier.ClassifierResult;
import kr.co.irskorea.separator.classifier.ConfusionMatrix;
//import org.apache.mahout.classifier.ConfusionMatrix;
import org.apache.mahout.math.stats.OnlineSummarizer;

/**
 * ResultAnalyzer captures the classification statistics and displays in a tabular manner
 */
public class ResultAnalyzer {
  
  private final ConfusionMatrix confusionMatrix;
  private final OnlineSummarizer summarizer;
  private boolean hasLL = false;
  
  /*
   * === Summary ===
   * 
   * Correctly Classified Instances 635 92.9722 % Incorrectly Classified Instances 48 7.0278 % Kappa statistic
   * 0.923 Mean absolute error 0.0096 Root mean squared error 0.0817 Relative absolute error 9.9344 % Root
   * relative squared error 37.2742 % Total Number of Instances 683
   */
  private int correctlyClassified;
  
  private int incorrectlyClassified;
  
  public ResultAnalyzer(Collection<String> labelSet, String defaultLabel) {
    confusionMatrix = new ConfusionMatrix(labelSet, defaultLabel);
    summarizer = new OnlineSummarizer();
  }
  
  public ConfusionMatrix getConfusionMatrix() {
    return this.confusionMatrix;
  }
  
  /**
   * 
   * @param correctLabel
   *          The correct label
   * @param classifiedResult
   *          The classified result
   * @return whether the instance was correct or not
   */
  public boolean addInstance(String correctLabel, ClassifierResult classifiedResult) {
    boolean result = correctLabel.equals(classifiedResult.getLabel());
    if (result) {
      correctlyClassified++;
    } else {
      incorrectlyClassified++;
    }
    confusionMatrix.addInstance(correctLabel, classifiedResult);
    if (classifiedResult.getLogLikelihood() != Double.MAX_VALUE){
      summarizer.add(classifiedResult.getLogLikelihood());
      hasLL = true;
    }
    return result;
  }
  
  @Override
  public String toString() {
    StringBuilder returnString = new StringBuilder();
    returnString.append("\n");
    returnString.append("=======================================================\n");
    returnString.append("요약 (Summary)\n");
    returnString.append("-------------------------------------------------------\n");
    int totalClassified = correctlyClassified + incorrectlyClassified;
    double percentageCorrect = (double) 100 * correctlyClassified / totalClassified;
    double percentageIncorrect = (double) 100 * incorrectlyClassified / totalClassified;
    NumberFormat decimalFormatter = new DecimalFormat("0.####");
    
    returnString.append(StringUtils.rightPad("정확한 분류 (Correctly Classified Instances)", 45)).append(": ").append(
      StringUtils.leftPad(Integer.toString(correctlyClassified), 10)).append('\t').append(
      StringUtils.leftPad(decimalFormatter.format(percentageCorrect), 10)).append("%\n");
    returnString.append(StringUtils.rightPad("부정학한 분류 (Incorrectly Classified Instances)", 45)).append(": ").append(
            StringUtils.leftPad(Integer.toString(incorrectlyClassified), 10)).append('\t').append(
      StringUtils.leftPad(decimalFormatter.format(percentageIncorrect), 10)).append("%\n");
    returnString.append(StringUtils.rightPad("총 분류 합계 (Total Classified Instances)", 45)).append(": ").append(
      StringUtils.leftPad(Integer.toString(totalClassified), 10)).append('\n');
    returnString.append('\n');

    returnString.append(confusionMatrix);
    returnString.append("=======================================================\n");
    returnString.append("통계 (Statistics)\n");
    returnString.append("-------------------------------------------------------\n");

    RunningAverageAndStdDev normStats = confusionMatrix.getNormalizedStats();
    returnString.append(StringUtils.rightPad("카파 (Kappa)", 45)).append(
            StringUtils.leftPad(decimalFormatter.format(confusionMatrix.getKappa()), 10)).append('\n');
    returnString.append(StringUtils.rightPad("정확도 (Accuracy)", 45)).append(
            StringUtils.leftPad(decimalFormatter.format(confusionMatrix.getAccuracy()), 10)).append("%\n");
    returnString.append(StringUtils.rightPad("신뢰성 (Reliability)", 45)).append(
            StringUtils.leftPad(decimalFormatter.format(normStats.getAverage() * 100.00000001), 10)).append("%\n");
    returnString.append(StringUtils.rightPad("신뢰성 (Reliability : standard deviation)", 45)).append(
            StringUtils.leftPad(decimalFormatter.format(normStats.getStandardDeviation()), 10)).append('\n');
    returnString.append(StringUtils.rightPad("정확도 (Weighted precision)", 45)).append(
            StringUtils.leftPad(decimalFormatter.format(confusionMatrix.getWeightedPrecision()), 10)).append('\n');
    returnString.append(StringUtils.rightPad("재현율 (Weighted recall)", 50)).append(
            StringUtils.leftPad(decimalFormatter.format(confusionMatrix.getWeightedRecall()), 10)).append('\n');
    returnString.append(StringUtils.rightPad("F1 score (Weighted F1 score)", 50)).append(
            StringUtils.leftPad(decimalFormatter.format(confusionMatrix.getWeightedF1score()), 10)).append('\n');



    if (hasLL) {
      returnString.append("\n\n");
      returnString.append("Avg. Log-likelihood: ").append(summarizer.getMean()).append(" 25%-ile: ").append(summarizer.getQuartile(1))
              .append(" 75%-ile: ").append(summarizer.getQuartile(2));
    }

    return returnString.toString();
  }
}