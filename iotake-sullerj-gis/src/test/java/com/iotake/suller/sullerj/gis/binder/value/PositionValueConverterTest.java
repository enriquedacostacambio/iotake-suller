package com.iotake.suller.sullerj.gis.binder.value;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.apache.solr.client.solrj.beans.BindingException;
import org.easymock.EasyMock;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.coordinate.Position;

import com.iotake.suller.sullerj.binder.source.Slot;
import com.iotake.suller.sullerj.binder.value.AbstractSingleSlotValueConverterAbstractTest;
import com.iotake.suller.sullerj.binder.value.SingleSlotValueConverter;
import com.iotake.suller.sullerj.gis.GisTestDocument;
import com.iotake.suller.sullerj.gis.binder.value.PositionValueConverterFactory;
import com.iotake.suller.sullerj.gis.provider.GeometryProvider;

public class PositionValueConverterTest extends
    AbstractSingleSlotValueConverterAbstractTest {

  /* TODO: FIGURE OUT TEST DEPENDENCIEs and remove this code */
  /* XXX: BEGIN: SingleSlotValueConverterAbstractTest */
  @Test
  public void construct() {
    createInstance();
  }

  protected Slot validatedGetSlot() {
    SingleSlotValueConverter conveter = createInstance();
    Slot slot = conveter.getSlot();
    assertNotNull(slot);
    assertNotNull(slot.getPath());
    assertNotNull(slot.getType());
    return slot;
  }

  @SuppressWarnings("unchecked")
  protected Object validatedToDocumentValue(Object beanValue) {
    SingleSlotValueConverter conveter = createInstance();
    Object documentValue = conveter.toDocumentValue(beanValue);
    Slot slot = conveter.getSlot();
    assertThat(
        documentValue,
        CoreMatchers.anyOf(CoreMatchers.<Object> nullValue(),
            CoreMatchers.instanceOf(slot.getWrapperType())));
    return documentValue;
  }

  @SuppressWarnings("unchecked")
  protected Object validatedToBeanValue(Object documentValue, Class<?> target) {
    SingleSlotValueConverter conveter = createInstance();
    Object beanValue = conveter.toBeanValue(documentValue);
    assertThat(
        beanValue,
        CoreMatchers.anyOf(CoreMatchers.<Object> nullValue(),
            CoreMatchers.instanceOf(target)));
    return beanValue;
  }

  /* XXX: END: SingleSlotValueConverterAbstractTest */
  /* XXX: BEGIN: AbstractSingleSlotValueConverterAbstractTest */
  @Test
  public void getSlotsType() {
    // AbstractSingleSlotValueConverter converter = createInstance();
    //
    // Class<?> slotsType = converter.getSlotType();
    // assertNotNull(slotsType);
  }

  /* XXX: END: AbstractSingleSlotValueConverterAbstractTest */

  private static final double LONGITUDE = 23;
  private static final double LATITUDE = 45.1;
  private static final double[] COORDINATES = { LONGITUDE, LATITUDE };

  @Override
  protected PositionValueConverterFactory.PositionValueConverter createInstance() {
    return createInstance(position);
  }

  protected PositionValueConverterFactory.PositionValueConverter createInstance(
      Position position) {
    return new PositionValueConverterFactory.PositionValueConverter(
        Position.class, GisTestDocument.class, GisTestDocument.POSITION_FIELD,
        createGeometryProvider(), "path__to__field");
  }

  private GeometryProvider createGeometryProvider() {
    return createGeometryProvider(position);
  }

  private GeometryProvider createGeometryProvider(Position position) {
    GeometryProvider geometryProvider = EasyMock
        .createMock(GeometryProvider.class);
    expect(geometryProvider.toCoordinates(position)).andReturn(COORDINATES)
        .anyTimes();
    expect(geometryProvider.toPositon(LONGITUDE, LATITUDE)).andReturn(position)
        .anyTimes();
    replay(geometryProvider);
    return geometryProvider;
  }

  private Position position = null;

  @Before
  public void createPosition() {
    DirectPosition directPosition = createMock(DirectPosition.class);
    expect(directPosition.getCoordinateReferenceSystem()).andReturn(
        DefaultGeographicCRS.WGS84);
    expect(directPosition.getCoordinate()).andReturn(COORDINATES);
    replay(directPosition);

    Position position = createMock(Position.class);
    expect(position.getDirectPosition()).andReturn(directPosition);
    replay(position);
    this.position = position;
  }

  @Test
  public void toBeanValue() {
    Object beanValue = validatedToBeanValue(LONGITUDE + " " + LATITUDE,
        Position.class);
    assertEquals(position, beanValue);
  }

  @Test
  public void toBeanValueWithNull() {
    Object beanValue = validatedToBeanValue(null, Position.class);
    assertNull(beanValue);
  }

  @Test(expected = BindingException.class)
  public void toBeanValueWithInvalidType() {
    validatedToBeanValue(new int[1], Position.class);
  }

  @Test(expected = BindingException.class)
  public void toBeanValueWithEmpty() {
    validatedToBeanValue("", Position.class);
  }

  @Test(expected = BindingException.class)
  public void toBeanValueWithHeadingSpace() {
    validatedToBeanValue(" " + LONGITUDE + " " + LATITUDE, Position.class);
  }

  @Test(expected = BindingException.class)
  public void toBeanValueWithTrailingSpace() {
    validatedToBeanValue(LONGITUDE + " ", Position.class);
  }

  @Test(expected = BindingException.class)
  public void toBeanValueNoSpace() {
    validatedToBeanValue(LONGITUDE, Position.class);
  }

  @Test(expected = BindingException.class)
  public void toBeanValueValidLongitude() {
    validatedToBeanValue("x " + LATITUDE, Position.class);
  }

  @Test(expected = BindingException.class)
  public void toBeanValueValidLatitude() {
    validatedToBeanValue(LONGITUDE + " x", Position.class);
  }

  @Test
  public void toDocumentValue() {
    Object documentValue = validatedToDocumentValue(position);
    assertEquals(LONGITUDE + " " + LATITUDE, documentValue);
  }

  @Test
  public void toDocumentValueWithNull() {
    Object documentValue = validatedToDocumentValue(null);
    assertNull(documentValue);
  }

  @Test(expected = BindingException.class)
  public void toDocumentValueWithInvalidType() {
    validatedToDocumentValue("boo");
  }

}
