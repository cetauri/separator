///**
// * Licensed to the Apache Software Foundation (ASF) under one or more
// * contributor license agreements.  See the NOTICE file distributed with
// * this work for additional information regarding copyright ownership.
// * The ASF licenses this file to You under the Apache License, Version 2.0
// * (the "License"); you may not use this file except in compliance with
// * the License.  You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package kr.co.irskorea.separator.classifier.training;
//
//import java.io.IOException;
//import java.util.Map;
//
//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.io.IntWritable;
//import org.apache.hadoop.io.Text;
//import org.apache.hadoop.mapreduce.Mapper;
//import org.apache.mahout.classifier.naivebayes.BayesUtils;
//import org.apache.mahout.math.Vector;
//import org.apache.mahout.math.VectorWritable;
//
//public class ThetaMapper extends Mapper<IntWritable, VectorWritable, Text, VectorWritable> {
//
//  public static final String ALPHA_I = ThetaMapper.class.getName() + ".alphaI";
//  static final String TRAIN_COMPLEMENTARY = ThetaMapper.class.getName() + ".trainComplementary";
//
//}