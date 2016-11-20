package com.trevor.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import java.util.Set;

@Entity
public class Order {
	@Id
	private Integer orderId;
	private Date date;
	private String status;
	private Customer customer;
	private Set<Item> items;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<Item> getItems() {
		return items;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}

	public Integer getOrderId() {

		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
}
