package com.fluxtion.example.cookbook.ml.linearregression.wekamodel;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

import java.util.Arrays;

public class PredictiveService {

    private final Instances data;
    private final LinearRegression classifier;

    private final Instance instance;

    public static void main(String[] args) throws Exception {
        ConverterUtils.DataSource source = new ConverterUtils.DataSource("src/main/resources/data/ml/linear_regression/input1.csv");
        source.getDataSet().forEach(System.out::println);
    }

    public PredictiveService() throws Exception {
        // Loading Albuquerque real estate prices
        ConverterUtils.DataSource source = new ConverterUtils.DataSource("com/fluxtion/example/cookbook/mlintegration/prices.arff");
        data = source.getDataSet();
        // Setting the last attribute (price) to the class index
        data.setClassIndex(data.numAttributes() - 1);
//        for(int i = 0; i < data.numAttributes(); i++){
//            var attribute = data.attribute(i);
//            printAttribute(attribute);
//        }

        // Creating a linear regression based classifier
        classifier = new LinearRegression();

        // Let's learn classifier with data
        classifier.buildClassifier(data);
        System.out.printf("capabilities:%s%nclassifier:%s %n%nco-efficients:%s%n%n%n",
                classifier.getCapabilities(),
                classifier,
                Arrays.toString(classifier.coefficients()));


//        classifier.

        // Creating an Instance for predictions
        instance = new DenseInstance(data.numAttributes());
        instance.setDataset(data);

//        for(int i = 0; i < instance.numAttributes(); i++){
//            var attribute = instance.attribute(i);
//            printAttribute(attribute);
//        }

    }

//    public void printAttribute(Attribute attribute){
//        System.out.printf("%s weight:%f value:%f%n",
//                attribute.toString(),
//                attribute.weight(),
//                instance.value(attribute));
//    }
//
//    /**
//     * Predict price of the real estate property in Seattle
//     * @param area sqft
//     * @param bedrooms bd
//     * @param milesAway mi
//     */
//    public void predictPrice(double area, int bedrooms, double milesAway) throws Exception {
//        // Let's ask for a price for the property:
//        instance.setValue(0, area);
//        instance.setValue(1, bedrooms);
//        instance.setValue(2, milesAway);
//
//        // Price prediction action
//        double predictedPrice = classifier.classifyInstance(instance);
//        System.out.println("-- Predicting price for [area -" + area + " sqft, bedrooms - " + bedrooms + ", miles away - " + milesAway + " mi]");
//        System.out.println("Predicted price: " + predictedPrice);
//
//        // Calculation error rate
//        Evaluation eval = new Evaluation(data);
//        eval.evaluateModel(classifier, data);
//        System.out.println("Calculation error rate: " + eval.errorRate());
//    }
}

