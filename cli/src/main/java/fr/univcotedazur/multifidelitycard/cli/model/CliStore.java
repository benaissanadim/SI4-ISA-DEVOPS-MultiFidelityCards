package fr.univcotedazur.multifidelitycard.cli.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CliStore {
    private Long id;
    private String name;

    private String address;

    private List<CliSchedule> schedule;

    private String zone;

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Store : {name=").append(name).append(", address=").append(address).append(", zone=").append(zone)
                .append("}");
        for(CliSchedule c : schedule){
            sb.append("\n\t\t").append(c.toString());
        }
        return sb.toString();
    }
}
