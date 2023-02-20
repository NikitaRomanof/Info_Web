package edu.school21.info.model.entity;

import edu.school21.info.model.BaseModel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "recommendations")
public class Recommendation implements BaseModel {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "recommendations_id_generator"
    )
    @SequenceGenerator(
            name = "recommendations_id_generator",
            sequenceName = "recommendations_id_seq",
            allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "peer", referencedColumnName = "nickname", nullable = false)
    private Peer peer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recommended_peer", referencedColumnName = "nickname")
    private Peer recommendedPeer;
}