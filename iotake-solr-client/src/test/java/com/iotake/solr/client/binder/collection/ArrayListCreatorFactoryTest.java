package com.iotake.solr.client.binder.collection;

import java.util.ArrayList;

/**
 * Test for {@link ArrayListCreatorFactory}
 * 
 * @author enrique.dacostacambio
 * 
 */
public class ArrayListCreatorFactoryTest extends
    CollectionCreatorFactoryAbstractTest {

  @Override
  protected CollectionCreatorFactory createInstance() {
    return new ArrayListCreatorFactory();
  }

  @Override
  protected Class<?> getCollectionImplementationType() {
    return ArrayList.class;
  }

  @Override
  protected Class<?> getCollectionImplementationTypeSubclass() {
    class ArrayListExtension<T> extends ArrayList<T> {
      private static final long serialVersionUID = 1L;
    }
    return ArrayListExtension.class;
  }
}
