import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;

public class RecommendationEngine {
	
	public static final String recipePath = "./src/main/resources/full_format_recipes.json/full_format_recipes.json";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JavaSparkContext jsc = new JavaSparkContext("local", "Recommendation Engine");
		SQLContext sqlContext = new SQLContext(jsc);
	
		
		/**
		 * Load Recipe data
		 */
		 
		
	}

}
