package crawler.marketplace;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import crawler.exception.CrawlTimeoutException;
import crawler.exception.InternetConnectionException;
import helper.DateIO;
import model.Collection;

public class Niftygateway extends Crawler {

	public Niftygateway() {
		super.marketplaceName = "Nifty Gateway";
	}

	public Niftygateway(String chain, String period) {
		super.period = period;
		super.chain = chain;
		super.marketplaceName = "Nifty Gateway";
	}

	@Override
	protected void getData() throws CrawlTimeoutException, InternetConnectionException, Exception {
		String api = "https://api.niftygateway.com/stats/rankings/?page=1&page_size=100&sort=-" + period;
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(api)).header("Content-Type", "application/json")
				.header("User-Agent",
						"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36 OPR/103.0.0.0")
				.timeout(timeOut).method("GET", HttpRequest.BodyPublishers.noBody()).build();

		respone = sendRequest(request);

	}

	@Override
	protected void preprocessData() {

		double rate = chain.equals("USD") ? 100 : 0.5 * 100;

		JsonArray rowsRaw = JsonParser.parseString(respone).getAsJsonObject().getAsJsonArray("results");

		List<Collection> rows = new ArrayList<Collection>();

		for (JsonElement rowRaw : rowsRaw) {
			JsonObject rowRawObj = rowRaw.getAsJsonObject();
			Collection col = new Collection();
			
			col.setId(rowRawObj.getAsJsonObject("collection").get("niftyTitle").getAsString());
			col.setLogo(rowRawObj.getAsJsonObject("collection").get("niftyDisplayImage").getAsString());
			col.setName(rowRawObj.getAsJsonObject("collection").get("niftyTitle").getAsString());
			col.setVolume(isGet(rowRawObj, period + "TotalVolume")
					? convertValueByNetworkRate(rowRawObj.get(period + "TotalVolume"), rate).getAsDouble()
					: 0.0);
			col.setVolumeChange(isGet(rowRawObj, period + "Change") ? convertValueByNetworkRate(rowRawObj.get(period + "Change"), 1).getAsDouble()
					: 0.0);
			col.setFloorPrice(isGet(rowRawObj, "floorPrice") ? convertValueByNetworkRate(rowRawObj.get("floorPrice"), rate).getAsDouble()
					: 0.0);
			col.setFloorPriceChange(0.0);
			col.setItems(isGet(rowRawObj, "itemsCount") ? rowRawObj.get("itemsCount").getAsInt() : 0);
			col.setOwners(isGet(rowRawObj, "itemsCount") ? rowRawObj.get("itemsCount").getAsInt() : 0);
			rows.add(col);
		}

		data.setMarketplaceName(marketplaceName);
		data.setCreatedAt(DateIO.getCurrentDateAsString());
		data.setChain(chain);
		data.setPeriod(period);
		data.setCurrency(chain);
		data.setData(rows);
	}

	private JsonElement convertValueByNetworkRate(JsonElement jElement, double rate) {
		return new JsonPrimitive(jElement.getAsFloat() / rate);
	}
}