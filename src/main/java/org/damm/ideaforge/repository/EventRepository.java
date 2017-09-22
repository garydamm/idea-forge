package org.damm.ideaforge.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.damm.ideaforge.pojo.Event;
import org.damm.ideaforge.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class EventRepository {

	private final static String SELECT_TEAM = "select id,name,user_id,start,end from event";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Event> findAll() {
		return jdbcTemplate.query(SELECT_TEAM, new EventRowMapper());
	}

	public Event insert(Event event, User currentUser) {
		String sql = "insert into event (name, user_id) values (?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
				ps.setString(1, event.getName());
				ps.setLong(2, currentUser.getId());
				return ps;
			}
		}, keyHolder);
		event.setId(keyHolder.getKey().longValue());
		return event;
	}

	public int update(Event event) {
		String sql = "update event set name = ? where id = ?";
		return jdbcTemplate.update(sql, event.getName(), event.getId());
	}

	public Event find(Long id) {
		String sql = String.format("%s %s", SELECT_TEAM, "where id = ?");
		return jdbcTemplate.queryForObject(sql, new EventRowMapper(), id);
	}

	private class EventRowMapper implements RowMapper<Event> {
		@Override
		public Event mapRow(ResultSet rs, int row) throws SQLException {
			Event event = new Event();
			event.setId(rs.getLong("id"));
			event.setName(rs.getString("name"));
			event.setUserId(rs.getLong("user_id"));
			event.setStart(rs.getLong("start"));
			event.setEnd(rs.getLong("end"));
			return event;
		}
	}

}
