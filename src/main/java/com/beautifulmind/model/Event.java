package com.beautifulmind.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Event {

    private long id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private String location;

}
