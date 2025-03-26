package com.javaweb.repository.impl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.javaweb.builder.BuildingSearchBuilder;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.utils.ConnectionJDBCUtil;

@Repository
public class BuildingRepositoryImpl implements BuildingRepository{
	
	public static void joinTable(BuildingSearchBuilder buildingSearchBuilder, StringBuilder sql) {
		Long staffId = buildingSearchBuilder.getStaffId();
		if (staffId != null) {
			sql.append(" INNER JOIN assignmentbuilding AS ass ON  ass.buildingid = b.id ");
		}
		List<String> typeCode = buildingSearchBuilder.getTypeCode();
		if (typeCode != null && typeCode.size() != 0) {
			sql.append(" INNER JOIN buildingrenttype AS br ON br.buildingid = b.id ");
			sql.append(" INNER JOIN renttype ON renttype.id = br.renttypeid ");
		}
	}
	
	public static void queryNomal(BuildingSearchBuilder buildingSearchBuilder, StringBuilder where) {	
		try {
			Field[] fields = BuildingSearchBuilder.class.getDeclaredFields();
			for (Field item : fields) {
				item.setAccessible(true);
				String fieldName = item.getName();
				if (!fieldName.equals("staffId") && !fieldName.equals("typeCode") && 
						!fieldName.startsWith("area") && !fieldName.startsWith("rentPrice")) {
					Object value = item.get(buildingSearchBuilder);
					if (value != null) {
						if (item.getType().getName().equals("java.lang.Long") || item.getType().getName().equals("java.lang.Integer")) {
							where.append(" AND b." + fieldName + " = " + value );
						} else if (item.getType().getName().equals("java.lang.String")) {
							where.append(" AND b." + fieldName +  " LIKE '%" + value + "%' ");
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void querySpecial(BuildingSearchBuilder buildingSearchBuilder, StringBuilder where) {
		Long staffId = buildingSearchBuilder.getStaffId();
		if (staffId != null) {
			where.append(" AND ass.staffid = " + staffId);
		}
		Long rentAreaFrom = buildingSearchBuilder.getAreaFrom();
		Long rentAreaTo = buildingSearchBuilder.getAreaTo();
		if (rentAreaFrom != null || rentAreaTo != null) {
			where.append(" AND EXISTS (SELECT * FROM rentarea r WHERE b.id = r.buildingid ");
			if (rentAreaFrom != null) {
				where.append(" AND r.value >= " + rentAreaFrom);
			}
			if (rentAreaTo != null) {
				where.append(" AND r.value <= " + rentAreaTo);
			}
			where.append(") ");
		}
		Long rentPriceFrom = buildingSearchBuilder.getRentPriceFrom();
		Long rentPriceTo = buildingSearchBuilder.getRentPriceTo();
		if (rentPriceFrom != null || rentPriceTo != null) {
			if (rentPriceFrom != null) {
				where.append(" AND b.rentprice >= " + rentPriceFrom);
			}
			if (rentPriceTo != null) {
				where.append(" AND b.rentprice <= " + rentPriceTo);
			}
		}
		List<String> typeCode = buildingSearchBuilder.getTypeCode();
		if (typeCode != null && typeCode.size() != 0) {
//			java7
//			List<String> convert = new ArrayList<String>();
//			for (String it : typeCode) {
//				convert.add("'" + it + "'");
//			}
//			where.append(" AND renttype.code IN (" + String.join(",", convert) + ") ");
			//java8
			where.append(" AND ( ");
			String sql = typeCode.stream().map(it -> " renttype.code like '%" + it + "%' ").collect(Collectors.joining(" OR "));
			where.append(sql);
			where.append(" ) ");
		}
	}
	
	@Override
	public List<BuildingEntity> findAll(BuildingSearchBuilder buildingSearchBuilder) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder("SELECT b.id, b.name, b.districtid, b.street, b.ward, b.numberofbasement, b.floorarea, b.rentprice, b.managername, b.managerphonenumber, b.servicefee, b.brokeragefee FROM building b ");
		joinTable(buildingSearchBuilder, sql);
		StringBuilder where = new StringBuilder("WHERE 1 = 1 ");
		queryNomal(buildingSearchBuilder, where);
		querySpecial(buildingSearchBuilder, where);
		where.append(" GROUP BY b.id;");
		sql.append(where);
		List<BuildingEntity> result = new ArrayList<BuildingEntity>();
		try (Connection conn = ConnectionJDBCUtil.getConnection();
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
