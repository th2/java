import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;

//imports require jsoup-1.7.3.jar core library from http://jsoup.org/download
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
			URL newLink = queue.removeFirst();
			Document doc = Jsoup.connect(newLink.toString()).get();
			Elements links = doc.select("a[href]");
			for(Element link : links){
				//System.out.println(link.attr("abs:href"));
				URL url = new URL(link.attr("abs:href"));
				if(found.containsKey(url)){
					found.put(url, found.get(url)+1);
					System.out.println(found.get(url)+" "+url);
				}else{
					found.put(url, 1);
					queue.add(url);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
