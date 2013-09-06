package com.iotake.suller.sullerj.binder.instantiator.basic;

import java.lang.reflect.Constructor;

import com.iotake.suller.sullerj.binder.instantiator.BeanInstantiator;

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
