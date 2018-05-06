import java.awt.Desktop;
import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class RecipeOutput {

	public void displayWebPage(Recipe item) throws IOException {
		
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	        SimpleDateFormat sdf = new SimpleDateFormat("HH.mm.ss");
	        String name = sdf.format(timestamp);
	        //Using timestamp to create unique file names for each recipe
	        File f = new File("output_"+name+".htm");
	        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
	        String title = item.getTitle();
	        List<String> ingredients = item.getIngredients();
	        List<String> directions = item.getDirections();
	        //Following image file must be there in the path we open the HTML file
	        String imgpath = "food1.jpg";
	        bw.write("<html>");
	        bw.write("<head>\r\n" + 
	        		"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n" + 
	        		"<style>\r\n" + 
	        		"* {\r\n" + 
	        		"  box-sizing: border-box;\r\n" + 
	        		"}\r\n" + 
	        		".main {\r\n" + 
	        		"  float: left;\r\n" + 
	        		"  width: 50%;\r\n" + 
	        		"  padding: 0 20px;\r\n" + 
	        		"  overflow: hidden;\r\n" + 
	        		"}\r\n" + 

	        		".dirs {\r\n" + 
	        		"  float: left;\r\n" + 
	        		"  padding: 0 20px;\r\n" + 
	        		"  overflow: hidden;\r\n" + 
	        		"}\r\n" + 
	        		".right {\r\n" + 
	        		"  float: right;\r\n" + 
	        		"  width: 50%;\r\n"  + 
	        		"}\r\n" + 
	        		"\r\n" + 
	        		"</style>\r\n" + 
	        		"</head>");
	        bw.write("<body style=\"font-family:Verdana; background-color:orange;>");
	        bw.write("<div style=\"background-color:#f1f1f1;padding:15px;\">");
	        bw.write("<h1 style=\"text-align:center;\">");
	        bw.write(title);
	        bw.write("</h1>");
	        bw.write("</div");
	        bw.write("<div style=\"overflow:auto\">\r\n" + 
	        		"\r\n" + 
	        		"  <div class=\"right\">");
	        
	        bw.write("<img src=\""+imgpath+"\" style=\"width:100%\">");
	        bw.write("</div>");
	        bw.write("<div class=\"main\">");
	        
	        bw.write("<h1>");
	        bw.write("Ingredients");
	        bw.write("</h1>");
	        
	        //Writes Ingredients 
	        bw.write("<p>");
	        for(String ingredient: ingredients) {
            	bw.write(ingredient);
                bw.write("<br>");
            }
	        bw.write("</p>");
	        
	      //Writes Directions 
	        bw.write("<h1>");
	        bw.write("Directions");
	        bw.write("</h1>");
	        
            for(String direction: directions) {
            	bw.write("<p>");
            	bw.write(direction);
            	bw.write("</p>");
            }
            
            bw.write("</div>");
            bw.write("</div>");
	        bw.write("</body>");
	        bw.write("</html>");
	        bw.close();

	        //Desktop.getDesktop().browse(f.toURI());
	        System.out.println(title);
	        System.out.println(f.toURI());
	}
	
    public static void main(String[] args) throws Exception {
        RecipeOutput display = new RecipeOutput();
        Recipe test_val = new Recipe("Lentil, Apple, and Turkey Wrap ", null, 
        							Arrays.asList("4 cups low-sodium vegetable or chicken stock", "1 cup dried brown lentils", 
        							 "1/2 cup dried French green lentils", "2 stalks celery, chopped", "1 large carrot, peeled and chopped",
        							 "1 sprig fresh thyme", "1 teaspoon kosher salt", "1 medium tomato, cored, seeded, and diced", 
        							 "1 small Fuji apple, cored and diced", "1 tablespoon freshly squeezed lemon juice",
        							 "2 teaspoons extra-virgin olive oil", "Freshly ground black pepper to taste", 
        							 "3 sheets whole-wheat lavash, cut in half crosswise, or 6 (12-inch) flour tortillas", 
        							 "3/4 pound turkey breast, thinly sliced", "1/2 head Bibb lettuce"), Arrays.asList("1. Place the stock,"
        							 	+ " lentils, celery, carrot, thyme, and salt in a medium saucepan and bring to a boil."
        							 	+ " Reduce heat to low and simmer until the lentils are tender, about 30 minutes, depending on the lentils."
        								+ " (If they begin to dry out, add water as needed.) Remove and discard the thyme. Drain and transfer the"
        						 		+ " mixture to a bowl; let cool." , "2. Fold in the tomato, apple, lemon juice, and olive oil. Season with"
        								+ " the pepper.", "3. To assemble a wrap, place 1 lavash sheet on a clean work surface. Spread some"
        		 						+ " of the lentil mixture on the end nearest you, leaving a 1-inch border. Top with several "
        		 						+ "slices of turkey, then some of the lettuce. Roll up the lavash, slice crosswise, and serve."
        		 						+ " If using tortillas, spread the lentils in the center, top with the turkey and lettuce, and "
        		 						+ "fold up the bottom, left side, and right side before rolling away from you."), Arrays.asList("Sandwich", 
						 				"Bean", "Fruit", "Tomato", "turkey", "Vegetable", "Kid-Friendly", "Apple", "Lentil", "Lettuce", "Cookie"));
        display.displayWebPage(test_val);
    }
}