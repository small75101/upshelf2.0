package com.avit.upshelf;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.avit.getDate.Constant;
import com.avit.upshelf.bean.AutoAction;
import com.avit.upshelf.bean.Category;
import com.avit.upshelf.bean.CategoryInfo;
import com.avit.util.InitConfig;
import com.avit.util.StringUtil;

public class DBConnect {
	Connection con = null;
	static String dbType = "";
	public static final String ORACLE = "oracle";
	public DBConnect(){	
		newConnection();
	}
	
	/** 
	  * 创建新连接 
	  * @return 
	  */ 
	private void newConnection() 
	{ 
		String driver = InitConfig.getConfigMap().get("driverClass");
		String url = InitConfig.getConfigMap().get("url");
		String user = InitConfig.getConfigMap().get("username");
		String password = InitConfig.getConfigMap().get("password");
		if (driver.indexOf(ORACLE) != -1) {
			dbType = ORACLE;
		}
		try { 
		   Class.forName(driver); 
		   con=DriverManager.getConnection(url, user, password); 
		} catch (ClassNotFoundException e) { 
		   e.printStackTrace(); 
		   System.out.println("sorry can't find db driver!"); 
		} catch (SQLException e1) { 
		   e1.printStackTrace(); 
		   System.out.println("sorry can't create Connection!"); 
		} 
	} 
	
	public static boolean isOracle(){
		if (dbType.equals(ORACLE)) {
			return true;
		} else
			return false;
	}
	
	/**
	 * 获取数据库连接
	 * @return
	 */
	public Connection getConnection(){
		Connection con = null;
		String driver = InitConfig.getConfigMap().get("driverClass");
		String url = InitConfig.getConfigMap().get("url");
		String user = InitConfig.getConfigMap().get("username");
		String password = InitConfig.getConfigMap().get("password");
		try { 
		   Class.forName(driver); 
		   con=DriverManager.getConnection(url, user, password); 
		} catch (ClassNotFoundException e) { 
		   e.printStackTrace(); 
		   System.out.println("sorry can't find db driver!"); 
		} catch (SQLException e1) { 
		   e1.printStackTrace(); 
		   System.out.println("sorry can't create Connection!"); 
		} 
		return con;
	}
	
