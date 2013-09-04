package com.iotake.solr.client.binder.itest;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;

import com.iotake.solr.client.binder.ExtendedDocumentObjectBinder;
import com.iotake.solr.client.binder.value.GlobalIdValueConverterFactory.GlobalIdValueConverter;

public class BaseITest {

  protected ExtendedDocumentObjectBinder binder = new ExtendedDocumentObjectBinder();

  @Before
  public void createBinder() {
    binder = new ExtendedDocumentObjectBinder();
  }

  public class EasyDocument extends org.apache.solr.common.SolrDocument {

    private static final long serialVersionUID = 1L;

    public EasyDocument(Class<?> idBeanClass, Object id, Class<?> beanClass,
        Class<?>... beanClasses) {
      String globalId = null;
      if (id != null) {
        globalId = idBeanClass.getName() + GlobalIdValueConverter.DELIMITER
            + id;
      }
      set(binder.getGlobalIdFieldName(), globalId);
      String beanClassName = null;
      if (beanClass != null) {
        beanClassName = beanClass.getName();
      }
      set(binder.getClassFieldName(), beanClassName);
      Set<String> beanClassNames = null;
      if (beanClasses != null) {
        beanClassNames = new LinkedHashSet<String>(beanClasses.length);
        for (Class<?> aBeanClass : beanClasses) {
          beanClassNames.add(aBeanClass.getName());
        }
      }
      set(binder.getClassesFieldName(), beanClassNames);
    }

    public EasyDocument set(String key, Object value) {
      setField(key, value);
      return this;
    }
  }
}
