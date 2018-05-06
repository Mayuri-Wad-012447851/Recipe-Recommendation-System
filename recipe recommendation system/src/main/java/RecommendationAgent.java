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
		
		//Creating list for storing final recipes to recommend
		List<Recipe> recommendations = new ArrayList<Recipe>();
	
		//
		List<Tuple2<UUID, Integer>> counts = new ArrayList<Tuple2<UUID, Integer>>();
		
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
	    Collections.sort(counts, comparator);
		
	    RecipeOutput webpageGenerater = new RecipeOutput();
	    for(Tuple2<UUID, Integer> e : counts.subList(0, 5)) {
			webpageGenerater.displayWebPage(RecommendationEngine.recipeMap.get(e._1()));
			System.out.println(e._2());
			recommendations.add(RecommendationEngine.recipeMap.get(e._1()));
		}
	    
		return recommendations;
	}
}
