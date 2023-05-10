package fr.univcotedazur.multifidelitycards.entities;

import lombok.*;

import javax.persistence.*;

/**
 * Schedule class
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
@EqualsAndHashCode(of = {"dayOfWeek"})
@Table(name = "schedule")
public class Schedule {

    @Id
    @GeneratedValue
    private Long id;

    private String beginTime;

    private String endTime;

    private String dayOfWeek;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    public Schedule(String beginTime, String endTime, String dayOfWeek) {
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.dayOfWeek = dayOfWeek;
    }

}
