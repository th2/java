import java.util.HashMap;
import java.util.Map.Entry;


public class Main {

	public static void main(String[] args) {
		Crawler crawler = new Crawler();
		crawler.addToQueue("http://en.wikipedia.org/wiki/Main_Page");
		for(int i = 0; i<100; i++)
			crawler.crawl();
		
		HashMap<String, Integer> sorted = (HashMap<String, Integer>) SortMap.sortByValue(crawler.found);
		for(Entry<String, Integer> entry : sorted.entrySet()) {
		    String key = entry.getKey();
		    Integer value = entry.getValue();
		    
		    System.out.println(value+" "+key);
		    
		}
	}

}
