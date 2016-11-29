package se.blinfo.genson;

import java.util.OptionalInt;

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
public class OptionalIntConverter implements Converter<OptionalInt> {
	public final static OptionalIntConverter instance = new OptionalIntConverter();
	private OptionalIntConverter() {
	}

	@Override
	public void serialize(OptionalInt object, ObjectWriter writer, Context ctx) throws Exception {
		// value type should not be null
        if (object == null) {
        	writer.writeNull();
            return;
        }
        if (!object.isPresent()) {
        	writer.writeNull();
            return;
        }
		writer.writeValue(object.getAsInt());
	}

	@Override
	public OptionalInt deserialize(ObjectReader reader, Context ctx) throws Exception {
		if (ValueType.NULL.equals(reader.getValueType())) {
			return OptionalInt.empty();
		}
		return OptionalInt.of(reader.valueAsInt());
	}
}