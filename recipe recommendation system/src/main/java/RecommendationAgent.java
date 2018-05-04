import java.util.ArrayList;
import java.util.Arrays;
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

	public List<Recipe> recommend(String[] ingredients, Dataset<Row> categories) {
		
		SparkSession spark = SparkSession
			      .builder()
			      .appName("JavaCountVectorizer")
			      .getOrCreate();

	    Dataset<Row> df = spark.createDataFrame(categories.collectAsList(), categories.schema());

	    // fit a CountVectorizerModel from the corpus
	    CountVectorizerModel cvModel = new CountVectorizer()
	      .setInputCol("text")
	      .setOutputCol("feature")
	      .fit(df);

	    cvModel.transform(df).show(false);

	    spark.stop();
		
		
		//List<String> corpus = buildCorpus(ingredients);
		
		//calculateTermFrequencies(corpus);
		
		//List<Integer> percept = generatePercept(ingredients);
		
		//= knn(percept, corpus);
		
		return null;
	}

	private void calculateTermFrequencies(List<String> corpus) {
		// TODO Auto-generated method stub
		for(Recipe r: RecommendationEngine.recipeMap.values()) {
			for(String term: r.getCategories()) {
				
			}
		}
	}

	private List<Integer> generatePercept(String[] ingredients) {
		return null;
		// TODO Auto-generated method stub
		
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
