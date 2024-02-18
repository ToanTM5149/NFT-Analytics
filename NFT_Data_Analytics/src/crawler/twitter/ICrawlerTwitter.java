package crawler.twitter;
import crawler.exception.InternetConnectionException;

public interface ICrawlerTwitter {

	void crawlTweetsAboutNFTs() throws InternetConnectionException, InterruptedException, Exception;

}
