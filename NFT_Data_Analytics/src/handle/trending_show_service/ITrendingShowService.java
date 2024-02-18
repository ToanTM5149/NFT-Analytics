package handle.trending_show_service;

import java.util.List;
import model.Trending;

public interface ITrendingShowService {
	Trending getTrending(String marketplaceType, String chain, String period);
	List<String> getMarketplaceList();
	List<String> getChainList(String marketplace);
	List<String> getPeriodList(String marketplace);
}