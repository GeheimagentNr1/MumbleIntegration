# CLAUDE.md - Mumble Integration

Mumble Integration übergibt Position und Blickrichtung des Spielers über das Mumble-Link-Plugin an Mumble (positionelles Audio) und kann sich automatisch mit einem Mumble-Server/Kanal verbinden. Mod-ID `mumbleintegration`, Package `de.geheimagentnr1.mumbleintegration`. **Client-only**: NeoForge `@Mod( value = MODID, dist = Dist.CLIENT )`, Forge-Branches initialisieren per `DistExecutor.safeRunWhenOn( Dist.CLIENT, .. )`; `displayTest=IGNORE_SERVER_VERSION`, `usableOnServerSide=false`.

> Diese Datei ist auf allen `develop_*`-Branches identisch; Quelle: `../Docs/mod-claude/MumbleIntegration.md` (Änderungen dort, dann `Docs/tools/sync_mod_claude_md.sh`). Versionsstand: [`../Docs/VERSIONEN.md`](../Docs/VERSIONEN.md).

## Versionen

- Welcher Branch welche MC-Versionen abdeckt (Range, Tag, NeoForge-Version): Zeile „MumbleIntegration“ in [`../Docs/VERSIONEN.md`](../Docs/VERSIONEN.md).
- NeoForge: `develop_1.21.1` (ein Jar 1.21.1 - 1.21.10), `develop_1.21.11`, `develop_26.1` (26.1 - 26.1.2), `develop_26.2` (26.2 - 26.3). Forge: `develop_<mc>` bis 1.21.3 und `develop_1.21.1_forge`.
- Das alte Release `1.21.1-3.0.1` stürzt ab 1.21.6 beim Öffnen der Config-GUI ab und ist deshalb auf 1.21.1 - 1.21.5 reduziert; `1.21.1-3.0.2` deckt 1.21.1 - 1.21.10 ab.

## Abhängigkeiten

- Keine Mod-Abhängigkeiten. Forge ab 1.20: MinecraftForgeAPI (per JarInJar, siehe [`../Docs/forge/README.md`](../Docs/forge/README.md)).
- Gebündelt (JarInJar, in `custom.gradle`): `com.skaggsm:java-mumble-link` (Version `java_mumble_link_version`) und `com.skaggsm:jvm-shared-memory`, jeweils ohne JNA (kommt aus Minecraft). Repo `https://maven.skaggsm.com/releases`.
- Spielerseitig: Mumble mit aktiviertem Link-Plugin.

## Architektur

- `MumbleIntegration` - Hauptklasse: legt `MumbleLinker` und `ClientConfig` an, registriert die Client-Config und (im Client-Setup) den Config-Screen.
- `linking/MumbleLinker` - Event-Handler (`ClientTickEvent.Post`, `ClientPlayerNetworkEvent.LoggingIn/LoggingOut`): öffnet/schließt den Link (`MumbleLinkImpl`), schreibt pro Tick Kamera-Position/-Richtung, Identität = Spieler-UUID. Ohne Dimensions-Kanäle wird Y um `Dimensionsindex << 9` verschoben, damit Spieler verschiedener Dimensionen sich nicht hören. Auto-Connect öffnet `mumble://<adresse>:<port>/<pfad>[/<Dimension>]`.
- `config/ClientConfig` - `ModConfigSpec` (Typ CLIENT) mit Gettern/Settern, `handleConfigChange()` verbindet/trennt den Link.
- `config/gui/ModConfigScreen` - eigener Config-Screen (Schalter, Text- und Port-Felder).
- Forge 1.20 - 1.21.3 und `develop_1.21.1_forge`: gleiche vier Klassen über `AbstractMod` aus MinecraftForgeAPI. Forge bis 1.19.4: statische `handlers/ModEventHandler`/`ForgeEventHandler` und eine eigene Optionslisten-GUI (`config/gui/GuiOptions`, `config/gui/config/*`).

## Funktionen, Befehle, Konfiguration

- Keine Befehle, keine Registrierungen, kein Netzwerk.
- `config/mumbleintegration-client.toml`: `mumble_active`, `auto_connect`, `address`, `port` (Standard 64738), `path`, `use_dimension_channels`. Alle Werte auch über den Config-Screen in der Mod-Liste änderbar.
- Ohne Auto-Connect muss Mumble vor Minecraft gestartet werden; mit Auto-Connect startet der `mumble://`-Link Mumble selbst.

## Fallstricke

- **Config speichern**: jeder Setter in `ClientConfig` ruft nach `set(..)` `save()` auf - `ConfigValue.set(..)` allein schreibt die Datei nicht (bis 3.0.1 gingen GUI-Änderungen beim Neustart verloren).
- **Windows-Links**: `Desktop.browse( mumble://.. )` gab den Link an den Web-Browser. Unter Windows deshalb `rundll32 url.dll,FileProtocolHandler` (wie Win+R), sonst `Desktop.browse`.
- **GUI ab 1.21.6**: `GuiGraphics.drawString(..)` liefert `void` (altes Jar: `NoSuchMethodError` beim Öffnen des Screens) und wertet Alpha aus (`0xFFFFFF` unsichtbar). Labels per `drawCenteredString` mit Alpha-Farben (`0xFFFFFFFF`), rechtsbündig `endX - width + width / 2`. Jeden Port per [`bincheck.ps1`](../Docs/testing/tools/bincheck.ps1) prüfen.
- **Versionsgrenzen im Code**: 1.21.11 `Identifier`, `Camera.position()`/`forwardVector()`/`upVector()`; 26.1 `GuiGraphicsExtractor` (`extractRenderState`, `text`/`centeredText`); 26.2 `minecraft.gui.setScreen`, `gameRenderer.mainCamera()`, `EditBox.setFilter` entfernt (Port-Filter per `setResponder`). Details: [`../Docs/migrations/1.21.1-to-1.21.2.md`](../Docs/migrations/1.21.1-to-1.21.2.md) 4h, [`../Docs/migrations/1.21.10-to-1.21.11.md`](../Docs/migrations/1.21.10-to-1.21.11.md), [`../Docs/migrations/1.21.11-to-26.1.md`](../Docs/migrations/1.21.11-to-26.1.md).

## Testen

- Allgemeines Vorgehen: [`../Docs/testing/README.md`](../Docs/testing/README.md). Jar in Client-Instanz **und** Server-Testpack (Modliste muss übereinstimmen); Jars nie bei laufendem Client tauschen.
- Server-Smoke allein sagt nichts (Mod lädt dort nicht) - Prüfung im Client: Config-Screen öffnen, Werte ändern, `config/mumbleintegration-client.toml` prüfen, Client neu starten, Werte noch da.
- Positionsaudio und Auto-Connect brauchen ein laufendes Mumble mit Link-Plugin; Auto-Connect muss Mumble öffnen, nicht den Browser. Eine vorbereitete `mumbleintegration-client.toml` liegt in allen Client-Instanzen.
- Ab 1.21.11 keine GameTests mehr; CI baut und führt JUnit aus.

## Links

- Wiki: https://github.com/GeheimagentNr1/MumbleIntegration/wiki
- CurseForge: https://www.curseforge.com/minecraft/mc-mods/mumble-integration (Projekt-ID 388413)
- Modrinth: https://modrinth.com/mod/d77bzdmt
- [`../Docs/migrations/BREAKING_CHANGES.md`](../Docs/migrations/BREAKING_CHANGES.md), [`../Docs/releasing/release-process.md`](../Docs/releasing/release-process.md)
