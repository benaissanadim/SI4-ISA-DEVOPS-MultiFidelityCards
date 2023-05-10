package fr.univcotedazur.multifidelitycards.controllers.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ScheduleDTO {

    private Long id;

    @NotBlank(message = "beginTime should not be blank")
    private String beginTime;

    @NotBlank(message = "endTime should not be blank")
    private String endTime;

    @NotBlank(message = "dayOfWeek should not be blank")
    private String dayOfWeek;

}
