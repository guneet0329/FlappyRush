# FlappyRush 🐦

A competitive twist on the classic Flappy Bird formula — built for Android.

## What makes it different
- **Ghost race mode** — compete against a real opponent in real time
- **Global leaderboards** — daily, weekly, and all-time rankings
- **Tournaments** — 8-player brackets with seasonal rewards
- **Cosmetic skins** — unlock birds, trails, and pipe themes

## Tech stack
- Kotlin + Android Canvas API (no game engine dependency)
- Firebase Realtime Database for multiplayer & leaderboards
- Firebase Auth for player accounts

## Development phases
| Phase | Focus | Status |
|-------|-------|--------|
| 1 | Core game mechanics | ✅ Done |
| 2 | Visual redesign & game feel | ✅ Done |
| 3 | Leaderboard & accounts | 🔄 In progress |
| 4 | Competitive multiplayer | ⏳ Upcoming |
| 5 | Polish & launch | ⏳ Upcoming |

## Getting started
1. Clone the repo
2. Open in Android Studio
3. Run on emulator or physical device (API 26+)

## Project structure
```
app/src/main/java/com/flappyrush/
├── game/
│   ├── engine/       # Game loop, physics, input
│   ├── objects/      # Bird, pipes, background
│   ├── screens/      # Activities & GameView
│   ├── competitive/  # Matchmaking, ghost bird, leaderboard
│   └── ui/           # HUD, score overlay
├── data/
│   ├── models/       # Player, Match data classes
│   └── repository/   # Firebase data access
└── utils/            # Constants, SoundManager
```
