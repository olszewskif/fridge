package io.github.olszewskif.fridge.identity.adapter.out.persistence.converter;

import io.github.olszewskif.fridge.identity.domain.UserRole;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserRoleConverter implements AttributeConverter<UserRole, String> {
    @Override
    public String convertToDatabaseColumn(UserRole attribute) {
       return attribute == null ? null : attribute.getName();
    }

    @Override
    public UserRole convertToEntityAttribute(String dbData) {
        return UserRole.fromName(dbData);
    }
}
