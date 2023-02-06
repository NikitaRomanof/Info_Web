package edu.school21.info.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PeerDto implements BaseDto {

    @NotBlank(groups = {New.class})
    private String nickname;

    @NotNull(groups = {New.class})
    private LocalDate birthday;
}
