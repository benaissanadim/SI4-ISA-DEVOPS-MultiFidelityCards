package fr.univcotedazur.multifidelitycard.cli.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class KPIsCASerializer extends JsonSerializer<KPIsCA> {
    @Override
    public void serialize(KPIsCA value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("ca", value.getCa());
        gen.writeStringField("month", value.getMonthYear().toString());
        gen.writeNumberField("caEvolution", value.getCaEvolution());
        gen.writeEndObject();
    }

}
