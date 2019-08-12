package com.naskar.fluentquery.metamodel.mappings.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="TB_ACCOUNT")
public class Account {
	
	@Id
	@Column(name="CD_ACCOUNT")
	private Long id;
	
	@Column(name="NU_ACCOUNT")
	private String accountNumber;
	
	@Column(name="VL_BALANCE")
	private Double balance;
	
	@ManyToOne
	@JoinColumn(name="CD_CUSTOMER")
	private Customer customer;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}
