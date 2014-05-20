import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Map.Entry;


//imports require jsoup-1.7.3.jar core library from http://jsoup.org/download
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Crawler {
	boolean deterministic;
	static boolean savePages;
	static ArrayList<String> saveLog = new ArrayList<String>();
	int saveCounter = 0;
	LinkedList<String> queue = new LinkedList<String>();
	HashMap<String, Integer> found = new HashMap<String, Integer>();
	HashMap<String, Integer> numlinks = new HashMap<String, Integer>();
	
	Crawler(){
		deterministic = false;
		savePages = false;
	}
	
	Crawler(boolean deterministic, boolean savePages){
		this.deterministic = deterministic;
		this.savePages = savePages;
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
			
			if(savePages){
				saveLog.add(newLink);
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter((saveCounter++)+".html", true)));
				out.println(doc.toString());
				out.close();
			}
			
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
	
	public static void main(String[] args) {
		if(args.length != 6)
			System.out.println("parameters: "
					+ "[START URL] "
					+ "[NUMBER OF PAGES] "
					+ "[DETERMINISTIC] "
					+ "[SAVE PAGES] "
					+ "[OUT FILE FOUND] "
					+ "[OUT FILE NUMLINKS] "
					+ System.lineSeparator()
					+ "example: java -cp ../jsoup-1.7.3.jar:. Crawler http://en.wikipedia.org/ 1500 true false found.txt numlinks.txt");
		else{
			int numPages = Integer.parseInt(args[1]);
			Crawler crawler = new Crawler(Boolean.parseBoolean(args[2]), Boolean.parseBoolean(args[3]));
			crawler.addToQueue(args[0]);
			for(int i = 0; i<numPages; i++){
				crawler.crawl();
				System.out.println("crawled "+i+" pages");
			}

			try {
				HashMap<String, Integer> sortedFound = (HashMap<String, Integer>) SortMap.sortByValue(crawler.found);
				PrintWriter outFound = new PrintWriter(new BufferedWriter(new FileWriter(args[4], true)));
				for(Entry<String, Integer> entry : sortedFound.entrySet()) {
					outFound.println(entry.getValue()+" "+entry.getKey());
				}
				outFound.close();

				HashMap<String, Integer> sortedNumlinks = (HashMap<String, Integer>) SortMap.sortByValue(crawler.numlinks);
				PrintWriter outNumlinks = new PrintWriter(new BufferedWriter(new FileWriter(args[5], true)));
				for(Entry<String, Integer> entry : sortedNumlinks.entrySet()) {
					outNumlinks.println(entry.getValue()+" "+entry.getKey());
				}
				outNumlinks.close();
				
				if(savePages) {
					PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("saveLog.txt", true)));
					for(String entry : saveLog) {
						out.println(entry);
					}
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
