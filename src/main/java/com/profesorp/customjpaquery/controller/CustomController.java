package com.profesorp.customjpaquery.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.profesorp.customjpaquery.entities.CustomersEntity;
import com.profesorp.customjpaquery.repositories.CustomersRepository;

@RestController
public class CustomController {
	@Autowired
	CustomersRepository customersRepository;
	
	@Autowired
	EntityManager em;
	
	public static final String GREATER_THAN="greater";
	public static final String LESS_THAN="less";
	public static final String EQUAL="equal";
	
	@GetMapping("/get")
	public List<CustomersEntity> getData(@RequestParam(required=false,name="idCustomer") Integer idCustomer,
			@RequestParam(value="nameCustomer",required=false) String nameCustomer,
			@RequestParam(required=false) String addressCustomer, 
			@RequestParam(required=false) @DateTimeFormat(pattern="dd-MM-yyyy") Date createdDate,@RequestParam(required=false) String dateCondition			)
	{		
		HashMap<String, Object> data=new HashMap<>();
		
		if (idCustomer!=null)
			data.put("id",idCustomer);
		if (nameCustomer!=null)
			data.put("name",nameCustomer);
		if (addressCustomer!=null)
			data.put("address",addressCustomer);
		if (dateCondition==null)
			dateCondition=GREATER_THAN;
		if (!dateCondition.equals(GREATER_THAN) && 	!dateCondition.equals(LESS_THAN) && !dateCondition.equals(EQUAL))
			dateCondition=GREATER_THAN;	
		if (createdDate!=null)
		{
			data.put("created",createdDate);
			data.put("dateCondition",dateCondition);
		}
		
		return customersRepository.getData(data);		
		
	}
	@GetMapping("/getQuery")
	public List<CustomersEntity> getDataQuery(@RequestParam(required=false,name="idCustomer") Integer idCustomer,
			@RequestParam(value="nameCustomer",required=false) String nameCustomer,
			@RequestParam(required=false) String addressCustomer, 
			@RequestParam(required=false) @DateTimeFormat(pattern="dd-MM-yyyy") Date createdDate,@RequestParam(required=false) String dateCondition			)
	{		
		HashMap<String, Object> data=new HashMap<>();
		
		
		String sql="select e from CustomersEntity e where 1=1";
		
		if (idCustomer!=null)
			sql+=" and e.id = :idCustomer";
		if (nameCustomer!=null)
			sql+=" and e.name = :nameCustomer";
			
		if (addressCustomer!=null)
			sql+=" and e.address = :addressCustomer";
		String cond;
		if (dateCondition==null)
			dateCondition=GREATER_THAN;		 
		switch (dateCondition)
		{
			case GREATER_THAN:
				cond=">";
				break;
			case LESS_THAN:
				cond="<";
				break;
			default:
				cond="=";				
		}	
		
		if (createdDate!=null)
			sql+=" and e.created "+cond+" :createdDate";
		TypedQuery<CustomersEntity> query= em.createQuery(sql,CustomersEntity.class);
		if (idCustomer!=null)
			query.setParameter("idCustomer", idCustomer);
		if (nameCustomer!=null)
			query.setParameter("nameCustomer", nameCustomer);
			
		if (addressCustomer!=null)
			query.setParameter("addressCustomer", addressCustomer);
		if (createdDate!=null)
			query.setParameter("createdDate", createdDate);
		return query.getResultList();			
		
	}
}
