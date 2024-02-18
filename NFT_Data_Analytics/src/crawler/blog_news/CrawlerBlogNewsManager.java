package crawler.blog_news;

import crawler.exception.CrawlTimeoutException;
import crawler.exception.InternetConnectionException;

import java.util.Arrays;
import java.util.List;

public class CrawlerBlogNewsManager {
    private List<ICrawlerBlogNews> crawlers;

    public CrawlerBlogNewsManager() {
        this.crawlers = Arrays.asList(new Cryptonews(), new Todaynftnews());
    }

    public void crawl() throws CrawlTimeoutException, InternetConnectionException, Exception  {
        for (ICrawlerBlogNews crawler : crawlers) {
            crawler.crawl();
        }
    }
}