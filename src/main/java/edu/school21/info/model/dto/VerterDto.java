package edu.school21.info.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VerterDto implements BaseDto {

    @Null(groups = {New.class})
    private Long id;

    @NotBlank(groups = {New.class})
    private Long checkId;

    private String status;

    private LocalTime time;
}
