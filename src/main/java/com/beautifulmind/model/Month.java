package com.beautifulmind.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Data
public class Month {
    private int month;
    private int year;
    private HashMap<LocalDate, List<Day>> dayMap;
}
