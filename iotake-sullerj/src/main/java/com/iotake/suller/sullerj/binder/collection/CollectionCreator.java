package com.iotake.suller.sullerj.binder.collection;

import java.util.Collection;

/**
 * Interface used to instantiate collection properties when building beans out
 * of documents.
 * 
 * @author enrique.dacostacambio
 * 
 */
public interface CollectionCreator {

  /**
   * Creates a new collection instance to be populated by the binder.
   * 
   * @param initialCapacity
   *          The number of elements that will be added to the collection. To be
   *          used with collections that can benefit from this information.
   * @return the newly created empty collection instance.
   */
  Collection<?> create(int initialCapacity);

}