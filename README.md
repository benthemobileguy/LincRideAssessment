# LincRide 🚗

A modern Android ride-sharing application built with Jetpack Compose, featuring real-time ride simulation, interactive maps, and a comprehensive modal system for driver-passenger interactions.

## ✨ Features

- **Interactive Maps Integration** - Google Maps with real-time car tracking and route visualization
- **Ride Simulation Engine** - Complete ride lifecycle from pickup to drop-off with animated progress
- **Modern Modal System** - Consistent bottom sheet modals for all ride states
- **Professional UI/UX** - SF Pro Display typography, Material 3 design system
- **Real-time Progress Tracking** - Animated progress bars with car movement
- **Multi-passenger Support** - Handle multiple passengers with individual avatars and route stops
- **Trip Analytics** - Earnings tracking, CO₂ savings calculation, passenger ratings

## 🏗️ Architecture

Built using modern Android development practices:

- **Jetpack Compose** for declarative UI
- **Material 3** design system with custom LincRide theme
- **Modular architecture** with feature-based modules
- **Clean Architecture** principles with domain/data/presentation layers
- **Kotlin Coroutines** for asynchronous operations

## 📱 Screenshots & UI States

The app simulates a complete ride-sharing experience with the following modal states:

1. **Get to Pickup** - Navigate to passenger location with progress tracking
2. **Pickup Confirmation** - Swipeable interface for "Picked up" vs "Didn't show"
3. **Heading to Drop-off** - Route visualization with multiple passenger stops
4. **Trip Ended** - Earnings summary, passenger rating, and CO₂ impact

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- Android SDK 34
- Kotlin 1.9.0+
- Google Maps API Key

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/LincRide.git
   cd LincRide
   ```

2. **Set up Google Maps API Key**
   
   **⚠️ IMPORTANT: You must add your Google Maps API key to run the app**
   
   a. Get your API key from [Google Cloud Console](https://console.cloud.google.com/apis/credentials)
   
   b. Enable the following APIs:
      - Maps SDK for Android
      - Places API
      - Directions API
   
   c. Create `local.properties` file in the project root:
   ```bash
   cp local.properties.template local.properties
   ```
   
   d. Edit `local.properties` and replace `YOUR_MAPS_API_KEY_HERE` with your actual API key:
   ```properties
   # Location of the SDK. This is only used by Gradle.
   sdk.dir=/Users/USERNAME/Library/Android/sdk
   
   # Google Maps API Key
   MAPS_API_KEY=AIzaSyBxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
   ```

3. **Build and run**
   ```bash
   ./gradlew assembleDebug
   ```
   
   Or open in Android Studio and run the `app` module.

### API Key Security

- The `local.properties` file is automatically ignored by git
- Never commit your API key to version control
- For production builds, use secure CI/CD environment variables

## 🎨 Design System

### Typography
- **Primary Font**: SF Pro Display (Light, Regular, Medium, Bold)
- **Material 3 Typography** with custom LincRide styles
- **Consistent sizing**: 11sp, 12sp, 14sp, 16sp, 18sp hierarchy

### Colors
- **Primary**: LincGreen (#00C853)
- **Secondary**: LincBlue (#2C75FF)  
- **Background**: PickupBackground (#EAFFF6)
- **Route Colors**: StopGreen (#4A941C), StopOrange (#D27B0D)

### Components
- **Reusable Modal System** with consistent patterns
- **Custom Progress Bars** with car animation
- **Professional Avatar System** with real images
- **Route Visualization** with dots, lines, and color coding

## 📁 Project Structure

```
LincRide/
├── app/                          # Main application module
│   ├── src/main/java/com/ben/lincride/
│   │   ├── ui/
│   │   │   ├── screens/          # Bottom sheet modals
│   │   │   ├── components/       # Reusable UI components
│   │   │   └── theme/           # App-specific theming
│   │   ├── domain/              # Business logic
│   │   └── MainActivity.kt      # Main entry point
├── core/                        # Core modules
│   ├── designsystem/           # Design system & theming
│   ├── common/                 # Shared utilities
│   ├── data/                   # Data layer
│   └── network/                # API & networking
├── feature/                    # Feature modules
│   ├── map/                    # Map functionality
│   ├── ride/                   # Ride management
│   └── trip/                   # Trip handling
└── build-logic/               # Build configuration
```

## 🔧 Development

### Key Components

- **BottomSheetContainer**: Reusable modal wrapper with animations
- **GradientProgressBarWithCar**: Animated progress tracking
- **RouteVisualization**: Multi-stop route display with passenger avatars
- **PassengerInfoCard**: Consistent passenger information layout

### Modal System
All modals follow a consistent pattern:
- **"Smart" composables** handle logic and state
- **"Dumb" composables** focus on UI display only
- **Reusable components** for consistency
- **Material 3 typography** for SF Pro Display integration

## 🛠️ Building for Production

1. Update app version in `app/build.gradle.kts`
2. Configure signing keys
3. Set up release API key management
4. Run: `./gradlew assembleRelease`

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Style
- Follow Kotlin coding conventions
- Use meaningful component names
- Maintain consistent spacing and typography
- Reuse existing components when possible

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Google Maps Platform** for location services
- **Material Design 3** for design system
- **Jetpack Compose** for modern Android UI
- **SF Pro Display** typography from Apple

---

**Note**: This app is for demonstration purposes. Ensure you have proper licensing for any fonts and comply with Google Maps API usage policies.