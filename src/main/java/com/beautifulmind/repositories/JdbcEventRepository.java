package com.beautifulmind.repositories;

import com.beautifulmind.model.Event;
import com.beautifulmind.model.EventSnapshotDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
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
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id "});
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

    @Override
    public Event updateEvent(Event event) {
        var queryBuilder = new StringBuilder("update event set");
        var mapSqlParameterSource = new MapSqlParameterSource();

        if (Objects.nonNull(event.getTitle())) {
            queryBuilder.append(" title = :title,");
            mapSqlParameterSource.addValue("title", event.getTitle());
        }

        if (Objects.nonNull(event.getDescription())) {
            queryBuilder.append(" description = :description,");
            mapSqlParameterSource.addValue("description", event.getDescription());
        }

        if (Objects.nonNull(event.getStartDateTime())) {
            queryBuilder.append(" start_datetime = :start_datetime,");
            queryBuilder.append(" date_id = :date_id,");
            mapSqlParameterSource.addValue("start_datetime", event.getStartDateTime());
            mapSqlParameterSource.addValue("date_id", Date.valueOf(event.getStartDateTime().toLocalDate()));
        }

        if (Objects.nonNull(event.getEndDateTime())) {
            queryBuilder.append(" end_datetime = :end_datetime,");
            mapSqlParameterSource.addValue("end_datetime", event.getEndDateTime());
        }

        if (Objects.nonNull(event.getLocation())) {
            queryBuilder.append(" location = :location,");
            mapSqlParameterSource.addValue("location", event.getLocation());
        }

        queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        queryBuilder.append(" where id = :id");
        mapSqlParameterSource.addValue("id", event.getId());

        var query = queryBuilder.toString();
        var namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        namedParameterJdbcTemplate.update(query, mapSqlParameterSource);

        return findById(event.getId()).get();
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
