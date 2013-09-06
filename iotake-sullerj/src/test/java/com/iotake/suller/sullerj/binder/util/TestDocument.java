package com.iotake.suller.sullerj.binder.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.iotake.suller.sullerj.binder.annotation.SolrDocument;
import com.iotake.suller.sullerj.binder.annotation.SolrEnumerated;
import com.iotake.suller.sullerj.binder.annotation.SolrId;
import com.iotake.suller.sullerj.binder.annotation.SolrEnumerated.EnumType;

@SolrDocument
public class TestDocument {

  public static class ComplexValue {
    private int fieldA;
    private Class<?> fieldB;

    public ComplexValue(int fieldA, Class<?> fieldB) {
      this.fieldA = fieldA;
      this.fieldB = fieldB;
    }

    public int getFieldA() {
      return fieldA;
    }

    public Class<?> getFieldB() {
      return fieldB;
    }

  }

  public static final Field ID_FIELD = getField("idField");

  @SolrId
  private Long idField;

  public static final Field LIST_OF_STRING_FIELD = getField("listOfStringField");

  private List<String> listOfStringField;

  public static final Field STRING_FIELD = getField("stringField");

  private String stringField;

  public static final Field NUMBER_FIELD = getField("numberField");

  private Number numberField;

  public static final Field DOUBLE_FIELD = getField("doubleField");

  private double doubleField;

  public static final Field CLASS_FIELD = getField("classField");

  private Class<?> classField;

  public static final Field COMPLEX_VALUE_FIELD = getField("complexValueField");

  private ComplexValue complexValueField;

  public static final Field LIST_OF_COMPLEX_VALUE_FIELD = getField("listOfComplexValueField");

  private List<ComplexValue> listOfComplexValueField;

  public static final Field DEFAULT_ENUM_FIELD = getField("defaultEnumField");

  private TimeUnit defaultEnumField;

  public static final Field NAME_ENUM_FIELD = getField("nameEnumField");

  @SolrEnumerated(EnumType.NAME)
  private TimeUnit nameEnumField;

  public static final Field ORDINAL_ENUM_FIELD = getField("ordinalEnumField");

  @SolrEnumerated(EnumType.ORDINAL)
  private TimeUnit ordinalEnumField;

  public static final Field INT_ARRAY_FIELD = getField("intArrayField");

  private int[] intArrayField;

  public Long getIdField() {
    return idField;
  }

  public void setIdField(Long idField) {
    this.idField = idField;
  }

  public List<String> getListOfStringField() {
    return listOfStringField;
  }

  public void setListOfStringField(List<String> listOfStringField) {
    this.listOfStringField = listOfStringField;
  }

  public String getStringField() {
    return stringField;
  }

  public void setStringField(String stringField) {
    this.stringField = stringField;
  }

  public Number getNumberField() {
    return numberField;
  }

  public void setNumberField(Number numberField) {
    this.numberField = numberField;
  }

  public double getDoubleField() {
    return doubleField;
  }

  public void setDoubleField(double doubleField) {
    this.doubleField = doubleField;
  }

  public Class<?> getClassField() {
    return classField;
  }

  public void setClassField(Class<?> classField) {
    this.classField = classField;
  }

  public ComplexValue getComplexValueField() {
    return complexValueField;
  }

  public void setComplexValueField(ComplexValue complexValueField) {
    this.complexValueField = complexValueField;
  }

  public List<ComplexValue> getListOfComplexValueField() {
    return listOfComplexValueField;
  }

  public void setListOfComplexValueField(
      List<ComplexValue> listOfComplexValueField) {
    this.listOfComplexValueField = listOfComplexValueField;
  }

  public TimeUnit getDefaultEnumField() {
    return defaultEnumField;
  }

  public void setDefaultEnumField(TimeUnit defaultEnumField) {
    this.defaultEnumField = defaultEnumField;
  }

  public TimeUnit getNameEnumField() {
    return nameEnumField;
  }

  public void setNameEnumField(TimeUnit nameEnumField) {
    this.nameEnumField = nameEnumField;
  }

  public TimeUnit getOrdinalEnumField() {
    return ordinalEnumField;
  }

  public void setOrdinalEnumField(TimeUnit ordinalEnumField) {
    this.ordinalEnumField = ordinalEnumField;
  }

  public int[] getIntArrayField() {
    return intArrayField;
  }

  public void setIntArrayField(int[] intArrayField) {
    this.intArrayField = intArrayField;
  }

  private static Field getField(String fieldName) {
    return getField(TestDocument.class, fieldName);
  }

  protected static Field getField(Class<?> documentClass, String fieldName) {
    try {
      return documentClass.getDeclaredField(fieldName);
    } catch (NoSuchFieldException e) {
      throw new IllegalArgumentException("Document class "
          + TestDocument.class.getName() + " must have a field named "
          + fieldName, e);
    } catch (SecurityException e) {
      throw new IllegalArgumentException(e);
    }
  }
}