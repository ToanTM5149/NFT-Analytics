package model;

import java.util.List;

public class Trending {
	private String marketplaceName;
	private String createdAt;
	private String chain;
	private String period;
	private String currency;
	private List<Collection> data;
	
	public Trending() {}
	
	public Trending(String marketplaceName, String createdAt, String chain, String period, String currency,
			List<Collection> data) {
		super();
		this.marketplaceName = marketplaceName;
		this.createdAt = createdAt;
		this.chain = chain;
		this.period = period;
		this.currency = currency;
		this.data = data;
	}

	
	public void setMarketplaceName(String marketplaceName) {
		this.marketplaceName = marketplaceName;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public void setChain(String chain) {
		this.chain = chain;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public void setData(List<Collection> data) {
		this.data = data;
	}

	public String getMarketplaceName() {
		return marketplaceName;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public String getChain() {
		return chain;
	}

	public String getPeriod() {
		return period;
	}

	public List<Collection> getData() {
		return data;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String unit) {
		this.currency = unit;
	}
}
