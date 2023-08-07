import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.File
import kotlin.io.path.Path

fun main() {
    val users = parseUserInfo(readUserInfo())
    val userStats = users.map {
        try {
            getPlayerStats(playerName = it.key, playerUUID = it.value)
        } catch (e: Exception) {
            println("error user: $it")
            null
        }
    }.filterNotNull()

    // print user stats
    userStats.forEach {
        println(it)
    }

    val mostRecords = mutableMapOf<String, Record>()
    val leastRecords = mutableMapOf<String, Record>()

    val fields = PlayerStats::class.java.declaredFields
    for (field in fields) {
        field.isAccessible = true
        when (field.type) {
            Integer.TYPE, Integer::class.java -> {
                val mostPlayer = userStats.maxBy { field.getInt(it) }
                val leastPlayer = userStats.minBy { field.getInt(it) }
                mostRecords[field.name] = Record(
                    name = mostPlayer.name,
                    value = field.getInt(mostPlayer)
                )
                leastRecords[field.name] = Record(
                    name = leastPlayer.name,
                    value = field.getInt(leastPlayer)
                )
            }
            Double::class.javaPrimitiveType, Double::class.java -> {
                val mostPlayer = userStats.maxBy { field.getDouble(it) }
                val leastPlayer = userStats.minBy { field.getDouble(it) }
                mostRecords[field.name] = Record(
                    name = mostPlayer.name,
                    value = field.getDouble(mostPlayer)
                )
                leastRecords[field.name] = Record(
                    name = leastPlayer.name,
                    value = field.getDouble(leastPlayer)
                )
            }
        }
    }

    val result = mutableMapOf<String, Any>()

    result["stats"] = userStats
    result["most"] = mostRecords
    result["least"] = leastRecords
    result["avgPlayTime"] = userStats.sumOf { it.playTimeHours } / userStats.size
    result["avgIrons"] = userStats.sumOf { it.irons } / userStats.size
    result["avgDiamonds"] = userStats.sumOf { it.diamonds } / userStats.size

    // write to a file
    val gson = GsonBuilder().setPrettyPrinting().create()
    val file = File(Path(System.getProperty("user.dir"), "result.json").toString())
    file.writeText(gson.toJson(result))
}

data class Record(val name: String, val value: Number)

private fun readUserInfo(): String {
    val path = Path(System.getProperty("user.dir"), "asset", "whitelist.json")
    val file = File(path.toString())

    check(file.exists())

    return file.readText()
}

private fun parseUserInfo(userInfoData: String): Map<String, String> {
    val parser = JsonParser()
    val userObjects = parser.parse(userInfoData).asJsonArray

    return userObjects.associate {
        val obj = it.asJsonObject
        obj.get("name").asString to obj.get("uuid").asString
    }
}

private fun getPlayerStats(playerName: String, playerUUID: String): PlayerStats {
    val path = Path(System.getProperty("user.dir"), "asset", "stats", "$playerUUID.json")

    val playerStatsFile = File(path.toString())

    check(playerStatsFile.exists()) {
        "no player stats file found"
    }

    val parser = JsonParser()

    val statsObject = parser.parse(playerStatsFile.readText()).asJsonObject.getAsJsonObject("stats")
    val customObject = statsObject.getAsJsonObject("minecraft:custom")
    val craftedObject = statsObject.getAsJsonObject("minecraft:crafted")
    val droppedObject = statsObject.getAsJsonObject("minecraft:dropped") ?: JsonObject()
    val pickedUpObject = statsObject.getAsJsonObject("minecraft:picked_up")

    return PlayerStats(
        name = playerName,
        uuid = playerUUID,
        mostCraftedItem = parseStatMost(statsObject, fieldName = "minecraft:crafted", ignoreFieldNames = listOf()),
        mostKilledBy = parseStatMost(statsObject, fieldName = "minecraft:killed_by"),
        mostUsedItem = parseStatMost(statsObject, fieldName = "minecraft:used"),
        mostMinedItem = parseStatMost(statsObject, fieldName = "minecraft:mined", ignoreFieldNames = listOf("minecraft:grass_block")),
        mostKilled = parseStatMost(statsObject, fieldName = "minecraft:killed"),
        bellRing = parseStatValue(customObject, fieldName = "minecraft:bell_ring"),
        playTimeHours = parseStatValue(customObject, fieldName = "minecraft:play_time") / 20 / 60 / 60.0,
        deaths = parseStatValue(customObject, fieldName = "minecraft:deaths"),
        jump = parseStatValue(customObject, fieldName = "minecraft:jump"),
        leaveGame = parseStatValue(customObject, fieldName = "minecraft:leave_game"),
        playerKills = parseStatValue(customObject, fieldName = "minecraft:player_kills"),
        mobKills = parseStatValue(customObject, fieldName = "minecraft:mob_kills"),
        eatCakeSlice = parseStatValue(customObject, fieldName = "minecraft:eat_cake_slice"),
        playRecord = parseStatValue(customObject, fieldName = "minecraft:play_record"),
        sleepInBed = parseStatValue(customObject, fieldName = "minecraft:sleep_in_bed"),
        fishCaught = parseStatValue(customObject, fieldName = "minecraft:fish_caught"),
        animalsBred = parseStatValue(customObject, fieldName = "minecraft:animals_bred"),
        irons = parseStatValue(craftedObject, fieldName = "minecraft:iron_ingot"),
        diamonds = parseStatValue(pickedUpObject, fieldName = "minecraft:diamond") - parseStatValue(droppedObject, fieldName = "minecraft:diamond"),
    )
}

data class PlayerStats(
    val name: String,
    val uuid: String,
    val mostCraftedItem: Pair<String, Int>?,
    val mostKilledBy: Pair<String, Int>?,
    val mostUsedItem: Pair<String, Int>?,
    val mostMinedItem: Pair<String, Int>?,
    val mostKilled: Pair<String, Int>?,
    val bellRing: Int,
    val eatCakeSlice: Int,
    val playRecord: Int,
    val sleepInBed: Int,
    val fishCaught: Int,
    val playTimeHours: Double,
    val deaths: Int,
    val jump: Int,
    val leaveGame: Int,
    val playerKills: Int,
    val mobKills: Int,
    val irons: Int,
    val diamonds: Int,
    val animalsBred: Int,
) {
    val deathPerPlaytime: Double = 0.0.takeIf { deaths == 0 } ?: (deaths / playTimeHours)
}

private fun parseStatValue(
    customObject: JsonObject,
    fieldName: String,
): Int {
    return try {
        customObject.get(fieldName).asInt
    } catch (e: Exception) {
        0
    }
}

private fun parseStatMost(
    statsObject: JsonObject,
    fieldName: String,
    ignoreFieldNames: List<String> = listOf(),
    ignoreKeywords: List<String> = listOf(),
): Pair<String, Int>? {
    return try {
        var result: Pair<String, Int>? = null
        val craftedObject = statsObject.getAsJsonObject(fieldName)

        val containsIgnoreKeywords: (key: String) -> Boolean = { key ->
            var contains = false
            for (i in ignoreKeywords) {
                if (key.contains(i)) {
                    contains = true
                    break
                }
            }
            contains
        }

        for (key in craftedObject.keySet()) {
            if (key in ignoreFieldNames || containsIgnoreKeywords(key)) continue

            val resultCount = (result?.second ?: 0)
            val currCount = craftedObject.get(key).asInt

            result = result.takeIf { currCount < resultCount } ?: Pair(key, currCount)
        }
        result
    } catch (e: Exception) {
        null
    }
}