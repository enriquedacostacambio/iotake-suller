package com.iotake.solr.client.binder.value;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.solr.client.solrj.beans.BindingException;

import com.google.common.primitives.Primitives;

public class StringifiableValueConverterFactory extends
    AbstractValueConverterFactory {

  public static class StringifiableValueConverter extends
      AbstractSingleSlotValueConverter {
    private final Method valueOfMethod;

    public StringifiableValueConverter(Class<?> targetType,
        Class<?> documentClass, Field field, String path) {
      super(Anything.class, targetType, documentClass, field, path);
      try {
        Class<?> wrappedType = Primitives.wrap(targetType);
        this.valueOfMethod = wrappedType.getMethod("valueOf", String.class);
        Class<?> valueOfMethodReturnType = valueOfMethod.getReturnType();
        if (!Modifier.isStatic(valueOfMethod.getModifiers())
            || !Modifier.isPublic(valueOfMethod.getModifiers())
            || valueOfMethodReturnType != wrappedType) {
          throw new BindingException("Solr field " + documentClass.getName()
              + "." + field.getName()
              + " refers to non natively supported type "
              + targetType.getName()
              + " and the type does not have a public static valueOf(String):"
              + targetType.getName() + " method");
        }
      } catch (NoSuchMethodException e) {
        throw new BindingException("Solr field " + documentClass.getName()
            + "." + field.getName() + " refers to non-natively supported type "
            + targetType.getName()
            + " and the type does not have a public static valueOf(String):"
            + targetType.getName() + " method", e);
      }
    }

    public Class<?> getSlotType() {
      return String.class;
    }

    public Object toDocumentValue(Object beanValue) {
      if (beanValue == null) {
        return null;
      }
      checkBeanValueType(beanValue);
      return beanValue.toString();
    }

    public Object toBeanValue(Object documentValue) {
      if (documentValue == null) {
        return null;
      }
      checkDocumentValueType(documentValue, getSlot());
      try {
        return valueOfMethod.invoke(null, documentValue);
      } catch (IllegalAccessException e) {
        throw reportInvalidDocumentValue(documentValue, getSlot(),
            "Failed to reconstruct.", e);
      } catch (InvocationTargetException e) {
        throw reportInvalidDocumentValue(documentValue, getSlot(),
            "Failed to reconstruct.", e);
      }
    }
  }

  public StringifiableValueConverterFactory() {
    super(Anything.class);
  }

  protected ValueConverter doCreate(Class<?> targetType,
      Class<?> documentClass, Field field, String slotName,
      String slotNameSeparator) {
    if (targetType == String.class) {
      return new NativeValueConverterFactory.NullableNativeValueConverter(
          targetType, documentClass, field, slotName);
    } else {
      return new StringifiableValueConverter(targetType, documentClass, field,
          slotName);
    }
  }

}