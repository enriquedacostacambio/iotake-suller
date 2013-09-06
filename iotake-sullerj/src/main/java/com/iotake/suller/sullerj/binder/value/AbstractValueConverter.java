package com.iotake.suller.sullerj.binder.value;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Field;

import org.apache.solr.client.solrj.beans.BindingException;

import com.google.common.primitives.Primitives;
import com.iotake.suller.sullerj.binder.source.Slot;
import com.iotake.suller.sullerj.binder.value.AbstractValueConverterFactory.Anything;

public abstract class AbstractValueConverter implements ValueConverter {

  private final Class<?> targetType;
  private final Class<?> wrapperTargetType;
  private final Class<?> documentClass;
  private final Field field;
  private final String path;

  public AbstractValueConverter(Class<?> targetRequiredType,
      Class<?> targetType, Class<?> documentClass, Field field, String path) {
    checkNotNull(targetRequiredType, "Target required type cannot be null.");
    checkNotNull(targetType, "Target type cannot be null.");
    checkArgument(
        targetRequiredType == Anything.class
            || targetRequiredType.isAssignableFrom(targetType),
        "Target type "
            + targetType.getName()
            + " must be "
            + targetRequiredType.getName()
            + " or an extending subclass class or or implementing interface of "
            + targetRequiredType.getName() + ".");
    checkNotNull(documentClass, "Document class cannot be null.");
    checkNotNull(field, "Field cannot be null.");
    checkNotNull(path, "Path cannot be null.");
    this.targetType = targetType;
    this.wrapperTargetType = Primitives.wrap(targetType);
    this.documentClass = documentClass;
    this.field = field;
    this.path = path;
  }

  protected Class<?> getTargetType() {
    return targetType;
  }

  public Class<?> getTargetWrapperType() {
    return wrapperTargetType;
  }

  protected Class<?> getDocumentClass() {
    return documentClass;
  }

  protected Field getField() {
    return field;
  }

  protected String getPath() {
    return path;
  }

  protected void checkBeanValueType(Object beanValue) {
    if (beanValue != null && !getTargetWrapperType().isInstance(beanValue)) {
      throw new BindingException("Bean value at " + path + " is a "
          + beanValue.getClass().getName() + ", but "
          + getDocumentClass().getName() + "." + getField().getName()
          + " is expecting a value of type " + getTargetType().getName()
          + " at this location: " + beanValue);
    }
  }

  protected BindingException reportInvalidDocumentValue(Object documentValue,
      Slot slot, String reason) {
    return reportInvalidDocumentValue(documentValue, slot, reason, null);
  }

  protected BindingException reportInvalidDocumentValue(Object documentValue,
      Slot slot, String reason, Exception exception) {
    checkNotNull(slot, "Slot cannot be null");
    checkNotNull(reason, "Reason cannot be null");
    return new BindingException("Document value at "
        + slot.getPath()
        + " of type "
        + (documentValue == null ? "<null>" : documentValue.getClass()
            .getName()) + " is invalid for field " + documentClass.getName()
        + "." + field.getName() + " at this location: " + documentValue
        + (reason == null ? "" : " - " + reason), exception);
  }

  protected BindingException reportInvalidBeanValue(Object beanValue,
      String reason) {
    return reportInvalidBeanValue(beanValue, reason, null);
  }

  protected BindingException reportInvalidBeanValue(Object beanValue,
      String reason, Exception exception) {
    checkNotNull(reason, "Reason cannot be null");
    return new BindingException("Bean value at " + path + " of type "
        + (beanValue == null ? "<null>" : beanValue.getClass().getName())
        + " is invalid for field " + documentClass.getName() + "."
        + field.getName() + " at this location: " + beanValue
        + (reason == null ? "" : " - " + reason), exception);
  }

  protected void checkNonNullDocumentValue(Object documentValue, Slot slot) {
    checkNotNull(slot, "Sot cannot be null");
    if (documentValue == null) {
      throw new BindingException("Document value at " + slot.getPath()
          + " is not supposed to be null for field " + documentClass.getName()
          + "." + field.getName() + " at this location.");
    }
  }

  protected void checkNonNullBeanValue(Object beanValue) {
    if (beanValue == null) {
      throw new BindingException("Document value at " + path
          + " is not supposed to be null for field " + documentClass.getName()
          + "." + field.getName() + " at this location.");
    }
  }

  protected void checkDocumentValueType(Object documentValue, Slot slot) {
    checkNotNull(slot, "Slot cannot be null");
    if (documentValue != null
        && !slot.getWrapperType().isInstance(documentValue)) {
      throw new BindingException("Document value at " + slot.getPath()
          + " is a " + documentValue.getClass().getName() + ", but "
          + getDocumentClass().getName() + "." + getField().getName()
          + " is expecting a value of type " + slot.getType().getName()
          + " at this location: " + documentValue);
    }
  }

}