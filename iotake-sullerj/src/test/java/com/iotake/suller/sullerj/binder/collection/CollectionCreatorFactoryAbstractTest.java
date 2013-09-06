package com.iotake.suller.sullerj.binder.collection;

import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;

import org.junit.Test;

import com.iotake.suller.sullerj.binder.collection.CollectionCreator;
import com.iotake.suller.sullerj.binder.collection.CollectionCreatorFactory;
import com.iotake.suller.sullerj.binder.util.TestDocument;


/**
 * Abstract test for implementations of {@link CollectionCreatorFactory}
 * 
 * @author enrique.dacostacambio
 * 
 */
public abstract class CollectionCreatorFactoryAbstractTest {

  private interface NotImplementedCollection<T> extends Collection<T> {
  }

  private abstract class NotExtendedCollection<T> implements Collection<T> {
  }

  protected abstract CollectionCreatorFactory createInstance();

  protected Class<?> getDocumentClass() {
    return TestDocument.class;
  }

  protected Field getCollectionField() {
    return TestDocument.LIST_OF_STRING_FIELD;
  }

  protected abstract Class<?> getCollectionImplementationType();

  /**
   * Obtains a subclass of the collection implementation type.
   * 
   * @return a subclass of the class returned by
   *         {@link CollectionCreatorFactoryAbstractTest#getCollectionImplementationType()}
   *         or null if it is final.
   */
  protected abstract Class<?> getCollectionImplementationTypeSubclass();

  @Test
  public void construct() {
    createInstance();
  }

  @Test
  public void create() {
    CollectionCreatorFactory factory = createInstance();
    CollectionCreator creator = factory.create(
        getCollectionImplementationType(), String.class, getDocumentClass(),
        getCollectionField());
    assertNotNull(creator);
  }

  @Test(expected = NullPointerException.class)
  public void createWithNullCollectionType() {
    CollectionCreatorFactory factory = createInstance();
    factory
        .create(null, String.class, getDocumentClass(), getCollectionField());
  }

  @Test
  public void createWithImplementedCollectionType() {
    CollectionCreatorFactory factory = createInstance();
    factory.create(Collection.class, String.class, getDocumentClass(),
        getCollectionField());
  }

  @Test
  public void createWithExtendedCollectionType() {
    Class<?> collectionImplementationType = getCollectionImplementationType();
    Class<?> extendedCollectionType = collectionImplementationType
        .getSuperclass();
    if (extendedCollectionType == null
        || !Collection.class.isAssignableFrom(extendedCollectionType)) {
      // cannot test the case ( and the test case doesn't make sense)
      return;
    }
    CollectionCreatorFactory factory = createInstance();
    factory.create(extendedCollectionType, String.class, getDocumentClass(),
        getCollectionField());
  }

  @Test(expected = IllegalArgumentException.class)
  public void createWithNotCollectionType() {
    CollectionCreatorFactory factory = createInstance();
    factory.create(String.class, String.class, getDocumentClass(),
        getCollectionField());
  }

  @Test(expected = IllegalArgumentException.class)
  public void createWithNotImplementedCollectionType() {
    CollectionCreatorFactory factory = createInstance();
    factory.create(NotImplementedCollection.class, String.class,
        getDocumentClass(), getCollectionField());
  }

  @Test(expected = IllegalArgumentException.class)
  public void createWithNotExtendedCollectionType() {
    CollectionCreatorFactory factory = createInstance();
    factory.create(NotExtendedCollection.class, String.class,
        getDocumentClass(), getCollectionField());
  }

  @Test(expected = IllegalArgumentException.class)
  public void createWithExtendingCollectionType() {
    CollectionCreatorFactory factory = createInstance();
    Class<?> collectionImplementationType = getCollectionImplementationType();
    if (Modifier.isFinal(collectionImplementationType.getModifiers())) {
      throw new IllegalArgumentException("This is fine,  "
          + collectionImplementationType.getName()
          + " is final and cannot be subclassed.");
    }
    Class<?> extendingCollectionType = getCollectionImplementationTypeSubclass();
    factory.create(extendingCollectionType, String.class, getDocumentClass(),
        getCollectionField());
  }

  @Test(expected = NullPointerException.class)
  public void createWithNullComponentType() {
    CollectionCreatorFactory factory = createInstance();
    factory.create(getCollectionImplementationType(), null, getDocumentClass(),
        getCollectionField());
  }

  @Test(expected = NullPointerException.class)
  public void createWithNullDocumentClass() {
    CollectionCreatorFactory factory = createInstance();
    factory.create(getCollectionImplementationType(), String.class, null,
        getCollectionField());
  }

  @Test(expected = NullPointerException.class)
  public void createWithNullField() {
    CollectionCreatorFactory factory = createInstance();
    factory.create(getCollectionImplementationType(), String.class,
        getDocumentClass(), null);
  }
}
