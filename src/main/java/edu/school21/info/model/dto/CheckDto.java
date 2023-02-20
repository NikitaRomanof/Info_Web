package edu.school21.info.model.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.*;

import java.time.LocalDate;

@Data
public class CheckDto implements BaseDto {

    @CsvBindByName(column = "id")
    @CsvBindByPosition(position = 0)
    private Long id;

    @CsvBindByName(column = "peer")
    @CsvBindByPosition(position = 1)
    private String peer;

    @CsvBindByName(column = "task")
    @CsvBindByPosition(position = 2)
    private String task;

    @CsvDate(value = "yyyy-MM-dd")
    @CsvBindByName(column = "date")
    @CsvBindByPosition(position = 3)
    private LocalDate date;
}
