package fr.univcotedazur.multifidelitycard.cli.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of="text")
public class CliQuestion {

    private List<CliAnswer> answers;
    private String text;

    public CliQuestion(String text) {
        this.answers = new ArrayList<>();
        this.text = text;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(text);
        for(int i =0; i < answers.size(); i++)
            sb.append("\n\t\tAnswer ").append(i).append(" : ").append(answers.get(i).toString());
        return sb.toString();
    }

}
