import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map.Entry;


public class Main {

	public static void main(String[] args) {
		Crawler crawler = new Crawler();
		crawler.addToQueue("http://en.wikipedia.org/wiki/Main_Page");
		for(int i = 0; i<100; i++)
			crawler.crawl();
		
		HashMap<String, Integer> sorted = (HashMap<String, Integer>) SortMap.sortByValue(crawler.found);
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("result.txt", true)));
			for(Entry<String, Integer> entry : sorted.entrySet()) {
				out.println(entry.getValue()+" "+entry.getKey());
			}
		    out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	   
	}

}
