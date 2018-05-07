import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import org.apache.spark.api.java.function.ForeachFunction;
import org.apache.spark.sql.SparkSession;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

/**
 * RecommendationEngine class represents engine of the recipe recommendation system
 * @author Mayuri Wadkar, Unnathi Bhandary, Nivetha Vijayraju
 *
 */
public class RecommendationEngine {
	
	public static Map<UUID, Recipe> recipeMap = new HashMap<UUID, Recipe>();	// A map to store recipe objects
	public static String recipePath = "./src/main/resources/full_format_recipes.json/full_format_recipes.json";	//path to dataset
	
	/**
	 * Run method accepts user input and displays recommendation results to the user.
	 * It also filters recipes based on rating and protein content
	 * It also wraps every row from recipe dataset into a recipe object
	 * @return 
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException {
		
		//Creating spark session
		SparkSession spark = SparkSession
		        .builder()
		        .appName("Recipe Recommendation System")
		        .master("local[2]")
		        .getOrCreate();
		
		//processing user input
		Scanner user_input = new Scanner(System.in);
		
		//Processing ingredients entered by user
		System.out.println("Enter ingredients:\t");
		String[] ingredients = user_input.nextLine().trim().split("[\\s,;]+");
		
		//Processing rating entered by user
		Double rating = 0.0;
		System.out.println("Enter rating:\t");
		String rating_input = user_input.nextLine();
		if(!rating_input.isEmpty()) {
			rating = Double.parseDouble(rating_input.trim());
		}
		
		//Processing protein_content entered by user
		Double protein_content = 0.0;
		System.out.println("Enter protein content:\t");
		String protein_input = user_input.nextLine();
		if(!protein_input.isEmpty()) {
			protein_content = Double.parseDouble(protein_input.trim());
		}
		
		//Loading json recipe dataset
		Dataset<Row> recipe = spark.read().json(recipePath);
		
		//Creating temporary recipe table or view 
		recipe.createOrReplaceTempView("recipe");
		
		//Filtering recipe dataset based on entered protein content and rating
		Dataset<Row> recipesFiltered = null;
		
		if(!protein_input.isEmpty() || !rating_input.isEmpty()) {
			if(protein_input.isEmpty()) {
				recipesFiltered = spark.sql("SELECT * FROM recipe WHERE rating >= "+rating);
			} else if(rating_input.isEmpty()) {
				recipesFiltered = spark.sql("SELECT * FROM recipe WHERE protein >="+protein_content);
			} else {
				recipesFiltered = spark.sql("SELECT * FROM recipe WHERE protein >="+protein_content+" AND rating >= "+rating);
			}
		} else {
			recipesFiltered = recipe;
		}
		
		//Removing duplicate entries from the dataset
		recipesFiltered = recipesFiltered.distinct();
		
		//Creating Recipe objects for each row in the recipe dataset
		//Storing recipe objects in HashMap
		recipesFiltered.foreach(new ForeachFunction<Row>() {
			
			private static final long serialVersionUID = 1L;
			
			public void call(Row r) throws Exception {
				String title = (r.get(10) == null ? "" : r.getString(10));
				String desc = (r.get(3) == null ? "" : r.getString(3));
				Double protein = (r.get(7) == null ? 0 : r.getDouble(7));
				Double rating = (r.get(8) == null ? 0 : r.getDouble(8));
				List<String> categories = (List<String>) (r.getList(1) == null ? new ArrayList<String>() : r.getList(1));
				List<String> ingredients = (List<String>) (r.getList(6) == null ? new ArrayList<String>() : r.getList(6));
				List<String> directions = (List<String>) (r.getList(4) == null ? new ArrayList<String>() : r.getList(4));
				
				Recipe rec = new Recipe(title, desc, ingredients, directions, categories, rating, protein);
				recipeMap.put(UUID.randomUUID(), rec);
			}
		});
		
		//Creating temporary table or view for filtered recipes
		recipesFiltered.createOrReplaceTempView("recipesFiltered");
		
		// Creating agent instance
		RecommendationAgent agent = new RecommendationAgent();
		
		// Agent's recommend method takes ingredients entered by user and returns a list of 5 recipes
		List<Recipe> recommendedRecipes = agent.recommend(ingredients);
		
		System.out.println("---------------------------------------------------------------------------");
		System.out.println("Our Agent recommends following recipes. Happy Cooking!!");
		System.out.println("---------------------------------------------------------------------------");
		// To generate HTML webpage output
		RecipeOutput webpageGenerater = new RecipeOutput();
		
		// Displaying results to the user on command line
		if(!recommendedRecipes.isEmpty()) {
			for(Recipe r: recommendedRecipes) {
				System.out.println(r);
				webpageGenerater.displayWebPage(r);
				System.out.println("\n-----------------------------------------------------------------------------------");
			}
		}
		
		spark.stop();
		user_input.close();
	}

}
