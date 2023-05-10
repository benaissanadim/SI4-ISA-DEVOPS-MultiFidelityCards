package fr.univcotedazur.multifidelitycards.connectors.externaldto.externaldto;

import lombok.Getter;

/**
 * External DTO (Data Transfert Object) to POST NOTIF to the external Notifier system
 */
@Getter
public class NotifDTO {
    String to;
    String subject;
    String text;

    public NotifDTO(String to, String subject, String text) {
        this.to = to;
        this.subject = subject;
        this.text = text;
    }

    public NotifDTO() {

    }
}