
# HealthInfoApp


A modern Android application for managing patients, healthcare programs, and enrollments, built with Jetpack Compose and following modern Android development best practices.

## Features

- **Patient Management**
    - View patient lists with search functionality
    - Register new patients
    - View patient details
- **Program Management**
    - Browse healthcare programs
    - Create new programs
    - Filter programs by type
- **Enrollment System**
    - Enroll patients in programs
    - View enrolled programs per patient
- **Modern UI**
    - Built with Jetpack Compose
    - Material 3 design
    - Dark/Light theme support
- **Data Management**
    - Local Room database caching
    - Network data synchronization
    - Pull-to-refresh functionality

## Screenshots

## Screenshots

| Patient List | Patient Details | Program List |
|--------------|-----------------|--------------|
| <img src="Screenshot/Screenshot%202025-04-27%20at%2017.40.32.png" width="100%"> | <img src="Screenshot/Screenshot%202025-04-27%20at%2017.40.51.png" width="100%"> | <img src="Screenshot/Screenshot%202025-04-27%20at%2017.40.54.png" width="100%"> |

| Program Creation | Enrollment | Search |
|------------------|------------|--------|
| <img src="Screenshot/Screenshot%202025-04-27%20at%2017.41.06.png" width="100%"> | <img src="Screenshot/Screenshot%202025-04-27%20at%2017.41.46.png" width="100%"> | <img src="Screenshot/Screenshot%202025-04-27%20at%2017.42.21.png" width="100%"> |

| Filter Options | Dark Mode | Success Dialog |
|----------------|-----------|----------------|
| <img src="Screenshot/Screenshot%202025-04-27%20at%2017.43.24.png" width="100%"> | <img src="Screenshot/Screenshot%202025-04-27%20at%2017.43.38.png" width="100%"> | <img src="Screenshot/Screenshot%202025-04-27%20at%2017.44.11.png" width="100%"> |


## Screenshots
<div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 16px; margin: 16px 0;">
  <img src="Screenshot/Screenshot%202025-04-27%20at%2017.40.32.png" width="100%" alt="Patient List Screen" />
  <img src="Screenshot/Screenshot%202025-04-27%20at%2017.40.51.png" width="100%" alt="Patient Details" />
  <img src="Screenshot/Screenshot%202025-04-27%20at%2017.40.54.png" width="100%" alt="Program List" />

  <img src="Screenshot/Screenshot%202025-04-27%20at%2017.41.06.png" width="100%" alt="Program Creation" />
  <img src="Screenshot/Screenshot%202025-04-27%20at%2017.41.46.png" width="100%" alt="Enrollment Screen" />
  <img src="Screenshot/Screenshot%202025-04-27%20at%2017.42.21.png" width="100%" alt="Search Functionality" />

  <img src="Screenshot/Screenshot%202025-04-27%20at%2017.43.24.png" width="100%" alt="Filter Options" />
  <img src="Screenshot/Screenshot%202025-04-27%20at%2017.43.38.png" width="100%" alt="Dark Mode" />
  <img src="Screenshot/Screenshot%202025-04-27%20at%2017.44.11.png" width="100%" alt="Success Dialog" />
</div>
## Technical Stack

- **UI**: Jetpack Compose, Material 3
- **Architecture**: MVVM, Clean Architecture
- **Dependency Injection**: Koin
- **Local Database**: Room
- **Networking**: Retrofit
- **State Management**: Kotlin Flows, StateFlow
- **Navigation**: Compose Navigation
- **Testing**: JUnit, MockK, Espresso

## Getting Started

### Prerequisites

- Android Studio Giraffe (2022.3.1) or later
- Android SDK 33+
- Java 17

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/stevechacha/HealthInfoApp.git

Code
