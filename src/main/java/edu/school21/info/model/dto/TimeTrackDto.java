package edu.school21.info.model.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TimeTrackDto implements BaseDto {

    @CsvBindByName(column = "id")
    @CsvBindByPosition(position = 0)
    private Long id;

    @CsvBindByName(column = "peer")
    @CsvBindByPosition(position = 1)
    private String peer;

    @CsvBindByName(column = "date")
    @CsvBindByPosition(position = 2)
    @CsvDate(value = "yyyy-MM-dd")
    private LocalDate date;

    @CsvBindByName(column = "time")
    @CsvBindByPosition(position = 3)
    @CsvDate(value = "HH:mm:ss")
    private LocalTime time;

    @CsvBindByName(column = "state")
    @CsvBindByPosition(position = 4)
    private Integer state;
}

