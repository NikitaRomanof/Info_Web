package edu.school21.info.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TimeTrackDto implements BaseDto {

    @Null(groups = {New.class})
    private Long id;

    @NotBlank(groups = {New.class})
    private String peer;

    private LocalDate date;

    private LocalTime time;

    private Integer state;
}

