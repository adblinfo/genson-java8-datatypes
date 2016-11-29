package se.blinfo.genson;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
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
	
}