	/**
	 * 获取创建DBConnect实例时所创建的Connection对象
	 * @return
	 */
	public Connection getLocalConnection(){
		return con;
	}
	/**
	 * 获取发送ids
	 * @return
	 */
	public String getSendServerIds(){
		StringBuffer sb = new StringBuffer();
		Statement stat = null;
		ResultSet rs = null;

		try {
			stat = con.createStatement();
			rs = stat.executeQuery("select id from t_send_destination t");
			while (rs.next()) {
				sb.append(rs.getInt(1)).append(",");
			}
			rs.close();
			rs = null;
			stat.close();
			stat = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				rs = null;
			}
			if(stat != null){
				try {
					stat.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				stat = null;
			}
		}
		if(sb.length()>0)
			sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	/**
	 * 获取栏目分类序列(0-Category,1-CategoryInfo)
	 * @return
	 */
	public long getCategorySeq(int flag) {
		long id = 0;
		try {
			if (flag == 0) {
				id = getSeq("SEQ_CATEGORY");
			} else {
				id = getSeq("SEQ_CATEGORYINFO");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
	
	/**
	 * 获取栏目分类模板ID
	 * @return
	 */
	public long getTemplateId(Long parentId,String resourceType) {
		Statement stat = null;
		ResultSet rs = null;
		long templateId = 0 ;
		try {
			stat = con.createStatement();
			rs = stat.executeQuery("select t.template_id from t_category t " +
					"where t.parent_id="+parentId+" and t.resource_type='"+resourceType+"'");
			while (rs.next()) {
				templateId = rs.getLong("template_id");
				break;

			}
			rs.close();
			rs = null;
			stat.close();
			stat = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				rs = null;
			}
			if(stat != null){
				try {
					stat.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				stat = null;
			}
		}
		return templateId;
	}
	
	/**
	 * 根据类型获取默认模板
	 * @param type
	 * @return
	 */
	public long getDefaultTempateIdByType(String type){
		Statement stat = null;
		ResultSet rs = null;
		long templateId = 0 ;
		try {
			stat = con.createStatement();
			rs = stat.executeQuery("select id from T_TEMPLATE where  isdefault=1 and templatetype='"+type+"'");
			while (rs.next()) {
				templateId = rs.getLong("id");
				break;

			}
			rs.close();
			rs = null;
			stat.close();
			stat = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				rs = null;
			}
			if(stat != null){
				try {
					stat.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				stat = null;
			}
		}
		return templateId;
	}
	
	/**
	 * 根据分类节点ID递归查询所有子节点ID对应的MAP
	 */
	public Map<Long,Category> getSubCateIdMap(Long cateId){
		Statement stmt = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		Map<Long,Category> map = new HashMap<Long,Category>();
		String sql = "select * from T_CATEGORY_BACKUP t where t.RESOURCE_TYPE in ('1','2','8','9') connect by prior t.ID=t.PARENT_ID start with t.ID="+cateId;
		
		try {
			if (isOracle()) {
				stmt = con.createStatement();
				rs = stmt.executeQuery(sql);
			} else {
				//mysql
				rs = getCategoryListForMySQL(cstmt, cateId, " and RESOURCE_TYPE in ('1','2','8','9')");
			}
			while (rs.next()) {
				Category cate = new Category();
				setCategory(rs, cate);
				map.put(rs.getLong("ID"), cate);
			}
			rs.close();
			rs = null;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStatement(stmt);
			closeStatement(cstmt);
		}
		map.remove(cateId);
		return map;
	}
	
	/**
	 * 判断是否存在分类
	 * （非根节点，资源和资源包根据类型、父节点ID、资源ID、产品ID查询；分类目录根据类型、父节点ID、资源名称查询）
	 * @param cate
	 * @return
	 */
	public Category getCategory(Category cate){
		Statement stmt = null;
		ResultSet rs = null;
		Category category = null;
		StringBuffer sql = new StringBuffer("select * from T_CATEGORY_BACKUP where 1=1 ");
		
		if (cate != null) {
			if(cate.getId() != null){
				sql.append(" and ID = "+cate.getId());
			}
			String resourceType = cate.getResourceType();
			if(StringUtils.isNotEmpty(resourceType)){
				sql.append(" and RESOURCE_TYPE='"+resourceType+"' ");
				
				if(cate.getParentId() != null){
					sql.append(" and PARENT_ID="+cate.getParentId());
				}
				
				if(Constant.CATEGORY_TYPE_ASSET.equals(resourceType) 
						|| Constant.CATEGORY_TYPE_ASSETP.equals(resourceType)){
					//资源ID、产品ID
					if(StringUtils.isNotEmpty(cate.getResourceId())){
						sql.append(" and RESOURCE_ID='"+cate.getResourceId()+"' ");
					}
					if(StringUtils.isNotEmpty(cate.getProductId())){
						sql.append(" and PRODUCT_ID='"+cate.getProductId()+"' ");
					}
				}else{
					//资源名称
					if(StringUtils.isNotEmpty(cate.getResourceName())){
						sql.append(" and RESOURCE_NAME='"+cate.getResourceName()+"' ");
					}
				}
			}
		}
//		System.out.println("query sql==="+sql.toString());
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql.toString());
			if (rs.next()) {
				category = new Category();
				setCategory(rs, category);
			}
//			if (category == null) {
//				if (!Constant.CATEGORY_TYPE_CATE.equals(resourceType)) {
//				}
//			}

			rs.close();
			rs = null;
			stmt.close();
			stmt = null;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			sql = null;
			closeResultSet(rs);
			closeStatement(stmt);
		}
		return category;
	}
	
	private void setCategory(ResultSet rs, Category category) throws SQLException {
		category.setId(rs.getLong("id"));
		category.setResourceType(rs.getString("resource_type"));
		category.setResourceId(rs.getString("resource_id"));
		category.setResourceName(rs.getString("resource_name"));
		category.setParentId(rs.getLong("parent_id"));
		category.setResourceOrder(rs.getLong("resource_order"));
		category.setResourcePath(rs.getString("resource_path"));
		category.setResourceShowPath(rs.getString("resource_show_path"));
		category.setResourceStencil(rs.getLong("resource_stencil"));
		category.setState(rs.getString("state"));
		category.setTemplateId(rs.getLong("template_id"));
		category.setProductId(rs.getString("product_id"));
		category.setIsAdv(rs.getString("is_adv"));
		category.setBusinessType(rs.getString("business_type"));
		category.setIsFocus(rs.getString("is_focus"));
		category.setIsCommend(rs.getString("is_commend"));
		category.setCreateTime(rs.getDate("create_time"));
		category.setLinkId(rs.getLong("link_id"));
		category.setUIType(rs.getString("uitype"));
	}
	
	/**
	 * 判断资源是否存在
	 * @param assetId
	 * @return boolean
	 */
	public boolean hasAssetByAssetId(String assetId, String operationCode) {
		boolean flag = false;
		StringBuffer sql = new StringBuffer();
		try {
			sql.append("select count(*) from T_ASSET_TRANSITION")
				.append(" where ASSET_ID='").append(assetId).append("'")
				.append(" and STATE='").append(Constant.ASSET_STATE_NORMAL).append("'")
				.append(" and OPERATION_CODE='").append(operationCode).append("'");
			long id = getID(sql.toString());
			if (id != 0) {
				flag = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			sql = null;
		}
		return flag;
	}
	
	/**
	 * 根据运营商查询树根ID
	 * @param operationCode
	 * @return Long
	 */
	public Long getRootId(String operationCode) {
		Long id = 0L;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("select TREE_ID from T_BUSINESS_INFO where OPERATOR_CODE='"+operationCode+"'");
			if (rs.next()) {
				id = rs.getLong(1);
			}
			rs.close();
			rs = null;
			stmt.close();
			stmt = null;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStatement(stmt);
		}
		return id;
	}
	
	/**
	 * 根据运营商查询业务ID和名称
	 * @param operationCode
	 * @return Long
	 */
	public String getBusiness(String operationCode) {
		String result = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("select ID, BUSINESS_NAME from T_BUSINESS_INFO where OPERATOR_CODE='"+operationCode+"'");
			if (rs.next()) {
				result = rs.getLong(1) + "_" + rs.getString(2);
			}
			rs.close();
			rs = null;
			stmt.close();
			stmt = null;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStatement(stmt);
		}
		return result;
	}
	
	/**
	 * 获取链接目标分类的ID
	 * @param rootId
	 * @param resourceId
	 * @return Long
	 */
	public Long getLinkId(Long rootId, String resourceId) {
		Long id = null;
		Statement stmt = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		try {
			if (isOracle()) {
				stmt = con.createStatement();
				rs = stmt.executeQuery("select ID from T_CATEGORY_BACKUP t where t.RESOURCE_ID='"+resourceId+"' connect by prior t.ID=t.PARENT_ID start with t.ID="+rootId+"");
			} else {
				//mysql
				cstmt = con.prepareCall("{call getLinkId(?,?)}");
				cstmt.setLong(1, rootId);
				cstmt.setString(2, resourceId);
				cstmt.execute();
				rs = cstmt.getResultSet();
			}
			if (rs.next()) {
				id = rs.getLong("ID");
			}
			rs.close();
			rs = null;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStatement(stmt);
			closeStatement(cstmt);
		}
		return id;
	}
	
	/**
	 * 获取链接目标分类的ID
	 * @param rootId
	 * @param resourceId
	 * @return Long
	 */
	public Long getLinkId(String operationCode, String resourceId) {
		Long id = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("select ID from T_CATEGORY_BACKUP t where t.RESOURCE_ID='"+resourceId+"' and t.OPERATION_CODE='"+operationCode+"'");
			if (rs.next()) {
				id = rs.getLong("ID");
			}
			rs.close();
			rs = null;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStatement(stmt);
		}
		return id;
	}
	
