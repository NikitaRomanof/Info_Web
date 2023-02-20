package edu.school21.info.model.entity;

import edu.school21.info.model.BaseModel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "procedure_calls")
public class CallBody implements BaseModel {

    @Id
    private Long id;

    @Column(name = "call")
    private String call;
}
