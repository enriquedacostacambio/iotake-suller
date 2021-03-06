package com.iotake.suller.sullerj.binder.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SolrTargetCollection {

  @SuppressWarnings("rawtypes")
  Class<? extends Collection> value();
}
