package crawler.marketplace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import crawler.exception.CrawlTimeoutException;
import crawler.exception.InternetConnectionException;
import dao.ServiceLocator;
import dao.marketplace.ADAOMarketplace;
import model.Trending;

public class CrawlerMarketplaceManager{	
    private ADAOMarketplace DB;
    private Map<String, Crawler> crawlerMap;

    public CrawlerMarketplaceManager() {
        crawlerMap = new HashMap<>();
        crawlerMap.put("Binance", new Binance());
        crawlerMap.put("OpenSea", new Opensea());
        crawlerMap.put("Nifty Gateway", new Niftygateway());
        crawlerMap.put("Rarible", new Rarible());
        DB = (ADAOMarketplace) ServiceLocator.getService("DAOMarketplace");
    }
    
    public void crawl() throws CrawlTimeoutException, InternetConnectionException, Exception {
        List<String> marketplaceList = DB.getMarketplaceNameList();
        for(String m : marketplaceList) {
            List<String> chains = DB.getChainList(m);
            List<String> periods = new ArrayList<>(DB.getPeriodNameToParam(m).values());
            System.out.println(periods);
            Crawler crawler = crawlerMap.get(m);
            
            for(String chain : chains) 
                for(String period : periods) {
                    try {
                        crawler.setChain(chain);
                        crawler.setPeriod(period);
                        Trending data =  crawler.crawlTrending();
                        DB.saveDataToFile(data);
                    } catch (CrawlTimeoutException e) {
                        System.out.println(m + " " + chain + " " + period + " time out");
                        throw e;
                    }
                }			
        }
    }
}