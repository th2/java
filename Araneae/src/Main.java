
public class Main {

	public static void main(String[] args) {
		Crawler crawler = new Crawler();
		crawler.addToQueue("http://en.wikipedia.org/");
		crawler.crawl();
	}

}
