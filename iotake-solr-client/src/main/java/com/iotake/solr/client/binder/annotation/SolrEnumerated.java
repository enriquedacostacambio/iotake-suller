package com.iotake.solr.client.binder.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SolrEnumerated {

  public static enum EnumType {
    ORDINAL,

    NAME
  }

  EnumType value();
}
