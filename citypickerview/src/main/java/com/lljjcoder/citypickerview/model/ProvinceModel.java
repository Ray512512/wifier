package com.lljjcoder.citypickerview.model;

import java.io.Serializable;
import java.util.List;

public class ProvinceModel implements Serializable{
	private String name;
	private String id;
	private List<CityModel> list;
	
	public ProvinceModel() {
		super();
	}

	public ProvinceModel(String name, List<CityModel> list) {
		super();
		this.name = name;
		this.list = list;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CityModel> getList() {
		return list;
	}

	public void setList(List<CityModel> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "ProvinceModel [name=" + name + ", list=" + list + "]";
	}
	
}
