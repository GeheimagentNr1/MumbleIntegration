# AGENTS.md - Mumble Integration

## Projekt-Übersicht

**Mumble Integration** ist ein NeoForge Minecraft Mod für Minecraft 1.21.1.
- **Mod ID**: `mumbleintegration`
- **Package**: `de.geheimagentnr1.mumbleintegration`
- **Java Version**: 21
- **NeoForge Version**: 21.1.x

Teilt Positionsdaten mit Mumble über das Mumble Link Plugin für positionelles Audio.

## Abhängigkeiten

Keine Mod-Abhängigkeiten - eigenständiger Mod.

## Externe Libraries

- **Java Mumble Link**

## Projektstruktur

```
src/main/java/de/geheimagentnr1/mumbleintegration/
├── MumbleIntegration.java         # Haupt-Mod-Klasse
├── config/
│   ├── ClientConfig.java          # Client-Konfiguration
│   └── gui/
│       └── ModConfigScreen.java   # Config-GUI
└── linking/
    └── MumbleLinker.java          # Mumble-Verbindung
```

## Besonderheiten

- **Client-Only**: `usableOnServerSide=false` - nur für Clients
- **Config GUI**: Hat eine eigene Konfigurations-GUI
- **Mumble Link**: Nutzt das Mumble Link Plugin für positionelles Audio

## Code-Stil

- **Annotations**: `@NotNull` aus `org.jetbrains.annotations`
- **Lombok**: Projekt nutzt Lombok
- **Formatierung**: Leerzeichen nach `(` und vor `)` bei Methodenaufrufen

## Build & Test

```bash
./gradlew build
./gradlew runClient
```

## Deployment

- **CurseForge**: `./gradlew curseforge`
- **Modrinth**: `./gradlew modrinth`

## Wichtige Hinweise

1. **Mumble erforderlich**: Spieler benötigt Mumble mit aktiviertem Link Plugin
2. **Client-seitig**: Funktioniert nur auf dem Client
