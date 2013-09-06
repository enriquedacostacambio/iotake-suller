package com.iotake.suller.sullerj.binder.instantiator.basic;

import com.iotake.suller.sullerj.binder.instantiator.BeanInstantiator;
import com.iotake.suller.sullerj.binder.instantiator.BeanInstantiatorFactory;

public class DefaultBeanInstantiatorFactory implements BeanInstantiatorFactory {

  public BeanInstantiator create(Class<?> beanClass) throws Exception {
    return new DefaultBeanInstantiator(beanClass);
  }
}
