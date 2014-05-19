import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map.Entry;


public class Main {

	public static void main(String[] args) {
		if(args.length != 5)
			System.out.println("parameters: [START URL] [NUMBER OF PAGES] [RANDOM] [OUT FILE FOUND] [OUT FILE NUMLINKS]");
		else{
			int numPages = Integer.parseInt(args[1]);
			Crawler crawler = new Crawler(Boolean.parseBoolean(args[2]));
			crawler.addToQueue(args[0]);
			for(int i = 0; i<numPages; i++){
				crawler.crawl();
				System.out.println("crawled "+i+" pages");
			}

			HashMap<String, Integer> sortedFound = (HashMap<String, Integer>) SortMap.sortByValue(crawler.found);
			HashMap<String, Integer> sortedNumlinks = (HashMap<String, Integer>) SortMap.sortByValue(crawler.numlinks);
			try {
				PrintWriter outFound = new PrintWriter(new BufferedWriter(new FileWriter(args[3], true)));
				for(Entry<String, Integer> entry : sortedFound.entrySet()) {
					outFound.println(entry.getValue()+" "+entry.getKey());
				}
				outFound.close();
			    
				PrintWriter outNumlinks = new PrintWriter(new BufferedWriter(new FileWriter(args[4], true)));
				for(Entry<String, Integer> entry : sortedNumlinks.entrySet()) {
					outNumlinks.println(entry.getValue()+" "+entry.getKey());
				}
				outNumlinks.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
