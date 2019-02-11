package com.bankia.msc.agregadorterceros.tarjetasdebitoterceros.persistence.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.bankia.msc.agregadorterceros.tarjetasdebitoterceros.persistence.entity.MovimientoTarjetaDebitoEntity;

public interface MvtosRepositoryCustom    {
	List<MovimientoTarjetaDebitoEntity> findMvtos(Map<String,Object> params,Pageable paginacion);
}
