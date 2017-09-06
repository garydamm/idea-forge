package org.damm.ideaforge.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.damm.ideaforge.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public User findUserByEmail(String email) {
		String sql = "select user_id, email, active, name, last_name from user where email = ?";
		RowMapper<User> userRowMapper = new RowMapper<User>() {

			@Override
			public User mapRow(ResultSet resultSet, int row) throws SQLException {
				User user = new User();
				user.setEmail(resultSet.getString("email"));
				user.setId(resultSet.getInt("user_id"));
				user.setActive(resultSet.getInt("active"));
				user.setLastName(resultSet.getString("last_name"));
				user.setName(resultSet.getString("name"));
				return user;
			}
		};
		try {
			return jdbcTemplate.queryForObject(sql, userRowMapper, email);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void insert(User user) {
		String sql = "insert into user (email, name, last_name, password, active) values (?,?,?,?,1); ";
		String password = bCryptPasswordEncoder.encode(user.getPassword());
		jdbcTemplate.update(sql, user.getEmail(), user.getName(), user.getLastName(), password);
		User newUser = findUserByEmail(user.getEmail());
		if (newUser != null) {
			insertUserRoles(newUser);
		}
	}

	private void insertUserRoles(User user) {
		String sql = "insert into user_role (user_id, role_id) values (?,1)";
		jdbcTemplate.update(sql, user.getId());
	}

}
