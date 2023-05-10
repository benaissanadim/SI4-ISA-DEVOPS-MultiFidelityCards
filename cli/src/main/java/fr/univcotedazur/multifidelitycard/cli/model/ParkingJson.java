package fr.univcotedazur.multifidelitycard.cli.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ParkingJson extends JsonSerializer<ParkingDTO> {
    @Override
    public void serialize(ParkingDTO value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("name", value.getName());
        gen.writeNumberField("minutes", value.getMinutes());
        gen.writeStringField("to", value.getTo());
        gen.writeEndObject();
    }

}
