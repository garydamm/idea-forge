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

	private final static String SELECT_USER = "select id, email, active, name, last_name from user";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public User findUserByEmail(String email) {
		String sql = String.format("%s %s", SELECT_USER, "where email = ?");
		try {
			return jdbcTemplate.queryForObject(sql, new UserRowMapper(), email);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public User find(long id) {
		String sql = String.format("%s %s", SELECT_USER, "where id = ?");
		try {
			return jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void insert(User user) {
		String sql = "insert into user (email, name, last_name, password, active) values (?,?,?,?,1); ";
		String password = null;
		if (user.getPassword() != null) {
			password = bCryptPasswordEncoder.encode(user.getPassword());
		}
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
