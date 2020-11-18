package dev.imabad.mceventsuite.core.modules.mysql;

import dev.imabad.mceventsuite.core.util.GsonUtils;
import dev.imabad.mceventsuite.core.util.PropertyMap;

import javax.persistence.AttributeConverter;
import java.util.Map;

public class PropertyMapConverter implements AttributeConverter<PropertyMap, String> {
    @Override
    public String convertToDatabaseColumn(PropertyMap attribute) {
        return GsonUtils.getGson().toJson(attribute);
    }

    @Override
    public PropertyMap convertToEntityAttribute(String dbData) {
        PropertyMap propertyMap = new PropertyMap();
        try {
            propertyMap = GsonUtils.getGson().fromJson(dbData, PropertyMap.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return propertyMap;
    }
}
