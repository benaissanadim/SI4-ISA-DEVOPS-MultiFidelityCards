package fr.univcotedazur.multifidelitycards.connectors.externaldto.externaldto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class NotfJson extends JsonSerializer<NotifDTO> {
    @Override
    public void serialize(NotifDTO value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("text", value.getText());
        gen.writeStringField("subject", value.getSubject());
        gen.writeStringField("to", value.getTo());
        gen.writeEndObject();
    }
}

