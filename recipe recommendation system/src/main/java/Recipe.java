import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String title;
	private String desc;
	private List<String> ingredients;
	private List<String> directions;
	private List<String> categories;
	private List<Integer> termFrequencyVector;
	private Double rating;
	private Double protein;
	
	
	public List<Integer> getTermFrequencyVector() {
		return termFrequencyVector;
	}

	public void setTermFrequencyVector(List<Integer> termFrequencyVector) {
		this.termFrequencyVector = termFrequencyVector;
	}

	public Recipe() {
		super();
	}
	
	public Recipe(String title, String desc, List<String> ingredients, List<String> directions, List<String> categories, Double rating, Double protein) {
		super();
		this.title = title;
		this.desc = desc;
		this.ingredients = ingredients;
		this.directions = directions;
		this.categories = categories;
		this.termFrequencyVector = null;
		this.protein = protein;
		this.rating = rating;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<String> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<String> ingredients) {
		this.ingredients = ingredients;
	}

	public List<String> getDirections() {
		return directions;
	}

	public void setDirections(List<String> directions) {
		this.directions = directions;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	
	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public Double getProtein() {
		return protein;
	}

	public void setProtein(Double protein) {
		this.protein = protein;
	}

	@Override
	public String toString() {
		
		System.out.println("Title:\t"+this.title);
		System.out.println("Rating:\t"+this.rating);
		System.out.println("Protein Content:\t"+this.protein);
		System.out.println("Ingredients:");
		 System.out.print("\n[");
			for(String k : this.categories) {
				System.out.print(k+", ");
			}
			System.out.print("]\n");
		return "";
	}
}
