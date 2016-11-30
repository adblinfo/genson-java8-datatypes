package se.blinfo.genson;

import static java.time.format.DateTimeFormatter.*;
import static se.blinfo.genson.Java8LocalDateConverter.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
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
			Class<?> rawClass = property.getRawClass();
			if (ann != null) {
				if (LocalDate.class.isAssignableFrom(rawClass)) {
					return makeLocalDateConverter(formatter(ann, localDateFormatter));
				} else if (LocalDateTime.class.isAssignableFrom(rawClass)) {
					return makeLocalDateTimeConverter(formatter(ann, localDateTimeFormatter));
				} else if (LocalTime.class.isAssignableFrom(rawClass)) {
					return makeLocalTimeConverter(formatter(ann, localTimeFormatter));
				} else if (Instant.class.isAssignableFrom(rawClass)) {
					makeInstantConverter(instantWriterFormatter);
				} else if (OffsetDateTime.class.isAssignableFrom(rawClass)) {
					makeOffsetDateTimeConverter(offsetDateTimeFormatter);
				} else if (OffsetTime.class.isAssignableFrom(rawClass)) {
					makeOffsetTimeConverter(offsetTimeFormatter);
				} else if (ZonedDateTime.class.isAssignableFrom(rawClass)) {
					makeZonedDateTimeConverter(zonnedDateTimeFormatter);
				}
			} // local formats
			else if (LocalDate.class.isAssignableFrom(rawClass)) {
				return makeLocalDateConverter(localDateFormatter);
			} else if (LocalDateTime.class.isAssignableFrom(rawClass)) {
				return makeLocalDateTimeConverter(localDateTimeFormatter);
			} else if (LocalTime.class.isAssignableFrom(rawClass)) {
				return makeLocalTimeConverter(localTimeFormatter);
			} else if (Instant.class.isAssignableFrom(rawClass)) {
				makeInstantConverter(instantWriterFormatter);
			} else if (OffsetDateTime.class.isAssignableFrom(rawClass)) {
				makeOffsetDateTimeConverter(offsetDateTimeFormatter);
			} else if (OffsetTime.class.isAssignableFrom(rawClass)) {
				makeOffsetTimeConverter(offsetTimeFormatter);
			} else if (ZonedDateTime.class.isAssignableFrom(rawClass)) {
				makeZonedDateTimeConverter(zonnedDateTimeFormatter);
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

	private DateTimeFormatter localDateFormatter = ISO_LOCAL_DATE;
	private DateTimeFormatter localDateTimeFormatter = ISO_LOCAL_DATE_TIME;
	private DateTimeFormatter localTimeFormatter = ISO_LOCAL_TIME;
	private DateTimeFormatter instantWriterFormatter = ISO_INSTANT;
	private DateTimeFormatter offsetDateTimeFormatter = ISO_OFFSET_DATE_TIME;
	private DateTimeFormatter offsetTimeFormatter = ISO_OFFSET_TIME;
	private DateTimeFormatter zonnedDateTimeFormatter = ISO_ZONED_DATE_TIME;

	@Override
	public void configure(GensonBuilder builder) {
		builder.withContextualFactory(new Java8TimeConverterContextualFactory())
		.withConverters(
				makeLocalDateConverter(localDateFormatter), 
				makeLocalDateTimeConverter(localDateTimeFormatter),
				makeLocalTimeConverter(localTimeFormatter),
				makeInstantConverter(instantWriterFormatter),
				makeOffsetDateTimeConverter(offsetDateTimeFormatter),
				makeOffsetTimeConverter(offsetTimeFormatter),
				makeZonedDateTimeConverter(zonnedDateTimeFormatter),
				DurationConverter.instance,
				MonthDayConverter.instance,
				PeriodConverter.instance,
				YearConverter.instance,
				YearMonthConverter.instance,
				ZoneOffsetConverter.instance,
				OptionalIntConverter.instance,
				OptionalLongConverter.instance,
				OptionalDoubleConverter.instance
				)
		.withConverterFactory(new OptionalConverter.OptionalConverterFactory());
	}

	public Java8TypesBundle useInstantFormatter(DateTimeFormatter instantWriterFormatter) {
		this.instantWriterFormatter = instantWriterFormatter;
		return this;
	}

	public Java8TypesBundle useLocalDateFormatter(DateTimeFormatter localDateFormatter) {
		this.localDateFormatter = localDateFormatter;
		return this;
	}

	public Java8TypesBundle useLocalDateTimeFormatter(DateTimeFormatter localDateTimeFormatter) {
		this.localDateTimeFormatter = localDateTimeFormatter;
		return this;
	}

	public Java8TypesBundle useLocalTimeFormatter(DateTimeFormatter localTimeFormatter) {
		this.localTimeFormatter = localTimeFormatter;
		return this;
	}
	
	public Java8TypesBundle useOffsetDateTimeFormatter(DateTimeFormatter offsetDateTimeFormatter) {
		this.offsetDateTimeFormatter = offsetDateTimeFormatter;
		return this;
	}
	
	public Java8TypesBundle useOffsetTimeFormatter(DateTimeFormatter offsetTimeFormatter) {
		this.offsetTimeFormatter = offsetTimeFormatter;
		return this;
	}
	
	public Java8TypesBundle useZonnedDateTimeFormatter(DateTimeFormatter zonnedDateTimeFormatter) {
		this.zonnedDateTimeFormatter = zonnedDateTimeFormatter;
		return this;
	}
}