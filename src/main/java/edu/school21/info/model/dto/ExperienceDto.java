package edu.school21.info.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceDto implements BaseDto {

    @Null(groups = {New.class})
    private Long id;

    @NotNull(groups = {New.class})
    private Long checkId;

    @Min(value = 0, message = "XP amount can not be less than 0", groups = {New.class})
    private Long amount;
}
