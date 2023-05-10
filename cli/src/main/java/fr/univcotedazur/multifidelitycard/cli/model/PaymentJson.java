package fr.univcotedazur.multifidelitycard.cli.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class PaymentJson extends JsonSerializer<PaymentDT> {
    @Override
    public void serialize(PaymentDT value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("amount", value.getAmount());
        gen.writeStringField("creditCard", value.getCreditCard());
        gen.writeEndObject();
    }
}
