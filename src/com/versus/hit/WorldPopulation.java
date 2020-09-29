package com.versus.hit;

public class WorldPopulation {
	private String name;
	private String country;
	private String logo;

	public WorldPopulation(String name, String country, String logo) {
		this.name = name;
		this.country = country;
		this.logo = logo;
	}

	public String getName() {
		return this.name;
	}

	public String getCountry() {
		return this.country;
	}

	public String getLogo() {
		return this.logo;
	}
}
