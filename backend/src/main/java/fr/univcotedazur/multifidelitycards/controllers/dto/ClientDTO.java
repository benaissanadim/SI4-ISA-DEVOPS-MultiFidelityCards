package fr.univcotedazur.multifidelitycards.controllers.dto;

import fr.univcotedazur.multifidelitycards.entities.ClientStatus;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ClientDTO {

    private Long id;

    @NotBlank(message = "username should not be blank")
    @Size(message = "username should be between 3 and 20 characters", min = 3, max = 20)
    @Pattern(message = "invalid username", regexp = "^[a-zA-Z0-9]+$")
    private String username;

    @NotBlank(message = "email should not be blank")
    @Pattern(message = "invalid email", regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
    private String email;

    private ClientStatus status;

}
