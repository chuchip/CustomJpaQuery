package com.profesorp.customjpaquery.repositories;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.profesorp.customjpaquery.entities.CustomersEntity;

public interface CustomersRepository extends JpaRepository<CustomersEntity, Integer> {
	
	public List<CustomersEntity> getData(HashMap<String, Object> conditions);
}
