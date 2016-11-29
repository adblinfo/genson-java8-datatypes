package se.blinfo.genson;

/**
 * 
 * @author ad
 *
 */
public interface JsonConverter {

    String serialize(Object object);

    <T> T deserialize(String json, Class<T> type);
    
    <T> T deserializeInto(String json, T destination);
}
