package fr.univcotedazur.multifidelitycards.controllers.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GeographicZoneDTO {

    private Long id;

    @NotBlank(message = "name should not be blank")
    private String name;
}