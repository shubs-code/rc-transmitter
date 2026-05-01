PROJECT STRUCTURE
=================

com.shubham.rctransmitter/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml
│   │   │   ├── java/com/shubham/rctransmitter/
│   │   │   │   ├── RCTransmitterApp.kt
│   │   │   │   ├── presentation/
│   │   │   │   │   ├── MainActivity.kt
│   │   │   │   │   ├── navigation/
│   │   │   │   │   │   └── AppNavigation.kt
│   │   │   │   │   ├── home/
│   │   │   │   │   │   ├── HomeScreen.kt
│   │   │   │   │   │   └── HomeViewModel.kt
│   │   │   │   │   └── settings/
│   │   │   │   │       ├── SettingsScreen.kt
│   │   │   │   │       └── SettingsViewModel.kt
│   │   │   │   ├── data/
│   │   │   │   │   └── SettingsManager.kt
│   │   │   │   ├── domain/
│   │   │   │   │   └── UDPController.kt
│   │   │   │   └── di/
│   │   │   │       └── AppModule.kt
│   ├── build.gradle.kts

FILES PLACEMENT
===============

1. RCTransmitterApp.kt
   → app/src/main/java/com/shubham/rctransmitter/RCTransmitterApp.kt

2. MainActivity.kt
   → app/src/main/java/com/shubham/rctransmitter/presentation/MainActivity.kt

3. AppNavigation.kt
   → app/src/main/java/com/shubham/rctransmitter/presentation/navigation/AppNavigation.kt

4. HomeScreen.kt
   → app/src/main/java/com/shubham/rctransmitter/presentation/home/HomeScreen.kt

5. HomeViewModel.kt
   → app/src/main/java/com/shubham/rctransmitter/presentation/home/HomeViewModel.kt

6. SettingsScreen.kt
   → app/src/main/java/com/shubham/rctransmitter/presentation/settings/SettingsScreen.kt

7. SettingsViewModel.kt
   → app/src/main/java/com/shubham/rctransmitter/presentation/settings/SettingsViewModel.kt

8. SettingsManager.kt
   → app/src/main/java/com/shubham/rctransmitter/data/SettingsManager.kt

9. UDPController.kt
   → app/src/main/java/com/shubham/rctransmitter/domain/UDPController.kt

10. AppModule.kt
    → app/src/main/java/com/shubham/rctransmitter/di/AppModule.kt

11. AndroidManifest.xml
    → app/src/main/AndroidManifest.xml

12. build.gradle.kts (REPLACE EXISTING)
    → app/build.gradle.kts

FEATURES
========
- Two rotary joystick controls (left and right)
- READY/STOP toggle button (green)
- E-STOP emergency button (red)
- Settings screen with persistent UDP IP and Port configuration
- DataStore for persistent settings across app restarts
- Sends control commands via UDP to configured IP and port
- Supports control commands: START, STOP, LEFT, RIGHT with X,Y values
