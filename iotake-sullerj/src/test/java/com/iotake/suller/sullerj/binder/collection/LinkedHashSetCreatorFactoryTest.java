package com.iotake.suller.sullerj.binder.collection;

import java.util.LinkedHashSet;

import com.iotake.suller.sullerj.binder.collection.CollectionCreatorFactory;
import com.iotake.suller.sullerj.binder.collection.LinkedHashSetCreatorFactory;

/**
 * Test for {@link LinkedHashSetCreatorFactory}
 * 
 * @author enrique.dacostacambio
 * 
 */
public class LinkedHashSetCreatorFactoryTest extends
    CollectionCreatorFactoryAbstractTest {

  @Override
  protected CollectionCreatorFactory createInstance() {
    return new LinkedHashSetCreatorFactory();
  }

  @Override
  protected Class<?> getCollectionImplementationType() {
    return LinkedHashSet.class;
  }

  @Override
  protected Class<?> getCollectionImplementationTypeSubclass() {
    class LinkedHashSetExtension<T> extends LinkedHashSet<T> {
      private static final long serialVersionUID = 1L;
    }
    return LinkedHashSetExtension.class;
  }

}
