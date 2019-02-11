package com.profesorp.customjpaquery.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity 
@Table(name="customers")
@Data
public class CustomersEntity {
	@Id
	int id;
	
	@Column
	String name;
	
	@Column
	String address;
	
	@Column
	String email;
		
	@Column(name="created_date")
	@Temporal(TemporalType.DATE)
	Date created;
}
