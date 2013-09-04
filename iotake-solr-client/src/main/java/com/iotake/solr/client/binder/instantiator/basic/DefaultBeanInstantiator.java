package com.iotake.solr.client.binder.instantiator.basic;

import java.lang.reflect.Constructor;

import com.iotake.solr.client.binder.instantiator.BeanInstantiator;

public class DefaultBeanInstantiator implements BeanInstantiator {

  private final Constructor<?> constructor;

  public DefaultBeanInstantiator(Class<?> beanClass) throws Exception {
    this.constructor = findConstructor(beanClass);
  }

  private Constructor<?> findConstructor(Class<?> beanClass) throws Exception {
    Constructor<?> constructor = beanClass.getDeclaredConstructor();
    constructor.setAccessible(true);
    return constructor;
  }

  public Object instantiate() throws Exception {
    return constructor.newInstance();
  }

}
