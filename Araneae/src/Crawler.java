import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class Crawler {
	LinkedList<URL> queue = new LinkedList<URL>();
	HashMap<URL, Integer> found = new HashMap<URL, Integer>();
	
	public void addToQueue(URL url){
		queue.add(url);
	}

	public void crawl() {
		try {
			//TODO: add randomization 
			//TODO: add exception handling if queue is empty
			Document doc = Jsoup.connect(queue.removeFirst().getPath()).get();
			Elements as = doc.select("a");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
