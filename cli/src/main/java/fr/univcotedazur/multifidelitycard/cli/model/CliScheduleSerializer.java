package fr.univcotedazur.multifidelitycard.cli.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class CliScheduleSerializer extends JsonSerializer<CliSchedule> {
    @Override
    public void serialize(CliSchedule value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("beginTime", value.getBeginTime());
        gen.writeStringField("endTime", value.getEndTime());
        gen.writeStringField("dayOfWeek", value.getDayOfWeek());
        gen.writeEndObject();
    }
}
