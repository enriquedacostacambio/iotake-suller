package com.iotake.suller.sullerj.binder.itest.transfer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.solr.common.SolrInputDocument;

import com.iotake.suller.sullerj.binder.itest.BaseITest;
import com.iotake.suller.sullerj.binder.value.GlobalIdValueConverterFactory.GlobalIdValueConverter;

public abstract class AbstractTransferITest extends BaseITest {

  private static final int EXTRA_FIELDS = 3; // globalId, class and classes

  protected void checkFieldCount(SolrInputDocument document, int fields) {
    assertEquals(EXTRA_FIELDS + fields, document.size());
  }

  protected void checkGlobalId(SolrInputDocument document, Class<?> beanClass,
      Object id) {
    assertEquals(beanClass.getName() + GlobalIdValueConverter.DELIMITER + id,
        document.getFieldValue(binder.getGlobalIdFieldName()));
  }

  protected void checkClass(SolrInputDocument document, Class<?> beanClass) {
    assertEquals(beanClass.getName(),
        document.getFieldValue(binder.getClassFieldName()));
  }

  protected void checkClasses(SolrInputDocument document,
      Class<?>... beanClasses) {
    Set<String> beanClassNames = new LinkedHashSet<String>(beanClasses.length);
    for (Class<?> beanClass : beanClasses) {
      beanClassNames.add(beanClass.getName());
    }
    assertEquals(beanClassNames,
        document.getFieldValues(binder.getClassesFieldName()));
  }

  protected void checkProperty(SolrInputDocument document, String path,
      Object value) {
    assertTrue(document.containsKey(path));
    assertEquals(value, document.getFieldValue(path));
  }

  protected void checkNoProperty(SolrInputDocument document, String path) {
    assertFalse(document.containsKey(path));
  }

  protected void checkProperty(SolrInputDocument document, String path,
      Object[] values) {
    if (values == null) {
      values = new Object[0];
    }
    assertEquals(Arrays.asList(values), document.getFieldValues(path));
  }

  protected void checkProperty(SolrInputDocument document, String path,
      Collection<?> values) {
    if (values == null) {
      values = Collections.EMPTY_LIST;
    }
    assertEquals(new ArrayList<Object>(values), document.getFieldValues(path));
  }

}
