package edu.school21.info.model.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
public class PeerDto implements BaseDto {

    @CsvBindByName(column = "nickname")
    @CsvBindByPosition(position = 0)
    private String nickname;

    @CsvDate(value = "yyyy-MM-dd")
    @CsvBindByName(column = "birthday")
    @CsvBindByPosition(position = 1)
    private LocalDate birthday;
}
