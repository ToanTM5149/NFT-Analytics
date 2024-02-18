package crawler.marketplace;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.TimeoutException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import crawler.exception.InternetConnectionException;
import helper.DateIO;
import model.Collection;

public class Binance extends Crawler {

	public Binance() {
		super.marketplaceName = "Binance";
	}

	public Binance(String chain, String period) {
		super.chain = chain;
		super.period = period;
		super.marketplaceName = "Binance";
	}

	@Override
	protected void getData() throws TimeoutException, InternetConnectionException, Exception {
		String requestBody = "{\"network\":\"" + chain + "\",\"period\":\""
				+ period
				+ "\",\"sortType\":\"volumeDesc\",\"page\":1,\"rows\":100}";
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://www.binance.com/bapi/nft/v1/friendly/nft/ranking/trend-collection"))
				.header("Content-Type", "application/json")
				.header("User-Agent",
						"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36 OPR/103.0.0.0")
				.timeout(timeOut).method("POST", HttpRequest.BodyPublishers.ofString(requestBody)).build();

		respone = sendRequest(request);

	}

	@Override
	protected void preprocessData() {
		JsonArray rowsRaw = JsonParser.parseString(respone).getAsJsonObject().getAsJsonObject("data")
				.getAsJsonArray("rows");

		List<Collection> rows = new ArrayList<Collection>();
		for (JsonElement rowRaw : rowsRaw) {
			JsonObject rowRawObj = rowRaw.getAsJsonObject();
			Collection col = new Collection();
			
			
			col.setId(rowRawObj.get("collectionId").getAsString());
			col.setLogo(rowRawObj.get("coverUrl").getAsString());
			col.setName(rowRawObj.get("title").getAsString());
			col.setVolume(rowRawObj.get("volume").getAsDouble());
			if(isGet(rowRawObj, "volumeRate")) {
				col.setVolumeChange(rowRawObj.get("volumeRate").getAsDouble());				
			} else {
				col.setVolumeChange(0.0);
			}
			
			if(isGet(rowRawObj, "floorPrice")) {
				col.setFloorPrice(rowRawObj.get("floorPrice").getAsDouble());				
			} else {
				col.setFloorPrice(0.0);
			}
			
			if(isGet(rowRawObj, "floorPriceRate")) {
				col.setFloorPriceChange(rowRawObj.get("floorPriceRate").getAsDouble());				
			} else {
				col.setFloorPriceChange(0.0);
			}
			
			if(isGet(rowRawObj, "itemsCount")) {
				col.setItems(rowRawObj.get("itemsCount").getAsInt());				
			} else {
				col.setItems(0);
			}
			
			if(isGet(rowRawObj, "itemsCount")) {
				col.setOwners(rowRawObj.get("itemsCount").getAsInt());				
			} else {
				col.setOwners(0);
			}
			rows.add(col);
		}
		
		data.setMarketplaceName(marketplaceName);
		data.setCreatedAt(DateIO.getCurrentDateAsString());
		data.setChain(chain);
		data.setPeriod(period);
		data.setCurrency(chain);
		data.setData(rows);
	}
}
