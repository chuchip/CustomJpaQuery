package com.bankia.msc.agregadorterceros.tarjetasdebitoterceros.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bankia.msc.agregadorterceros.tarjetasdebitoterceros.persistence.entity.TarjetaDebitoTercerosEntity;
import com.bankia.msc.agregadorterceros.tarjetasdebitoterceros.persistence.entity.TarjetaDebitoTercerosPK;

public interface TarjetasDebitoTercerosRepository extends JpaRepository<TarjetaDebitoTercerosEntity, TarjetaDebitoTercerosPK> {

	@Query("SELECT c FROM ContratoTarjetaDebito as c WHERE c.id.codCliente LIKE CONCAT(:codCliente,'%') AND c.id.codEntidadFinanciera = :codEntidadFinanciera")
	Page<TarjetaDebitoTercerosEntity> findByCodClienteAndCodContratoExterno(
						@Param("codCliente") String codCliente, 
						@Param("codEntidadFinanciera") String codEntidadFinanciera,Pageable page);
	
	@Query("SELECT c FROM ContratoTarjetaDebito as c WHERE c.id.codCliente LIKE CONCAT(:codCliente,'%') and c.id.codEntidadFinanciera = :codEntidadFinanciera"+
						" and c.id.codContratoExterno = :codContrato ")
	List<TarjetaDebitoTercerosEntity> findById(
						@Param("codCliente") String codCliente,
						@Param("codEntidadFinanciera") String codEntidadFinanciera,
						@Param("codContrato") String codContrato
						);
	

	@Query("SELECT c FROM ContratoTarjetaDebito as c WHERE c.id.codCliente LIKE CONCAT(:codCliente,'%') ")
	Page<TarjetaDebitoTercerosEntity> findByCodCliente(
						@Param("codCliente") String codCliente, 
						Pageable page);
	
}