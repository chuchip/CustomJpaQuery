package com.bankia.msc.agregadorterceros.tarjetasdebitoterceros.persistence.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bankia.msc.agregadorterceros.tarjetasdebitoterceros.persistence.entity.MovimientoTarjetaDebitoEntity;
import com.bankia.msc.agregadorterceros.tarjetasdebitoterceros.persistence.entity.MovimientoTarjetaDebitoEntityPK;


public interface MovimientosTarjetaDebitoRepository extends JpaRepository<MovimientoTarjetaDebitoEntity, MovimientoTarjetaDebitoEntityPK> {

				
	Page<MovimientoTarjetaDebitoEntity> findByCodClienteAndCodContratoExterno(String codCliente,String codContratoExterno,Pageable paginacion);
	
	
	@Query("SELECT c from MovimientoTarjetaDebitoExterno as c where"
			+ " c.codCliente LIKE CONCAT(:idCliente,'%') AND c.codEntidadFinanciera LIKE CONCAT(:idEntidad,'%')"
			+ " AND c.codContratoExterno LIKE CONCAT (:idContrato,'%') ORDER BY c.fechaValor DESC")			
	Page<MovimientoTarjetaDebitoEntity> findMovimientos(@Param("idCliente") String idCliente,@Param("idEntidad") String idEntidad,@Param("idContrato") String idContrato,Pageable paginacion);
	

	public List<MovimientoTarjetaDebitoEntity> findMvtos(Map<String,Object> params,Pageable paginacion );
	@Query("SELECT c from MovimientoTarjetaDebitoExterno as c where c.codCliente LIKE CONCAT(:idCliente,'%') AND c.codEntidadFinanciera LIKE CONCAT(:idEntidad,'%') AND c.codContratoExterno LIKE CONCAT (:idContrato,'%')"+
	"AND c.fechaValor >= :fechaDesde AND c.fechaValor <= :fechaHasta AND ABS(c.cantidad)>= :cantidad ORDER BY c.fechaValor DESC" )			
	Page<MovimientoTarjetaDebitoEntity> findMovimientosPorFechaImporteMayor(@Param("idCliente") String idCliente,
																				@Param("idEntidad") String idEntidad,
																				@Param("idContrato") String idContrato,
																				@Param("fechaDesde") java.util.Date fechaDesde,
																				@Param("fechaHasta") java.util.Date fechaHasta,
																				@Param("cantidad") double cantidad,
																				Pageable paginacion);
	
	@Query("SELECT c from MovimientoTarjetaDebitoExterno as c where c.codCliente LIKE CONCAT(:idCliente,'%') AND c.codEntidadFinanciera LIKE CONCAT(:idEntidad,'%') AND c.codContratoExterno LIKE CONCAT (:idContrato,'%')"+
	"AND c.fechaValor >= :fechaDesde AND c.fechaValor <= :fechaHasta AND ABS(c.cantidad)<= :cantidad ORDER BY c.fechaValor DESC")			
	Page<MovimientoTarjetaDebitoEntity> findMovimientosPorFechaImporteMenor(@Param("idCliente") String idCliente,
																				@Param("idEntidad") String idEntidad,
																				@Param("idContrato") String idContrato,
																				@Param("fechaDesde") java.util.Date fechaDesde,
																				@Param("fechaHasta") java.util.Date fechaHasta,
																				@Param("cantidad") double cantidad,
																				Pageable paginacion);
	
	@Query("SELECT c from MovimientoTarjetaDebitoExterno as c where c.codCliente LIKE CONCAT(:idCliente,'%') AND c.codEntidadFinanciera LIKE CONCAT(:idEntidad,'%') AND c.codContratoExterno LIKE CONCAT (:idContrato,'%')"+
	"AND c.fechaValor >= :fechaDesde AND c.fechaValor <= :fechaHasta AND ABS(c.cantidad) = :cantidad ORDER BY c.fechaValor DESC")			
	Page<MovimientoTarjetaDebitoEntity> findMovimientosPorFechaImporteIgual(@Param("idCliente") String idCliente,
																				@Param("idEntidad") String idEntidad,
																				@Param("idContrato") String idContrato,
																				@Param("fechaDesde") java.util.Date fechaDesde,
																				@Param("fechaHasta") java.util.Date fechaHasta,
																				@Param("cantidad") double cantidad,
																				Pageable paginacion);
	
}
