import java.net.MalformedURLException;
import java.net.URL;


public class Main {

	public static void main(String[] args) {
		Crawler crawler = new Crawler();
		try {
			crawler.addToQueue(new URL("http://en.wikipedia.org/"));
			crawler.crawl();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
