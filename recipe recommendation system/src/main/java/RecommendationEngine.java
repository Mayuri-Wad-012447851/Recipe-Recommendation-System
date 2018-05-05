import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import org.apache.spark.api.java.function.ForeachFunction;
import org.apache.spark.sql.SparkSession;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class RecommendationEngine {
	
	public static Map<UUID, Recipe> recipeMap = new HashMap<UUID, Recipe>();
	public static String recipePath = "./src/main/resources/full_format_recipes.json/full_format_recipes.json";
	public static SparkSession spark = null;
	
	public RecommendationEngine() {
		//Creating spark session
		spark = SparkSession
		        .builder()
		        .appName("Recipe Recommendation System")
		        .master("local[2]")
		        .getOrCreate();
	}
	
	public static void run() {
		
		//processing user input
		Scanner user_input = new Scanner(System.in);
		
		//Processing ingredients entered by user
		System.out.println("Enter ingredients:\t");
		String[] ingredients = user_input.nextLine().trim().split("[\\s,;]+");
		
		//Processing rating entered by user
		Double rating = null;
		System.out.println("Enter rating: (Optional)\t");
		String rating_input = user_input.nextLine();
		if(!rating_input.isEmpty()) {
			rating = Double.parseDouble(rating_input.trim());
		}
		
		//Processing protein_content entered by user
		Double protein_content = null;
		System.out.println("Enter protein content: (Optional)\t");
		String protein_input = user_input.nextLine();
		if(!protein_input.isEmpty()) {
			protein_content = Double.parseDouble(protein_input.trim());
		}
		
		//Displaying user options ingredients entered by user
		System.out.println("---------------------------------------------\nYou entered..");
		System.out.println("Ingredients:\t");
		for(int i = 0 ; i < ingredients.length; i++) {
			System.out.print(ingredients[i]+"  ");
		}
		System.out.println("\nRating:\t"+rating);
		System.out.println("Protein Content:\t"+protein_content);
		
		//Loading json recipe dataset
		Dataset<Row> recipe = spark.read().json(recipePath);
		
		//Creating temporary recipe table or view 
		recipe.createOrReplaceTempView("recipe");
		
		//Printing recipe table schema
		recipe.printSchema();
		
		//Filtering recipe dataset based on entered protein content and rating
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
		
		//Removing duplicate entries from the dataset
		recipesFiltered = recipesFiltered.distinct();
		
		//Displaying filtered recipes
		//recipesFiltered.show();
		
		
		//Creating Recipe objects for each row in the recipe dataset
		// Storing recipe objects in HashMap
		recipesFiltered.foreach(new ForeachFunction<Row>() {
			
			private static final long serialVersionUID = 1L;
			
			public void call(Row r) throws Exception {
				String title = r.getString(10);
				String desc = r.getString(3);
				List<String> categories = r.getList(1);
				List<String> ingredients = r.getList(6);
				List<String> directions = r.getList(4);
				
				Recipe rec = new Recipe(title, desc, ingredients, directions, categories);
				recipeMap.put(UUID.randomUUID(), rec);
			}
		});
		
		recipesFiltered.createOrReplaceTempView("recipesFiltered");
		
		Dataset<Row> categories = recipesFiltered.select("categories");
		categories.show();
		
		RecommendationAgent agent = new RecommendationAgent();
		
		List<Recipe> recommendedRecipes = agent.recommend(ingredients, categories, spark);
		
		System.out.println("\n\nOur Agent recommends following recipes. Happy Cooking!! \n\n");
		
		if(!recommendedRecipes.isEmpty()) {
			for(Recipe r: recommendedRecipes) {
				System.out.println(r); //it should print recipe title with link to webpage.
			}
		}
		spark.stop();
	}

}
