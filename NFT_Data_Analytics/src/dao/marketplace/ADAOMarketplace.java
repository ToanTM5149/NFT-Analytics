package dao.marketplace;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dao.DAOService;
import model.Trending;

public abstract class ADAOMarketplace implements DAOService {
	
	@Override
	public String getName() {
		return "DAOMarketplace";
	}
	
	public abstract List<Trending> getAllTrending() throws FileNotFoundException;
	public abstract Trending getTrending(String marketplace, String chain, String period) throws FileNotFoundException;
	public abstract void saveDataToFile(Trending trending) throws IOException;
	public abstract List<String> getMarketplaceNameList() throws FileNotFoundException;
	public abstract List<String> getChainList(String marketplaceName) throws FileNotFoundException;
	public abstract Map<String, String> getPeriodNameToParam(String marketplaceName) throws FileNotFoundException;
	public abstract void clearData() throws IOException;
}
