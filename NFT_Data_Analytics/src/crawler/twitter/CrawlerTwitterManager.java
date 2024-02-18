package crawler.twitter;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import crawler.exception.InternetConnectionException;
import dao.ServiceLocator;
import dao.twitter.ADAOTwitter;
import model.Tweet;

public class CrawlerTwitterManager {
	
	private ADAOTwitter DB;
	private WebDriver driver; 
	private ActionOnTwitter action;
	
	public CrawlerTwitterManager() {
		System.setProperty("webdriver.chrome.driver", ".\\lib\\ChromeDriver\\chromedriver.exe");	
		DB = (ADAOTwitter) ServiceLocator.getService("DAOTwitter");
	}

	public void loginTwitter() throws InternetConnectionException {
		action.visitWebsite("https://twitter.com/i/flow/login");
		action.enterUsername("fongdo113");
		action.enterPassword("Phong@2003");
	}

	public void crawlTweetsAboutNFTs() throws InternetConnectionException, InterruptedException, Exception {
		int tweetsQuantity = 60;
		driver = new ChromeDriver(); 
		action = new ActionOnTwitter(driver);
		loginTwitter();
		action.searchByTag("(nft OR nfts) (#nft OR #nfts)");
		List<Tweet> tweetList = action.getArrayTweetList(tweetsQuantity);
		DB.saveDataToFile(tweetList);
		action.quitTwitter();

	}
}
