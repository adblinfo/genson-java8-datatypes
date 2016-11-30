package se.blinfo.genson;

import static org.junit.Assert.*;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

import org.junit.Test;

/**
 * Java8 Types tests e.g {@link Optional} {@link LocalDate}
 * 
 * @author ad
 *
 */
public class GensonJava8TypesBundleTest {
	private JsonConverter genson = new GensonConverter();
	
	@Test
    public void testOptional() {
        assertEquals("10", genson.serialize(OptionalInt.of(10)));
        assertEquals(OptionalInt.of(10), genson.deserialize("10", OptionalInt.class));
        
        assertEquals("null", genson.serialize(OptionalInt.empty()));
        
        assertEquals(OptionalInt.empty(), genson.deserialize("null", OptionalInt.class));
        assertEquals("10", genson.serialize(OptionalLong.of(10)));
        assertEquals(OptionalLong.of(10), genson.deserialize("10", OptionalLong.class));
        assertEquals("10.0", genson.serialize(OptionalDouble.of(10.0)));
        assertEquals(OptionalDouble.of(10.0), genson.deserialize("10.0", OptionalDouble.class));

        assertEquals("\"test\"", genson.serialize(Optional.of("test")));
        assertEquals("\"test\"", genson.serialize(Optional.of(Optional.of("test"))));

        assertEquals("null", genson.serialize(Optional.empty()));
        assertEquals("null", genson.serialize(Optional.of(Optional.empty())));
        assertEquals(Optional.empty(), genson.deserialize("null", Optional.class));
    }
	

    @Test
    public void testDateTime() throws Exception {
        // java8 datetime
        ZoneId zoneId = ZoneId.of("Asia/Kolkata");
        Instant instant = Instant.ofEpochMilli(1457595643101L);
        assertEquals("\"2016-03-10T07:40:43.101Z\"", genson.serialize(instant));
        assertEquals(instant, genson.deserialize("\"2016-03-10T07:40:43.101Z\"", Instant.class));
        //assertEquals(instant, genson.deserialize("\"2016-03-10T08:40:43.101+01:00\"", Instant.class));
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId);
        assertEquals("\"2016-03-10T13:10:43.101+05:30[Asia/Kolkata]\"", genson.serialize(zonedDateTime));
        assertEquals(zonedDateTime, genson.deserialize("\"2016-03-10T13:10:43.101+05:30[Asia/Kolkata]\"", ZonedDateTime.class));
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
        assertEquals("\"2016-03-10T13:10:43.101\"", genson.serialize(localDateTime));
        assertEquals(localDateTime, genson.deserialize("\"2016-03-10T13:10:43.101\"", LocalDateTime.class));
        LocalDate localDate = localDateTime.toLocalDate();
        assertEquals("\"2016-03-10\"", genson.serialize(localDate));
        assertEquals(localDate, genson.deserialize("\"2016-03-10\"", LocalDate.class));
        LocalTime localTime = localDateTime.toLocalTime();
        assertEquals("\"13:10:43.101\"", genson.serialize(localTime));
        assertEquals(localTime, genson.deserialize("\"13:10:43.101\"", LocalTime.class));
        OffsetDateTime offsetDateTime = OffsetDateTime.ofInstant(instant, zoneId);
        assertEquals("\"2016-03-10T13:10:43.101+05:30\"", genson.serialize(offsetDateTime));
        assertEquals(offsetDateTime, genson.deserialize("\"2016-03-10T13:10:43.101+05:30\"", OffsetDateTime.class));
        OffsetTime offsetTime = offsetDateTime.toOffsetTime();
        assertEquals("\"13:10:43.101+05:30\"", genson.serialize(offsetTime));
        assertEquals(offsetTime, genson.deserialize("\"13:10:43.101+05:30\"", OffsetTime.class));

        assertNull(genson.deserialize("null", OffsetTime.class));

        YearMonth yearMonth = YearMonth.of(2016, 3);
        assertEquals("\"2016-03\"", genson.serialize(yearMonth));
        assertEquals(yearMonth, genson.deserialize("\"2016-03\"", YearMonth.class));
        Year year = Year.of(2016);
        assertEquals("\"2016\"", genson.serialize(year));
        assertEquals(year, genson.deserialize("\"2016\"", Year.class));

        Period period = Period.ofDays(1);
        assertEquals("\"P1D\"", genson.serialize(period));
        assertEquals(period, genson.deserialize("\"P1D\"", Period.class));

        Duration duration = Duration.ofDays(1);
        assertEquals("\"P1D\"", genson.serialize(period));
        assertEquals(duration, genson.deserialize("\"P1D\"", Duration.class));
    }
	
}
