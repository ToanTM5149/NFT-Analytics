package crawler;

import crawler.exception.CrawlTimeoutException;
import crawler.exception.InternetConnectionException;

public interface ICrawlerService {
	void crawl() throws CrawlTimeoutException, InternetConnectionException, Exception;
}
