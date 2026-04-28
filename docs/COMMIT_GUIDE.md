# Commit history guide

Follow this sequence to build a clean, readable GitHub history.

## Phase 1 — Core mechanics
```
git init
git add README.md .gitignore
git commit -m "init: scaffold Android project structure"

git add app/src/main/java/com/flappyrush/game/objects/Bird.kt \
        app/src/main/java/com/flappyrush/game/engine/PhysicsEngine.kt \
        app/src/main/java/com/flappyrush/utils/Constants.kt
git commit -m "feat: add basic bird physics & tap controls"

git add app/src/main/java/com/flappyrush/game/objects/Pipe.kt \
        app/src/main/java/com/flappyrush/game/objects/PipeManager.kt \
        app/src/main/java/com/flappyrush/game/objects/Background.kt
git commit -m "feat: implement scrolling pipe obstacles"

git add app/src/main/java/com/flappyrush/game/engine/ \
        app/src/main/java/com/flappyrush/game/screens/ \
        app/src/main/java/com/flappyrush/game/ui/ \
        app/src/main/java/com/flappyrush/utils/SoundManager.kt
git commit -m "feat: add collision detection & game over screen"
```

## Phase 2 — Visual redesign (upcoming)
```
git commit -m "design: new bird sprite & color palette"
git commit -m "design: animated background & parallax scrolling"
git commit -m "feat: add score counter & best score UI"
git commit -m "feat: sound effects & haptic feedback"
```

## Phase 3 — Leaderboard & accounts (upcoming)
```
git commit -m "feat: Firebase setup & auth flow"
git commit -m "feat: global leaderboard with top 100"
git commit -m "feat: weekly & daily leaderboard tabs"
git commit -m "feat: player profile & stats screen"
git commit -m "feat: friends list & compare scores"
```

## Phase 4 — Competitive multiplayer (upcoming)
```
git commit -m "feat: real-time 1v1 matchmaking"
git commit -m "feat: split-screen ghost race mode"
git commit -m "feat: tournament brackets (8 players)"
git commit -m "feat: post-match result & replay screen"
git commit -m "feat: season ranking & rank badges"
```

## Phase 5 — Polish & launch (upcoming)
```
git commit -m "feat: cosmetic skins & daily rewards"
git commit -m "fix: performance & battery optimization"
git commit -m "test: unit & integration test suite"
git commit -m "release: v1.0 Play Store build"
```

## Commit message convention
- `init:` — project setup
- `feat:` — new feature
- `fix:` — bug fix
- `design:` — visual/UI changes
- `refactor:` — code restructure, no behavior change
- `test:` — adding tests
- `release:` — version bump / store build
