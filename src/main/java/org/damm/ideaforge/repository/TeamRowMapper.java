package org.damm.ideaforge.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.damm.ideaforge.pojo.Team;
import org.springframework.jdbc.core.RowMapper;

public class TeamRowMapper implements RowMapper<Team> {
	@Override
	public Team mapRow(ResultSet rs, int row) throws SQLException {
		Team team = new Team();
		team.setId(rs.getLong("id"));
		team.setName(rs.getString("name"));
		team.setUserId(rs.getLong("user_id"));
		return team;
	}
}
