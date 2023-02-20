package edu.school21.info.util;

import edu.school21.info.model.Status;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class StatusConverter implements AttributeConverter<Status, String> {

    @Override
    public String convertToDatabaseColumn(Status status) {
        return status.name();
    }

    @Override
    public Status convertToEntityAttribute(String string) {
        return Status.valueOf(string);
    }
}