package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.utils.NumberUtil;
import com.javaweb.utils.StringUtil;

@Repository
public class BuildingRepositoryImpl implements BuildingRepository{
	static final String DB_URL = "jdbc:mysql://localhost:3306/estatebasic"; 
	static final String USER = "root";
	static final String PASS = "123456";
	
	public static void joinTable(Map<String, Object> params, List<String> typeCode, StringBuilder sql) {
		String staffId = (String)params.get("staffId");
		if (StringUtil.checkString(staffId)) {
			sql.append(" INNER JOIN assignmentbuilding AS ass ON  ass.buildingid = b.id ");
		}
		String rentAreaFrom = (String)params.get("areaFrom");
		String rentAreaTo = (String)params.get("areaTo");
		if (StringUtil.checkString(rentAreaFrom) || StringUtil.checkString(rentAreaTo)) {
			sql.append(" INNER JOIN rentarea ON rentarea.buildingid = b.id ");
		}
		if (typeCode != null && typeCode.size() != 0) {
			sql.append(" INNER JOIN buildingrenttype AS br ON br.buildingid = b.id ");
			sql.append(" INNER JOIN renttype ON renttype.id = br.renttypeid ");
		}
	}
	
	public static void queryNomal(Map<String, Object> params, StringBuilder where) {
		for (Map.Entry<String, Object> it : params.entrySet()) {
			if (!it.getKey().equals("staffId") && !it.getKey().equals("typeCode") && 
					!it.getKey().startsWith("area") && !it.getKey().startsWith("rentPrice")) {
				String value = it.getValue().toString();
				if (StringUtil.checkString(value)) {
					if (NumberUtil.isNumber(value)) {
						where.append(" AND b." + it.getKey() + " = " + value );
					} else {
						where.append(" AND b." + it.getKey() +  " LIKE '%" + value + "%' ");
					}
				}
			}
		}
	}
	
	public static void querySpecial(Map<String, Object> params, List<String> typeCode, StringBuilder where) {
		String staffId = (String)params.get("staffId");
		if (StringUtil.checkString(staffId)) {
			where.append(" AND ass.staffid = " + staffId);
		}
		String rentAreaFrom = (String)params.get("areaFrom");
		String rentAreaTo = (String)params.get("areaTo");
		if (StringUtil.checkString(rentAreaFrom) || StringUtil.checkString(rentAreaTo)) {
			if (StringUtil.checkString(rentAreaFrom)) {
				where.append(" AND rentarea.value >= " + rentAreaFrom);
			}
			if (StringUtil.checkString(rentAreaTo)) {
				where.append(" AND rentarea.value <= " + rentAreaTo);
			}
		}
		String rentPriceFrom = (String)params.get("rentPriceFrom");
		String rentPriceTo = (String)params.get("rentPriceTo");
		if (StringUtil.checkString(rentPriceFrom) || StringUtil.checkString(rentPriceTo)) {
			if (StringUtil.checkString(rentPriceFrom)) {
				where.append(" AND b.rentprice >= " + rentPriceFrom);
			}
			if (StringUtil.checkString(rentPriceTo)) {
				where.append(" AND b.rentprice <= " + rentPriceTo);
			}
		}
		if (typeCode != null && typeCode.size() != 0) {
			List<String> convert = new ArrayList<String>();
			for (String it : typeCode) {
				convert.add("'" + it + "'");
			}
			where.append(" AND renttype.code IN (" + String.join(",", convert) + ") ");
		}
	}
	
	@Override
	public List<BuildingEntity> findAll(Map<String, Object> params, List<String> typeCode) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder("SELECT b.id, b.name, b.districtid, b.street, b.ward, b.numberofbasement, b.floorarea, b.rentprice, b.managername, b.managerphonenumber, b.servicefee, b.brokeragefee FROM building b ");
		joinTable(params, typeCode, sql);
		StringBuilder where = new StringBuilder("WHERE 1 = 1 ");
		queryNomal(params, where);
		querySpecial(params, typeCode, where);
		where.append(" GROUP BY b.id");
		sql.append(where);
		List<BuildingEntity> result = new ArrayList<BuildingEntity>();
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				Statement sttm = conn.createStatement();
				ResultSet rs = sttm.executeQuery(sql.toString());){
			
			while (rs.next()) {
				BuildingEntity buildingEntity = new BuildingEntity();
				buildingEntity.setId(rs.getLong("id"));
				buildingEntity.setName(rs.getString("name"));
				buildingEntity.setNumberOfBasement(rs.getInt("numberofbasement"));
				buildingEntity.setStreet(rs.getString("street"));
				buildingEntity.setWard(rs.getString("ward"));
				buildingEntity.setDistrictId(rs.getLong("districtId"));
				buildingEntity.setFloorArea(rs.getLong("floorArea"));
				buildingEntity.setRentPrice(rs.getLong("rentPrice"));
				buildingEntity.setManagerName(rs.getString("managerName"));
				buildingEntity.setManagerPhonenumber(rs.getString("managerPhonenumber"));
				buildingEntity.setServiceFee(rs.getString("serviceFee"));
				buildingEntity.setBrokerageFee(rs.getLong("brokerageFee"));
				result.add(buildingEntity);
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Connect database failed...");
		}
		return result;
	}

}
