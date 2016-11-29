package se.blinfo.genson;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;
import java.util.TimeZone;

import com.owlike.genson.Context;
import com.owlike.genson.Converter;
import com.owlike.genson.annotation.HandleBeanView;
import com.owlike.genson.annotation.HandleClassMetadata;
import com.owlike.genson.stream.ObjectReader;
import com.owlike.genson.stream.ObjectWriter;
import com.owlike.genson.stream.ValueType;

/**
 * Java8 DateTime converters.
 * 
 * @author ad
 *
 * @param <T>
 */
@HandleClassMetadata
@HandleBeanView
public abstract class Java8LocalDateConverter<T extends Temporal> implements Converter<T> {
	protected final DateTimeFormatter formatter;
	protected final TemporalQuery<TemporalAccessor> temporalQuery;
	protected Java8LocalDateConverter(DateTimeFormatter formatter, TemporalQuery<TemporalAccessor> temporalQuery) {
		this.formatter = formatter;
		this.temporalQuery = temporalQuery;
	}
	
	@Override
	public void serialize(T object, ObjectWriter writer, Context ctx) throws Exception {
		writer.writeString(formatter.format(object));
	}

	@Override
	public T deserialize(ObjectReader reader, Context ctx) throws Exception {
		if (ValueType.INTEGER == reader.getValueType()) {
			return fromLong(reader.valueAsLong());
		} else {
			return fromString(reader.valueAsString());
		}
	}

	protected abstract T fromLong(long value);

	protected abstract T fromString(String value);

	static Java8LocalDateConverter<LocalDate> makeLocalDateConverter(DateTimeFormatter formatter) {
		return new Java8LocalDateConverter<LocalDate>(formatter, LocalDate::from) {
			protected LocalDate fromString(String value) {
				return (LocalDate) formatter.parse(value, temporalQuery);
			}

			protected LocalDate fromLong(long value) {
				return getDateFromTimestamp(value);
			}
		};
	}

	static Java8LocalDateConverter<LocalDateTime> makeLocalDateTimeConverter(DateTimeFormatter formatter) {
		return new Java8LocalDateConverter<LocalDateTime>(formatter, LocalDateTime::from) {
			private final DateTimeFormatter localFormatter = ISO_LOCAL_DATE_TIME;

			protected LocalDateTime fromString(String value) {
				return (LocalDateTime) localFormatter.parse(value, temporalQuery);
			}

			protected LocalDateTime fromLong(long value) {
				return getDateTimeFromTimestamp(value);
			}
		};
	}

	static Java8LocalDateConverter<LocalTime> makeLocalTimeConverter(DateTimeFormatter formatter) {
		return new Java8LocalDateConverter<LocalTime>(formatter, LocalTime::from) {
			private final DateTimeFormatter localFormatter = ISO_LOCAL_TIME;

			protected LocalTime fromString(String value) {
				return (LocalTime) localFormatter.parse(value, temporalQuery);
			}

			protected LocalTime fromLong(long value) {
				return getTimeFromTimestamp(value);
			}
		};
	}

	private static LocalDateTime getDateTimeFromTimestamp(long timestamp) {
		if (timestamp == 0)
			return null;
		return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), TimeZone.getDefault().toZoneId());
	}

	private static LocalDate getDateFromTimestamp(long timestamp) {
		LocalDateTime date = getDateTimeFromTimestamp(timestamp);
		return date == null ? null : date.toLocalDate();
	}
	
	private static LocalTime getTimeFromTimestamp(long timestamp) {
		LocalDateTime date = getDateTimeFromTimestamp(timestamp);
		return date == null ? null : date.toLocalTime();
	}
}