# 🎵 MusicApp

Android music search application developed in ≈ 4 days.  
_API used : [iTunes Search API](https://developer.apple.com/library/archive/documentation/AudioVideo/Conceptual/iTuneSearchAPI/Searching.html#//apple_ref/doc/uid/TP40017632-CH5-SW1)_

---

## 📱 Search UI States

The search screen explicitly handles all key UI states: idle, loading, empty result, and error.

| Idle                                          | Loading                                             | No Result                                              | Error                                           |
|-----------------------------------------------|-----------------------------------------------------|--------------------------------------------------------|-------------------------------------------------|
| ![Idle](screenshots/MusicApp_Idle_Screen.jpg) | ![Loading](screenshots/MusicApp_Loading_Screen.jpg) | ![No Result](screenshots/MusicApp_NoResult_Screen.jpg) | ![Error](screenshots/MusicApp_Error_Screen.jpg) |

---

## 📄 Pagination & Result UI States

Overview of key states related to pagination and displaying results:

| Search result                                            | Pagination : Loader                                              | Pagination : End                                                 | Pagination : Error                                             |
|----------------------------------------------------------|------------------------------------------------------------------|------------------------------------------------------------------|----------------------------------------------------------------|
| ![Search Result](screenshots/MusicApp_Search_Result.jpg) | ![Pagination Loader](screenshots/MusicApp_Pagination_Loader.jpg) | ![Pagination Ending](screenshots/MusicApp_Pagination_Ending.jpg) | ![Pagination Error](screenshots/MusicApp_Pagination_Error.jpg) |

---

## 🎧 Audio Player UI States

Overview of key states for the integrated audio player (SongDetails):

| Idle                                          | Loader                                            | Playing                                             | Pause                                           |
|-----------------------------------------------|---------------------------------------------------|-----------------------------------------------------|-------------------------------------------------|
| ![Idle](screenshots/MusicApp_Player_Idle.jpg) | ![Loader](screenshots/MusicApp_Player_Loader.jpg) | ![Playing](screenshots/MusicApp_Player_Playing.jpg) | ![Pause](screenshots/MusicApp_Player_Pause.jpg) |

| Replay                                            | Error                                           | Loader after Retry                                                        |
|---------------------------------------------------|-------------------------------------------------|---------------------------------------------------------------------------|
| ![Replay](screenshots/MusicApp_Player_Replay.jpg) | ![Error](screenshots/MusicApp_Player_Error.jpg) | ![Loader after Retry](screenshots/MusicApp_Player_Loader_After_Retry.jpg) |

---

## 🏛️ Architecture & Stack

- **MVVM + Clean Architecture** (UseCases, Repository, Feature)
- **UI** : Jetpack Compose (Material3, animations, state hoisting)
- **Navigation** : Jetpack Navigation Compose
- **DI** : Hilt (ViewModelScoped, Singleton…)
- **Audio Player** : ExoPlayer encapsulated via `MusicPlayerController` (interface testable)
- **Asynchronicity** : Kotlin Coroutines, Flow, SavedStateHandle & getStateFlow (process-death safe)
- **Serialization** : kotlinx.serialization

---

## 📦 Modularization

- **`app`**  
  UI host & navigation (Jetpack Compose).  
  Contains the _Search_, _SongDetails_, _AlbumDetails_, _ArtistDetails_ screens and orchestrates the
  bridge **ViewModel ↔ UseCases**.  
  _Depends on_ : `domains`, `repositories`.

- **`domains`**  
  **Pure** business logic: entities, use cases, repository contracts.  
  **No Android dependency** → 100 % JVM/KMP-friendly module.

- **`repositories`**  
  Data access: implementations of contracts (iTunes network calls, DTO → domain mapping).  
  _Depends on_ : `domains`.

**Each module is fully tested.**

---

## ⚙️ Gradle

- Gradle KTS with `libs.versions.toml` (centralized version catalog)
- Plugins : Hilt, KSP, Kotlinx-Serialization
- Support for `testFixtures` to share test models

> 🛠️ **Centralized configuration**  
> All common settings (SDK, JVM, runners, Compose…) are defined at the root to:
> - Avoid duplication
> - Ensure consistency
> - Simplify maintenance

---

## ✅ Testing

- **100 % isolated unit tests (JVM), Turbine on StateFlow:**
    - ViewModels tested with **Fakes** for UseCases and Repositories
    - UseCases and Repositories tested independently
    - Audio player (`MusicPlayerController`) tested with a **Fake** (ExoPlayer simulator)
- **Business cases covered** :
    - **General** : success, error, loading, retry
    - **Pagination** : end of list, errors, duplicates, loading, retry
    - **Audio player** : seek, replay, loading, idempotence, release

---

## ✨ Features

- Search for **songs, albums, artists** (iTunes API)
- Smooth multi-screen navigation
- **Pagination** : manual management of offset, errors, result limits, and
  client-side duplicate filtering (non-idempotent API)
- **Lightweight animations** : crossfade on transitions and lists
- **SongDetails** :  audio player, interactive timeline, animated title, external links
- **AlbumDetails** : artwork, genre, year, copyright, Apple Music links
- **ArtistDetails** : basic info + redirection
- **Comprehensive UI state handling** : loading, error, idle, retry, for search and
  pagination

## 🚀 Improvements Roadmap

- **UX & Functional**
    - Pager for category swipe, state persistence
    - Favorites support (local storage with Room)
    - History of last 10 searches (local storage with Room)
    - Enhanced detail screens: additional APIs (more info, richer content, higher image quality)
- **Technical**
    - Background audio playback (MediaSessionService)
    - Advanced network error handling (user-friendly messages, airplane‑mode handling, automatic reconnection)
- **Architecture & Build**
    - Modular: extract new feature modules when needed
    - Gradle build logic : advanced factorization via custom plugins

---