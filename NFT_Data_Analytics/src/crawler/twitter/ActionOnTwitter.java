package crawler.twitter;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import crawler.exception.InternetConnectionException;
import helper.DateIO;
import model.Tweet;

public class ActionOnTwitter {

	private WebDriver driver;
	
	public ActionOnTwitter(WebDriver driver) {
		this.driver = driver;
	}

	public void visitWebsite(String pathToWebsite) throws InternetConnectionException {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.navigate().to(pathToWebsite);
	}

	public void enterUsername(String usename) {
		driver.findElement(By.xpath("//input[@autocomplete='username']")).sendKeys(usename);
		driver.findElement(By.xpath("//input[@autocomplete='username']/following::div[@role='button']")).click();
	}

	public void enterPassword(String password) {
		driver.findElement(By.xpath("//input[@name='password']")).sendKeys(password);
		driver.findElement(By.xpath("//div[@data-testid='LoginForm_Login_Button']")).click();
	}

	public void scrollDown() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
	}

	public void searchByTag(String tag) throws InterruptedException {

		driver.findElement(By.xpath("//a[@data-testid='AppTabBar_Explore_Link']")).click();
		WebElement search = driver.findElement(By.xpath("//input[@data-testid='SearchBox_Search_Input']"));
		search.sendKeys(tag);
		search.sendKeys(Keys.ENTER);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//div[@data-testid='cellInnerDiv']//article[@data-testid='tweet']")));
		
	}

	public List<Tweet> getArrayTweetList(int tweetsQuantity) throws InterruptedException {
		List<WebElement> posts = new ArrayList<WebElement>();
		
		List<Tweet> tweetList = new ArrayList<Tweet>();
		
		while (posts.size() < tweetsQuantity) {
			
			scrollDown();
			Thread.sleep(1000);
			List<WebElement> newPosts = driver.findElements(By
					.xpath("//div[@data-testid='cellInnerDiv']//article[@data-testid='tweet']//time/ancestor::article"));
			
			for (WebElement post : newPosts) {
				if (!posts.contains(post)) {
					posts.add(post);
					Tweet convertTweet = takeTweet(post);
					tweetList.add(convertTweet);
				}
			}
		}
		return tweetList;
	}

	public static String getAuthor(WebElement tweet) {
		return tweet.findElement(By.xpath(".//div[@data-testid='User-Name']//a")).getText();
	}

	public static String getContent(WebElement tweet) {
		return tweet.findElement(By.xpath(".//div[@data-testid='tweetText']")).getText();
	}

	public static List<String> getTags(WebElement tweet) {
		List<WebElement> crawlTags = tweet.findElements(By.partialLinkText("#"));
		List<String> tags = crawlTags.stream()
				.map(WebElement::getText)
				.collect(Collectors.toList());
		if (tags.size() == 0) tags.add("#nfts");
		return tags;
	}

	public static String getImageURL(WebElement tweet) {
		List<WebElement> images = tweet.findElements(By.xpath(".//div[@data-testid='tweetPhoto']//img"));
		if (!images.isEmpty()) return images.get(0).getAttribute("src");
		return "";
	}

	public static LocalDate getTimePost(WebElement tweet) {
		WebElement timePost = tweet.findElement(By.xpath(".//div[@data-testid='User-Name']//time"));
		String dateString = timePost.getAttribute("datetime");
		LocalDate localDate = DateIO.parseStringToDate(dateString, "yyyy-MM-dd'T'HH:mm:ss.SSSX");
		
		return localDate;
	}


	public static Tweet takeTweet(WebElement tweet) {
		String author = getAuthor(tweet);
		List<String> tags = getTags(tweet);
		String content = getContent(tweet);
		String imageURL = getImageURL(tweet);
		LocalDate date = getTimePost(tweet);

		return (new Tweet(author, content, tags, imageURL, date));
	}

	public void quitTwitter() {
		driver.quit();
	}
}
