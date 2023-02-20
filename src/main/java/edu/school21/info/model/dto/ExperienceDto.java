package edu.school21.info.model.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;

@Data
public class ExperienceDto implements BaseDto {

    @CsvBindByName(column = "id")
    @CsvBindByPosition(position = 0)
    private Long id;

    @CsvBindByName(column = "check_id")
    @CsvBindByPosition(position = 1)
    private Long checkId;

    @CsvBindByName(column = "xp_amount")
    @CsvBindByPosition(position = 2)
    private Long amount;
}
