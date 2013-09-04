package com.iotake.solr.client.binder.value;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Field;

public abstract class AbstractValueConverterFactory implements
    ValueConverterFactory {

  protected static final class Anything {
    private Anything() {
    }
  };

  private Class<?> targetRequiredType;

  public AbstractValueConverterFactory(Class<?> targetRequiredType) {
    checkNotNull(targetRequiredType, "Target required type cannot be null.");
    this.targetRequiredType = targetRequiredType;
  }

  public ValueConverter create(Class<?> targetType, Class<?> documentClass,
      Field field, String path, String slotNameSeparator) {
    checkNotNull(targetType, "Target type cannot be null.");
    checkNotNull(documentClass, "Document class cannot be null.");
    checkNotNull(field, "Field cannot be null.");
    checkNotNull(path, "Path cannot be null.");
    checkNotNull(slotNameSeparator, "Slot name separator cannot be null.");
    checkArgument(
        targetRequiredType == Anything.class
            || targetRequiredType.isAssignableFrom(targetType),
        "Target type "
            + targetType.getName()
            + " must be "
            + targetRequiredType.getName()
            + " or an extending subclass class or or implementing interface of "
            + targetRequiredType.getName() + ".");
    return doCreate(targetType, documentClass, field, path, slotNameSeparator);
  }

  protected abstract ValueConverter doCreate(Class<?> targetType,
      Class<?> documentClass, Field field, String slotName,
      String slotNameSeparator);

}
