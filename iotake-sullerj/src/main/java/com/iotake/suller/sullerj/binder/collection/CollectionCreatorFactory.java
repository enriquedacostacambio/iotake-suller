package com.iotake.suller.sullerj.binder.collection;

import java.lang.reflect.Field;

/**
 * Factory of {@link CollectionCreator}s.
 * 
 * @author enrique.dacostacambio
 * 
 */
public interface CollectionCreatorFactory {

  /**
   * Creates a new collection creator for the given field.
   * 
   * @param collectionType
   *          The expected collection type.
   * @param componentType
   *          The type of the elements that will be added to the collection.
   * @param documentClass
   *          The bean document class.
   * @param field
   *          The bean field in which the collections will be set.
   * @return The collection creator.
   */
  CollectionCreator create(Class<?> collectionType, Class<?> componentType,
      Class<?> documentClass, Field field);

}