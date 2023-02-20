package edu.school21.info.model.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;

@Data
public class TransferredPointsDto implements BaseDto {

    @CsvBindByName(column = "id")
    @CsvBindByPosition(position = 0)
    private Long id;

    @CsvBindByName(column = "checking_peer")
    @CsvBindByPosition(position = 1)
    private String checkingPeer;

    @CsvBindByName(column = "checked_peer")
    @CsvBindByPosition(position = 2)
    private String checkedPeer;

    @CsvBindByName(column = "points_amount")
    @CsvBindByPosition(position = 3)
    private Long amount;
}
