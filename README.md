# 마크 통계 추출기

마인크래프트 유저 통계 일부를 json 파일로 정리합니다.

## 추출결과 (Sample)

추출 결과는 `result.json`에 저장됩니다.

```json
{
  "stats": [
    {
      "name": "Rail_kim",
      "uuid": "user-uuid",
      "mostCraftedItem": {
        "first": "minecraft:birch_planks",
        "second": 136
      },
      "mostKilledBy": {
        "first": "minecraft:guardian",
        "second": 14
      },
      "mostUsedItem": {
        "first": "minecraft:stone_pickaxe",
        "second": 1192
      },
      "mostMinedItem": {
        "first": "minecraft:stone",
        "second": 1519
      },
      "mostKilled": {
        "first": "alexsmobs:lobster",
        "second": 26
      },
      "bellRing": 0,
      "eatCakeSlice": 0,
      "playRecord": 0,
      "sleepInBed": 11,
      "fishCaught": 0,
      "playTimeHours": 20.183333333333334,
      "deaths": 29,
      "jump": 8152,
      "leaveGame": 6,
      "playerKills": 2,
      "mobKills": 54,
      "irons": 144,
      "diamonds": 0,
      "animalsBred": 0,
      "deathPerPlaytime": 1.4368290668868704
    },
    {
      "name": "loki24park",
      "uuid": "ad898192-fb71-4435-8e13-326c9e8e7558",
      "mostCraftedItem": {
        "first": "minecraft:oak_planks",
        "second": 552
      },
      "mostKilledBy": {
        "first": "alexsmobs:bison",
        "second": 7
      },
      "mostUsedItem": {
        "first": "minecraft:stone_shovel",
        "second": 1511
      },
      "mostMinedItem": {
        "first": "minecraft:red_sand",
        "second": 869
      },
      "mostKilled": {
        "first": "minecraft:zombie",
        "second": 57
      },
      "bellRing": 0,
      "eatCakeSlice": 0,
      "playRecord": 0,
      "sleepInBed": 40,
      "fishCaught": 12,
      "playTimeHours": 38.11666666666667,
      "deaths": 25,
      "jump": 18849,
      "leaveGame": 24,
      "playerKills": 5,
      "mobKills": 311,
      "irons": 213,
      "diamonds": 0,
      "animalsBred": 12,
      "deathPerPlaytime": 0.6558810668998688
    },
    {
      ...
    }
  ],
  "most": {
    "bellRing": {
      "name": "sample_user_nickname",
      "value": 59
    },
    "eatCakeSlice": {
      "name": "sample_user_nickname",
      "value": 7
    },
    "playRecord": {
      "name": "sample_user_nickname",
      "value": 11
    },
    "sleepInBed": {
      "name": "sample_user_nickname",
      "value": 120
    },
    "fishCaught": {
      "name": "sample_user_nickname",
      "value": 643
    },
    "playTimeHours": {
      "name": "sample_user_nickname",
      "value": 66.65
    },
    "deaths": {
      "name": "sample_user_nickname",
      "value": 101
    },
    "jump": {
      "name": "sample_user_nickname",
      "value": 72399
    },
    "leaveGame": {
      "name": "sample_user_nickname",
      "value": 29
    },
    "playerKills": {
      "name": "loki24park",
      "value": 5
    },
    "mobKills": {
      "name": "sample_user_nickname",
      "value": 583
    },
    "irons": {
      "name": "sample_user_nickname",
      "value": 1112
    },
    "diamonds": {
      "name": "sample_user_nickname",
      "value": 250
    },
    "animalsBred": {
      "name": "sample_user_nickname",
      "value": 339
    },
    "deathPerPlaytime": {
      "name": "sample_user_nickname",
      "value": 2.3076923076923075
    }
  },
  "least": {
    "bellRing": {
      "name": "sample_user_nickname",
      "value": 0
    },
    "eatCakeSlice": {
      "name": "sample_user_nickname",
      "value": 0
    },
    "playRecord": {
      "name": "sample_user_nickname",
      "value": 0
    },
    "sleepInBed": {
      "name": "sample_user_nickname",
      "value": 0
    },
    "fishCaught": {
      "name": "sample_user_nickname",
      "value": 0
    },
    "playTimeHours": {
      "name": "sample_user_nickname",
      "value": 0.8666666666666667
    },
    "deaths": {
      "name": "sample_user_nickname",
      "value": 0
    },
    "jump": {
      "name": "sample_user_nickname",
      "value": 330
    },
    "leaveGame": {
      "name": "sample_user_nickname",
      "value": 2
    },
    "playerKills": {
      "name": "sample_user_nickname",
      "value": 0
    },
    "mobKills": {
      "name": "sample_user_nickname",
      "value": 2
    },
    "irons": {
      "name": "sample_user_nickname",
      "value": 0
    },
    "diamonds": {
      "name": "sample_user_nickname",
      "value": 0
    },
    "animalsBred": {
      "name": "sample_user_nickname",
      "value": 0
    },
    "deathPerPlaytime": {
      "name": "sample_user_nickname",
      "value": 0.0
    }
  },
  "avgPlayTime": 32.12931034482759,
  "avgIrons": 152,
  "avgDiamonds": 11
}
```

## 개인 통계
```
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
```


## Usage

`stats` 폴더와 `whitelist.json`를 asset/에 넣습니다.
```agsl
.
├── README.md
├── asset
│   ├── stats
│   │   ├── 0921a0eb-9447-4b23-85e0-840881580426.json
│   │   ├── 0a107803-022f-4ad0-92bb-c529d04e1683.json
│   │   ├── 0bda2930-2e83-4b02-98d0-4aec7112210c.json
│   │   ├── 1b33d604-198a-4d02-82f9-473210fc003c.json
│   │   ├── 2f04e29d-a417-4e90-a710-a848e3faca97.json
│   └── whitelist.json
├── build
│   └── ...
├── build.gradle.kts
├── gradle
│   └── ...
├── gradle.properties
├── gradlew
├── gradlew.bat
├── result.json
├── settings.gradle.kts
└── src
    └── ...
```