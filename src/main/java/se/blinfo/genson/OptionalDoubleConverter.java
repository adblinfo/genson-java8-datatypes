package se.blinfo.genson;

import java.util.OptionalDouble;

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
public class OptionalDoubleConverter implements Converter<OptionalDouble> {
	public final static OptionalDoubleConverter instance = new OptionalDoubleConverter();

	private OptionalDoubleConverter() {
	}

	@Override
	public void serialize(OptionalDouble object, ObjectWriter writer, Context ctx) throws Exception {
		// value type should not be null
        if (object == null) {
        	writer.writeNull();
            return;
        }
        if (!object.isPresent()) {
        	writer.writeNull();
            return;
        }
		writer.writeValue(object.getAsDouble());
	}

	@Override
	public OptionalDouble deserialize(ObjectReader reader, Context ctx) throws Exception {
		if (ValueType.NULL.equals(reader.getValueType())) {
			return OptionalDouble.empty();
		}
		return OptionalDouble.of(reader.valueAsDouble());
	}
}

