package se.blinfo.genson;

import java.util.OptionalLong;

import com.owlike.genson.Context;
import com.owlike.genson.Converter;
import com.owlike.genson.annotation.HandleBeanView;
import com.owlike.genson.annotation.HandleClassMetadata;
import com.owlike.genson.annotation.HandleNull;
import com.owlike.genson.stream.ObjectReader;
import com.owlike.genson.stream.ObjectWriter;
import com.owlike.genson.stream.ValueType;

/**
 * 
 * @author ad
 *
 */
@HandleNull
@HandleClassMetadata
@HandleBeanView
public class OptionalLongConverter implements Converter<OptionalLong> {
	public final static OptionalLongConverter instance = new OptionalLongConverter();

	private OptionalLongConverter() {
	}

	@Override
	public void serialize(OptionalLong object, ObjectWriter writer, Context ctx) throws Exception {
		// value type should not be null
        if (object == null) {
        	writer.writeNull();
            return;
        }
        if (!object.isPresent()) {
        	writer.writeNull();
            return;
        }
		writer.writeValue(object.getAsLong());
	}

	@Override
	public OptionalLong deserialize(ObjectReader reader, Context ctx) throws Exception {
		if (ValueType.NULL.equals(reader.getValueType())) {
			return OptionalLong.empty();
		}
		return OptionalLong.of(reader.valueAsLong());
	}
}
