package crawler.marketplace;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import com.google.gson.JsonObject;

import crawler.exception.CrawlTimeoutException;
import crawler.exception.InternetConnectionException;
import model.Trending;

public abstract class Crawler  {
	protected String marketplaceName;
	protected String period;
	protected String chain;
	protected String respone;
	protected Trending data = new Trending();
	protected Duration timeOut = Duration.ofSeconds(30);
	
	
	protected abstract void getData() throws CrawlTimeoutException, InternetConnectionException, Exception;
	
	protected abstract void preprocessData();
	
	public Trending crawlTrending() throws CrawlTimeoutException, InternetConnectionException, Exception {
		getData();
		preprocessData();			
		return data;
	}
	
	protected static String sendRequest(HttpRequest request) throws CrawlTimeoutException, InternetConnectionException, Exception {
		try {
			HttpResponse<String> res = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			 return res.body();
		} catch (HttpConnectTimeoutException e) {			
			throw new CrawlTimeoutException("Time out", e);
		} catch (IOException e) {
				throw new InternetConnectionException("Check your internet connection", e);				
		} catch (Exception e) {
			throw new Exception("Something went wrong", e);
		}
	}
	
	protected static boolean isGet(JsonObject jObject ,String property) {
		if(!jObject.has(property) || jObject.get(property).isJsonNull()) {			
			return false;
		}
		return true;
	}
	
	public void setPeriod(String period) {
		this.period = period;
	}

	public void setChain(String chain) {
		this.chain = chain;
	}

}
