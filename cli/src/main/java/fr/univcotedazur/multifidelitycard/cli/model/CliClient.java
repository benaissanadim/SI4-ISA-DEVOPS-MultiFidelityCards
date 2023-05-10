package fr.univcotedazur.multifidelitycard.cli.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "username")
public class CliClient {

    private Long id;
    private String username;
    private String email;
    private String status;

    public CliClient(String name, String email) {
        this.username = name;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Client : {email=" + email + ", username=" + username + ", status=" + status + "}";
    }

}
