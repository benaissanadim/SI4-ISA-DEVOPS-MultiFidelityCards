package fr.univcotedazur.multifidelitycard.cli.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.IOException;


@JsonSerialize(using = CliScheduleSerializer.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CliSchedule {
    private Long id;

    @NotBlank(message = "beginTime should not be blank")
    private String beginTime;

    @NotBlank(message = "endTime should not be blank")
    private String endTime;

    @NotBlank(message = "dayOfWeek should not be blank")
    private String dayOfWeek;

    public String toString(){
        return "- DayOfWeek=" + dayOfWeek+ " : From "+ beginTime+" to "+ endTime;
    }


}
