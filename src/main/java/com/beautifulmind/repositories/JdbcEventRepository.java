package com.beautifulmind.repositories;

import com.beautifulmind.model.Event;
import com.beautifulmind.model.EventSnapshotDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
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

//    @Override
//    public void saveEvent(Event event) {
//        var sql = "insert into event (title, description, dueDate, location) values (?, ?, ?, ?)";
//        jdbcTemplate.update(sql, event.getTitle(), event.getDescription(), event.getDueDate(), event.getLocation());
//    }

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
