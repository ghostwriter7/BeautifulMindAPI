package com.beautifulmind.repositories;

import com.beautifulmind.model.Event;
import com.beautifulmind.model.EventSnapshotDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
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
    public Iterable<Event> findAllByMonth(LocalDate date) {
        var sql = "select * from event where month(date_id) = ? and year(date_id) = ?";
        return this.jdbcTemplate.query(sql, this::mapRowToEvent, date.getMonthValue(), date.getYear());
    }

    @Override
    public Iterable<EventSnapshotDTO> findAllEventSnapshotsByMonth(LocalDate date) {
        var sql = "select id, title, date_id from event where month(date_id) = ? and year(date_id) = ?";
        return this.jdbcTemplate.query(sql, (rs, rowNum) -> {
            var es = new EventSnapshotDTO();
            es.setTitle(rs.getString("title"));
            es.setId(rs.getInt("id"));
            es.setDate(rs.getDate("date_id").toLocalDate());
            return es;
        }, date.getMonthValue(), date.getYear());
    }

    @Override
    public Event saveEvent(Event event) {
        var sql = "insert into event (title, description, location, start_datetime, end_datetime, date_id) values (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((connection) -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id "});
            ps.setString(1, event.getTitle());
            ps.setString(2, event.getDescription());
            ps.setString(3, event.getLocation());
            ps.setTimestamp(4, Timestamp.valueOf(event.getStartDateTime()));
            ps.setTimestamp(5, Timestamp.valueOf(event.getEndDateTime()));
            ps.setDate(6, Date.valueOf(event.getDateId()));
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            var id = keyHolder.getKey().intValue();
            event.setId(id);
            return event;
        }
        return event;
    }

    @Override
    public void deleteEvent(int eventId) {
        var sql = "delete from event where id = ?";
        this.jdbcTemplate.update(sql, eventId);
    }

    private Event mapRowToEvent(ResultSet resultSet, int rowNum) throws SQLException {
        var e = new Event();
        e.setId(resultSet.getLong("id"));
        e.setDateId(resultSet.getDate("date_id").toLocalDate());
        e.setTitle(resultSet.getString("title"));
        e.setDescription(resultSet.getString("description"));
        e.setStartDateTime(resultSet.getTimestamp("start_datetime").toLocalDateTime());
        e.setEndDateTime(resultSet.getTimestamp("end_datetime").toLocalDateTime());
        e.setLocation(resultSet.getString("location"));
        return e;
    }
}
