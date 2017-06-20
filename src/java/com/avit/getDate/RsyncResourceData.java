package com.avit.getDate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.avit.getDate.bean.AssetTransition;
import com.avit.getDate.bean.ProductOffering;
import com.avit.getDate.bean.RelaContPCont;
import com.avit.upshelf.DBConnect;
import com.avit.util.Pinyin4jUtil;
import com.avit.util.StringUtil;

public class RsyncResourceData {
	
	private Connection conn = null;
	//初始数据库中的资源集合
	private Map<String,String> assetinfoMap = null;
	//需要删除的资源集合
	private Map<String,String> delAssetMap = null;
	//运营商编码
	private String operationCode;
	
	public RsyncResourceData(Connection con) throws SQLException{
		this.conn = con;
	}

	
	/**
	 * 节目初始化（首先根据商品编码和运营商编码过滤）
	 * @param po_id	商品编码
	 * @param operationCode	运营商编码
	 * @throws SQLException
	 */
	public Map<String,AssetTransition> initMap(String po_id, String operationCode) throws SQLException{
		this.operationCode = operationCode;
		assetinfoMap = new HashMap<String,String>();
		delAssetMap = new HashMap<String,String>();
		Map<String,AssetTransition> oldAssetinfoMap = new HashMap<String,AssetTransition>();
		Statement stmt = null;
		ResultSet rs = null;
		try {
//			String sql = "select a.id,a.asset_id from t_asset_transition a,t_product_offering b where a.product_offering_code=b.product_offering_code and b.product_offering_code='"+po_id+"'";
//			String sql = "select ASSET_ID, ASSET_ID from T_ASSET_TRANSITION where (PRODUCT_OFFERING_CODE is null or PRODUCT_OFFERING_CODE='"+po_id+"') and OPERATION_CODE='"+this.operationCode+"'";
			//String sql = "select * from T_ASSET_TRANSITION where (PRODUCT_OFFERING_CODE is null or PRODUCT_OFFERING_CODE='"+po_id+"') and OPERATION_CODE='"+this.operationCode+"'";
			String sql = "select * from T_ASSET_TRANSITION where (PRODUCT_OFFERING_CODE='"+po_id+"') and OPERATION_CODE='"+this.operationCode+"'";

			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				AssetTransition asset = setAsset(rs);
				oldAssetinfoMap.put(asset.getAssetId(), asset);
				assetinfoMap.put(asset.getAssetId(), asset.getAssetId());
			}
			rs.close();
			rs = null;
			stmt.close();
			stmt = null;
			
			//将最新的资源集合保存进需要删除的资源集合中
			delAssetMap.putAll(assetinfoMap);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			closeResultSet(rs);
			closeStatement(stmt);
		} 
		return oldAssetinfoMap;
	}
	
	private AssetTransition setAsset(ResultSet rs) {
		AssetTransition asset = new AssetTransition();
		try {
			asset.setAssetId(rs.getString("ASSET_ID"));
			asset.setAssetName(rs.getString("ASSETNAME"));
			asset.setAssetDesc(rs.getString("ASSETDESC"));
			asset.setTitle(rs.getString("TITLE"));
			asset.setYear(rs.getString("YEAR"));
			asset.setKeyword(rs.getString("KEYWORD"));
			asset.setRating(rs.getString("RATING"));
			asset.setRunTime(rs.getString("RUNTIME"));
			asset.setDisplayRunTime(rs.getString("DISPLAYRUNTIME"));
			asset.setPosterUrl(rs.getString("POSTERURL"));
			asset.setDirector(rs.getString("DIRECTOR"));
			asset.setActor(rs.getString("ACTOR"));
			asset.setState(rs.getString("STATE"));
			asset.setPrice(rs.getString("PRICE"));
			asset.setEntryCount(rs.getString("ENTRYCOUNT"));
			asset.setCategory(rs.getString("CATEGORY"));
			asset.setOperationCode(rs.getString("OPERATION_CODE"));
			asset.setFirstSpell(rs.getString("FIRST_SPELL"));
			asset.setGenre(rs.getString("GENRE"));
			asset.setProviderId(rs.getString("PROVIDER_ID"));
			asset.setProviderAssetId(rs.getString("PROVIDER_ASSET_ID"));
		} catch (SQLException e) {
		}
		return asset;
	}
	
	public void updateAssetinfo(AssetTransition obj) throws SQLException{
		List<AssetTransition> assets = new ArrayList<AssetTransition>();
		assets.add(obj);
		updateAssetinfo(assets);
	}
	
	/**
	 * 更新节目/节目包（川网）
	 * @param objs
	 * @throws SQLException
	 */
	public void updateAssetinfo(List<AssetTransition> objs) throws SQLException{
		List<AssetTransition> addAssets = new ArrayList<AssetTransition>();
		Map<String, AssetTransition> updateAssets = new HashMap<String, AssetTransition>();
		PreparedStatement pstmt = null;
		String sql = "update T_ASSET_TRANSITION set " +
							"ASSETNAME=?,TITLE=?,YEAR=?,KEYWORD=?,RATING=?,RUNTIME=?,DISPLAYRUNTIME=?," +
							"ASSETDESC=?,POSTERURL=?,DIRECTOR=?,ACTOR=?,MODIFYTIME=?,STATE=?,CATEGORY=?,PRICE=?," +
							"ENTRYCOUNT=?,CONTENT_ORDER=?,PROVIDER_ID=?,PROVIDER_ASSET_ID=?,FIRST_SPELL=? where ASSET_ID=? and OPERATION_CODE=?";
		try {
			
			for (AssetTransition obj : objs) {
				long id = getID("select count(*) from T_ASSET_TRANSITION where ASSET_ID='"+obj.getAssetId()+"' and OPERATION_CODE='"+this.operationCode+"'");
				if (id == 0) {
					addAssets.add(obj);
				} else {
					if(!assetinfoMap.containsKey(obj.getAssetId())) {
//					 	assetinfoMap.put(obj.getAssetId(),String.valueOf(id));
						assetinfoMap.put(obj.getAssetId(),obj.getAssetId());
					}
//					updateAssets.put(String.valueOf(id), obj);
					updateAssets.put(obj.getAssetId(), obj);
				}
			}

			//add
			if (addAssets.size() > 0) {
				addAssetinfo(addAssets);
			}
			
			//update
			int count = 0;
			pstmt = conn.prepareStatement(sql);
			Iterator<String> set = updateAssets.keySet().iterator();
			while (set.hasNext()) {
				
				count++;
				String tmpId = set.next();
				AssetTransition obj = updateAssets.get(tmpId);
//				System.out.println("update asset :"+count+obj.getAssetId()+":"+obj.getAssetName());
				pstmt.setString(1, obj.getAssetName());
				pstmt.setString(2, obj.getTitle());
				pstmt.setString(3, obj.getYear());
				pstmt.setString(4, obj.getKeyword());
				pstmt.setString(5, obj.getRating());
				pstmt.setString(6, obj.getRunTime());
				pstmt.setString(7, obj.getDisplayRunTime());
				pstmt.setString(8, obj.getAssetDesc());
				pstmt.setString(9, obj.getPosterUrl());
				pstmt.setString(10,obj.getDirector());
				pstmt.setString(11, obj.getActor());
				pstmt.setTimestamp(12, new Timestamp(new Date().getTime()));//MODIFYTIME
				pstmt.setString(13, obj.getState());
				pstmt.setString(14, obj.getCategory());
				pstmt.setString(15, obj.getPrice());
				pstmt.setString(16, obj.getEntryCount());
				pstmt.setInt(17, obj.getContentOrder());
				pstmt.setString(18, obj.getProviderId());
				pstmt.setString(19, obj.getProviderAssetId());
				if (StringUtil.isNotEmpty(obj.getAssetName())) {
					pstmt.setString(20, Pinyin4jUtil.cn2FirstSpell(obj.getAssetName().trim())); //FIRST_SPELL
				} else {
					pstmt.setString(20, "");
				}
				pstmt.setString(21, tmpId);
				pstmt.setString(22, this.operationCode);
				
				pstmt.addBatch();
				if (count % 1000 == 0) {
					pstmt.executeBatch();
					pstmt.clearBatch();
				}
			}
			pstmt.executeBatch();
			
			pstmt.close();
			pstmt = null;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			closeStatement(pstmt);
		}
	}	
	
	/**
	 * 新增节目/节目包（川网）
	 * @param objs
	 * @throws SQLException
	 */
	private void addAssetinfo(List<AssetTransition> objs) throws SQLException{
		PreparedStatement pstmt = null;
		String sql = "insert into T_ASSET_TRANSITION values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
																						"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
																						"?,?)";
		try {
			int count = 0;
			pstmt = conn.prepareStatement(sql);
			for (AssetTransition obj : objs) {
				count++;
//				String assetId = Constant.ASSETID_PRE + StringUtil.lpad(getSeq("SEQ_ASSET_TRANSITION")+"", 
//																	Constant.ASSETID_LEN, Constant.ASSETID_REPLACE);
				pstmt.setString(1, obj.getAssetId());
				pstmt.setString(2, obj.getProductCode());
				pstmt.setString(3, obj.getProductOfferingCode());
				pstmt.setString(4, obj.getContentPackageCode());
				pstmt.setString(5, obj.getContentCode());
				pstmt.setInt(6, obj.getChargeType());
				pstmt.setInt(7, obj.getContentType());
				pstmt.setInt(8, obj.getProductType());
				pstmt.setString(9, obj.getParentAssetId());
				pstmt.setString(10, obj.getAssetName());
				pstmt.setString(11, obj.getAssetDesc());
				pstmt.setString(12, obj.getTitle());
				pstmt.setString(13, obj.getYear());
				pstmt.setString(14, obj.getKeyword());
				pstmt.setString(15,obj.getRating());
				pstmt.setString(16, obj.getRunTime());
				pstmt.setString(17, obj.getDisplayRunTime());
				pstmt.setString(18, obj.getPosterUrl());
				//-----------------2014年4月23日修改----------------------------
				pstmt.setString(19, obj.getDirector());
				pstmt.setString(20, obj.getActor());
//				pstmt.setString(20, obj.getDirector());
				
				//-----------------2014年4月23日修改----------------------------
//				if (obj.getCreateTime() != null) {//CREATETIME
//					pstmt.setTimestamp(21, obj.getCreateTime());
//				} else {
					pstmt.setTimestamp(21, new Timestamp(new Date().getTime()));
//				}
				pstmt.setTimestamp(22, new Timestamp(new Date().getTime()));//MODIFYTIME
				pstmt.setString(23,obj.getState());
				pstmt.setString(24, obj.getPrice());
				pstmt.setString(25, obj.getEntryCount());
				pstmt.setString(26, obj.getCategory());
				pstmt.setString(27, obj.getBusinessCode());
				pstmt.setString(28, obj.getPoSpecCode());
				pstmt.setString(29, obj.getPoSpecName());
				pstmt.setString(30, obj.getOperationCode());
				pstmt.setString(31, "VOD");
				pstmt.setString(32, obj.getServiceName());
				pstmt.setInt(33, obj.getContentOrder());
				pstmt.setString(34, obj.getPosterWidthHeight());
				pstmt.setString(35, obj.getVodFormat());
				pstmt.setString(36, obj.getOttFormat());
				if (StringUtil.isNotEmpty(obj.getAssetName())) {
					pstmt.setString(37, Pinyin4jUtil.cn2FirstSpell(obj.getAssetName().trim())); //FIRST_SPELL
				} else {
					pstmt.setString(37, "");
				}
				pstmt.setString(38, obj.getValidStart());
				pstmt.setString(39, obj.getValidEnd());
				pstmt.setString(40, obj.getGenre());
				pstmt.setString(41, obj.getProviderId());
				pstmt.setString(42, obj.getProviderAssetId());
				
				pstmt.addBatch();
				if (count % 1000 == 0) {
					pstmt.executeBatch();
					pstmt.clearBatch();
				}
				
//				assetinfoMap.put(obj.getAssetId(),String.valueOf(id));
				assetinfoMap.put(obj.getAssetId(),obj.getAssetId());
			}
			
			pstmt.executeBatch();
			pstmt.close();
			pstmt = null;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			closeStatement(pstmt);
		}
	}

	/**
	 * 更新节目包/节目关系
	 * @param list
	 * @param bundleId
	 * @return
	 * @throws SQLException boolean
	 */
	public boolean updatePackageAsset(List<RelaContPCont> list,String bundleId) throws SQLException{
		boolean result = false;
//		long id = 0;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt_2 = null;
		//主键获取写在触发器中
		String addSql = "insert into T_RELA_CONPACK_CONTENT values(?,?,?,?,?,?,?)";
		String updateSql = "update T_RELA_CONPACK_CONTENT set RELA_ORDER=?,NEW_ORDER=? where CONTENTPACKAGE_CODE=? and CONTENT_CODE=? and OPERATION_CODE=?";
		String delSql = "delete from T_RELA_CONPACK_CONTENT where CONTENTPACKAGE_CODE=? and CONTENT_CODE=? and OPERATION_CODE=?";
		try{
			if(assetinfoMap.containsKey(bundleId)){
				//id = Long.parseLong(assetinfoMap.get(bundleId));
			}else
				return result;
			Map<String,String> sortMap = getProductPackage("select CONTENT_CODE,RELA_ORDER from T_RELA_CONPACK_CONTENT where CONTENTPACKAGE_CODE='"+bundleId + "' and OPERATION_CODE='"+this.operationCode+"'");
			//add、update
			String tmpAsset_id = "";
			pstmt = conn.prepareStatement(addSql);
			pstmt_2 = conn.prepareStatement(updateSql);
			for(RelaContPCont tpa:list){
				if(assetinfoMap.containsKey(tpa.getContentCode())){
					tmpAsset_id = assetinfoMap.get(tpa.getContentCode());
					if(sortMap.containsKey(tmpAsset_id)){
//						if(Integer.parseInt(sortMap.get(tmpAsset_id))!=tpa.getRelaOrder()){
							pstmt_2.setInt(1, tpa.getRelaOrder());
							pstmt_2.setInt(2, tpa.getRelaOrder());
							pstmt_2.setString(3, bundleId);
							pstmt_2.setString(4, tmpAsset_id);
							pstmt_2.setString(5, this.operationCode);
							pstmt_2.addBatch();
//						}
						sortMap.remove(tmpAsset_id);
					}else{
						if (DBConnect.isOracle()) {
							//触发器
//							pstmt.setLong(1, 0);
							long id = getSeq("HIBERNATE_SEQUENCE");
							pstmt.setLong(1, id);
						} else {
							//mysql 自增长
							pstmt.setNull(1, Types.NUMERIC);
						}
						pstmt.setString(2, bundleId);
						pstmt.setString(3, tmpAsset_id);
						pstmt.setInt(4, tpa.getContentType());
						pstmt.setInt(5, tpa.getRelaOrder());
						pstmt.setInt(6, tpa.getRelaOrder());
						pstmt.setString(7, this.operationCode);
						pstmt.addBatch();
					}
				}else{
					System.out.println("pls do addAssetinfo first!  updatePackageAsset package="+bundleId+" content="+tpa.getContentCode());
				}
			}
			pstmt.executeBatch();
			pstmt_2.executeBatch();
			pstmt.close();
			pstmt = null;
			pstmt_2.close();
			pstmt_2 = null;
			
			//delete
			Iterator<String> set = sortMap.keySet().iterator();
			pstmt = conn.prepareStatement(delSql);
			while(set.hasNext()){
				pstmt.setString(1, bundleId);
				pstmt.setString(2, set.next());
				pstmt.setString(3, this.operationCode);
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			pstmt.close();
			pstmt = null;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			closeStatement(pstmt);
			closeStatement(pstmt_2);
		}
		return result;
	}
	
	/**
	 * 更新产品
	 * @param obj
	 * @return long
	 * @throws SQLException
	 */
	public long updateProductOffering(ProductOffering obj) throws SQLException{
		long id = 0;
		PreparedStatement pstmt = null;
		String sql = "update T_PRODUCT_OFFERING set " +
							"PRODUCT_OFFERING_NAME=?,PRODUCT_OFFERING_PRICE=?,CHARGE_TYPE=?,REMARK=?," +
							"EFFECTIVE_TIME=?,EXPIRE_TIME=?,PRODUCT_OFFERING_TYPE=?,PRODUCT_OFFERING_STATUS=?,PRODUCT_OFFERING_SUMMARYSHORT=?" +
							" where ID=?";
		try {
			pstmt = conn.prepareStatement(sql);
			id = getID("select ID from T_PRODUCT_OFFERING where PRODUCT_OFFERING_CODE='"+obj.getProductOfferingCode()+"' and OP_CODE='"+obj.getOpCode()+"'");
			if (id == 0) {
				id = addProductOffering(obj);
				return id;
			}
				
			pstmt.setString(1, obj.getProductOfferingName());
			pstmt.setDouble(2, obj.getProductOfferingPrice());
			pstmt.setLong(3, obj.getChargeType());
			pstmt.setString(4, obj.getRemark());
			pstmt.setString(5, obj.getEffectiveTime());
			pstmt.setString(6, obj.getExpireTime());
			pstmt.setLong(7, obj.getProductOfferingType());
			pstmt.setLong(8, obj.getProductOfferingStatus());
			pstmt.setString(9, obj.getProductOfferingSummaryshort());
			pstmt.setLong(10, id);

			pstmt.execute();
			pstmt.close();
			pstmt = null;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			closeStatement(pstmt);
		}
		return id;
	}
	
	/**
	 * 新增产品
	 * @param obj
	 * @return long
	 * @throws SQLException
	 */
	private long addProductOffering(ProductOffering obj) throws SQLException{
		long id = 0;
		PreparedStatement pstmt = null;
		String sql = "insert into T_PRODUCT_OFFERING(ID,PRODUCT_OFFERING_CODE,PRODUCT_OFFERING_NAME,PRODUCT_OFFERING_PRICE," +
							"CHARGE_TYPE,REMARK,EFFECTIVE_TIME,EXPIRE_TIME,PRODUCT_OFFERING_TYPE,PRODUCT_OFFERING_STATUS,OP_CODE,PRODUCT_OFFERING_SUMMARYSHORT) " +
							"values(?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
//			long specId = getProductOfferingSpec(obj);
			pstmt = conn.prepareStatement(sql);
			if (DBConnect.isOracle()) {
				id = getSeq("SEQ_PRODUCT_OFFERING");
				pstmt.setLong(1, id);
			} else {
				//mysql 自增长
				pstmt.setNull(1, Types.NUMERIC);
			}
			pstmt.setString(2, obj.getProductOfferingCode());
			pstmt.setString(3, obj.getProductOfferingName());
			pstmt.setDouble(4, obj.getProductOfferingPrice());
			pstmt.setInt(5, obj.getChargeType());
			pstmt.setString(6, obj.getRemark());
			pstmt.setString(7, obj.getEffectiveTime());
			pstmt.setString(8, obj.getExpireTime());
			pstmt.setInt(9, obj.getProductOfferingType());
			pstmt.setInt(10, obj.getProductOfferingStatus());
			pstmt.setString(11, obj.getOpCode());
			pstmt.setString(12, obj.getProductOfferingSummaryshort());
			
			pstmt.execute();
			pstmt.close();
			pstmt = null;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			closeStatement(pstmt);
		}
		return id;		
	}
	
	/**
	 * 新增商品规格
	 * @param obj
	 * @return long
	 * @throws SQLException
	 */
