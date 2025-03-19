package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;

@Repository
public class BuildingRepositoryImpl implements BuildingRepository{
	static final String DB_URL = "jdbc:mysql://localhost:3306/estatebasic"; 
	static final String USER = "root";
	static final String PASS = "123456";
	@Override
	public List<BuildingEntity> findAll(String name, Long districtId) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder("SELECT * FROM building b WHERE 1 = 1 ");
		if (name != null && !name.equals("")) {
			sql.append("AND b.name like '%" + name + "%' ");
		}
		if (districtId != null) {
			sql.append("AND b.districtid = " + districtId + " ");
		} 
		List<BuildingEntity> result = new ArrayList<BuildingEntity>();
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				Statement sttm = conn.createStatement();
				ResultSet rs = sttm.executeQuery(sql.toString());){
			
			while (rs.next()) {
				BuildingEntity building = new BuildingEntity();
				building.setName(rs.getString("name"));
				building.setNumberOfBasement(rs.getInt("numberofbasement"));
				building.setStreet(rs.getString("street"));
				building.setWard(rs.getString("ward"));
				result.add(building);
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Connect database failed...");
		}
		return result;
	}

}
