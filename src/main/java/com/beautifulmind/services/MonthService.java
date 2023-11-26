package com.beautifulmind.services;

import com.beautifulmind.model.Month;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class MonthService {

    private final EventService eventService;

    public MonthService(EventService eventService) {
        this.eventService = eventService;
    }

    public Month getMonthDataByYearAndMonth(int month, int year) {
        log.info("Fetching month data for MONTH: {}, YEAR: {}", month, year);
        var eventsMap = eventService.getAllEventSnapshotsByMonth(LocalDate.of(year, month, 1));
        var monthDO = new Month(month, year);
        monthDO.setEventMap(eventsMap);
        return monthDO;
    }
}
