package handle.correlation_service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dao.ServiceLocator;
import dao.marketplace.ADAOMarketplace;
import model.Collection;
import model.CollectionFilter;
import model.Trending;

public class MarketplaceSearch {
	private ADAOMarketplace DBMarketplace;

	public MarketplaceSearch() {
		DBMarketplace = (ADAOMarketplace) ServiceLocator.getService("DAOMarketplace");
	}
	
	public Set<CollectionFilter> filterCollectionByName(String name) {
		try {
			Set<CollectionFilter> result = new HashSet<CollectionFilter>();
			List<Trending> trendingList = DBMarketplace.getAllTrending();
			for(Trending trend : trendingList) {
				for(Collection col : trend.getData()) {
					if(col.getName().contains(name)) {
						CollectionFilter colFilter = new CollectionFilter(col, trend.getMarketplaceName(), trend.getCurrency(), trend.getChain(), trend.getPeriod());
						result.add(colFilter);
					}
				}
			}
			return result;
		} catch (Exception e) {
			return new HashSet<CollectionFilter>();
		}
		
	}

	

}
