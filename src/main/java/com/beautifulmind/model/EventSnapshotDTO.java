package com.beautifulmind.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EventSnapshotDTO {
    private int id;
    private String title;
    private LocalDate date;
}
