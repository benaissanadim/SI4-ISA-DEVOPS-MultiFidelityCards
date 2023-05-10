package fr.univcotedazur.multifidelitycard.cli.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of="name")
public class CliSurvey {
    private String name;
    private Long id;
    private List<CliQuestion> questions;

    public CliSurvey(String name, List<CliQuestion> questions) {
        this.name = name;
        this.questions = questions;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Survey : name=").append(name);
        for(int i =0; i < questions.size(); i++)
            sb.append("\n\tQuestion ").append(i).append(" : ").append(questions.get(i).toString());
        return sb.toString();
    }


}
