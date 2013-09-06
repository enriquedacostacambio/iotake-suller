package com.iotake.suller.sullerj.binder.instantiator;

public interface BeanInstantiatorFactory {

  BeanInstantiator create(Class<?> beanClass) throws Exception;

}
