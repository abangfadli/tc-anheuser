package topcoder.topcoder.anheuser.util.pref;


import java.util.List;
import java.util.Map;

/**
 * Created by ahmadfadli on 8/29/15.
 */
public interface PrefManager {
    boolean writeMany(Map<String, Object> values);
    boolean writeString(String key, String value);
    boolean writeInteger(String key, Integer value);
    boolean writeLong(String key, Long value);
    boolean writeFloat(String key, Float value);
    boolean writeBoolean(String key, Boolean value);

    Map<String, Object> getMany(Map<String, Object> keysAndDefaultValues);
    String getString(String key, String defaultValue);
    Integer getInteger(String key, Integer defaultValue);
    Float getFloat(String key, Float defaultValue);
    Long getLong(String key, Long defaultValue);
    Boolean getBoolean(String key, Boolean defaultValue);

    boolean deleteAll();
    boolean delete(String key);
    boolean deleteMany(List<String> keys);
}
