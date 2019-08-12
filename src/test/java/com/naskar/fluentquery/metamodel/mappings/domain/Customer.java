package com.naskar.fluentquery.metamodel.mappings.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TB_CUSTOMER")
public class Customer {
	
	@Id
	@Column(name="CD_CUSTOMER")
	private Long id;
	
	@Column(name="DS_NAME")
	private String name;
	
	@Column(name="NU_REGION_CODE")
	private Long regionCode;
	
	@Column(name="VL_MIN_BALANCE")
	private Double minBalance;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Long getRegionCode() {
		return regionCode;
	}
	
	public void setRegionCode(Long regionCode) {
		this.regionCode = regionCode;
	}
	
	public Double getMinBalance() {
		return minBalance;
	}
	
	public void setMinBalance(Double minBalance) {
		this.minBalance = minBalance;
	}
	
}
