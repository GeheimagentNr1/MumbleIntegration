# CLAUDE.md - Mumble Integration

## Projekt-Übersicht

**Mumble Integration** ist ein NeoForge Minecraft Mod.
- **Mod ID**: `mumbleintegration`
- **Package**: `de.geheimagentnr1.mumbleintegration`
- **Java Version**: 21 (`develop_26.1`/`develop_26.2`: 25, `jdk-25.0.4.7-hotspot`)
- **NeoForge Version**: je Branch, siehe Tabelle

| Branch | MC | Range | NeoForge (kompiliert gegen) | Hinweis |
|---|---|---|---|---|
| `develop_1.21.1` | 1.21.1 - 1.21.10 | `[1.21.1,1.21.10]` | `21.1.216` | Config-Labels per `drawCenteredString` mit Alpha-Farben (`drawString` liefert ab 1.21.6 `void`, Bytecode bis 1.21.10 identisch) |
| `develop_1.21.11` | 1.21.11 | `[1.21.11,1.21.12)` | `21.11.45` | `Identifier`, `Camera.position()`/`forwardVector()`/`upVector()`, GameTest entfernt |
| `develop_26.1` | 26.1 - 26.1.2 | `[26.1,26.2)` | `26.1.0.19-beta` (Java 25) | 26.x-Tooling, `GuiGraphicsExtractor` (`extractRenderState`, `text`/`centeredText`) |
| `develop_26.2` | 26.2 - 26.3 | `[26.2,27)` | `26.2.0.88` (Java 25) | `minecraft.gui.setScreen`, `gameRenderer.mainCamera()`, Port-Filter per `setResponder` (`EditBox.setFilter` entfernt) |

Alle 3.0.2, released 2026-10-02 (Config-`save()`-Fix, Windows-Link-Fix). Das alte `1.21.1-3.0.1` ist auf 1.21.1 - 1.21.5 reduziert (Config-GUI stürzt ab 1.21.6 ab). Details: [`../Docs/migrations/1.21.1-to-1.21.2.md`](../Docs/migrations/1.21.1-to-1.21.2.md) 4h, [`../Docs/migrations/1.21.10-to-1.21.11.md`](../Docs/migrations/1.21.10-to-1.21.11.md) 9, [`../Docs/migrations/1.21.11-to-26.1.md`](../Docs/migrations/1.21.11-to-26.1.md).

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
- **Config speichern**: Jeder Setter in `ClientConfig` ruft nach `set(..)` `save()` auf - `ConfigValue.set(..)` allein schreibt die Datei nicht (bis 3.0.1 gingen GUI-Änderungen beim Neustart verloren)
- **Auto Connect / Dimension Channels**: Öffnet `mumble://<adresse>:<port>/<pfad>[/<Dimension>]`. Unter Windows per `rundll32 url.dll,FileProtocolHandler` (wie Win+R), weil `Desktop.browse` den Link an den Web-Browser gab; sonst `Desktop.browse`

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

## Testing

Client-only: Jar in die Client-Instanz **und** in den Server-Testpack legen (Modliste muss übereinstimmen). Für Mumble-Tests liegt `config/mumbleintegration-client.toml` in allen Client-Instanzen. GUI-Änderungen prüfen: Wert ändern, Datei prüfen, Client neu starten. Jars nie bei laufendem Client tauschen.

### Java-Versionen

Verschiedene Java-Versionen sind unter `C:\Program Files\Eclipse Adoptium` installiert. Für einen Gradle-Build muss die passende Java-Version gewählt werden:

```powershell
# Java 21 für 1.21.x-Branches, Java 25 (jdk-25.0.4.7-hotspot) für 26.x-Branches
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.12.8-hotspot"
./gradlew build
```

### Unit Tests (JUnit 5)

Für reine Logik-Tests ohne Minecraft-Abhängigkeiten:

```bash
./gradlew test
```

Tests liegen unter `src/test/java/`. Ergebnisse: `build/reports/tests/test/index.html`

### NeoForge GameTest Framework

Für Integration Tests in einer echten Minecraft-Umgebung:

```bash
./gradlew runGameTestServer
```

GameTest-Klassen werden mit `@GameTestHolder` annotiert und liegen unter `src/main/java/.../elements/gametests/`.

### CI/CD (GitHub Actions)

Der Workflow `.github/workflows/build-and-test.yml` führt automatisch aus:
1. **Build**: Kompiliert den Mod
2. **Unit Tests**: Führt JUnit Tests aus
3. **GameTests**: Startet GameTestServer (optional)

### Was kann automatisiert getestet werden?

| Aspekt | Automatisiert? | Methode |
|--------|----------------|---------|
| Utility-Klassen | ✅ | JUnit |
| Config-Parsing | ✅ | JUnit |
| Commands | ✅ | GameTest |
| Block/Item-Verhalten | ✅ | GameTest |
| Multi-MC-Version | ⚠️ Pro Branch | CI Matrix |

## Referenzen

- [NeoForge Migration Primer](https://docs.neoforged.net/primer/docs/) — Dokumentiert API-Aenderungen zwischen Minecraft/NeoForge-Versionen; nuetzlich fuer die Pruefung von Breaking Changes beim Upgrade auf neue Versionen

---

## Wissensdatenbank

Versionsübergreifende Migrations- und Entwicklungs-Erkenntnisse (Breaking Changes, Fixes, Testumgebungs-Patterns) werden zentral in [`../Docs/`](../Docs/) gepflegt. Bei neuen relevanten Erkenntnissen dort ergänzen, nicht nur hier.
