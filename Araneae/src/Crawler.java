import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;

import java.util.Random;

//imports require jsoup-1.7.3.jar core library from http://jsoup.org/download
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Crawler {
	boolean deterministic;
	LinkedList<String> queue = new LinkedList<String>();
	HashMap<String, Integer> found = new HashMap<String, Integer>();
	HashMap<String, Integer> numlinks = new HashMap<String, Integer>();
	
	Crawler(){
		deterministic = false;
	}
	
	Crawler(boolean deterministic){
		this.deterministic = deterministic;
	}
	
	public void addToQueue(String url){
		queue.add(url);
	}
	
	private String cleanup(String url){
		//TODO: add more canonization
		String result = url.split("#")[0];
		return result;
	}
	
	private String getNext() {
		String newLink = "";
		if(deterministic)
			newLink = queue.removeFirst();
		else {
			int pos = (new Random()).nextInt(queue.size());
			newLink = queue.remove(pos);
		}
		if(newLink.startsWith("http"))
			return newLink;
		else
			return getNext();
	}
	

	public void crawl() {
		try {
			String newLink = getNext();
				
			System.out.println("new:"+newLink);
			Document doc = Jsoup.connect(newLink).get();
			Elements links = doc.select("a[href]");
			numlinks.put(newLink, links.size());
			for(Element link : links){
				//System.out.println(link.attr("abs:href"));
				String url = cleanup(link.attr("abs:href"));
				if(!url.equals(newLink)){
					if(found.containsKey(url)){
						found.put(url, found.get(url)+1);
						//System.out.println(found.get(url)+" "+url);
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
