package org.damm.ideaforge.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.damm.ideaforge.pojo.Role;
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
		String sql = "select id, email, active, name, last_name from user where email = ?";
		RowMapper<User> userRowMapper = new RowMapper<User>() {

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
		Role role = findRoleByName("USER");
		if (newUser != null) {
			insertUserRoles(newUser, role);
		}
	}

	private Role findRoleByName(String role) {
		String sql = "select id, role from role where role = ?";
		RowMapper<Role> rowMapper = new RowMapper<Role>() {
			@Override
			public Role mapRow(ResultSet rs, int row) throws SQLException {
				Role role = new Role();
				role.setId(rs.getInt("id"));
				role.setRole(rs.getString("role"));
				return role;
			}
		};
		return jdbcTemplate.queryForObject(sql, rowMapper, role);
	}

	private void insertUserRoles(User user, Role role) {
		String sql = "insert into user_role (user_id, role_id) values (?,?)";
		jdbcTemplate.update(sql, user.getId(), role.getId());
	}

}
