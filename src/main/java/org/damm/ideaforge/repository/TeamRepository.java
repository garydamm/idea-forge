package org.damm.ideaforge.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.damm.ideaforge.pojo.Team;
import org.damm.ideaforge.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
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

	private class TeamRowMapper implements RowMapper<Team> {
		@Override
		public Team mapRow(ResultSet rs, int row) throws SQLException {
			Team team = new Team();
			team.setId(rs.getLong("id"));
			team.setName(rs.getString("name"));
			team.setUserId(rs.getLong("user_id"));
			return team;
		}
	}

}
