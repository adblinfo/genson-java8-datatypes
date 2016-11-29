package se.blinfo.genson;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;

/**
 * 
 * @author ad
 *
 */
public class GensonConverter implements JsonConverter {
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private final Genson genson = new GensonBuilder()
			.useDateAsTimestamp(false)
			//.withConverterFactory(new DefaultConverters.EnumConverterFactory(false))
			.withConverter(BigDecimalConverter.instance, BigDecimal.class)
			.withBundle(new Java8TypesBundle()
					.useDateTimeFormatter(formatter)
					.useLocalDateTimeFormatter(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
			.create();

	@Override
	public String serialize(Object object) {
		return genson.serialize(object);
	}

	@Override
	public <T> T deserialize(String json, Class<T> type) {
		return genson.deserialize(json, type);
	}

	@Override
	public <T> T deserializeInto(String json, T destination) {
		return genson.deserializeInto(json.replace('\'', '"'), destination);
	}
	  
}
