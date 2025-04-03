package com.javaweb.repository.custom.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.javaweb.repository.RentAreaRepository;
import com.javaweb.repository.entity.RentAreaEntity;
import com.javaweb.utils.ConnectionJDBCUtil;

@Repository
public class RentAreaRepositoryImpl implements RentAreaRepository{

	@Override
	public List<RentAreaEntity> getValueByBuildingId(Long id) {
		String sql = "SELECT r.value FROM rentarea r WHERE r.buildingid = " + id;
		List<RentAreaEntity> rentAreas = new ArrayList<>(); 
		try (Connection conn = ConnectionJDBCUtil.getConnection();
				Statement sttm = conn.createStatement();
				ResultSet rs = sttm.executeQuery(sql);){
			
			while (rs.next()) {
				RentAreaEntity rentAreaEntity = new RentAreaEntity();
				rentAreaEntity.setValue(rs.getString("value"));
				rentAreas.add(rentAreaEntity);
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Connect database failed...");
		}
		return rentAreas;
	}
	
}
