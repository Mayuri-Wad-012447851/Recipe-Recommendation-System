import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.feature.CountVectorizer;
import org.apache.spark.ml.feature.CountVectorizerModel;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.ArrayType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

public class RecommendationAgent {

	public List<Recipe> recommend(String[] ingredients, Dataset<Row> categories, SparkSession spark) {
		
		List<Recipe> recommendations = new ArrayList<Recipe>();
		
		//Row percept = generatePercept(ingredients);
		
	    List<Row> categoryLists = categories.collectAsList();
		Dataset<Row> df = spark.createDataFrame(categoryLists, categories.schema());
	    
	    // fit a CountVectorizerModel from the corpus
	    CountVectorizerModel cvModel = new CountVectorizer()
	      .setInputCol("categories")
	      .setOutputCol("feature")
	      .fit(df);
	    
	    String[] vocab = cvModel.vocabulary();
	    
	    calculateTermFrequencies(vocab);
	    
	    for(String v : vocab) {
	    	System.out.print(v+" ,");
	    }
	    spark.stop();
		
		
		//List<String> corpus = buildCorpus(ingredients);
		
		
		
		//List<Integer> percept = generatePercept(ingredients);
		
		//= knn(percept, corpus);
		
		return recommendations;
	}

	private void calculateTermFrequencies(String[] vocab) {
		Collection<Recipe> rs = RecommendationEngine.recipeMap.values();
		for(Recipe r: rs) {
			System.out.println(r.getTitle());
			List<Integer> tfVector = new ArrayList<Integer>(Collections.nCopies(vocab.length, 0));
			for(int i = 0 ; i < vocab.length; i++) {
				String term = vocab[i];
				if(r.getCategories().contains(term)) {
					tfVector.add(i, 1);
				}
			}
			r.setTermFrequencyVector(tfVector);
			System.out.print("\n[");
			for(Integer k : r.getTermFrequencyVector()) {
				System.out.print(k+", ");
			}
			System.out.print("]\n");
		}
	}

	private Row generatePercept(String[] ingredients) {
		return RowFactory.create(Arrays.asList(ingredients));
	}

	private List<String> buildCorpus(String[] ingredients) {
		
		List<String> termsCorpus = new ArrayList<String>();
		
		for(String term: ingredients) {
			if(!termsCorpus.contains(term)) {
				termsCorpus.add(term.toLowerCase());
			}
		}
		
		for(Recipe r: RecommendationEngine.recipeMap.values()) {
			List<String> categories = r.getCategories();
			for(String term : categories) {
				if(!termsCorpus.contains(term)) {
					termsCorpus.add(term.toLowerCase());
				}
			}
		}
		
		return termsCorpus;
	}
	
	private double calculateEucledeanDistance(List<Integer> percept,List<Integer> record) {
		double d = 0.0;
        for(int i = 0; i < percept.size(); i++) {
        	Integer x = percept.get(i);
        	Integer y = record.get(i);
        	d += ((x-y) ^ 2);
        }
        //d = math.sqrt(d);
        return d;
	}

}
