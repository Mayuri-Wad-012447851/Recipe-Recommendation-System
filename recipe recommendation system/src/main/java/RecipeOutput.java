
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
	        bw.write("<div class=\"main\"");
	        bw.write("Protein:");
	        bw.write(item.getProtein().toString());
	        bw.write("<br>");
	        bw.write("Rating:");
	        bw.write(item.getRating().toString());
	        bw.write("<br>");
	        bw.write("Ingredients:");
	        for(String i: item.getIngredients()) {
            	bw.write(i);
            	bw.write(", ");
            }
	        bw.write("<br>");
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
}