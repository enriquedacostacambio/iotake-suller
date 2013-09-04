package com.iotake.solr.client.binder.source;

import java.lang.reflect.Field;

import junit.framework.Assert;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.easymock.EasyMock;
import org.junit.Test;

import com.iotake.solr.client.binder.source.DocumentSource.SourceType;
import com.iotake.solr.client.binder.util.TestDocument;

public class FieldSourceTest extends FieldSourceAbstractTest {

  private static final String KEY = "key";
  
  @Override
  protected FieldSource createInstance(Class<?> documentClass) {
    return createInstance(documentClass, TestDocument.ID_FIELD, false, true);
  }
  

  @Override
  protected FieldSource createInstance(Class<?> documentClass, Field field,
      boolean readable, boolean writable) {
    return new FieldSource(documentClass, field, readable, writable) {

      public SourceType getType() {
        return getExpectedType();
      }

      public DocumentSource[] getInnerSources() {
        return new DocumentSource[0];
      }

      @Override
      protected Object doFieldExtract(SolrDocument document) {
        return document.get(KEY);
      }

      @Override
      protected void doFieldTransfer(Object value, SolrInputDocument document) {
        document.setField(KEY, value);
      }
    };
  }

  @Override
  protected SourceType getExpectedType() {
    return SourceType.PROPERTY;
  }

  @Test
  public void getDocumentClass() {
    AbstractDocumentSource source = createInstance();
    Class<?> documentClass = source.getDocumentClass();
    Assert.assertEquals(TestDocument.class, documentClass);
  }

  @Test
  public void getField() {
    FieldSource source = createInstance(TestDocument.class,
        TestDocument.ID_FIELD, true, true);
    Field field = source.getField();
    Assert.assertEquals(TestDocument.ID_FIELD, field);
  }

  @Test
  public void extract() {
    FieldSource source = createInstance(TestDocument.class,
        TestDocument.STRING_FIELD, true, true);
    SolrDocument document = EasyMock.createMock(SolrDocument.class);
    EasyMock.expect(document.get(KEY)).andReturn("abc");
    EasyMock.replay(document);
    Object value = source.extract(document);
    Assert.assertEquals("abc", value);
    EasyMock.verify(document);
  }

  @Test
  public void extractForNonReadable() {
    FieldSource source = createInstance(TestDocument.class,
        TestDocument.STRING_FIELD, false, true);
    SolrDocument document = EasyMock.createMock(SolrDocument.class);
    EasyMock.replay(document);
    Object value = source.extract(document);
    Assert.assertNull(value);
    EasyMock.verify(document);
  }

  @Test
  public void transfer() {
    FieldSource source = createInstance(TestDocument.class,
        TestDocument.STRING_FIELD, true, true);
    SolrInputDocument document = EasyMock.createMock(SolrInputDocument.class);
    document.setField(KEY, "abc");
    EasyMock.replay(document);
    source.transfer("abc", document);
    EasyMock.verify(document);
  }

  @Test
  public void transferForNonWritable() {
    FieldSource source = createInstance(TestDocument.class,
        TestDocument.STRING_FIELD, true, false);
    SolrInputDocument document = EasyMock.createMock(SolrInputDocument.class);
    EasyMock.replay(document);
    source.transfer("abc", document);
    EasyMock.verify(document);
  }

  @Test
  public void getFieldValue() {
    FieldSource source = createInstance(TestDocument.class,
        TestDocument.ID_FIELD, true, true);
    TestDocument bean = new TestDocument();
    bean.setIdField(123L);
    Object value = source.getFieldValue(bean);
    Assert.assertEquals(123L, value);
  }

  @Test
  public void setFieldValue() {
    FieldSource source = createInstance(TestDocument.class,
        TestDocument.ID_FIELD, true, true);
    TestDocument bean = new TestDocument();
    source.setFieldValue(bean, 123L);
    Object value = bean.getIdField();
    Assert.assertEquals(123L, value);
  }
}
