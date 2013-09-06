package com.iotake.suller.sullerj.binder.source;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

import org.apache.solr.client.solrj.beans.BindingException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

public class ArraySource extends MultiValuedSource {

  public ArraySource(Class<?> documentClass, Field field,
      Class<?> componentType, MultiValuableDocumentSource itemSource, boolean readable,
      boolean writable) {
    super(documentClass, field, componentType, itemSource, readable, writable);
    if (!field.getType().isArray()) {
      throw new BindingException("Field type must be an array "
          + field.getDeclaringClass().getName() + "." + field.getName());
    }
  }

  public SourceType getType() {
    return SourceType.ARRAY;
  }

  protected void doFieldTransfer(Object value, SolrInputDocument document) {
    int length = value == null ? 0 : Array.getLength(value);
    if (length == 0) {
      transferEmpty(document);
      return;
    }
    for (int i = 0; i < length; i++) {
      Object item = Array.get(value, i);
      getItemSource().transfer(item, document);
    }
  }

  protected Object doFieldExtract(SolrDocument document) {
    Object values = getItemSource().extract(document);
    Collection<?> collection;
    if (values == null) {
      collection = Collections.EMPTY_LIST;
    } else if (values instanceof Collection) {
      collection = (Collection<?>) values;
    } else {
      collection = Collections.singletonList(values);
    }
    int length = collection.size();
    Object array = Array.newInstance(getComponentType(), length);
    int index = 0;
    for (Object value : collection) {
      Array.set(array, index++, value);
    }
    return array;
  }
}