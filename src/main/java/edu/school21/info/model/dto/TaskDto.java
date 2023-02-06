package edu.school21.info.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto implements BaseDto {

    @NotBlank(groups = {New.class})
    private String title;

    private String parentTask;

    @NotNull(groups = {New.class})
    @Min(value = 1, message = "maximum xp value should be > 0", groups = {New.class})
    private Long maxExperience;
}
