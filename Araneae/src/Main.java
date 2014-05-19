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
		for(int i = 0; i<1500; i++){
			crawler.crawl();
			System.out.println("crawled "+i+" pages");
		}

		HashMap<String, Integer> sortedFound = (HashMap<String, Integer>) SortMap.sortByValue(crawler.found);
		HashMap<String, Integer> sortedNumlinks = (HashMap<String, Integer>) SortMap.sortByValue(crawler.numlinks);
		try {
			PrintWriter outFound = new PrintWriter(new BufferedWriter(new FileWriter("found.txt", true)));
			for(Entry<String, Integer> entry : sortedFound.entrySet()) {
				outFound.println(entry.getValue()+" "+entry.getKey());
			}
			outFound.close();
		    
			PrintWriter outNumlinks = new PrintWriter(new BufferedWriter(new FileWriter("numlinks.txt", true)));
			for(Entry<String, Integer> entry : sortedNumlinks.entrySet()) {
				outNumlinks.println(entry.getValue()+" "+entry.getKey());
			}
			outNumlinks.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	   
	}

}
