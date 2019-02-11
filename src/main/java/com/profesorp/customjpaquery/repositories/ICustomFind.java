package com.profesorp.customjpaquery.repositories;

import java.util.HashMap;
import java.util.List;

import com.profesorp.customjpaquery.entities.CustomersEntity;

public interface ICustomFind {
	public List<CustomersEntity> getData(HashMap<String, Object> conditions);
}
