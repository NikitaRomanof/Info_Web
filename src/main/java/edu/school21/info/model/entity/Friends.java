package edu.school21.info.model.entity;

import edu.school21.info.model.BaseModel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "friends")
public class Friends implements BaseModel {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "friends_id_generator"
    )
    @SequenceGenerator(
            name = "friends_id_generator",
            sequenceName = "friends_id_seq",
            allocationSize = 1
    )
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "peer1", referencedColumnName = "nickname")
    private Peer firstPeer;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "peer2", referencedColumnName = "nickname")
    private Peer secondPeer;
}
