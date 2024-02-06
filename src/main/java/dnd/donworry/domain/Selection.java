package dnd.donworry.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Selection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Vote vote;

    @OneToOne(mappedBy = "selection", optional = true)
    private OptionImage optionImage;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int count = 0;

}