package com.iotake.suller.sullerj.binder.itest;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;

import com.iotake.suller.sullerj.binder.ExtendedDocumentObjectBinder;
import com.iotake.suller.sullerj.binder.value.GlobalIdValueConverterFactory.GlobalIdValueConverter;

public class BaseITest {

  protected ExtendedDocumentObjectBinder binder = new ExtendedDocumentObjectBinder();

  @Before
  public void createBinder() {
    binder = new ExtendedDocumentObjectBinder();
  }

  public class EasyDocument extends org.apache.solr.common.SolrDocument {

    private static final long serialVersionUID = 1L;

    public EasyDocument(Object id, Class<?> beanClass, Class<?>... beanClasses) {
      setGlobalId(id, beanClass);
      setBeanClass(beanClass);
      setBeanClasses(beanClass, beanClasses);
    }

    public EasyDocument setGlobalId(Object id, Class<?> beanClass) {
      String globalId = null;
      if (id != null) {
        globalId = beanClass.getName() + GlobalIdValueConverter.DELIMITER + id;
      }
      return set(binder.getGlobalIdFieldName(), globalId);
    }

    public EasyDocument setBeanClasses(Class<?> beanClass,
        Class<?>... beanClasses) {
      Set<String> beanClassNames = null;
      if (beanClasses != null) {
        beanClassNames = new LinkedHashSet<String>(beanClasses.length);
        for (Class<?> aBeanClass : beanClasses) {
          String beanClassName = null;
          if (beanClass != null) {
            beanClassName = aBeanClass.getName();
          }
          beanClassNames.add(beanClassName);
        }
      }
      return set(binder.getClassesFieldName(), beanClassNames);
    }

    public EasyDocument setBeanClass(Class<?> beanClass) {
      String beanClassName = null;
      if (beanClass != null) {
        beanClassName = beanClass.getName();
      }
      return set(binder.getClassFieldName(), beanClassName);
    }

    public EasyDocument set(String key, Object value) {
      setField(key, value);
      return this;
    }
  }
}
