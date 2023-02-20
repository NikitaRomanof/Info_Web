package edu.school21.info.model.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;

@Data
public class RecommendationDto implements BaseDto {

    @CsvBindByName(column = "id")
    @CsvBindByPosition(position = 0)
    private Long id;

    @CsvBindByName(column = "peer")
    @CsvBindByPosition(position = 1)
    private String peer;

    @CsvBindByName(column = "recommended_peer")
    @CsvBindByPosition(position = 2)
    private String recommendedPeer;
}