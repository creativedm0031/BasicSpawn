## **BasicSpawn — Modernes Spawn-Teleport Plugin**

**BasicSpawn** ist ein leichtgewichtiges, zuverlässiges und optisch ansprechendes Spawn-System für Minecraft-Server.
Es ermöglicht Spielern eine stilvolle Teleportation zum festgelegten Spawn-Punkt, inklusive Countdown, Effekte und konfigurierbarer Nachrichten.

---

### **Features**

* Setzt einen zentralen Spawn-Punkt inkl. Blickrichtung
* `/spawn` Teleport mit **Countdown** und **Actionbar-Anzeige**
* Animierter Partikel-Effekt während der Wartezeit
* Visuelle Effekte + Ender-Teleport-Sound am Zielort
* **Instant-Teleport** für Teammitglieder oder VIP-Ränge
* vollständig **konfigurierbare Nachrichten** (mit `&`-Farbcodes)
* Countdown-Dauer frei einstellbar
* unterstützt Platzhalter wie `%player%` und `%time%`
* Chat bleibt frei, alle Anzeigen über Actionbar

---

### **Befehle**

| Befehl           | Beschreibung                               |
| ---------------- | ------------------------------------------ |
| `/setspawn`      | Setzt den aktuellen Punkt als Spawn        |
| `/spawn`         | Teleport mit Countdown                     |
| `/spawn instant` | Sofortige Teleportation (falls berechtigt) |

---

### **Berechtigungen**

| Permission           | Funktion                 |
| -------------------- | ------------------------ |
| `basicspawn.edit`    | Erlaubt `/setspawn`      |
| `basicspawn.instant` | Erlaubt `/spawn instant` |

---

### **Konfiguration**

* Countdown-Zeit anpassbar
* Alle Nachrichten editierbar
* Farben via `&`-Codes
* Actionbar-Anzeige statt Chat-Spam

---

### **Kompatibilität**

* **Spigot / Paper**
* Empfohlene Versionen: **1.16 – 1.21+**

---

**BasicSpawn** eignet sich für **Lobby-Server**, **Survival-Projekte** und **Netzwerke**, die eine saubere, klare und ästhetische Spawn-Teleport-Lösung benötigen.