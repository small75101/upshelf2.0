package com.avit.upshelf.bean;

import java.io.Serializable;
import java.util.Date;

public class Category implements Serializable,Comparable<Category>{

	private static final long serialVersionUID = 3237133743121181032L;
	private Long id;//SEQ_CATEGORY获取
	private String resourceType;//资源类型 9：链接  8：分类   7：业务   6：应用   5：频道   4：    3：产品   2：资源包   1：资源
	private String resourceId;//资源ID   如果是分类，则资源ID为c+“ID +1”
	private String resourceName;//资源名称
	private Long parentId;//上级ID
	private Long resourceOrder;//资源排序号
	private String resourcePath;//文件路径
	private String resourceShowPath;//资源导航路径   川网省中心+BO路径
	private Long resourceStencil;//
	private String state = "1";//状态（0-初始化,1-上线,2-下线）
	private Long templateId = 0L;//套用的非事件模板ID
	private String productId;//对应产品ID
	private String isAdv = "0";//0-不包含广告,1-包含广告    
	private String businessType = "2";//1-单表单向,2-代表双向  
	private String isFocus = "0";//0-不是焦点,1-是焦点
	private String isCommend = "0";//0-不推荐,1-推荐
	private Date createTime;//创建时间
	private Long linkId;//链接栏目ID
	public String operationCode;
	public String uiType;
	
	private int type;//操作类型（0-修改排序,1-修改,2-新增）
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public Long getResourceOrder() {
		return resourceOrder;
	}
	public void setResourceOrder(Long resourceOrder) {
		this.resourceOrder = resourceOrder;
	}
	public String getResourcePath() {
		return resourcePath;
	}
	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}
	public String getResourceShowPath() {
		return resourceShowPath;
	}
	public void setResourceShowPath(String resourceShowPath) {
		this.resourceShowPath = resourceShowPath;
	}
	public Long getResourceStencil() {
		return resourceStencil;
	}
	public void setResourceStencil(Long resourceStencil) {
		this.resourceStencil = resourceStencil;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getIsAdv() {
		return isAdv;
	}
	public void setIsAdv(String isAdv) {
		this.isAdv = isAdv;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getIsFocus() {
		return isFocus;
	}
	public void setIsFocus(String isFocus) {
		this.isFocus = isFocus;
	}
	public String getIsCommend() {
		return isCommend;
	}
	public void setIsCommend(String isCommend) {
		this.isCommend = isCommend;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getLinkId() {
		return linkId;
	}
	public void setLinkId(Long linkId) {
		this.linkId = linkId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getOperationCode() {
		return operationCode;
	}
	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}
	
	public String getUIType() {
		return uiType;
	}
	public void setUIType(String uiType) {
		this.uiType = uiType;
	}
	
	public int compareTo(Category c) {
		if((this.parentId.compareTo(c.getParentId()))==0){
			if((this.resourceType.compareTo(c.getResourceType()))==0){
				if(this.getResourceOrder()>c.getResourceOrder()){
					return 1;
				}else if(this.getResourceOrder() == c.getResourceOrder()){
					return 0;
				}else{
					return -1;
				}
			}else{
				return this.resourceType.compareTo(c.getResourceType());
			}
		}else{
			return this.parentId.compareTo(c.getParentId());
		}
	}
	
}
