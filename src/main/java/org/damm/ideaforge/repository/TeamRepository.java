package org.damm.ideaforge.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.damm.ideaforge.pojo.Team;
import org.damm.ideaforge.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class TeamRepository {

	private final static String SELECT_TEAM = "select id,name,user_id from team";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Team> findAll() {
		return jdbcTemplate.query(SELECT_TEAM, new TeamRowMapper());
	}

	public Team insert(Team team, User currentUser) {
		String sql = "insert into team (name, user_id) values (?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
				ps.setString(1, team.getName());
				ps.setLong(2, currentUser.getId());
				return ps;
			}
		}, keyHolder);
		team.setId(keyHolder.getKey().longValue());
		return team;
	}

	public int update(Team team) {
		String sql = "update team set name = ? where id = ?";
		return jdbcTemplate.update(sql, team.getName(), team.getId());
	}

	public Team find(Long id) {
		String sql = String.format("%s %s", SELECT_TEAM, "where id = ?");
		return jdbcTemplate.queryForObject(sql, new TeamRowMapper(), id);
	}

	public Team findByName(String name) {
		String sql = String.format("%s %s", SELECT_TEAM, "where name = ?");
		try {
			return jdbcTemplate.queryForObject(sql, new TeamRowMapper(), name);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public int addMember(long teamId, long userId) {
		String sql = "insert into team_member (team_id, user_id) values (?,?)";
		return jdbcTemplate.update(sql, teamId, userId);
	}

	public List<User> members(long teamId) {
		String sql = "select u.id, u.email, u.active, u.name, u.last_name from user u join team_member tm on u.id = tm.user_id where tm.team_id = ?";
		return jdbcTemplate.query(sql, new UserRowMapper(), teamId);
	}

}
