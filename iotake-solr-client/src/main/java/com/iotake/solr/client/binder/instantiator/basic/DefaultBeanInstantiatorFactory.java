package com.iotake.solr.client.binder.instantiator.basic;

import com.iotake.solr.client.binder.instantiator.BeanInstantiator;
import com.iotake.solr.client.binder.instantiator.BeanInstantiatorFactory;

public class DefaultBeanInstantiatorFactory implements BeanInstantiatorFactory {

  public BeanInstantiator create(Class<?> beanClass) throws Exception {
    return new DefaultBeanInstantiator(beanClass);
  }
}
