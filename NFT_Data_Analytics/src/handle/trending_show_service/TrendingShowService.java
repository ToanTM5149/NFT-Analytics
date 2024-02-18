package handle.trending_show_service;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import dao.ServiceLocator;
import dao.marketplace.ADAOMarketplace;
import model.Trending;

public class TrendingShowService implements ITrendingShowService {
	
	private ADAOMarketplace DB;
	
	public TrendingShowService() {
		DB = (ADAOMarketplace) ServiceLocator.getService("DAOMarketplace");
	}

	@Override
	public Trending getTrending(String marketplaceType, String chain, String period){
		try {
			return DB.getTrending(marketplaceType, chain, period);
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	@Override
	public List<String> getMarketplaceList() {
		try {
			return DB.getMarketplaceNameList();
		} catch (FileNotFoundException e) {
			return new ArrayList<String>();
		}
		
	}

	@Override
	public List<String> getChainList(String marketplace) {
		try {
			return DB.getChainList(marketplace);
		} catch (FileNotFoundException e) {
			return new ArrayList<String>();			
		}
	}

	@Override
	public List<String> getPeriodList(String marketplace) {
		try {
			return new ArrayList<String>(DB.getPeriodNameToParam(marketplace).keySet());
		} catch (FileNotFoundException e) {
			return new ArrayList<String>();
		}
	}
}
