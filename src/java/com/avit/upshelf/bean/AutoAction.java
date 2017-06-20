package com.avit.upshelf.bean;

/**
 * @author panxincheng
 *
 * @date 2012-10-27 下午04:07:11
 */
public class AutoAction {

	private int id;
	private String url;
	private int state;
	private int isRun;
	private int isRsync;
	private int isUptree;
	private int isBuildPage;
	private int isUpload;
	private String rsyncUrl;
	private int sleeptime;
	private String poid;
	private long menuid;
	private String treeroot;
	private int uptreeFlag;
	private String operationCode;

	public long getMenuid() {
		return menuid;
	}
	public void setMenuid(long menuid) {
		this.menuid = menuid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getIsRun() {
		return isRun;
	}
	public void setIsRun(int isRun) {
		this.isRun = isRun;
	}
	public int getIsRsync() {
		return isRsync;
	}
	public void setIsRsync(int isRsync) {
		this.isRsync = isRsync;
	}
	public int getIsUptree() {
		return isUptree;
	}
	public void setIsUptree(int isUptree) {
		this.isUptree = isUptree;
	}
	public int getIsBuildPage() {
		return isBuildPage;
	}
	public void setIsBuildPage(int isBuildPage) {
		this.isBuildPage = isBuildPage;
	}
	public int getIsUpload() {
		return isUpload;
	}
	public void setIsUpload(int isUpload) {
		this.isUpload = isUpload;
	}
	public String getRsyncUrl() {
		return rsyncUrl;
	}
	public void setRsyncUrl(String rsyncUrl) {
		this.rsyncUrl = rsyncUrl;
	}
	public int getSleeptime() {
		return sleeptime;
	}
	public void setSleeptime(int sleeptime) {
		this.sleeptime = sleeptime;
	}
	public String getPoid() {
		return poid;
	}
	public void setPoid(String poid) {
		this.poid = poid;
	}
	public String getTreeroot() {
		return treeroot;
	}
	public void setTreeroot(String treeroot) {
		this.treeroot = treeroot;
	}
	public int getUptreeFlag() {
		return uptreeFlag;
	}
	public void setUptreeFlag(int uptreeFlag) {
		this.uptreeFlag = uptreeFlag;
	}
	public String getOperationCode() {
		return operationCode;
	}
	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}

	
}
