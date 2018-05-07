import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import scala.Tuple2;

public class RecommendationAgent {

	public List<Recipe> recommend(String[] ingredients) throws IOException {
		
		//Creating a list to store final recipes to recommend
		List<Recipe> recommendations = new ArrayList<Recipe>();
	
		//List to store tuples
		//Each tuple is a pair containing recipe's UUID and count of ingredients common between a recipe and user input  
		List<Tuple2<UUID, Integer>> counts = new ArrayList<Tuple2<UUID, Integer>>();
		
		//Computing counts for each recipe
		for(Entry<UUID, Recipe> e: RecommendationEngine.recipeMap.entrySet()) {
			Integer count = 0;
			List<String> cat = e.getValue().getCategories();
			if(cat != null && !cat.isEmpty()) {
				for (String word : ingredients) {
					if(cat.contains(word)) {
						count += 1;
					}
				}
			}
			counts.add(new Tuple2<UUID, Integer>(e.getKey(), count));
		}
		
		Comparator<Tuple2<UUID, Integer>> comparator = new Comparator<Tuple2<UUID, Integer>>()
	    {
		 	public int compare(Tuple2<UUID, Integer> tuple1, Tuple2<UUID, Integer> tuple2) {
				if(tuple1._2() > tuple2._2()) {
					return -1;
				} else if(tuple1._2() < tuple2._2()) {
					return 1;
				} else {
					return 0;
				}
			}
	    };
	    //Sorting counts list in descending order
	    Collections.sort(counts, comparator);
		
	    //Returning 5 recipes with highest counts
	    for(Tuple2<UUID, Integer> e : counts.subList(0, 5)) {
			recommendations.add(RecommendationEngine.recipeMap.get(e._1()));
		}
	    
		return recommendations;
	}
}
