package com.avit.getDate;

/**
 * 常量定义
 * @author mengjt
 *
 */
public class Constant {
	/** HTTP连接 */
	public static final int HTTP_TIMEOUT = 2000; //该参数和程序中乱用实际值照成混乱 全部采用配置参数 dsir
	public static final String HTTP_ENCODING = "utf-8";
	
	/** BO数据类型 */
	public static final String PRODUCTOFFERING = "ProductOffering";
	public static final String VIEW = "View";
	public static final String FOLDER = "Folder";
	public static final String BUNDLE = "Bundle";
	public static final String ASSET = "Asset";
	public static final String SYMBOLLINK = "SymbolLink";
	public static final String SPACE = "Space";
	/** BO商品类型 */
	public static final String CHARGE_TYPE_ANCI_BO= "1";
	public static final String CHARGE_TYPE_BAOYUE_BO = "2";
	public static final String CHARGE_TYPE_MIANFEI_BO= "4";
	
	/** Portal业务常量 */
	public static final String ASSETID_PRE = "avit";
	public static final int ASSETID_LEN = 16;
	public static final String ASSETID_REPLACE = "0";
	
	public static final int PO_STATUS_NORMAL = 2;
	/** Portal商品类型 */
	public static final int CHARGE_TYPE_ANCI = 0;
	public static final int CHARGE_TYPE_BAOYUE = 1;
	public static final int CHARGE_TYPE_MIANFEI= 2;
	/** AssetTransition 状态：可用 */
	public static final String ASSET_STATE_NORMAL= "0";
	/** AssetTransition 内容类型：视频 */
	/** RelaContPCont 包内容类型：节目 */
	public static final int PAC_ASSET_TYPE = 1;
	/** AssetTransition 产品类型 **/
	public static final int PRODUCT_TYPE_BAOYUE = 2;
	public static final int PRODUCT_TYPE_ANCI_P = 1;
	public static final int PRODUCT_TYPE_ANCI_C = 0;
	/**
	 * 获取商品规格名称
	 * @param chargeType
	 * @return String
	 */ 
	public static String getPoSpecName(int chargeType) {
		String name = "";
		switch (chargeType) {
			case CHARGE_TYPE_BAOYUE:
				name = "包月商品";
				break;
			case CHARGE_TYPE_ANCI:
				name = "按次商品";
				break;
			case CHARGE_TYPE_MIANFEI:
				name = "免费商品";
				break;
	
			default:
				name = "无此商品规格";
				break;
		}
		return name;
	}
	
	/** Category 类型 */
	public static final String CATEGORY_TYPE_ASSET = "1";
	public static final String CATEGORY_TYPE_ASSETP = "2";
	public static final String CATEGORY_TYPE_CATE = "8";
	public static final String CATEGORY_TYPE_LINK = "9";
	/** 自动上架 操作类型 */
	public static final int OPE_UPD_ORDER= 0;
	public static final int OPE_UPD = 1;
	public static final int OPE_ADD = 2;
	
	
}
