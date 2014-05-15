import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;


public class Crawler {
	LinkedList<URL> queue = new LinkedList<URL>();
	HashMap<URL, Integer> found = new HashMap<URL, Integer>();
	
	public void addToQueue(URL url){
		queue.add(url);
	}
	
	public String getPage(URL url){
		try {
			String line;
			StringBuilder page = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			while ((line = reader.readLine()) != null) {
				page.append(line);
	        }
			return page.toString();			
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	public void crawl() {
		//TODO: add randomization 
		//TODO: add exception handling if queue is empty
		String page = getPage(queue.removeFirst());
	}
}
