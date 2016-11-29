package se.blinfo.genson;

import java.math.BigDecimal;

import com.owlike.genson.Context;
import com.owlike.genson.Converter;
import com.owlike.genson.annotation.HandleBeanView;
import com.owlike.genson.annotation.HandleClassMetadata;
import com.owlike.genson.stream.ObjectReader;
import com.owlike.genson.stream.ObjectWriter;

/**
 * 
 * @author ad
 *
 */
@HandleClassMetadata
@HandleBeanView
public class BigDecimalConverter implements Converter<BigDecimal> {
  public final static BigDecimalConverter instance = new BigDecimalConverter();

  private BigDecimalConverter() {
  }

  @Override
  public BigDecimal deserialize(ObjectReader reader, Context ctx) {
    return new BigDecimal(reader.valueAsString());
  }

  @Override
  public void serialize(BigDecimal object, ObjectWriter writer, Context ctx) {
    writer.writeValue(null != object ? object.toString() : null);
  }
}
