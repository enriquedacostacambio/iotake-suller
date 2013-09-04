package com.iotake.solr.client.binder.collection;

import static org.junit.Assert.assertSame;

import java.lang.reflect.Field;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

import org.easymock.EasyMock;
import org.junit.Test;

import com.google.common.collect.Iterators;

/**
 * Test for {@link AbstractCollectionCreatorFactory}
 * 
 * @author enrique.dacostacambio
 * 
 */
public class AbstractCollectionFactoryTest extends
    CollectionCreatorFactoryAbstractTest {

  private static class TestCollection<T> extends AbstractCollection<T> {

    @Override
    public Iterator<T> iterator() {
      return Iterators.emptyIterator();
    }

    @Override
    public int size() {
      return 0;
    }

  }

  @Override
  protected CollectionCreatorFactory createInstance() {
    return createInstance(TestCollection.class);
  }

  @Override
  protected Class<?> getCollectionImplementationType() {
    return TestCollection.class;
  }

  @Override
  protected Class<?> getCollectionImplementationTypeSubclass() {
    class TestCollectionExtension<T> extends TestCollection<T> {
    }
    return TestCollectionExtension.class;
  }

  private AbstractCollectionCreatorFactory createInstance(
      Class<?> collectionImplementationType) {
    return new AbstractCollectionCreatorFactory(collectionImplementationType) {
      @Override
      public CollectionCreator doCreate(Class<?> collectionType,
          Class<?> componentType, Class<?> documentClass, Field field) {
        return emptyCreatorMock();
      }
    };
  }

  private CollectionCreator emptyCreatorMock() {
    CollectionCreator creator = EasyMock.createMock(CollectionCreator.class);
    EasyMock.replay(creator);
    return creator;
  }

  @Test(expected = NullPointerException.class)
  public void constructWithNullCollectionImplementationType() {
    createInstance(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructWithNotCollectionImplementationType() {
    createInstance(String.class);
  }

  @Test
  public void doCreate() {
    final Class<?> expectedCollectionType = Collection.class;
    final Class<?> expectedComponentType = String.class;
    final Class<?> expectedDocumentClass = getDocumentClass();
    final Field expectedField = getCollectionField();
    CollectionCreatorFactory factory = new AbstractCollectionCreatorFactory(
        TestCollection.class) {

      @Override
      public CollectionCreator doCreate(Class<?> collectionType,
          Class<?> componentType, Class<?> documentClass, Field field) {
        assertSame(expectedCollectionType, collectionType);
        assertSame(expectedComponentType, componentType);
        assertSame(expectedDocumentClass, documentClass);
        assertSame(expectedField, field);
        return emptyCreatorMock();
      }
    };
    factory.create(expectedCollectionType, expectedComponentType,
        expectedDocumentClass, expectedField);
  }

}
