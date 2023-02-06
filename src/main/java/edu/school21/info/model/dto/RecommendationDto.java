package edu.school21.info.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationDto implements BaseDto {

    @Null(groups = {New.class})
    private Long id;

    @NotBlank(groups = {New.class})
    private String peer;

    @NotBlank(groups = {New.class})
    private String recommendedPeer;
}