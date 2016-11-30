package se.blinfo.genson;

import java.time.YearMonth;

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
public class YearMonthConverter implements Converter<YearMonth> {
	
	public final static YearMonthConverter instance = new YearMonthConverter ();
	
	@Override
	public void serialize(YearMonth object, ObjectWriter writer, Context ctx) throws Exception {
		// value type should not be null
		if (object == null) {
			writer.writeNull();
			return;
		}
		writer.writeString(object.toString());
	}

	@Override
	public YearMonth deserialize(ObjectReader reader, Context ctx) throws Exception {
		if (ValueType.NULL.equals(reader.getValueType())) {
			return null;
		}
		return YearMonth.parse(reader.valueAsString());
	}

}
