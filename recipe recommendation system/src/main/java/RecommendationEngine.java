import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.ForeachFunction;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;

import scala.Tuple2;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Row;

public class RecommendationEngine {
	
	public static final Map<Integer, Recipe> recipeMap = new HashMap<Integer, Recipe>();
	
	public static final String recipePath = "./src/main/resources/full_format_recipes.json/full_format_recipes.json";
	
	@SuppressWarnings("deprecation")
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
		Dataset<Row> recipe = spark.read().json(recipePath);
		
		recipe.registerTempTable("recipe");
		
		recipe.printSchema();
		
		
		Dataset<Row> recipesFiltered = null;
		
		if(!protein_input.isEmpty() || !rating_input.isEmpty()) {
			if(protein_input.isEmpty()) {
				recipesFiltered = spark.sql("SELECT * FROM recipe WHERE rating >= "+rating+";");
			} else if(rating_input.isEmpty()) {
				recipesFiltered = spark.sql("SELECT * FROM recipe WHERE protein >="+protein_content+";");
			} else {
				recipesFiltered = spark.sql("SELECT * FROM recipe WHERE protein >="+protein_content+" AND rating >= "+rating);
			}
		} else {
			recipesFiltered = spark.sql("SELECT * FROM recipe;");
		}
		
		recipesFiltered.show();
		recipesFiltered = recipesFiltered.distinct();
		
		recipesFiltered.foreach(new ForeachFunction<Row>() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			Integer id = 1;
			public void call(Row r) throws Exception {
				String title = r.getString(10);
				String desc = r.getString(3);
				List<String> categories = r.getList(1);
				List<String> ingredients = r.getList(6);
				List<String> directions = r.getList(4);
				
				Recipe rec = new Recipe(title, desc, ingredients, directions, categories);
				recipeMap.put(id, rec);
				id += 1;
			}
			
		});
		
		RecommendationAgent agent = new RecommendationAgent();
		List<Recipe> recommendedRecipes = agent.recommend(ingredients);
		
		
		System.out.println("\n\nOur Agent recommends following recipes. Happy Cooking!! \n\n");
		
		for(Recipe r: recommendedRecipes) {
			System.out.println(r); //it should print recipe title with link to webpage.
		}
	}

}
