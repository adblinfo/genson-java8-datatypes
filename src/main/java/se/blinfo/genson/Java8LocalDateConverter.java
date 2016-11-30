package se.blinfo.genson;

import static java.time.format.DateTimeFormatter.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
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
	public Java8LocalDateConverter(DateTimeFormatter formatter) {
		this.formatter = formatter;
	}

	public static Java8LocalDateConverter<LocalDate> makeLocalDateConverter(DateTimeFormatter formatter) {

		return new Java8LocalDateConverter<LocalDate>(formatter) {
			private final DateTimeFormatter localFormatter = ISO_LOCAL_DATE;

			protected LocalDate fromString(String value) {
				return (LocalDate) localFormatter.parse(value, LocalDate::from);
			}

			protected LocalDate fromLong(long value) {
				return getDateFromTimestamp(value);
			}
		};
	}

	public static Java8LocalDateConverter<LocalDateTime> makeLocalDateTimeConverter(DateTimeFormatter formatter) {
		return new Java8LocalDateConverter<LocalDateTime>(formatter) {
			private final DateTimeFormatter localFormatter = ISO_LOCAL_DATE_TIME;

			protected LocalDateTime fromString(String value) {
				return (LocalDateTime) localFormatter.parse(value, LocalDateTime::from);
			}

			protected LocalDateTime fromLong(long value) {
				return getDateTimeFromTimestamp(value);
			}
		};
	}

	public static Java8LocalDateConverter<LocalTime> makeLocalTimeConverter(DateTimeFormatter formatter) {
		return new Java8LocalDateConverter<LocalTime>(formatter) {
			private final DateTimeFormatter localFormatter = ISO_LOCAL_TIME;

			protected LocalTime fromString(String value) {
				return (LocalTime) localFormatter.parse(value, LocalTime::from);
			}

			protected LocalTime fromLong(long value) {
				return getTimeFromTimestamp(value);
			}
		};
	}

	public static Java8LocalDateConverter<Instant> makeInstantConverter(DateTimeFormatter formatter) {
		return new Java8LocalDateConverter<Instant>(formatter) {
			private final DateTimeFormatter localFormatter = ISO_INSTANT;

			protected Instant fromString(String value) {
				return (Instant) localFormatter.parse(value, Instant::from);
			}

			protected Instant fromLong(long timestamp) {
				return getInstant(timestamp);
			}
		};
	}

	public static Java8LocalDateConverter<OffsetDateTime> makeOffsetDateTimeConverter(DateTimeFormatter formatter) {
		return new Java8LocalDateConverter<OffsetDateTime>(formatter) {
			private final DateTimeFormatter localFormatter = ISO_OFFSET_DATE_TIME;

			protected OffsetDateTime fromString(String value) {
				return (OffsetDateTime) localFormatter.parse(value, OffsetDateTime::from);
			}

			protected OffsetDateTime fromLong(long timestamp) {
				if (timestamp == 0)
					return null;
				return OffsetDateTime.ofInstant(getInstant(timestamp), TimeZone.getDefault().toZoneId());
			}
		};
	}

	public static Java8LocalDateConverter<OffsetTime> makeOffsetTimeConverter(DateTimeFormatter formatter) {
		return new Java8LocalDateConverter<OffsetTime>(formatter) {
			private final DateTimeFormatter localFormatter = ISO_OFFSET_TIME;

			protected OffsetTime fromString(String value) {
				return (OffsetTime) localFormatter.parse(value, OffsetTime::from);
			}

			protected OffsetTime fromLong(long timestamp) {
				if (timestamp == 0)
					return null;
				return OffsetTime.ofInstant(getInstant(timestamp), TimeZone.getDefault().toZoneId());
			}
		};
	}

	public static Java8LocalDateConverter<ZonedDateTime> makeZonedDateTimeConverter(DateTimeFormatter formatter) {
		return new Java8LocalDateConverter<ZonedDateTime>(formatter) {
			private final DateTimeFormatter localFormatter = ISO_ZONED_DATE_TIME;

			protected ZonedDateTime fromString(String value) {
				return (ZonedDateTime) localFormatter.parse(value, ZonedDateTime::from);
			}

			protected ZonedDateTime fromLong(long timestamp) {
				if (timestamp == 0)
					return null;
				return ZonedDateTime.ofInstant(getInstant(timestamp), TimeZone.getDefault().toZoneId());
			}
		};
	}

	@Override
	public void serialize(T object, ObjectWriter writer, Context ctx) throws Exception {
		// value type should not be null
		if (object == null) {
			writer.writeNull();
			return;
		}
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

	private static LocalDateTime getDateTimeFromTimestamp(long timestamp) {
		if (timestamp == 0)
			return null;
		return LocalDateTime.ofInstant(getInstant(timestamp), TimeZone.getDefault().toZoneId());
	}

	private static Instant getInstant(long timestamp) {
		if (timestamp == 0)
			return null;
		return Instant.ofEpochSecond(timestamp);
	}

	private static LocalDate getDateFromTimestamp(long timestamp) {
		LocalDateTime date = getDateTimeFromTimestamp(timestamp);
		return date == null ? null : date.toLocalDate();
	}

	private static LocalTime getTimeFromTimestamp(long timestamp) {
		LocalDateTime date = getDateTimeFromTimestamp(timestamp);
		return date == null ? null : date.toLocalTime();
	}

	protected abstract T fromLong(long value);

	protected abstract T fromString(String value);
}