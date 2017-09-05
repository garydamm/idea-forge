package org.damm.ideaforge.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.damm.ideaforge.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public User findUserByEmail(String email) {
		String sql = "select email from user where email = ?";
		RowMapper<User> userRowMapper = new RowMapper<User>() {

			@Override
			public User mapRow(ResultSet resultSet, int row) throws SQLException {
				User user = new User();
				user.setEmail(resultSet.getString("email"));
				return user;
			}
		};
		return jdbcTemplate.queryForObject(sql, userRowMapper, email);
	}

}
