package com.iotake.solr.client.binder.value;

import java.lang.reflect.Field;

public interface ValueConverterFactory {

  ValueConverter create(Class<?> targetType, Class<?> documentClass,
      Field field, String path, String slotNameSeparator);

}