package crawler.blog_news;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import io.github.bonigarcia.wdm.WebDriverManager;
import model.Article;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import crawler.exception.CrawlTimeoutException;
import crawler.exception.InternetConnectionException;
import dao.ServiceLocator;
import dao.blog_news.ADAOBlognews;
import helper.DateIO;

public class Cryptonews implements ICrawlerBlogNews{
    	private String baseUrl = "https://crypto.news/tag/nft/";
    	private WebDriver driver;
    	private ADAOBlognews DB;
    	
    	public Cryptonews() {
    		DB = (ADAOBlognews) ServiceLocator.getService("DAOBlognews");
    	}
    	
    	public void crawl() throws CrawlTimeoutException, InternetConnectionException, Exception {
    	    List<Article> data = crawlCryptoNews(); 	
    	    DB.saveDataToFile(data);
    	}

    	private List<Article> crawlCryptoNews() throws CrawlTimeoutException, InternetConnectionException, Exception {
    		WebDriverManager.chromedriver().setup();
    		System.setProperty("webdriver.chrome.driver", ".\\lib\\ChromeDriver\\chromedriver.exe");
    		ChromeOptions opt = new ChromeOptions();
			opt.setPageLoadStrategy(PageLoadStrategy.EAGER);
//			opt.addArguments("--headless");
			opt.addArguments("--remote-allow-origins=*");
			driver = new ChromeDriver(opt);
	        driver.get(baseUrl);
	        for (int i = 1; i <= 3; i++) {
            	((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            	((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -1000);");
           		Thread.sleep(2000); // Tạm dừng để đợi dữ liệu mới tải
            }
	        
	        List<Article> result = new ArrayList<Article>();
	    	try {
	            Document doc = Jsoup.parse(driver.getPageSource());
	            Elements articles = doc.select("div.post-loop.post-loop--style-horizontal.post-loop--category-news");
	            
	            for (Element article : articles) {
	                    Article currentArticle = new Article();	                    
	                    Elements titleElements = article.select("a"); 
	                    boolean isRelated = titleElements.parents().stream().anyMatch(parent ->
	                    parent.hasClass("related-posts__inner"));
	
	                    if (!isRelated) {
	                        String title = titleElements.text().replace("Read more -", "").trim();
	                        if (result.stream().anyMatch(existingArticle -> existingArticle.getTitle().equals(title))) {
	                            System.out.println("Article already exists: " + title);
	                            continue; // Skip further processing for this article
	                        }else {
	                        	currentArticle.setTitle(title);
	                        }
	                    }else {
	                    	continue;
	                    }
	                    
	                    currentArticle.setAbsoluteURL(article.select("a").attr("href"));
	                    Document articleDoc = Jsoup.connect(currentArticle.getAbsoluteURL()).get();
	
	                    // Lấy nội dung bài viết
	                    Elements content = articleDoc.select("div[class='post-detail__content blocks']");
	                    String fullContent = content.text();
	                    currentArticle.setFullContent(fullContent);
	
	                    // Lấy tags/keywords
	                    Elements tags = articleDoc.select("div[class=post-detail__tags] div[class=tags] div[class=tags__list] a");
	                    List<String> tagList = new ArrayList<>();
	                    for (Element tagElement : tags) {
	                        String tagText = "#" + tagElement.text();
	                        tagList.add(tagText);
	                    }
	                    currentArticle.setTags(tagList);
	
	                    // Lấy ngày xuất bản
	                    String publishDateStr = articleDoc.select("div[class=post-detail__meta] time").attr("datetime");
	                    if (!publishDateStr.isEmpty()) {
	                        currentArticle.setPublishDate(DateIO.parseStringToDate(publishDateStr, "yyyy-MM-dd'T'HH:mm:ssXXX"));
	                    }
	
	                    result.add(currentArticle);
	
	                    System.out.println("Processed article: " + currentArticle.getTitle());
	            }
	                	                
	    	} catch (TimeoutException e) {
				throw new CrawlTimeoutException("Time out");
			} catch (WebDriverException e) {
				throw new InternetConnectionException("Check your connection");
			} catch (Exception e) {
				throw new Exception("Something went wrong");
			} finally {
				System.out.println("Crawl https://crypto.news/tag/nft done!!!");
				driver.quit();
			}
	    	return result;
    	}
}
