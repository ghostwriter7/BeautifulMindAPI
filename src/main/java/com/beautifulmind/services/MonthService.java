package com.beautifulmind.services;

import com.beautifulmind.model.Month;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MonthService {

    public Month getMonthByYearAndMonth(int month, int year) {
        log.info("Fetching month data for MONTH: {}, YEAR: {}", month, year);
        return new Month();
    }
}
