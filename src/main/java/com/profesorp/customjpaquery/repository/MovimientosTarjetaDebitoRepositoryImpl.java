package com.bankia.msc.agregadorterceros.tarjetasdebitoterceros.persistence.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Pageable;

import com.bankia.msc.agregadorterceros.tarjetasdebitoterceros.persistence.entity.MovimientoTarjetaDebitoEntity;


public class MovimientosTarjetaDebitoRepositoryImpl implements MvtosRepositoryCustom{

	 @PersistenceContext
	 private EntityManager entityManager;
	 
	 @Override
	    public List<MovimientoTarjetaDebitoEntity> findMvtos(Map<String,Object> params,Pageable paginacion ) {
	        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	        CriteriaQuery<MovimientoTarjetaDebitoEntity> query = cb.createQuery(MovimientoTarjetaDebitoEntity.class);
	        Root<MovimientoTarjetaDebitoEntity> user = query.from(MovimientoTarjetaDebitoEntity.class);	 	      
	 
	        List<Predicate> predicates = new ArrayList<>();
	        params.forEach((field,value) -> 
	        {  
	        	
	        	switch (field)
	        	{
	        		case "fechaDe":
	        			predicates.add(cb.greaterThanOrEqualTo(user.<java.util.Date>get("fechaValor"),(java.util.Date)value));
	        			break;
	        		case "fechaHasta":
	        			predicates.add(cb.lessThanOrEqualTo(user.<java.util.Date>get("fechaValor"),(java.util.Date)value));
	        			break;
	        		case "importe":
	        			predicates.add(cb.greaterThanOrEqualTo(user.get("cantidad"),(Double)value));
	        			break;

	        	}
	        }) ;
	        
	        query.select(user)
	            .where(cb.or(predicates.toArray(new Predicate[predicates.size()])));
	        
	        List<MovimientoTarjetaDebitoEntity> resultado= entityManager.createQuery(query)
	        		.getResultList();
	        return resultado;
//	        PagedListHolder page = new PagedListHolder(resultado);
//	        page.setPageSize(paginacion.getPageSize()); // number of items per page
//	        page.setPage(paginacion.getPageNumber());      // set to first page
//	        
//	        return page.getPageList();  // a List which represents the current page
	    }
}
