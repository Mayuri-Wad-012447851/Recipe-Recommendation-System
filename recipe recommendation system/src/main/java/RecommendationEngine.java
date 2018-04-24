import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class RecommendationEngine {
	
	public static final String recipePath = "./src/main/resources/full_format_recipes.json/full_format_recipes.json";
	
	public static void main(String[] args) {
		//JavaSparkContext jsc = new JavaSparkContext("local", "Recommendation Engine");
		//SQLContext sqlContext = new SQLContext(jsc);
		
		
		SparkSession spark = SparkSession
		        .builder()
		        .appName("Recipe Recommendation System")
		        .master("local[2]")
		        .getOrCreate();
		
		/**
		 * Load Recipe data
		 */
		JavaRDD<Row> recipeRDD = spark.read().json(recipePath).toJavaRDD();
		
		System.out.println(recipeRDD.first());
	}

}