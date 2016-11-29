package se.blinfo.genson;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;
import static java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME;
import static se.blinfo.genson.Java8LocalDateConverter.makeLocalDateConverter;
import static se.blinfo.genson.Java8LocalDateConverter.makeLocalDateTimeConverter;
import static se.blinfo.genson.Java8LocalDateConverter.makeLocalTimeConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.owlike.genson.Converter;
import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;
import com.owlike.genson.annotation.JsonDateFormat;
import com.owlike.genson.convert.ContextualFactory;
import com.owlike.genson.ext.GensonBundle;
import com.owlike.genson.reflect.BeanProperty;

/**
 * Custom Genson Bundle for Java8 DateTime api.
 * 
 * @author ad
 *
 */
public class Java8TypesBundle extends GensonBundle {
	@SuppressWarnings("rawtypes")
	private final class Java8TimeConverterContextualFactory implements ContextualFactory {

		@Override
		public Converter create(BeanProperty property, Genson genson) {
			JsonDateFormat ann = property.getAnnotation(JsonDateFormat.class);
			if (ann != null) {
				if (LocalDate.class.isAssignableFrom(property.getRawClass())) {
					return makeLocalDateConverter(formatter(ann, localDateFormatter));
				} else if (LocalDateTime.class.isAssignableFrom(property.getRawClass())) {
					return makeLocalDateTimeConverter(formatter(ann, localDateTimeFormatter));
				} else if (LocalTime.class.isAssignableFrom(property.getRawClass())) {
					return makeLocalTimeConverter(formatter(ann, localTimeFormatter));
				}
			} // local formats
			else if (LocalDate.class.isAssignableFrom(property.getRawClass())) {
				return makeLocalDateConverter(localDateFormatter);
			} else if (LocalDateTime.class.isAssignableFrom(property.getRawClass())) {
				return makeLocalDateTimeConverter(localDateTimeFormatter);
			} else if (LocalTime.class.isAssignableFrom(property.getRawClass())) {
				return makeLocalTimeConverter(localTimeFormatter);
			}
			return null;
		}

		private DateTimeFormatter formatter(JsonDateFormat ann, DateTimeFormatter defaultFormatter) {
			Locale locale = ann.lang().isEmpty() ? Locale.getDefault() : new Locale(ann.lang());
			if (ann.value() == null || ann.value().isEmpty())
				return defaultFormatter;
			else
				return DateTimeFormatter.ofPattern(ann.value()).withLocale(locale);
		}
	}

	@SuppressWarnings("unused")
	private DateTimeFormatter dateTimeFormatter = ISO_LOCAL_DATE_TIME;
	private DateTimeFormatter localDateFormatter = ISO_LOCAL_DATE;
	private DateTimeFormatter localDateTimeFormatter = ISO_ZONED_DATE_TIME;
	private DateTimeFormatter localTimeFormatter = ISO_LOCAL_TIME;

	@Override
	public void configure(GensonBuilder builder) {
		builder.withContextualFactory(new Java8TimeConverterContextualFactory())
		.withConverters(
				makeLocalDateConverter(localDateFormatter), 
				makeLocalDateTimeConverter(localDateTimeFormatter),
				makeLocalTimeConverter(localTimeFormatter),
				OptionalIntConverter.instance,
				OptionalLongConverter.instance,
				OptionalDoubleConverter.instance
				)
		//.useDefaultValue(Optional.empty(), Optional.class)
		.withConverterFactory(new OptionalConverter.OptionalConverterFactory());
	}

	public Java8TypesBundle useDateTimeFormatter(DateTimeFormatter formatter) {
		this.dateTimeFormatter = formatter;
		return this;
	}

	public Java8TypesBundle useLocalDateFormatter(DateTimeFormatter formatter) {
		this.localDateFormatter = formatter;
		return this;
	}

	public Java8TypesBundle useLocalDateTimeFormatter(DateTimeFormatter formatter) {
		this.localDateTimeFormatter = formatter;
		return this;
	}

	public Java8TypesBundle useLocalTimeFormatter(DateTimeFormatter formatter) {
		this.localTimeFormatter = formatter;
		return this;
	}
}