	/**
	 * 获取分类排序
	 * @param cateId
	 * @return
	 */
	public Map<Long,Long> getResourceOrderMap(Long cateId){
		Map<Long,Long> orderMap = new HashMap<Long,Long>();
		Statement stmt = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		String sql = "select t.ID,t.RESOURCE_ORDER from T_CATEGORY_BACKUP t connect by prior t.ID=t.PARENT_ID start with t.ID="+cateId;
		
		try {
			if (isOracle()) {
				stmt = con.createStatement();
				rs = stmt.executeQuery(sql);
			} else {
				//mysql
				rs = getCategoryListForMySQL(cstmt, cateId, "");
			}
			while (rs.next()) {
				orderMap.put(rs.getLong("ID"), rs.getLong("RESOURCE_ORDER"));
			}
			rs.close();
			rs = null;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStatement(stmt);
			closeStatement(cstmt);
		}
		return orderMap;
	}
	
	/**
	 * 新增栏目信息(Category 和CategoryInfo)
	 * @param categoryList
	 */
	public void addCategory(List<Category> categoryList) {
		
		if(categoryList == null || categoryList.size()<=0){
			return;
		}
		PreparedStatement pstmt = null;
		PreparedStatement pstmt_2 = null;
		try {
			int count = 0;
			int count_2 = 0;
			pstmt = con.prepareStatement("insert into T_CATEGORY_BACKUP values(?,?,?,?,?,?,?,?,null,?,?,?,?,?,?,?,?,?,?,?)");
			pstmt_2 = con.prepareStatement("insert into T_CATEGORYINFO values(?,?,?,?,?,?,?,?,?,?,?)");
			
			for(Category cate : categoryList){
            	count++;
            	String resourceType = cate.getResourceType();
            	pstmt.setLong(1, cate.getId());
            	pstmt.setString(2, resourceType);
            	pstmt.setString(3, cate.getResourceId());
            	pstmt.setString(4, cate.getResourceName());
            	pstmt.setLong(5, cate.getParentId());
            	pstmt.setLong(6, cate.getResourceOrder());
            	pstmt.setString(7, cate.getResourcePath());
            	pstmt.setString(8, cate.getResourceShowPath());
            	pstmt.setString(9, cate.getState());
            	pstmt.setLong(10, cate.getTemplateId());
            	pstmt.setString(11, cate.getProductId());
            	pstmt.setString(12, cate.getIsAdv());
            	pstmt.setString(13, cate.getBusinessType());
            	pstmt.setString(14, cate.getIsFocus());
            	pstmt.setString(15, cate.getIsCommend());
            	pstmt.setDate(16, new java.sql.Date(new Date().getTime()));
				if (cate.getLinkId() != null) {
					pstmt.setLong(17, cate.getLinkId());
				} else {
					pstmt.setNull(17, Types.NUMERIC);
				}
            	pstmt.setString(18, cate.getOperationCode());
            	if(cate.getUIType()!=null){
            		pstmt.setNull(19, Types.VARCHAR);
            	}else{
            		pstmt.setString(19, cate.getUIType());
            	}
            	pstmt.addBatch();
            	
            	if (Constant.CATEGORY_TYPE_CATE.equals(resourceType)
            			|| Constant.CATEGORY_TYPE_LINK.equals(resourceType)) {
            		count_2++;
            		if (isOracle()) {
            			long id = getSeq("HIBERNATE_SEQUENCE");
            			pstmt_2.setLong(1, id);
					} else {
						//mysql 自增长
						pstmt_2.setNull(1, Types.NUMERIC);
					}
            		pstmt_2.setLong(2, cate.getId());
                	pstmt_2.setString(3, cate.getResourceName());
                	pstmt_2.setString(4, "");
                	pstmt_2.setString(5, cate.getResourceName());
                	pstmt_2.setString(6, resourceType);
                	pstmt_2.setString(7, "");
                	pstmt_2.setTimestamp(8, new Timestamp(new Date().getTime()));
                	pstmt_2.setTimestamp(9, new Timestamp(new Date().getTime()));
                	pstmt_2.setString(10, cate.getState());
                	
                	if(cate.getUIType()!=null){
                		pstmt_2.setNull(11, Types.VARCHAR);
                	}else{
                		pstmt_2.setString(11, cate.getUIType());
                	}
                	
                	pstmt_2.addBatch();
				}
            	
				if (count % 1000 == 0) {
					pstmt.executeBatch();
					pstmt.clearBatch();
				}
				if (count_2 % 1000 == 0) {
					pstmt_2.executeBatch();
					pstmt_2.clearBatch();
				}
            }
            pstmt.executeBatch();
            pstmt.close();
            pstmt = null;
            
            pstmt_2.executeBatch();
            pstmt_2.close();
            pstmt_2 = null;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeStatement(pstmt);
			closeStatement(pstmt_2);
		}
	}
	
