import java.net.MalformedURLException;
import java.net.URL;


public class Main {

	public static void main(String[] args) {
		Crawler crawler = new Crawler();
		crawler.addToQueue("http://en.wikipedia.org/wiki/Main_Page");
		crawler.crawl();
	}

}
