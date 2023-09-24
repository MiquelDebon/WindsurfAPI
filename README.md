## Windsurf Weather API

This app tell you the best day and hour of the week to do windsurf by order!

The origin data source is from **[open-meteo.com](https://open-meteo.com/en/docs/marine-weather-api#latitude=41.3888&longitude=2.159&start_date=2023-09-23&end_date=2023-09-28)**

**It also can send you by mail every Friday**

    http://localhost:8080/weather/current_week              ->      Best days in Barcelona this week
    http://localhost:8080/weather/next_week                 ->      Best days in Barcelona next week
    http://localhost:8080/weather/{cityName}/current_week   ->      Best days in {city} this week
    http://localhost:8080/weather/{cityName}/next_week      ->      Best days in {city} next week
    http://localhost:8080/weather/{mail}                    ->      It emails you the best days in Barcelona every Friday
    http://localhost:8080/weather/{cityName}/{mail}         ->      It emails you the best days in {city} every Friday

    *The schedule has been restricted to 12:00, 15:00, 17:00 which are the classes time

```
[
  {
    "timeHourly": "2023-09-24T12:00",
    "windspeed_10m": 10.1,
    "wave_height": 0.2
  },
  {
    "timeHourly": "2023-09-24T17:00",
    "windspeed_10m": 11.2,
    "wave_height": 0.24
  }
]

```



<hr>

#### Why this API?
Last summer, I joined a center to learn windsurfing. What happens is that they offer a package of 10 classes, and every Friday, they provide the schedule for the following week, and you must choose wisely. 

**You want wind but not waves, and at the same time, a sunny day**, so these three parameters can make it tricky sometimes to decide, but in the end, it's all about mathematics.
<hr>



**WMO Weather interpretation codes (WW)**
```
Code	Description
0	Clear sky
1, 2, 3	Mainly clear, partly cloudy, and overcast
45, 48	Fog and depositing rime fog
51, 53, 55	Drizzle: Light, moderate, and dense intensity
56, 57	Freezing Drizzle: Light and dense intensity
61, 63, 65	Rain: Slight, moderate and heavy intensity
66, 67	Freezing Rain: Light and heavy intensity
71, 73, 75	Snow fall: Slight, moderate, and heavy intensity
77	Snow grains
80, 81, 82	Rain showers: Slight, moderate, and violent
85, 86	Snow showers slight and heavy
95 *	Thunderstorm: Slight or moderate
96, 99 *	Thunderstorm with slight and heavy hail
(*) Thunderstorm forecast with hail is only available in Central Europe
```