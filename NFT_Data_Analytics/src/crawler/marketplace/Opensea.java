package crawler.marketplace;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import crawler.exception.CrawlTimeoutException;
import crawler.exception.InternetConnectionException;
import helper.DateIO;
import model.Collection;

public class Opensea extends Crawler {

	public Opensea() {
		super.marketplaceName = "OpenSea";
	}

	public Opensea(String chain, String period) {
		super.chain = chain;
		super.period = period;
		super.marketplaceName = "OpenSea";
	}

	@Override
	protected void getData() throws CrawlTimeoutException, InternetConnectionException, Exception {
		System.setProperty("webdriver.chrome.driver", ".\\lib\\ChromeDriver\\chromedriver.exe");
		ChromeOptions opt = new ChromeOptions();
		opt.setPageLoadStrategy(PageLoadStrategy.NONE);
		opt.addArguments("--headless");
		opt.addArguments(
				"--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36");
		WebDriver driver = new ChromeDriver(opt);
		driver.manage().timeouts().pageLoadTimeout(timeOut);
		driver.manage().timeouts().scriptTimeout(timeOut);

		try {
			String url = "https://opensea.io/rankings/trending?chain=" + chain + "&sortBy=" + period + "_volume";
			driver.get(url);
			WebDriverWait wait = new WebDriverWait(driver, timeOut);
			wait.until(ExpectedConditions.attributeContains(By.cssSelector("script[id=__NEXT_DATA__]"), "id",
					"__NEXT_DATA__"));
			respone = driver.findElement(By.cssSelector("script[id=__NEXT_DATA__]")).getAttribute("innerHTML");
//        driver.manage().deleteAllCookies();

		} catch (TimeoutException e) {
			throw new CrawlTimeoutException("Time out");
		} catch (WebDriverException e) {
			throw new InternetConnectionException("Check your connection");
		} catch (Exception e) {
			throw new Exception("Something went wrong");
		} finally {
			driver.quit();
		}

	}

	@Override
	protected void preprocessData() {
		JsonObject recordsRaw = JsonParser.parseString(respone).getAsJsonObject().getAsJsonObject("props")
				.getAsJsonObject("pageProps").getAsJsonObject("initialRecords");

		List<Collection> rows = new ArrayList<Collection>();

		Set<Entry<String, JsonElement>> entrySet = recordsRaw.entrySet();
		Collection curRow = null;

		int index = -2;
		String currency = "";
		for (Map.Entry<String, JsonElement> entry : entrySet) {
			if (index <= 0) {
				index++;
				continue;
			}

			String key = entry.getKey();

			if (key.startsWith("client:root:__RankingsPageTop_rankings_connection"))
				break;

			if (key.startsWith("client:root:topCollectionsByCategory"))
				continue;

			JsonObject e = entry.getValue().getAsJsonObject();

			if (!key.startsWith("client")) {
				curRow = new Collection();
				curRow.setFloorPrice(0);
				curRow.setId(e.get("__id").getAsString());
				curRow.setName(e.get("name").getAsString());
				if(isGet(e, "logo")) {
					curRow.setLogo(e.get("logo").getAsString());					
				} else {
					curRow.setLogo("");
				}
				if(isGet(e, "floorPricePercentChange(statsTimeWindow:\"" + period + "\")")) {
					curRow.setFloorPriceChange(e.get("floorPricePercentChange(statsTimeWindow:\"" + period + "\")").getAsDouble());
				} else {
					curRow.setFloorPrice(0.0);
				}
			}

			if (key.endsWith("\")")) {
				curRow.setOwners(e.get("numOwners").getAsInt());
				curRow.setItems(e.get("totalSupply").getAsInt());
				curRow.setVolumeChange(e.get("volumeChange").getAsDouble());
			}

			if (key.endsWith("floorPrice")) {
				curRow.setFloorPrice(e.get("unit").getAsDouble());

				if (currency.equals("")) {
					currency = e.get("symbol").getAsString();
				}
			}

			if (key.endsWith("volume")) {
				curRow.setVolume(e.get("unit").getAsDouble());
			}

			if (key.endsWith(":edges:" + index) || index == 100) {
				rows.add(curRow);
				index++;
			}

		}
		
		data.setMarketplaceName(marketplaceName);
		data.setCreatedAt(DateIO.getCurrentDateAsString());
		data.setChain(chain);
		data.setPeriod(period);
		data.setCurrency(currency);
		data.setData(rows);
	}
}
