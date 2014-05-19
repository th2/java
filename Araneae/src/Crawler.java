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
	LinkedList<String> queue = new LinkedList<String>();
	HashMap<String, Integer> found = new HashMap<String, Integer>();
	
	public void addToQueue(String url){
		queue.add(url);
	}
	
	public String cleanup(String url){
		return url.split("#")[0];
	}
	

	public void crawl() {
		try {
			//TODO: add randomization 
			//TODO: add exception handling if queue is empty
			String newLink = queue.removeFirst();
			Document doc = Jsoup.connect(newLink).get();
			Elements links = doc.select("a[href]");
			for(Element link : links){
				//System.out.println(link.attr("abs:href"));
				String url = cleanup(link.attr("abs:href"));
				if(!url.equals(newLink)){
					if(found.containsKey(url)){
						found.put(url, found.get(url)+1);
						System.out.println(found.get(url)+" "+url);
					}else{
						found.put(url, 1);
						queue.add(url);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
