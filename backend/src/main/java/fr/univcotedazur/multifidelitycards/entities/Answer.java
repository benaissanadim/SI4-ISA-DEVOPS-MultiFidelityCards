package fr.univcotedazur.multifidelitycards.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * Answer class
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
    @ManyToOne
    private Client client;
    private String text;

    public Answer(Question question, String text, Client client) {
        this.question = question;
        this.text = text;
        this.client = client;
    }
}


