package crawler.marketplace;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import crawler.exception.CrawlTimeoutException;
import crawler.exception.InternetConnectionException;
import helper.DateIO;
import model.Collection;

public class Rarible extends Crawler {

	public Rarible() {
		super.marketplaceName = "Rarible";
	}

	public Rarible(String chain, String period) {
		super.chain = chain;
		super.period = period;
		super.marketplaceName = "Rarible";
	}

	@Override
	protected void getData() throws CrawlTimeoutException, InternetConnectionException, Exception {
		String requestBody = "{\"size\":100 ,\"filter\":{\"verifiedOnly\":false,\"sort\":\"VOLUME_DESC\",\"blockchains\":[\""
				+ chain + "\"],\"showInRanking\":false,\"period\":\""
				+ period + "\",\"hasCommunityMarketplace\":false,\"currency\":\"NATIVE\"}}";
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://rarible.com/marketplace/api/v4/collections/search"))
				.header("Content-Type", "application/json")
				.header("User-Agent",
						"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36 OPR/103.0.0.0")
				.timeout(timeOut).method("POST", HttpRequest.BodyPublishers.ofString(requestBody)).build();

		respone = sendRequest(request);
	}

	@Override
	protected void preprocessData() {
		JsonArray rowsRaw = JsonParser.parseString(respone).getAsJsonArray();

		List<Collection> rows = new ArrayList<Collection>();
		String currency = "";
		for (JsonElement rowRaw : rowsRaw) {
			JsonObject rowRawObj = rowRaw.getAsJsonObject();
			Collection col = new Collection();
			col.setId(rowRawObj.get("id").getAsString());
			col.setName(rowRawObj.get("name").getAsString());

			String logo = "";
			JsonObject rowRawObjCollection = rowRawObj.getAsJsonObject("collection");
			if (isGet(rowRawObjCollection, "pic")) {
				String pic = rowRawObjCollection.get("pic").getAsString();
				logo = pic.contains("ipfs") ? "" : pic;
			}
			if (rowRawObjCollection.getAsJsonArray("imageMedia").size() >= 1) {
				logo = rowRawObjCollection.getAsJsonArray("imageMedia").get(1).getAsJsonObject().get("url")
						.getAsString();
			}

			col.setLogo(logo);
			JsonObject rowRawObjStatistics = rowRawObj.getAsJsonObject("statistics");
			col.setFloorPrice(
					isGet(rowRawObjStatistics, "floorPrice")
							? rowRawObjStatistics.getAsJsonObject("floorPrice").get("value").getAsDouble()
							: 0.0);
			col.setFloorPriceChange(
					isGet(rowRawObjStatistics, "floorPrice") && isGet(rowRawObjStatistics.getAsJsonObject("floorPrice"), "changePercent")
							? rowRawObjStatistics.getAsJsonObject("floorPrice").get("changePercent").getAsDouble()
							: 0.0);
			col.setVolume(
					isGet(rowRawObjStatistics, "amount") ? rowRawObjStatistics.getAsJsonObject("amount").get("value").getAsDouble()
							: 0.0);
			col.setVolumeChange(
					isGet(rowRawObjStatistics, "usdAmount") && isGet(rowRawObjStatistics.getAsJsonObject("usdAmount"), "changePercent")
							? rowRawObjStatistics.getAsJsonObject("usdAmount").get("changePercent").getAsDouble()
							: 0.0);
			col.setOwners(isGet(rowRawObjStatistics, "owners") ? rowRawObjStatistics.get("owners").getAsInt() : 0);
			col.setItems(isGet(rowRawObjStatistics, "items") ? rowRawObjStatistics.get("items").getAsInt() : 0);

			if (currency.equals("")) {
				currency = isGet(rowRawObjStatistics, "floorPrice")
						? rowRawObjStatistics.getAsJsonObject("floorPrice").get("currency").getAsString()
						: "";
			}
			rows.add(col);
		}

		data.setMarketplaceName(marketplaceName);
		data.setCreatedAt(DateIO.getCurrentDateAsString());
		data.setChain(chain);
		data.setPeriod(period);
		data.setCurrency("");
		data.setData(rows);

	}
}