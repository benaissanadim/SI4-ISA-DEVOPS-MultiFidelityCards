package fr.univcotedazur.multifidelitycards.controllers.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StoreDTO {
    private Long id;

    private String name;

    private String address;

    //@NotBlank(message = "name should not be blank")
    //@Size(message = "name should be between 4 and 20 characters", min = 4, max = 20)
    private List<ScheduleDTO> schedule;

    private String zone;
}
