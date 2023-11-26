package com.beautifulmind.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class Month {
    private final int month;
    private final int year;
    private Map<LocalDate, List<EventSnapshotDTO>> eventMap;

    public Month(int month, int year) {
        this.month = month;
        this.year = year;
    }
}
