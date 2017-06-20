package com.avit.getDate.bean;

/**
 * TProductOffering entity. @author MyEclipse Persistence Tools
 */

public class ProductOffering implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String productOfferingCode;
	private String productOfferingName;
	private Double productOfferingPrice;
	private Integer chargeType;
	private String posterPath;
	private String remark;
	private String effectiveTime;
	private String expireTime;
	private Integer productOfferingType;
	private Integer productOfferingStatus;
	private String productOfferingDirector;
	private String productOfferingActor;
	private String productOfferingSummaryshort;
	private String productCode;
	private String contentCode;
	private Integer contentType;
	private String productOfferingSpecCode;
	private String businessCode;
	private String opCode;

	// Constructors

	/** default constructor */
	public ProductOffering() {
	}

	/** full constructor */
	public ProductOffering(String productOfferingCode,
			String productOfferingName, Double productOfferingPrice,
			Integer chargeType, String posterPath, String remark,
			String effectiveTime, String expireTime,
			Integer productOfferingType, Integer productOfferingStatus,
			String productOfferingDirector, String productOfferingActor,
			String productOfferingSummaryshort, String productCode,
			String contentCode, Integer contentType,
			String productOfferingSpecCode, String businessCode, String opCode) {
		this.productOfferingCode = productOfferingCode;
		this.productOfferingName = productOfferingName;
		this.productOfferingPrice = productOfferingPrice;
		this.chargeType = chargeType;
		this.posterPath = posterPath;
		this.remark = remark;
		this.effectiveTime = effectiveTime;
		this.expireTime = expireTime;
		this.productOfferingType = productOfferingType;
		this.productOfferingStatus = productOfferingStatus;
		this.productOfferingDirector = productOfferingDirector;
		this.productOfferingActor = productOfferingActor;
		this.productOfferingSummaryshort = productOfferingSummaryshort;
		this.productCode = productCode;
		this.contentCode = contentCode;
		this.contentType = contentType;
		this.productOfferingSpecCode = productOfferingSpecCode;
		this.businessCode = businessCode;
		this.opCode = opCode;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductOfferingCode() {
		return this.productOfferingCode;
	}

	public void setProductOfferingCode(String productOfferingCode) {
		this.productOfferingCode = productOfferingCode;
	}

	public String getProductOfferingName() {
		return this.productOfferingName;
	}

	public void setProductOfferingName(String productOfferingName) {
		this.productOfferingName = productOfferingName;
	}

	public Double getProductOfferingPrice() {
		return this.productOfferingPrice;
	}

	public void setProductOfferingPrice(Double productOfferingPrice) {
		this.productOfferingPrice = productOfferingPrice;
	}

	public Integer getChargeType() {
		return this.chargeType;
	}

	public void setChargeType(Integer chargeType) {
		this.chargeType = chargeType;
	}

	public String getPosterPath() {
		return this.posterPath;
	}

	public void setPosterPath(String posterPath) {
		this.posterPath = posterPath;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getEffectiveTime() {
		return this.effectiveTime;
	}

	public void setEffectiveTime(String effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	public String getExpireTime() {
		return this.expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}

	public Integer getProductOfferingType() {
		return this.productOfferingType;
	}

	public void setProductOfferingType(Integer productOfferingType) {
		this.productOfferingType = productOfferingType;
	}

	public Integer getProductOfferingStatus() {
		return this.productOfferingStatus;
	}

	public void setProductOfferingStatus(Integer productOfferingStatus) {
		this.productOfferingStatus = productOfferingStatus;
	}

	public String getProductOfferingDirector() {
		return this.productOfferingDirector;
	}

	public void setProductOfferingDirector(String productOfferingDirector) {
		this.productOfferingDirector = productOfferingDirector;
	}

	public String getProductOfferingActor() {
		return this.productOfferingActor;
	}

	public void setProductOfferingActor(String productOfferingActor) {
		this.productOfferingActor = productOfferingActor;
	}

	public String getProductOfferingSummaryshort() {
		return this.productOfferingSummaryshort;
	}

	public void setProductOfferingSummaryshort(
			String productOfferingSummaryshort) {
		this.productOfferingSummaryshort = productOfferingSummaryshort;
	}

	public String getProductCode() {
		return this.productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getContentCode() {
		return this.contentCode;
	}

	public void setContentCode(String contentCode) {
		this.contentCode = contentCode;
	}

	public Integer getContentType() {
		return this.contentType;
	}

	public void setContentType(Integer contentType) {
		this.contentType = contentType;
	}

	public String getProductOfferingSpecCode() {
		return this.productOfferingSpecCode;
	}

	public void setProductOfferingSpecCode(String productOfferingSpecCode) {
		this.productOfferingSpecCode = productOfferingSpecCode;
	}

	public String getBusinessCode() {
		return this.businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public String getOpCode() {
		return this.opCode;
	}

	public void setOpCode(String opCode) {
		this.opCode = opCode;
	}
	
	private boolean compareTo(Object a, Object b){
		if(a == null && b == null)
			return true;
		else
			return a==null ? b.equals(a) : a.equals(b);
	}
	
	public boolean compareTo(ProductOffering obj) {

		if (obj != null) {
			if ( compareTo(this.productOfferingCode, obj.productOfferingCode) &&
				 compareTo(this.productOfferingName, obj.productOfferingName) &&
				 compareTo(this.productOfferingPrice, obj.productOfferingPrice) &&
				 compareTo(this.chargeType, obj.chargeType) &&
				 compareTo(this.posterPath, obj.posterPath) &&
				 compareTo(this.remark, obj.remark) &&
				 compareTo(this.effectiveTime, obj.effectiveTime) &&
				 compareTo(this.expireTime, obj.expireTime) &&
				 compareTo(this.productOfferingType, obj.productOfferingType) &&
				 compareTo(this.productOfferingStatus, obj.productOfferingStatus) &&
				 compareTo(this.productOfferingDirector, obj.productOfferingDirector) &&
				 compareTo(this.productOfferingActor, obj.productOfferingActor) &&
				 compareTo(this.productOfferingSummaryshort, obj.productOfferingSummaryshort) &&
				 compareTo(this.productCode, obj.productCode) &&
				 compareTo(this.contentCode, obj.contentCode) &&
				 compareTo(this.contentType, obj.contentType) &&
				 compareTo(this.productOfferingSpecCode, obj.productOfferingSpecCode) &&
				 compareTo(this.businessCode, obj.businessCode) &&
				 compareTo(this.opCode, obj.opCode)
					) {
				return true;
			}
		}
		return false;
	}
	

}