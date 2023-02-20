package edu.school21.info.model.entity;

import edu.school21.info.model.BaseModel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "time_tracking")
public class TimeTrack implements BaseModel {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "time_tracking_id_generator"
    )
    @SequenceGenerator(
            name = "time_tracking_id_generator",
            sequenceName = "time_tracking_id_seq",
            allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "peer", referencedColumnName = "nickname", nullable = false)
    private Peer peer;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "time")
    private LocalTime time;

    @Column(name = "state")
    private Integer state;
}

