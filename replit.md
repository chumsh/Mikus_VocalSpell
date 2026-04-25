# Miku's VocalSpell — Iron's Spells Addon

A Minecraft NeoForge mod (Java/Gradle project) targeting Minecraft 1.21.1. This is an addon for the Iron's Spells 'n Spellbooks mod.

## Project Type
- **Language:** Java 21
- **Build System:** Gradle 9.2.1 (via wrapper) with the NeoForged ModDevGradle plugin (`net.neoforged.moddev` v2.0.140)
- **Target:** Minecraft 1.21.1, NeoForge 21.1.218
- **Output:** A mod JAR at `build/libs/mikus_vocal_spell-1.0.0.jar`

This is **not a web application** — it has no HTTP frontend or backend. It is a desktop game mod that ships as a JAR loaded by the Minecraft NeoForge mod loader.

## Replit Environment Setup
The Replit environment provides Java 19 (GraalVM 22.3.1) by default, but this project requires Java 21. The project's `Build Mod` workflow pins JDK 21 from the Nix store:

```
JAVA_HOME=/nix/store/k95pqfzyvrna93hc9a4cg5csl7l4fh0d-openjdk-21.0.7+6
```

The `gradlew` wrapper script is marked executable.

## Workflow
- **Build Mod** (console): Runs `./gradlew build --no-daemon --console=plain` with Java 21 to compile the mod and produce the distributable JAR. Initial build downloads Minecraft + NeoForge dependencies (~5 minutes, multi-hundred-MB cache under `~/.gradle`). Subsequent builds use the configuration cache and finish much faster.

## Key Dependencies
- GeckoLib (animations)
- Player Animator
- Curios API (accessory slots)
- Patchouli (in-game documentation)
- Iron's Spells 'n Spellbooks (compileOnly + localRuntime)

## Project Layout
- `src/main/java/` — mod source code
- `src/main/resources/` — mod assets, lang files, models, `META-INF/neoforge.mods.toml`
- `src/main/templates/` — templated metadata expanded at build time
- `src/generated/resources/` — datagen-generated recipes/advancements/loot tables
- `build.gradle`, `settings.gradle`, `gradle.properties` — Gradle config
- `build/libs/` — built JAR output

## Deployment
Not applicable. This project produces a JAR for users to drop into their Minecraft mods folder; it is not a web service and cannot be deployed to a Replit hosting target.