	/**
	 * 新增栏目信息
	 * @param categoryList
	 */
	public void insertCategory(List<Category> categoryList) {
		
		if(categoryList == null || categoryList.size()<=0){
			return;
		}
		PreparedStatement pstmt = null;
		try {
			int count = 0;
			pstmt = con.prepareStatement("insert into T_CATEGORY_BACKUP values(?,?,?,?,?,?,?,?,null,?,?,?,?,?,?,?,?,?)");
	         
			for(Category cate : categoryList){
            	count++;
            	pstmt.setLong(1, cate.getId());
            	pstmt.setString(2, cate.getResourceType());
            	pstmt.setString(3, cate.getResourceId());
            	pstmt.setString(4, cate.getResourceName());
            	pstmt.setLong(5, cate.getParentId());
            	pstmt.setLong(6, cate.getResourceOrder());
            	pstmt.setString(7, cate.getResourcePath());
            	pstmt.setString(8, cate.getResourceShowPath());
            	pstmt.setString(9, cate.getState());
            	pstmt.setLong(10, cate.getTemplateId());
            	pstmt.setString(11, cate.getProductId());
            	pstmt.setString(12, cate.getIsAdv());
            	pstmt.setString(13, cate.getBusinessType());
            	pstmt.setString(14, cate.getIsFocus());
            	pstmt.setString(15, cate.getIsCommend());
            	pstmt.setDate(16, new java.sql.Date(new Date().getTime()));
				if (cate.getLinkId() != null) {
					pstmt.setLong(17, cate.getLinkId());
				} else {
					pstmt.setNull(17, Types.NUMERIC);
				}
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
			e.printStackTrace();
		} finally {
			closeStatement(pstmt);
		}
	}
	
	/**
	 * 新增栏目信息
	 * @param categoryList
	 */
	public void insertCategoryInfo(List<CategoryInfo> cateInfoList) {
		
		if(cateInfoList == null || cateInfoList.size()<=0){
			return;
		}
		PreparedStatement pstmt = null;
		try {
			int count = 0;
			pstmt = con.prepareStatement("insert into T_CATEGORYINFO values(?,?,?,?,?,?,?,?,?,?)");
	         
			for(CategoryInfo cate : cateInfoList){
            	count++;
            	pstmt.setLong(1, cate.getId());
            	pstmt.setLong(2, cate.getCategoryId());
            	pstmt.setString(3, cate.getCategoryName());
            	pstmt.setString(4, cate.getPosterUrl());
            	pstmt.setString(5, cate.getCategoryDesc());
            	pstmt.setString(6, cate.getType());
            	pstmt.setString(7, cate.getBizId());
            	pstmt.setTimestamp(8, new Timestamp(new Date().getTime()));
            	pstmt.setTimestamp(9, new Timestamp(new Date().getTime()));
            	pstmt.setString(10, cate.getState());

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
			e.printStackTrace();
		} finally {
			closeStatement(pstmt);
		}
	}
	
	/**
	 * 删除分类信息(Category和CategoryInfo)
	 * @param delCateIdMap
	 */
	public void delCategory(Map<Long,Category> delCateIdMap) {
		
		if(delCateIdMap == null || delCateIdMap.size()<=0){
			return;
		}
		PreparedStatement pstmt = null;
		PreparedStatement pstmt_2 = null;
		try {
			int count = 0;
			int count_2 = 0;
			pstmt = con.prepareStatement("delete from T_CATEGORY_BACKUP where ID = ?");
			pstmt_2 = con.prepareStatement("delete from T_CATEGORYINFO where CATEGORYID=?");
            
			Iterator<Long> set = delCateIdMap.keySet().iterator();
            long cateId = 0;
            while(set.hasNext()){
            	cateId = set.next();
            	String resourceType = delCateIdMap.get(cateId).getResourceType();
            	count++;
        		pstmt.setLong(1, cateId);
        		pstmt.addBatch();
        		System.out.println("删除的backup id = "+cateId);
            	if(Constant.CATEGORY_TYPE_CATE.equals(resourceType)
            			|| Constant.CATEGORY_TYPE_LINK.equals(resourceType)){
                	count_2++;
                	pstmt_2.setLong(1, cateId);
                	pstmt_2.addBatch();
            	}
            	
				if (count % 1000 == 0) {
					pstmt.executeBatch();
					pstmt.clearBatch();
				}
				if (count_2 % 1000 == 0) {
					pstmt_2.executeBatch();
					pstmt_2.clearBatch();
				}
            }
            pstmt.executeBatch();
            pstmt.close();
            pstmt = null;
            
            pstmt_2.executeBatch();
            pstmt_2.close();
            pstmt_2 = null;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeStatement(pstmt);
			closeStatement(pstmt_2);
		}
	}
	
	/**
	 * 修改分类信息(Category和CategoryInfo)
	 * @param updateCategoryList
	 */
	public void updateCategory(List<Category> updateCategoryList) {
		
		if(updateCategoryList == null || updateCategoryList.size()<=0){
			return;
		}
		PreparedStatement pstmt = null;
		PreparedStatement pstmt_2 = null;
		try {
			int count = 0;
			int count_2 = 0;
			pstmt = con.prepareStatement("update T_CATEGORY_BACKUP set RESOURCE_NAME=?,RESOURCE_ORDER=?,RESOURCE_SHOW_PATH=?,LINK_ID=? where id=? ");
			pstmt_2 = con.prepareStatement("update T_CATEGORYINFO set CATEGORYNAME=?,CATEGORYDESC=?,MODIFYTIME=? where CATEGORYID=? ");

			for(Category cate : updateCategoryList){
				count++;
				pstmt.setString(1, cate.getResourceName());
				pstmt.setLong(2, cate.getResourceOrder());
				pstmt.setString(3, cate.getResourceShowPath());
				if (cate.getLinkId() != null) {
					pstmt.setLong(4, cate.getLinkId());
				} else {
					pstmt.setNull(4, Types.NUMERIC);
				}
				pstmt.setLong(5, cate.getId());
				pstmt.addBatch();
				
				if (Constant.CATEGORY_TYPE_CATE.equals(cate.getResourceType())
						|| Constant.CATEGORY_TYPE_LINK.equals(cate.getResourceType())) {
					count_2++;
					pstmt_2.setString(1, cate.getResourceName());
					pstmt_2.setString(2, cate.getResourceName());
					pstmt_2.setTimestamp(3, new Timestamp(new Date().getTime()));
					pstmt_2.setLong(4, cate.getId());
					pstmt_2.addBatch();
				}
				
				if (count % 1000 == 0) {
					pstmt.executeBatch();
					pstmt.clearBatch();
				}
				if (count_2 % 1000 == 0) {
					pstmt_2.executeBatch();
					pstmt_2.clearBatch();
				}
            }
            pstmt.executeBatch();
            pstmt.close();
            pstmt = null;
            
            pstmt_2.executeBatch();
            pstmt_2.close();
            pstmt_2 = null;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeStatement(pstmt);
			closeStatement(pstmt_2);
		}
	}
	
	/**
	 * 关闭连接
	 */
	public void closeConnection(){
		if(con != null){
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			con = null;
		}
	}

	/**
	 * 获取AutoAction的id列表
	 * @return List<String>
	 */
	public List<String> getAutoIds(String operationCode){
		List<String> ids = new ArrayList<String>();
		Statement stmt = null;
		ResultSet rs = null;
		
		String sql = "select id from t_autoaction where operation_code='"+operationCode+"'";
		try {	
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				ids.add(rs.getString("id"));
			}
			rs.close();
			rs = null;
			stmt.close();
			stmt = null;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStatement(stmt);
		}
		return ids;
	}
	
	/**
	 * 获取AutoAction信息
	 * @param id
	 * @return List<AutoAction>
	 */
	public List<AutoAction> queryAutoAction(AutoAction obj) {
		List<AutoAction> autos = new ArrayList<AutoAction>();
		Statement stmt = null;
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from t_autoaction where 1=1");
		if (obj.getId() != 0) {
			sql.append(" and id = ").append(obj.getId());
		}
		if (obj.getMenuid() != 0) {
			sql.append(" and menuid = ").append(obj.getMenuid());
		}
		
		try {	
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql.toString());
			while(rs.next()){
				AutoAction auto = new AutoAction();
				auto.setId(rs.getInt("id"));
				auto.setUrl(rs.getString("url"));
				auto.setState(rs.getInt("state"));
				auto.setIsRun(rs.getInt("isrun"));
				auto.setIsRsync(rs.getInt("isrsync"));
				auto.setIsUptree(rs.getInt("isuptree"));
				auto.setIsBuildPage(rs.getInt("isbuildpage"));
				auto.setIsUpload(rs.getInt("isupload"));
				auto.setRsyncUrl(rs.getString("rsyncurl"));
				auto.setSleeptime(rs.getInt("sleeptime"));
				auto.setPoid(rs.getString("poid"));
				auto.setMenuid(rs.getLong("menuid"));
				auto.setTreeroot(rs.getString("treeroot"));
				auto.setUptreeFlag(rs.getInt("uptree_flag"));
				auto.setOperationCode(rs.getString("operation_code"));
				autos.add(auto);
			}
			rs.close();
			rs = null;
			stmt.close();
			stmt = null;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			sql = null;
			closeResultSet(rs);
			closeStatement(stmt);
		}
		return autos;
	}
	/**
	 * 根据ID查询同步表信息
	 * @param id
	 * @return AutoAction
	 */
	public AutoAction getAutoActionById(String id) {
		AutoAction a = new AutoAction();
		a.setId(StringUtil.toInt(id));
		List<AutoAction> list = queryAutoAction(a);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * 根据菜单ID查询同步表信息
	 * @param menuId
	 * @return
	 */
	public AutoAction getAutoActionByMenuId(Long menuId) {
		AutoAction a = new AutoAction();
		a.setMenuid(StringUtil.toLong(menuId));
		List<AutoAction> list = queryAutoAction(a);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * 更新自动上架当前状态
	 */
	public void updateAutoState(int state, int id, String operationCode) {
		
		Statement stmt = null;
		StringBuffer sql = new StringBuffer();
		//当前进行的状态 0等待，1同步数据，2上架，3生成静态页面，xml，js；4发布
		sql.append("update t_autoaction set state=").append(state).append(" where 1=1");
		if (id != 0) {
			sql.append(" and id=").append(id);
		}
		if (StringUtils.isNotEmpty(operationCode)) {
			sql.append(" and operation_code='").append(operationCode).append("'");
		}

		try {	
			stmt = con.createStatement();
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
	
	/**
	 * 修改上架默认标识
	 */
	public void updateDefaulUpTreeFlag(String operationCode) {
		
		//去运营商的自动操作状态为2正在上架的ids
		List<Integer>  autoIdList = this.queryRunUpTree(operationCode);
		if(autoIdList == null || autoIdList.size()<=0){
			return;
		}
		PreparedStatement pstmt = null;
		try {	
			pstmt = con.prepareStatement("update t_autoaction set uptree_flag=1 where id =?"); //允许上架标识  0 不允许上架  1 允许上架'  2 正在上架
			for(Integer id : autoIdList){
				pstmt.setInt(1, id);
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			pstmt.close();
			pstmt = null;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeStatement(pstmt);
		}
	}
	
	/**
	 * 查询所有正在运行上架标识的记录ID
	 * @return
	 */
	public List<Integer> queryRunUpTree(String operationCode) {
		List<Integer> autoIdList = new ArrayList<Integer>();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("select id from t_autoaction where uptree_flag=2 and operation_code='"+operationCode+"'");
			while(rs.next()){
				autoIdList.add(rs.getInt("id"));
			}
			rs.close();
			rs = null;
			stmt.close();
			stmt = null;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStatement(stmt);
		}
		return autoIdList;
	}
	
	/**
	 * 根据菜单ID修改上架标识（0-不允许上架，1-允许上架，2-正在上架）
	 * update t_autoaction set uptree_flag=? where menuid=?
	 * @param upTreeFlag
	 * @param menuId
	 */
	public void updateUpTreeByMenuId(int upTreeFlag, Long menuId) {
		
		Statement stmt = null;
		StringBuffer sql = new StringBuffer();
		sql.append("update t_autoaction set uptree_flag=").append(upTreeFlag).append(" where menuid=").append(menuId);;
		try {	
			stmt = con.createStatement();
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
	/**
	 * 修改树的状态（0-正常，1-修改，2-删除）
	 * @param state
	 * @param treeId
	 */
	public void updateTreeState(int state, Long treeId) {
		Statement stmt = null;
		StringBuffer sql = new StringBuffer();
		sql.append("update T_FOREST set STATE=").append(state).append(" where TREE_ID=").append(treeId);
		try {	
			stmt = con.createStatement();
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
	/**
	 * 获取树的状态（0-正常，1-修改，2-删除）
	 * @param treeId
	 */
	public int getTreeState(String operationCode) {
		Statement stmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("select t.state from t_forest t, t_business_info b where t.tree_id = b.tree_id and b.operator_code='"+operationCode+"'");
			while(rs.next()){
				result = rs.getInt(1);
			}
			rs.close();
			rs = null;
			stmt.close();
			stmt = null;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStatement(stmt);
		}
		return result;
	}
	
	public void cleanPackageInfo(String operationCode){
		Statement stmt = null;
		ResultSet rs = null;
		int result = 0;
		//select asset_id from t_asset_transition t where t.content_type=3 and t.OPERATION_CODE='OP_YB'
		//select distinct p.content_code from t_rela_conpack_content p where p.OPERATION_CODE='OP_YB'
		
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("select distinct p.content_code from t_rela_conpack_content p where p.OPERATION_CODE='"+operationCode+"'");
			Map<String,String> packages = new HashMap<String,String>();
			while(rs.next()){
				packages.put(rs.getString(1),rs.getString(1));
			}
			rs.close();
			rs = null;
			rs = stmt.executeQuery("select asset_id from t_asset_transition t where t.content_type=3 and t.OPERATION_CODE='"+operationCode+"'");			
			String[] assets = new String[100000];
			int i = 0;
			while(rs.next()){				
				assets[i++] = rs.getString(1);
			}
			rs.close();
			rs = null;
			con.setAutoCommit(false);
			for(int j=0;j<i;j++){
				if(!packages.containsKey(assets[j]))
					stmt.execute("delete t from t_asset_transition t where t.asset_id='1' and t.operation_code='"+operationCode+"'");
			}
			con.commit();
			//stmt.execute("delete t from t_asset_transition t where t.content_type=3  and t.asset_id not in (select distinct p.content_code from t_rela_conpack_content p where p.OPERATION_CODE='"+operationCode+"') and t.OPERATION_CODE='"+operationCode+"' ");
			stmt.close();
			stmt = null;
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			closeResultSet(rs);
			closeStatement(stmt);
		}
	}
	
	//获取指定序列值
	private long getSeq(String seqName) throws SQLException{
		StringBuffer sb = new StringBuffer();
		long result = 0;
		sb.append("select ").append(seqName).append(".nextval from dual");
		result = getID(sb.toString());
		return result;
	}
	
	//oracle、mysql 获取t_category表的序号
	public long getCategorySeqForAll() {
		long result = 0;
		CallableStatement cstmt = null;
		try {
			cstmt = con.prepareCall("{? = call getCategorySeq()}");
			cstmt.registerOutParameter(1, Types.NUMERIC);
			cstmt.execute();
			result = cstmt.getLong(1);
			cstmt.close();
		} catch (Exception e) {
			closeStatement(cstmt);
		}
		return result;
	}
	
	//MySQL 获取t_category树型列表
	public ResultSet getCategoryListForMySQL(CallableStatement cstmt, Long nodeId, String condition) {
		ResultSet rs = null;
		try {
			cstmt = con.prepareCall("{call findCategoryBackupList(?,?)}");
			cstmt.setLong(1, nodeId);
			cstmt.setString(2, condition);
			cstmt.execute();
			rs = cstmt.getResultSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	private long getID(String sql) throws SQLException{
		
		long result = 0;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
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
	
	private void closeStatement(CallableStatement cstmt) {
		if (cstmt != null) {
			try {
				cstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cstmt = null;
		}
	}
	
}
