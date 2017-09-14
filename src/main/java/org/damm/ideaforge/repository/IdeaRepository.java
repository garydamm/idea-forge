package org.damm.ideaforge.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.damm.ideaforge.pojo.Idea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class IdeaRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Idea insert(Idea idea) {
		String sql = "insert into idea (title, description) values (?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
				ps.setString(1, idea.getTitle());
				ps.setString(2, idea.getDescription());
				return ps;
			}
		}, keyHolder);
		idea.setId(keyHolder.getKey().longValue());
		return idea;
	}

	public int update(Idea idea) {
		String sql = "update idea set title = ?, description = ? where id = ?";
		return jdbcTemplate.update(sql, idea.getTitle(), idea.getDescription(), idea.getId());
	}

}
