package fr.univcotedazur.multifidelitycard.cli.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class KPIsOffersSerializer extends JsonSerializer<KPIsOffers> {
    @Override
    public void serialize(KPIsOffers value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("nbGifts", value.getNbGifts());
        gen.writeStringField("month", value.getMonthYear().toString());
        gen.writeNumberField("giftsEvolution", value.getGiftsEvolution());
        gen.writeEndObject();
    }

}
