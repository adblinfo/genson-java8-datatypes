package se.blinfo.genson;

import java.lang.reflect.Type;
import java.util.Optional;

import com.owlike.genson.Context;
import com.owlike.genson.Converter;
import com.owlike.genson.Factory;
import com.owlike.genson.Genson;
import com.owlike.genson.annotation.HandleNull;
import com.owlike.genson.reflect.TypeUtil;
import com.owlike.genson.stream.ObjectReader;
import com.owlike.genson.stream.ObjectWriter;
import com.owlike.genson.stream.ValueType;

/**
 * Java8 types converters.
 * 
 * @author ad
 *
 * @param <T>
 */
@HandleNull
public class OptionalConverter<T> implements Converter<Optional<T>> {
	/**
	 * 
	 * @author ad
	 *
	 */
	static class OptionalConverterFactory implements Factory<Converter<Optional<Object>>> {

		@Override
		public Converter<Optional<Object>> create(Type type, Genson genson) {
			Type typeOfValue = TypeUtil.typeOf(0, type);

			return new OptionalConverter<Object>(genson.provideConverter(typeOfValue));
		}
	}

	private final Converter<T> delegateConverter;

	private OptionalConverter(Converter<T> delegateConverter) {
		this.delegateConverter = delegateConverter;
	}

	@Override
	public void serialize(Optional<T> object, ObjectWriter writer, Context ctx) throws Exception {
		// value type should not be null
		if (object == null) {
			writer.writeNull();
			return;
		}
		if (!object.isPresent()) {
			writer.writeNull();
			return;
		}
		delegateConverter.serialize(object.get(), writer, ctx);
	}

	@Override
	public Optional<T> deserialize(ObjectReader reader, Context ctx) throws Exception {
		if (ValueType.NULL == reader.getValueType()) {
			return Optional.empty();
		}
		return Optional.of(delegateConverter.deserialize(reader, ctx));
	}
}