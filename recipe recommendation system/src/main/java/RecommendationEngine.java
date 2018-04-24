import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;

import scala.Tuple2;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class RecommendationEngine {
	
	public static final String recipePath = "./src/main/resources/full_format_recipes.json/full_format_recipes.json";
	
	public static void main(String[] args) {
		
		//processing user input
		Scanner user_input = new Scanner(System.in);
		
		System.out.println("Enter ingredients:\t");
		String[] ingredients = user_input.nextLine().trim().split("[\\s,;]+");
		
		Double rating = null;
		System.out.println("Enter rating: (Optional)\t");
		String rating_input = user_input.nextLine();
		if(!rating_input.isEmpty()) {
			rating = Double.parseDouble(rating_input.trim());
		}
		
		Double protein_content = null;
		System.out.println("Enter protein content: (Optional)\t");
		String protein_input = user_input.nextLine();
		if(!protein_input.isEmpty()) {
			protein_content = Double.parseDouble(protein_input.trim());
		}
		
		System.out.println("You entered..\n");
		for(int i = 0 ; i < ingredients.length; i++) {
			System.out.print(ingredients[i]+"  ");
		}
		System.out.println("\nRating:\t"+rating);
		System.out.println("Protein Content:\t"+protein_content);
		
		//Creating spark session
		SparkSession spark = SparkSession
		        .builder()
		        .appName("Recipe Recommendation System")
		        .master("local[2]")
		        .getOrCreate();
		
		/**
		 * Load Recipe data
		 */
		JavaRDD<String> recipeRDD = spark.read().json(recipePath).toJSON().toJavaRDD();
		System.out.println(recipeRDD.first());
		
		JavaRDD<String> recipeFilteredRating = null;
		//filtering recipes with rating equal or higher than user input 
		if(!rating_input.isEmpty()) {
			recipeFilteredRating = recipeRDD.filter(new Function<String, Boolean>() {
				
				private static final long serialVersionUID = 1L;

				public Boolean apply(String t) {
					// TODO Auto-generated method stub
					return null;
				}
			});
			recipeRDD = recipeFilteredRating;
		}
		
		JavaRDD<String> recipeFilteredProteinContent = null;
		if(!protein_input.isEmpty()) {
			recipeFilteredProteinContent = recipeFilteredRating.filter(new Function<String, Boolean>() {
				private static final long serialVersionUID = 1L;

				public Boolean apply(String t) {
					// TODO Auto-generated method stub
					return null;
				}
			});
			recipeRDD = recipeFilteredProteinContent;
		}
		
		
		RecommendationAgent agent = new RecommendationAgent();
		List<Recipe> recommendedRecipes = agent.recommend(recipeRDD, ingredients);
		
		
		System.out.println("\n\nOur Agent recommends following recipes. Happy Cooking!! \n\n");
		
		for(Recipe recipe: recommendedRecipes) {
			System.out.println(recipe); //it should print recipe title with link to webpage.
		}
	}

}
