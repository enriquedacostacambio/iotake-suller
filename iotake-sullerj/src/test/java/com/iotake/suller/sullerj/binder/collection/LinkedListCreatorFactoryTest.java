package com.iotake.suller.sullerj.binder.collection;

import java.util.LinkedList;

import com.iotake.suller.sullerj.binder.collection.CollectionCreatorFactory;
import com.iotake.suller.sullerj.binder.collection.LinkedListCreatorFactory;

/**
 * Test for {@link LinkedListCreatorFactory}
 * 
 * @author enrique.dacostacambio
 * 
 */
public class LinkedListCreatorFactoryTest extends
    CollectionCreatorFactoryAbstractTest {

  @Override
  protected CollectionCreatorFactory createInstance() {
    return new LinkedListCreatorFactory();
  }

  @Override
  protected Class<?> getCollectionImplementationType() {
    return LinkedList.class;
  }

  @Override
  protected Class<?> getCollectionImplementationTypeSubclass() {
    class LinkedListExtension<T> extends LinkedList<T> {
      private static final long serialVersionUID = 1L;
    }
    return LinkedListExtension.class;
  }
}
