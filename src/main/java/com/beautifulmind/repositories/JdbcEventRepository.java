package com.beautifulmind.repositories;

import com.beautifulmind.model.Event;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class JdbcEventRepository implements EventRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcEventRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Event> findById(long id) {
        var sql = "select * from event where id = ?";
        var result = this.jdbcTemplate.query(sql, this::mapRowToEvent, id);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public Iterable<Event> findAllByDate(LocalDateTime dateTime) {
        var sql = "select * from event";
        return this.jdbcTemplate.query(sql, this::mapRowToEvent);
    }

    @Override
    public void saveEvent(Event event) {
        var sql = "insert into event (title, description, dueDate, location) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, event.getTitle(), event.getDescription(), event.getDueDate(), event.getLocation());
    }

    private Event mapRowToEvent(ResultSet resultSet, int rowNum) throws SQLException {
        var e = new Event();
        e.setId(resultSet.getLong("id"));
        e.setTitle(resultSet.getString("title"));
        e.setDescription(resultSet.getString("description"));
        e.setDueDate(resultSet.getTimestamp("dueDate").toLocalDateTime());
        e.setLocation(resultSet.getString("location"));
        return e;
    }
}
