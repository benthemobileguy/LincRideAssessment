# LincRide - Senior Android Engineer Assessment

This project is a technical assessment submission for the Senior Android Engineer position at LINCRIDE. It demonstrates proficiency in modern Android development using Kotlin, Jetpack Compose, and Google Maps SDK through implementation of a ride-sharing UI with event-driven simulation.

## Assessment Requirements Implemented

- **Figma Design Implementation**: Pixel-perfect replication of provided designs using Jetpack Compose
- **Google Maps SDK Integration**: Functional map with animated car movement and route visualization
- **Event-Driven Architecture**: Complete ride simulation flow with 5 distinct events
- **State Management**: Modern Compose state management with reactive UI updates
- **Bottom Sheet Modals**: Consistent modal system for all ride states
- **Animations**: Progress tracking, car movement, and modal transitions
- **Clean Architecture**: MVVM pattern with proper separation of concerns

## Architecture

Built using modern Android development practices:

- Jetpack Compose for declarative UI
- Material 3 design system with custom LincRide theme
- Modular architecture with feature-based modules
- Clean Architecture principles with domain/data/presentation layers
- Kotlin Coroutines for asynchronous operations

## Event Simulation Flow

The application implements the required 5-event simulation:

1. **Event 1: App Load** - Display Screen 3.2.1 (Main Map View)
2. **Event 2: User Clicks "Offer a Ride"** - Show Screen 14.1.1 (Bottom Sheet)
3. **Event 3: Get to Pick Up** - Car progress animation, transition to Screen 14.2.1
4. **Event 4: Rider Action** - Swipeable "Didn't Show"/"Picked Up", show Screen 14.4.1
5. **Event 5: Heading to Destination** - Final car animation, show Screen 14.7.3 (Trip Ended)

## Getting Started

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
   
   Create `local.properties` file in the project root:
   ```bash
   cp local.properties.template local.properties
   ```
   
   Edit `local.properties` and add the API key:
   ```properties
   # Location of the SDK. This is only used by Gradle.
   sdk.dir=/Users/USERNAME/Library/Android/sdk
   
   # Google Maps API Key
   MAPS_API_KEY=AIzaSyB2vUCvcTcnoeOykj5nuZQwDXZFA5_3oJA
   ```

3. **Build and run**
   ```bash
   ./gradlew assembleDebug
   ```
   
   Or open in Android Studio and run the `app` module.

## How to Trigger Events

The simulation runs automatically when you launch the app:

1. **App Load**: Automatically displays the main map view
2. **Offer a Ride**: Click the "Offer a Ride" button on the main screen
3. **Get to Pick Up**: Automatically starts after Event 2 with progress animation
4. **Rider Action**: Automatically transitions after pickup animation completes
5. **Heading to Destination**: Final simulation with trip completion

You can also restart the simulation by clicking the "New Trip" button in the Trip Ended overlay.

## Design System

### Typography
- Primary Font: SF Pro Display (Light, Regular, Medium, Bold)
- Material 3 Typography with custom LincRide styles
- Consistent sizing: 11sp, 12sp, 14sp, 16sp, 18sp hierarchy

### Colors
- Primary: LincGreen (#00C853)
- Secondary: LincBlue (#2C75FF)  
- Background: PickupBackground (#EAFFF6)
- Route Colors: StopGreen (#4A941C), StopOrange (#D27B0D)

### Components
- Reusable Modal System with consistent patterns
- Custom Progress Bars with car animation
- Professional Avatar System with real images
- Route Visualization with dots, lines, and color coding

## Project Structure

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

## Technical Implementation

### Key Components

- **BottomSheetContainer**: Reusable modal wrapper with slide-up animations
- **GradientProgressBarWithCar**: Animated progress tracking with car movement
- **RouteVisualization**: Multi-stop route display with passenger avatars
- **RideSimulationEngine**: Event-driven simulation logic
- **MapScreen**: Google Maps integration with route and marker animations

### Architecture Decisions

- **MVVM Pattern**: Clear separation between UI and business logic
- **Event-Driven Design**: Reactive state management using Kotlin Coroutines
- **Compose State Management**: Leveraging `remember`, `mutableStateOf`, and `LaunchedEffect`
- **Modular Structure**: Feature-based modules for scalability
- **Clean Code**: Well-commented, idiomatic Kotlin following Android best practices

### Modal System Design
All modals follow a consistent pattern:
- "Smart" composables handle simulation logic and state
- "Dumb" composables focus purely on UI display
- Reusable components for consistency across screens
- Material 3 typography with SF Pro Display integration

## Known Limitations

- **Hardcoded Routes**: Map routes are predefined for simulation purposes
- **Simplified Networking**: No real API integration, uses local simulation
- **Basic Error Handling**: Limited error scenarios implemented for assessment scope
- **Single User Flow**: Designed for driver-side experience only

## Assessment Submission

This project demonstrates:
- **Figma Design Accuracy**: Pixel-perfect implementation of provided designs
- **Modern Android Development**: Jetpack Compose, Material 3, Clean Architecture
- **Google Maps Integration**: Functional mapping with animated markers
- **Event-Driven Simulation**: Complete 5-event ride-sharing flow
- **Code Quality**: Clean, maintainable, well-documented Kotlin code
- **Performance Considerations**: Efficient animations and state management

Submitted as part of the Senior Android Engineer assessment for LINCRIDE.