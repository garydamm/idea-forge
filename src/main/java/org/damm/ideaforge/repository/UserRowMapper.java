package org.damm.ideaforge.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.damm.ideaforge.pojo.User;
import org.springframework.jdbc.core.RowMapper;

public class UserRowMapper implements RowMapper<User> {

	@Override
	public User mapRow(ResultSet resultSet, int row) throws SQLException {
		User user = new User();
		user.setEmail(resultSet.getString("email"));
		user.setId(resultSet.getInt("id"));
		user.setActive(resultSet.getInt("active"));
		user.setLastName(resultSet.getString("last_name"));
		user.setName(resultSet.getString("name"));
		return user;
	}
}