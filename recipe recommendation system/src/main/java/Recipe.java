import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String title;
	private String desc;
	private List<String> ingredients;
	private List<String> directions;
	private List<String> categories;
	
	
	public Recipe() {
		super();
	}
	
	public Recipe(String title, String desc, List<String> ingredients, List<String> directions, List<String> categories) {
		super();
		this.title = title;
		this.desc = desc;
		this.ingredients = ingredients;
		this.directions = directions;
		this.categories = categories;
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
	
	@Override
	public String toString() {
		return "Recipe [title=" + title + "\n directions=" + directions + "\n ingredients=" + ingredients + "]";
	}
}