//	private long getProductOfferingSpec(ProductOffering obj) throws SQLException{
//		long id = 0;
//		PreparedStatement pstmt = null;
//		String sql = "insert into T_PRODUCT_OFFER_SPEC values(?,?,?,?,?,?,?,?,?)";
//		try {
//			id = getID("select ID from T_PRODUCT_OFFER_SPEC where CHARGETYPE='"+obj.getChargeType()+"'");
//			if (id == 0) {
//				pstmt = conn.prepareStatement(sql);
//				id = getSeq("SEQ_PRODUCT_OFFER_SPEC");
//				pstmt.setLong(1, id);
//				pstmt.setString(2, String.valueOf(id));
//				pstmt.setString(3, Constant.getPoSpecName(obj.getChargeType()));
//				pstmt.setString(4, obj.getEffectiveTime());
//				pstmt.setString(5, obj.getExpireTime());
//				pstmt.setString(6, obj.getChargeType().toString());
//				pstmt.setString(7, "");
//				pstmt.setString(8, Constant.getPoSpecName(obj.getChargeType()));
//				pstmt.setInt(9, 0);
//
//				pstmt.execute();
//				pstmt.close();
//				pstmt = null;
//			}
//			
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			throw e;
//		} finally {
//			closeStatement(pstmt);
//		}
//		return id;		
//	}


	/**
	 * 删除相应运营商、产品的资源、资源包与资源的关系
	 * @param assetID
	 * @param operationCode
	 * @throws SQLException
	 */
	public void deleteAssetinfo(String poId, String assetID) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
