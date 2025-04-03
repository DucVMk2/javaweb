package com.javaweb.repository.custom.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Repository;

import com.javaweb.repository.DistrictRepository;
import com.javaweb.repository.entity.DistrictEntity;
import com.javaweb.utils.ConnectionJDBCUtil;

@Repository
public class DistrictRepositoryImpl implements DistrictRepository{
	
	@Override
	public DistrictEntity findNameById(Long id) {
		String sql = "SELECT d.name FROM district d WHERE d.id = " + id;
		DistrictEntity districtEntity = new DistrictEntity();
		try (Connection conn = ConnectionJDBCUtil.getConnection();
				Statement sttm = conn.createStatement();
				ResultSet rs = sttm.executeQuery(sql);){
			
			while (rs.next()) {
				districtEntity.setName(rs.getString("name"));
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Connect database failed...");
		}
		return districtEntity;
	}

}
