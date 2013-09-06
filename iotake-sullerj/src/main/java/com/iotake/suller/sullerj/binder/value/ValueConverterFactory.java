package com.iotake.suller.sullerj.binder.value;

import java.lang.reflect.Field;

public interface ValueConverterFactory {

  ValueConverter create(Class<?> targetType, Class<?> documentClass,
      Field field, String path, String slotNameSeparator);

}