
---

# 🚀 DevDeck

**Build. Organize. Manage.. but faster**

A blazing-fast cross-platform developer setup manager and CLI tool, powered by Kotlin Multiplatform + Compose Desktop.

DevDeck simplifies managing developer environments across Linux, macOS, and Windows using smart, declarative deck files. Whether you're spinning up projects, syncing tools, or exploring new tech, DevDeck has your back—with features like curated news, one-click project control, quick environment setup, ready-made templates, public deckfile downloads, and even an disposable code runner.

---

## 📃 Planned Application Features
* Feed – Curated tech news and dev content at a glance.
* My Deck – View and manage your current environment setup.
* New Project – Create new apps using ready-to-go templates.
* MyProjects – Open, build, and run all your dev projects in one place.
* Downloads – Discover and import public deckfiles and tools.
* QuickCode – Instantly run small scripts for quick testing.

---

## 🌟 Project Features

* Compose Desktop GUI for easy access and visuals
* Firebase-powered authentication (Email + GitHub)
* Deckfile compiler + execution engine
* Process orchestration for parallel setup
* Install reference management (e.g., Flutter, Git, JetBrains IDEs and extensible via external references)
* Custom encrypted local datastore using MapDB
* Modular architecture (Core + Features)
* Easy environment export + sync (Imprint)

---

## 🧠 Project Structure

* :composeApp → Main Compose UI Application (window, Firebase setup)

* core:data → Repository layer (data sources, interactors)
* core:datastore → Encrypted MapDB wrapper for secure persistent storage
* core:deck-api → CLI core: task DSL, compiler, runners, refs
* core:di → Koin module loading interface using Java SPI
* core:logging → Logging utilities
* core:model → Common model/data classes
* core:network → Network layer (e.g., AccountManager)
* core:ui → Shared Compose UI components

* features:auth → Authentication UI (email login, GitHub OAuth)

---

## 💻 Deckfile DSL Example
```
deck.name = "TestDeck"
deck.author = "Justin"

deck.start

STEP BEGIN setup-c-hat-server
CLONE JustINCodingUK/C-Hat-Server-V2 IN /home/justinw/projects/
RUN "./gradlew --refresh-dependencies" IN /home/justinw/projects/C-Hat-Server-V2
STEP END

STEP BEGIN setup-c-hat-client
INSTALL google.flutter@3.32.5
CLONE JustINCodingUK/C-Hat-Client IN /home/justinw/projects/
RUN "flutter pub get" IN /home/justinw/projects/C-Hat-Client
RUN "code ." IN /home/justinw/projects/C-Hat-Client
STEP END
```

> Deck tasks run in parallel by default (unless inside Step tasks), allowing lightning-fast setups ⚡

---

## 🧪 Tech Stack

* Kotlin Multiplatform (JVM)
* Compose Multiplatform Desktop
* Firebase (Authentication)
* [Credence](https://github.com/JustINCodingUK/Credence) (OAuth Helper Library)
* Ktor (Networking)
* Koin (Dependency Injection)
* MapDB (Encrypted key-value storage)

---


## 📝 License

MIT License

---

## 🤝 Contributing

Wanna help? PRs are welcome!
Issues would be resolved as soon as possible.