package fr.univcotedazur.multifidelitycard.cli.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class AnswerJson extends JsonSerializer<CliAnswer> {
    @Override
    public void serialize(CliAnswer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("text", value.getText());
        gen.writeStringField("clientName", value.getClientName());
        gen.writeEndObject();
    }
}
