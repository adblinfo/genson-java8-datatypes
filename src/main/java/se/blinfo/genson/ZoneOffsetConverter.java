package se.blinfo.genson;

import java.time.ZoneOffset;

import com.owlike.genson.Context;
import com.owlike.genson.Converter;
import com.owlike.genson.annotation.HandleBeanView;
import com.owlike.genson.annotation.HandleClassMetadata;
import com.owlike.genson.annotation.HandleNull;
import com.owlike.genson.stream.ObjectReader;
import com.owlike.genson.stream.ObjectWriter;
import com.owlike.genson.stream.ValueType;

@HandleNull
@HandleClassMetadata
@HandleBeanView
public class ZoneOffsetConverter implements Converter<ZoneOffset> {
	
	public final static ZoneOffsetConverter instance = new ZoneOffsetConverter ();
	
	@Override
	public void serialize(ZoneOffset object, ObjectWriter writer, Context ctx) throws Exception {
		// value type should not be null
		if (object == null) {
			writer.writeNull();
			return;
		}
		writer.writeString(object.getId());
	}

	@Override
	public ZoneOffset deserialize(ObjectReader reader, Context ctx) throws Exception {
		if (ValueType.NULL.equals(reader.getValueType())) {
			return null;
		}
		return ZoneOffset.of(reader.valueAsString());
	}

}
