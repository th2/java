import java.util.HashMap;
import java.util.LinkedList;


public class Crawler {
	LinkedList<String> queue = new LinkedList<String>();
	HashMap<String, Integer> found = new HashMap<String, Integer>();
	
	public void addToQueue(String url){
		queue.add(url);
	}

	public void crawl() {
		
	}
}
