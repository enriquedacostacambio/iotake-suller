package com.iotake.suller.sullerj.binder.source;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

import org.apache.solr.client.solrj.beans.BindingException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

import com.iotake.suller.sullerj.binder.collection.CollectionCreator;

public class CollectionSource extends MultiValuedSource {

  private final CollectionCreator collectionCreator;

  private final Class<?> collectionType;

  public CollectionSource(Class<?> documentClass, Field field,
      Class<?> collectionType, Class<?> componentType,
      CollectionCreator collectionCreator,
      MultiValuableDocumentSource itemSource, boolean readable, boolean writable) {
    super(documentClass, field, componentType, itemSource, readable, writable);
    if (!Collection.class.isAssignableFrom(collectionType)) {
      throw new BindingException("Field type must be an array "
          + field.getDeclaringClass().getName() + "." + field.getName());
    }
    this.collectionType = collectionType;
    this.collectionCreator = collectionCreator;
  }

  public SourceType getType() {
    return SourceType.COLLECTION;
  }

  public Class<?> getCollectionType() {
    return collectionType;
  }

  protected void doFieldTransfer(Object value, SolrInputDocument document) {
    Collection<?> collection = (Collection<?>) value;
    if (collection == null || collection.isEmpty()) {
      transferEmpty(document);
      return;
    }
    for (Object item : collection) {
      getItemSource().transfer(item, document);
    }
  }

  protected Object doFieldExtract(SolrDocument document) {
    Object values = getItemSource().extract(document);
    Collection<?> valuesCollection;
    if (values == null) {
      valuesCollection = Collections.EMPTY_LIST;
    } else if (values instanceof Collection) {
      valuesCollection = (Collection<?>) values;
    } else {
      valuesCollection = Collections.singleton(values);
    }
    int size = valuesCollection.size();
    @SuppressWarnings("unchecked")
    Collection<Object> collection = (Collection<Object>) collectionCreator
        .create(size);
    collection.addAll((Collection<?>) valuesCollection);
    return collection;
  }
}