//			if (isUseByOtherPackage(poId, assetID)) {
//				return;
//			}
			System.out.println("delete poId="+poId+" assetID="+assetID);
			pstmt = conn.prepareStatement("delete from T_ASSET_TRANSITION where ASSET_ID=? and OPERATION_CODE=?");
			pstmt.setString(1, assetID);
			pstmt.setString(2, this.operationCode);
			pstmt.execute();
			pstmt.close();
			pstmt = null;		
			
			pstmt = conn.prepareStatement("delete from T_RELA_CONPACK_CONTENT where CONTENTPACKAGE_CODE=? and OPERATION_CODE=?");
			pstmt.setString(1, assetID);
			pstmt.setString(2, this.operationCode);
			pstmt.execute();
			pstmt.close();
			pstmt = null;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			closeResultSet(rs);
			closeStatement(stmt);
			closeStatement(pstmt);
		}
	}

	
	/**
	 * 根据sql查询，返回前两列键值对
	 * @param sql
	 * @return Map<String,String>
	 * @throws SQLException
	 */
	private Map<String,String> getProductPackage(String sql) throws SQLException{
		Map<String,String> map = new HashMap<String,String>();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next())
				map.put(rs.getString(1),rs.getString(2));
			rs.close();
			rs = null;
			stmt.close();
			stmt = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			closeResultSet(rs);
			closeStatement(stmt);
		}
		return map;
	}
	
	/**
	 * 判断节目是否还存在于其他节目包中
	 * @param assetID
	 * @return boolean
	 * @throws SQLException
	 */
	private boolean isUseByOtherPackage(String poId, String assetID) throws SQLException{
		boolean result = false;
		StringBuffer sql = new StringBuffer("select count(*) from T_RELA_CONPACK_CONTENT t, T_ASSET_TRANSITION ta");
		sql.append(" where t.CONTENTPACKAGE_CODE=ta.ASSET_ID and ta.PRODUCT_OFFERING_CODE<>?")
			.append(" and t.CONTENT_CODE=? and t.OPERATION_CODE=?");
//		String sql = "select count(*) from T_RELA_CONPACK_CONTENT t where CONTENT_CODE='"+assetID+"' and OPERATION_CODE='"+this.operationCode+"'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			int count = 0;
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, poId);
			pstmt.setString(2, assetID);
			pstmt.setString(3, this.operationCode);
			rs = pstmt.executeQuery();
			if (rs.next())
				count = rs.getInt(1);
			rs.close();
			rs = null;
			pstmt.close();
			pstmt = null;
			if (count > 0)
				result = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			closeResultSet(rs);
			closeStatement(pstmt);
		}
		return result;
	}
	
	/**
	 * 判断节目是否还存在于其他节目包中
	 * @param assetID
	 * @return boolean
	 * @throws SQLException
	 */
	public Set<String> isUseByOtherPackageAssets(String poId) throws SQLException{
		Set<String> isUsedAsset = new HashSet<String>();
		StringBuffer sql = new StringBuffer("select distinct t.CONTENT_CODE from T_RELA_CONPACK_CONTENT t, T_ASSET_TRANSITION ta");
		sql.append(" where t.OPERATION_CODE=ta.OPERATION_CODE and t.CONTENTPACKAGE_CODE=ta.ASSET_ID")
			.append(" and ta.PRODUCT_OFFERING_CODE<>? and t.OPERATION_CODE=?");
		// select t.CONTENT_CODE,count(*) as count 
		// from T_RELA_CONPACK_CONTENT t where t.OPERATION_CODE='OP_YB' group by t.CONTENT_CODE order by count desc 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, poId);
			pstmt.setString(2, this.operationCode);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				isUsedAsset.add(rs.getString(1));
			}
			rs.close();
			rs = null;
			pstmt.close();
			pstmt = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			closeResultSet(rs);
			closeStatement(pstmt);
		}
		return isUsedAsset;
	}
	
	private long getID(String sql) throws SQLException{
		
		long result = 0;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if(rs.next())
				result = rs.getLong(1);
			rs.close();
			rs = null;
			stmt.close();
			stmt = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			closeResultSet(rs);
			closeStatement(stmt);
		}
		return result;
	}

	//获取指定序列值
	private long getSeq(String seqName) throws SQLException{
		StringBuffer sb = new StringBuffer();
		long result = 0;
		sb.append("select ").append(seqName).append(".nextval from dual");
		result = getID(sb.toString());
		return result;
	}
	
	private void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			rs = null;
		}
	}
	
	private void closeStatement(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			stmt = null;
		}
	}
	
	private void closeStatement(PreparedStatement pstmt) {
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pstmt = null;
		}
	}
	
	public void tranStart() throws SQLException{
		this.conn.setAutoCommit(false);
	}
	
	public void tranCommit() throws SQLException{
		conn.commit();
		conn.setAutoCommit(true);
	}
	
	public void tranRollback() throws SQLException{
		conn.rollback();
	}
	/**
	 * 修改树的状态（0-正常，1-修改，2-删除）
	 * @param state
	 * @param treeId
	 */
	public void updateTreeState(int state, String operationCode) {
		Statement stmt = null;
		StringBuffer sql = new StringBuffer();
		sql.append("update T_FOREST set STATE=").append(state).append(" where TREE_ID=")
			.append("(select tree_id from t_business_info where operator_code='").append(operationCode).append("')");
		try {
			stmt = conn.createStatement();
			stmt.execute(sql.toString());
			stmt.close();
			stmt = null;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			sql = null;
			closeStatement(stmt);
		}
	}
	
	
	public Map<String, String> getAssetinfoMap() {
		return assetinfoMap;
	}
	public Map<String, String> getDelAssetMap() {
		return delAssetMap;
	}

}
