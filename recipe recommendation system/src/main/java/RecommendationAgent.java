import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.spark.ml.feature.CountVectorizer;
import org.apache.spark.ml.feature.CountVectorizerModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;

import scala.Tuple2;

public class RecommendationAgent {

	public List<Recipe> recommend(String[] ingredients, Dataset<Row> categories, SparkSession spark) {
		
	List<Recipe> recommendations = new ArrayList<Recipe>();
		
	    List<Row> categoryLists = categories.collectAsList();
		Dataset<Row> df = spark.createDataFrame(categoryLists, categories.schema());
	    
	    // fit a CountVectorizerModel from the corpus
	    CountVectorizerModel cvModel = new CountVectorizer()
	      .setInputCol("categories")
	      .setOutputCol("feature")
	      .fit(df);
	    
	    String[] vocab = cvModel.vocabulary();
	    
	    List<String> vocabulary = new ArrayList<String>(Arrays.asList(vocab));
	    
	    for(String word : ingredients) {
	    	if(!vocabulary.contains(word)) {
	    		vocabulary.add(word);
	    	}
	    }
	    
	    calculateTermFrequencies(vocabulary);
	    
	    List<Integer> percept = generatePercept(ingredients, vocabulary);
	    

	    System.out.print("\n[");
		for(Integer k : percept) {
			System.out.print(k+", ");
		}
		System.out.print("]\n");
	    
	    spark.stop();
		
		List<Tuple2<UUID, Double>> recipeIDs = knn(percept);
		
		return recommendations;
	}


	private void calculateTermFrequencies(List<String> vocab) {
		Collection<Recipe> rs = RecommendationEngine.recipeMap.values();
		for(Recipe r: rs) {
			System.out.println(r.getTitle());
			List<Integer> tfVector = new ArrayList<Integer>(Collections.nCopies(vocab.size(), 0));
			for(int i = 0 ; i < vocab.size(); i++) {
				String term = vocab.get(i);
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

	private List<Integer> generatePercept(String[] ingredients, List<String> vocab) {
		
		List<String> ingred = Arrays.asList(ingredients);
		List<Integer> perceptVector = new ArrayList<Integer>(Collections.nCopies(vocab.size(), 0));
		for(int i = 0 ; i < vocab.size(); i++) {
			String term = vocab.get(i);
			if(ingred.contains(term)) {
				perceptVector.add(i, 1);
			}
		}
		return perceptVector;
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
        	d += Math.pow((x-y), 2);
        }
        d = Math.sqrt(d);
        return d;
	}

	
	private List<Tuple2<UUID, Double>> knn(List<Integer> percept){
		
		List<Tuple2<UUID, Double>> EucledeanDistances = new ArrayList<Tuple2<UUID, Double>>();
		
		for(Entry<UUID, Recipe> pair : RecommendationEngine.recipeMap.entrySet()) {
			Tuple2<UUID, Double> pairDistances = new Tuple2<UUID, Double>(pair.getKey(), calculateEucledeanDistance(percept, pair.getValue().getTermFrequencyVector()));
			EucledeanDistances.add(pairDistances);
		}
		 Comparator<Tuple2<UUID, Double>> comparator = new Comparator<Tuple2<UUID, Double>>()
		    {

				public int compare(Tuple2<UUID, Double> tuple1, Tuple2<UUID, Double> tuple2) {
					return tuple1._2.compareTo(tuple2._2);
				}

		    };
		Collections.sort(EucledeanDistances, comparator);

		return EucledeanDistances.subList(0, 4);
		
	}

}
