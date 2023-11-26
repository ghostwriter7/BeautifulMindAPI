package com.beautifulmind.controllers;

import com.beautifulmind.model.Month;
import com.beautifulmind.services.MonthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/month")
public class MonthController {

    private final MonthService monthService;

    public MonthController(MonthService monthService) {
        this.monthService = monthService;
    }

    @GetMapping
    public Month getMonthData(
            @RequestParam int month,
            @RequestParam int year
    ) {
       return monthService.getMonthByYearAndMonth(month, year);
    }
}
