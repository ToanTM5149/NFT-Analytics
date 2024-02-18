package dao.marketplace;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import com.google.gson.reflect.TypeToken;

import helper.JsonIO;
import model.Collection;
import model.Trending;

public class DAOMarketplace extends ADAOMarketplace {
	
	private static final String PATHSAVEDATA = ".\\data\\marketplace";
	private static final String METADATA =  ".\\data\\metadata.json";
    
	@Override
	public List<Trending> getAllTrending() throws FileNotFoundException{
		File filesList[] = new File(PATHSAVEDATA).listFiles();
		Type typeOfTrending = new TypeToken<Trending>() {}.getType();
		List<Trending> result = new ArrayList<Trending>(); 
		for(File f : filesList) {
			result.add(JsonIO.getInstance().readFromJson(f.getPath(), typeOfTrending));
		}
		return result;
	}

	@Override
	public Trending getTrending(String marketplaceName, String chain, String period) throws FileNotFoundException{
		period = getPeriodNameToParam(marketplaceName).get(period);
		String path = getFileSaveData(marketplaceName, chain, period);
		Type typeOfTrending = new TypeToken<Trending>() {}.getType();
		Trending result = null;
		result = JsonIO.getInstance().readFromJson(path, typeOfTrending);			
		return result;
	}

	@Override
	public void saveDataToFile(Trending trending) throws IOException {
		String marketplaceName = trending.getMarketplaceName();
		String chain = trending.getChain();
		String period = trending.getPeriod();
		String path = getFileSaveData(marketplaceName, chain, period);
		JsonIO.getInstance().writeToJson(trending, path);
	}
	
	@Override
	public void clearData() throws IOException{
		FileUtils.cleanDirectory(new File(PATHSAVEDATA));

	}
	
	private static String getFileSaveData(String marketplaceName, String chain, String period) {
		return PATHSAVEDATA + "\\" + marketplaceName + "_" + period + "_" + chain + ".json";
	}

	@Override
	public List<String> getMarketplaceNameList() throws FileNotFoundException {
	    Type typeOfMetadata = new TypeToken<List<Map<String, Object>>>() {}.getType();
	    List<Map<String, Object>> metadata;
	    metadata = JsonIO.getInstance().readFromJson(METADATA, typeOfMetadata);
	    return metadata.stream().map(m -> (String) m.get("name")).collect(Collectors.toList());    
	}

	@Override
	public List<String> getChainList(String marketplaceName) throws FileNotFoundException {
	    Type typeOfMetadata = new TypeToken<List<Map<String, Object>>>() {}.getType();
	    List<Map<String, Object>> metadata;
	    metadata = JsonIO.getInstance().readFromJson(METADATA, typeOfMetadata);
	    return metadata.stream()
	       		.filter(m -> m.get("name").equals(marketplaceName))
	       		.findFirst()
	       		.map(m -> (List<String>) m.get("chainList"))
	       		.orElse(new ArrayList<String>());
	}

	@Override
	public Map<String, String> getPeriodNameToParam(String marketplaceName) throws FileNotFoundException {
	    Type typeOfMetadata = new TypeToken<List<Map<String, Object>>>() {}.getType();
	    List<Map<String, Object>> metadata;
	    metadata = JsonIO.getInstance().readFromJson(METADATA, typeOfMetadata);
	    return metadata.stream()
	            .filter(m -> m.get("name").equals(marketplaceName))
	            .findFirst()
	            .map(m -> ((List<Map<String, String>>) m.get("periodNameToParam")).stream().collect(Collectors.toMap(p -> p.get("name"), p -> p.get("param"))))
	            .orElse(null);
	}
}	