# Howest Prime Android App

The Howest Prime Android app helps cinema visitors manage tickets and use services around their visit. It receives ticket messages, stores tickets locally, shows ticket details and QR codes, and schedules movie reminders. It also supports browsing snacks, building a cart, viewing the cinema map, and using device sensors.

## Features

- **Tickets:** Incoming tickets are persisted in a local Room database and shown in ticket screens. A ticket detail view can display a QR code.
- **Movie reminders:** Android WorkManager schedules local notifications before a movie starts. The reminder interval is selected in the app.
- **Snacks and cart:** Snack data is fetched through a REST client and cached locally. Cart orders can be shared with another Android app and published through the message broker.
- **Cinema map:** Mapbox displays the visitor’s location and the cinema outline loaded from GeoJSON. The app can determine whether the visitor is inside the mapped building boundary.
- **Sensors:** The app reads ambient light and linear acceleration sensor data, and provides a flashlight control.

## Technology

Kotlin, Android SDK, Jetpack Compose, Room, DataStore, Retrofit, Moshi, RabbitMQ AMQP, WorkManager, Hilt, Mapbox Maps SDK, Google Play Services Location, and the Qrose QR code library.

The Gradle configuration targets SDK 35, supports Android API 26 and later, and uses Java 17 bytecode.

## Build

Use a compatible JDK and Android SDK, then build the debug APK with the Gradle wrapper:

```sh
./gradlew assembleDebug
```

The app’s Gradle configuration reads these local build properties: `API_BASE_URL`, `AMQPusername`, `AMQPpassword`, `AMQPurl`, `AMQPpublishtopic`, `AMQPsubscribetopic`, `AMQPconsumertag`, and `MAPBOX_ACCESS_TOKEN`. Supply your own environment-specific values through local Gradle configuration. Do not commit credentials or access tokens.

## Screenshots

Screenshots are available in the [screens](screens/) directory, including the home, ticket, snack, cart, map, sensor, and notification views.

## Related repositories

- [Movies microservice](https://github.com/Howest-Prime/st-microservice-movies)
- [Ticketing microservice](https://github.com/Howest-Prime/st-microservice-ticketing)
