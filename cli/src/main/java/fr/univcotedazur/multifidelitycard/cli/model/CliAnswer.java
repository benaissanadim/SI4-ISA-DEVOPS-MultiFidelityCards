package fr.univcotedazur.multifidelitycard.cli.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CliAnswer {

    private String clientName;
    private String text;

    public CliAnswer(String text, String clientName) {
        this.text = text;
        this.clientName = clientName;
    }

    public String toString(){
        return "{text=" + text + ", clientName=" + clientName + "}";
    }
}