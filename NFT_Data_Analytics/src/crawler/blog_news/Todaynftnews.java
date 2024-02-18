package crawler.blog_news;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.Duration;

import helper.DateIO;

import java.util.ArrayList;
import java.util.List;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import crawler.exception.CrawlTimeoutException;
import crawler.exception.InternetConnectionException;
import dao.ServiceLocator;
import dao.blog_news.ADAOBlognews;
import model.Article;


public class Todaynftnews implements ICrawlerBlogNews {
	private String baseUrl = "https://www.todaynftnews.com/nft-news/";
	private WebDriver driver;
	private WebDriverWait wait;
	private ADAOBlognews DB;
	
	public Todaynftnews() {
		DB = (ADAOBlognews) ServiceLocator.getService("DAOBlognews");
	}
	
	public void crawl() throws IOException, TimeoutException, Exception {
		List<Article> data = crawlTodayNFTnews();
		DB.saveDataToFile(data);
	}

	private List<Article> crawlTodayNFTnews() throws IOException, TimeoutException, Exception{
		WebDriverManager.chromedriver().setup();
		System.setProperty("webdriver.chrome.driver", ".\\lib\\ChromeDriver\\chromedriver.exe");
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(6));
        driver.manage().window().maximize();
        driver.get(baseUrl);
        List<Article> result = new ArrayList<Article>();
    	try {
    		    for (int morePage = 2; morePage <= 3; morePage++) {
    		    	clickLoadMoreButton();
    		    }

                Document doc = Jsoup.parse(driver.getPageSource());
                Elements articles = doc.select("article");
                for (Element article : articles) {
                    Article currentArticle = new Article();

                    currentArticle.setTitle(article.select("h2[class='title front-view-title']").text());
                    currentArticle.setAbsoluteURL(article.select("a").attr("href"));

                    Document articleDoc = Jsoup.connect(currentArticle.getAbsoluteURL()).get();

                    // Lấy nội dung bài viết
                    String fullContent = articleDoc.select("div.thecontent").text();
                    currentArticle.setFullContent(fullContent);

                    // Lấy tags/keywords
                    Elements tags = articleDoc.select("div[class=tags] a");
                    List<String> tagList = new ArrayList<>();
                    for (Element tagElement : tags) {
                        String tagText = "#" + tagElement.text();
                        tagList.add(tagText);
                    }
                    currentArticle.setTags(tagList);

                    // Lấy ngày xuất bản
                    Element dateElement = articleDoc.select("div.right span.thetime span").first();
                    String publishDateStr = dateElement.text();
                    if (!publishDateStr.isEmpty()) {
                        currentArticle.setPublishDate(DateIO.parseStringToDate(publishDateStr, "MMM dd, yyyy"));
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
        	System.out.println("Crawl https://todaynftnews.com/tags/nft done!!!");
            driver.quit();
        }
    	return result;
	}
	
	private void scrollNearEnd() throws Exception {
	    try {
	        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

	        // Scroll đến vị trí gần cuối trang
	        jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight-1000);");
	        Thread.sleep(2000); // Đợi 2 giây để trang load thêm dữ liệu
	    } catch (Exception e) {
	        throw e;
	    }
	}

	private void clickLoadMoreButton() throws Exception{
	    try {
	        // Đóng popup nếu có
	        closePopup();

	        // Scroll xuống gần cuối trang
	        scrollNearEnd();

	        List<WebElement> loadMoreButtons = driver.findElements(By.cssSelector("div#load-posts a"));
	        if (!loadMoreButtons.isEmpty()) {
	            WebElement loadMoreButton = loadMoreButtons.get(0);
	            // Thực hiện click vào nút Load more posts
	            loadMoreButton.click();

	            waitForPageLoad(); // Đợi một khoảng thời gian để trang load thêm dữ liệu
	        }
	    } catch (Exception e) {
	        throw e;
	    }
	}

	private void closePopup() {
	    try {
	        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".pum-close.popmake-close")));
	        WebElement closeButton = driver.findElement(By.cssSelector(".pum-close.popmake-close"));
	        closeButton.click();
	        System.out.println("Popup đã được đóng.");
	        Thread.sleep(500);
	    } catch (Exception e) {
	        System.out.println("Popup không xuất hiện hoặc đã hết thời gian chờ.");
	    }
	}

	private void waitForPageLoad() {
	    // Sử dụng expected condition để kiểm tra trạng thái của trang
	    wait.until((ExpectedCondition<Boolean>) wd ->
	            ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
	}

}
