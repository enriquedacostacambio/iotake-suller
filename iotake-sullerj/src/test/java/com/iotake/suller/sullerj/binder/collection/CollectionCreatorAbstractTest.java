package com.iotake.suller.sullerj.binder.collection;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import java.util.Collection;

import org.junit.Test;

import com.iotake.suller.sullerj.binder.collection.CollectionCreator;

/**
 * Abstract test for implementations of {@link CollectionCreator}
 * 
 * @author enrique.dacostacambio
 * 
 */
public abstract class CollectionCreatorAbstractTest {

  protected abstract CollectionCreator createInstance();

  protected abstract Class<?> getCollectionImplementationType();

  @Test
  public void construct() {
    createInstance();
  }

  @Test
  public void create() {
    CollectionCreator creator = createInstance();
    Collection<?> collection = creator.create(1);
    assertThat(collection, instanceOf(getCollectionImplementationType()));
  }

  @Test
  public void createWithZeroInitialCapacity() {
    CollectionCreator creator = createInstance();
    Collection<?> collection = creator.create(0);
    assertThat(collection, instanceOf(getCollectionImplementationType()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void createWithNegativeInitialCapacity() {
    CollectionCreator creator = createInstance();
    creator.create(-1);
  }
}