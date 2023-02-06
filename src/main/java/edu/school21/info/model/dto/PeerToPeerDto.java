package edu.school21.info.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PeerToPeerDto implements BaseDto {

    @Null(groups = {New.class})
    private Long id;

    @NotNull(groups = {New.class})
    private Long checkId;

    @NotBlank(groups = {New.class})
    private String checkingPeer;

    private String status;

    private LocalTime time;
}
