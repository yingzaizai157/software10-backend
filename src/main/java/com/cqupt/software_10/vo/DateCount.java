package com.cqupt.software_10.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateCount {
    private Timestamp date;
    private int count;


}