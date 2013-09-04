package com.iotake.solr.client.binder.instantiator;

public interface BeanInstantiatorFactory {

  BeanInstantiator create(Class<?> beanClass) throws Exception;

}
