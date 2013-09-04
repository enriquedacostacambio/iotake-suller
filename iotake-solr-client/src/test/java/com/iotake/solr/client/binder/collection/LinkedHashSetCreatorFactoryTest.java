package com.iotake.solr.client.binder.collection;

import java.util.LinkedHashSet;

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
