package edu.school21.info.model.entity;

import edu.school21.info.model.BaseModel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "xp")
public class Experience implements BaseModel {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "xp_id_generator"
    )
    @SequenceGenerator(
            name = "xp_id_generator",
            sequenceName = "xp_id_seq",
            allocationSize = 1
    )
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "check_id", referencedColumnName = "id", nullable = false)
    private Check check;

    @Column(name = "xp_amount")
    private Long amount;
}
