package crawler.blog_news;

import java.io.IOException;

import org.openqa.selenium.TimeoutException;

public interface ICrawlerBlogNews {
	void crawl() throws IOException, TimeoutException, Exception;
}
