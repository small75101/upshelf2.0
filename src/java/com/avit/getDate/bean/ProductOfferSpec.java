package com.avit.getDate.bean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * TProductOfferSpec entity. @author MyEclipse Persistence Tools
 */

public class ProductOfferSpec implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private BigDecimal id;
	private String code;
	private String name;
	private Date effdate;
	private Date expdate;
	private String chargetype;
	private String offmethodid;
	private String describe;

	// Constructors

	/** default constructor */
	public ProductOfferSpec() {
	}

	/** minimal constructor */
	public ProductOfferSpec(String describe) {
		this.describe = describe;
	}

	/** full constructor */
	public ProductOfferSpec(String code, String name, Date effdate,
			Date expdate, String chargetype, String offmethodid, String describe) {
		this.code = code;
		this.name = name;
		this.effdate = effdate;
		this.expdate = expdate;
		this.chargetype = chargetype;
		this.offmethodid = offmethodid;
		this.describe = describe;
	}

	// Property accessors

	public BigDecimal getId() {
		return this.id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getEffdate() {
		return this.effdate;
	}

	public void setEffdate(Date effdate) {
		this.effdate = effdate;
	}

	public Date getExpdate() {
		return this.expdate;
	}

	public void setExpdate(Date expdate) {
		this.expdate = expdate;
	}

	public String getChargetype() {
		return this.chargetype;
	}

	public void setChargetype(String chargetype) {
		this.chargetype = chargetype;
	}

	public String getOffmethodid() {
		return this.offmethodid;
	}

	public void setOffmethodid(String offmethodid) {
		this.offmethodid = offmethodid;
	}

	public String getDescribe() {
		return this.describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

}