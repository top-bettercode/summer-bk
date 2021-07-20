package top.bettercode.config

import org.springframework.boot.actuate.endpoint.annotation.*
import org.springframework.lang.Nullable
import top.bettercode.lang.property.PropertiesSource
import top.bettercode.lang.property.Settings
import java.util.*

/**
 * @author Peter Wu
 */
@Endpoint(id = "settings")
class SettingsEndpoint {

    @WriteOperation
    fun write(@Selector baseName: String?, key: String, value: String): Any {
        val propertiesSource: PropertiesSource? = Settings[baseName]
        return if (propertiesSource == null) {
            emptyMap<Any, Any>()
        } else {
            propertiesSource.put(key, value)
            Collections.singletonMap(key, value)
        }
    }

    @DeleteOperation
    fun delete(@Selector baseName: String, key: String): Any {
        val propertiesSource: PropertiesSource? = Settings[baseName]
        return if (propertiesSource == null) {
            emptyMap<Any, Any>()
        } else {
            val remove: String? = propertiesSource.remove(key)
            val map: MutableMap<String, String?>
            if (Settings.isDicCode(baseName)) {
                map = propertiesSource.mapOf(key).toMutableMap()
                val prefix = "$key."
                for (k in map.keys) {
                    propertiesSource.remove(prefix + k)
                }
                val typeKey = "$key|TYPE"
                val removeType: String? = propertiesSource.remove(typeKey)
                if (removeType != null) {
                    map["TYPE"] = removeType
                }
                if (remove != null) {
                    map[key] = remove
                }
            } else {
                map = Collections.singletonMap(key, remove)
            }
            map
        }
    }

    @ReadOperation
    fun read(@Selector baseName: String, @Nullable key: String?): Any {
        val propertiesSource: PropertiesSource? = Settings[baseName]
        return if (propertiesSource == null) {
            emptyMap<Any, Any>()
        } else {
            if (!key.isNullOrBlank()) {
                val value: String? = propertiesSource[key]
                val map: MutableMap<String, String?>
                if (Settings.isDicCode(baseName)) {
                    map = propertiesSource.mapOf(key).toMutableMap()
                    val typeKey = "$key|TYPE"
                    val typeValue: String? = propertiesSource[typeKey]
                    if (typeValue != null) {
                        map["TYPE"] = typeValue
                    }
                    if (value != null) {
                        map[key] = value
                    }
                } else {
                    map = Collections.singletonMap(key, value)
                }
                map
            } else {
                propertiesSource.all()
            }
        }
    }
}