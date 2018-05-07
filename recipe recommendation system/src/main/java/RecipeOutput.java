
import java.io.*;
import java.util.List;

public class RecipeOutput {

	public void displayWebPage(Recipe item) throws IOException {
		
			String fileName = String.join("", item.getTitle().trim().split(" "));
	        File f = new File("output_"+fileName+".htm");
	        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
	        String title = item.getTitle();
	        List<String> directions = item.getDirections();
	        //Following image file must be there in the path we open the HTML file
	        bw.write("<html>");
	        bw.write("<head>\r\n" + 
	        		"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n" + 
	        		"<style>\r\n" + 
	        		"* {\r\n" + 
	        		"  box-sizing: border-box;\r\n" + 
	        		"}\r\n" + 
	        		".main {\r\n" + 
	        		"  float: left;\r\n" + 
	        		"  width: 60%;\r\n" + 
	        		"  padding: 0 20px;\r\n" + 
	        		"  overflow: hidden;\r\n" + 
	        		"}\r\n" + 
	        		".right {\r\n" + 
	        		"  float: left;\r\n" + 
	        		"  width: 40%;\r\n"  + 
	        		"}\r\n" + 
	        		"\r\n" + 
	        		"</style>\r\n" + 
	        		"</head>");
	        bw.write("<body style=\"font-family:Verdana\"; background=\"background_template.png\">");
	        bw.write("<div style=\"padding-top:20px; padding-right:500px; padding-left:200px;\">");
	        bw.write("<h1 style=\"text-align:center;\">");
	        bw.write(title);
	        bw.write("</h1>");
	        bw.write("</div");
	        
	        bw.write("<div class=\"main\"");
	        
	        bw.write("<br><br>");
	        bw.write("<b>Protein: </b>");
	        bw.write(item.getProtein().toString());
	        bw.write("<br><br>");
	        bw.write("<b>Rating: </b>");
	        bw.write(item.getRating().toString());
	        bw.write("<br><br>");
	        bw.write("<b>Ingredients: </b><br>");
	        for(String i: item.getIngredients()) {
            	bw.write(i);
            	bw.write("<br>");
            }
	        bw.write("<br><br>");
	        bw.write("<b>Directions: </b>");
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
	        System.out.println("Webpage URL:\n"+f.toURI());
	}
}