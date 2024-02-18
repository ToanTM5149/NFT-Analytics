package crawler;

import crawler.blog_news.CrawlerBlogNewsManager;
import crawler.exception.CrawlTimeoutException;
import crawler.exception.InternetConnectionException;
import crawler.marketplace.CrawlerMarketplaceManager;
import crawler.twitter.CrawlerTwitterManager;
import dao.ServiceLocator;
import dao.blog_news.ADAOBlognews;
import dao.marketplace.ADAOMarketplace;
import dao.twitter.ADAOTwitter;

public class CrawlerService implements ICrawlerService{
	private CrawlerMarketplaceManager marketplace;
	private CrawlerTwitterManager twitter;
	private CrawlerBlogNewsManager blogNews;
	private ADAOBlognews DBBlognews;
	private ADAOMarketplace DBMarketplace;
	private ADAOTwitter DBTwitter;

	
	public CrawlerService() {
		marketplace = new CrawlerMarketplaceManager();
		twitter = new CrawlerTwitterManager();
		blogNews = new CrawlerBlogNewsManager();
		DBBlognews = (ADAOBlognews) ServiceLocator.getService("DAOBlognews");
		DBMarketplace = (ADAOMarketplace) ServiceLocator.getService("DAOMarketplace");
		DBTwitter = (ADAOTwitter) ServiceLocator.getService("DAOTwitter");
	}
	
	@Override
	public void crawl() throws CrawlTimeoutException, InternetConnectionException, Exception {
		DBBlognews.clearData();
//		DBMarketplace.clearData();
//		DBTwitter.clearData();
//		marketplace.crawl();
//		twitter.crawlTweetsAboutNFTs();
		blogNews.crawl();
		
	}
}
