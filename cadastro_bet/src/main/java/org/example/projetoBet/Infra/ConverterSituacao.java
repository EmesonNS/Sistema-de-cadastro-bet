package org.example.projetoBet.Infra;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ConverterSituacao implements AttributeConverter<Situacao, String> {
    @Override
    public String convertToDatabaseColumn(Situacao situacao) {
        return situacao != null ? situacao.getValor() : null;
    }

    @Override
    public Situacao convertToEntityAttribute(String s) {
        return s != null ? Situacao.fromValor(s) : null;
    }
}
