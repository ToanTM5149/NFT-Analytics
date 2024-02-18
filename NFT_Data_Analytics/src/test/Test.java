package test;

import crawler.CrawlerService;
import crawler.exception.CrawlTimeoutException;
import crawler.exception.InternetConnectionException;

public class Test {
	public static void main(String[] args) throws CrawlTimeoutException, InternetConnectionException, Exception {
		CrawlerService c = new CrawlerService();
		c.crawl();
	}
    
}
