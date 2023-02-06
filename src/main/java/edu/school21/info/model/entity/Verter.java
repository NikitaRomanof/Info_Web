package edu.school21.info.model.entity;

import edu.school21.info.model.BaseModel;
import edu.school21.info.model.Status;
import edu.school21.info.util.StatusConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "verter")
public class Verter implements BaseModel {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "verter_id_generator"
    )
    @SequenceGenerator(
            name = "verter_id_generator",
            sequenceName = "verter_id_seq",
            allocationSize = 1
    )
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "check_id", referencedColumnName = "id", nullable = false)
    private Check check;

    @Convert(converter = StatusConverter.class)
    @Column(name = "state")
    private Status status;

    @Column(name = "time")
    private LocalTime time;
}
