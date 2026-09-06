# AntiSpam

AntiSpam is a fully configurable Minecraft plugin that monitors chat activity and effectively prevents spam, repeated messages, blocked words, and other unwanted player behavior.

The plugin is designed with performance, clarity, and flexibility in mind, ensuring it does not impact your server's TPS, even on large networks.

This repository may contain **partial source code only**, published for transparency purposes, allowing users to verify that the plugin is safe and does not contain any malicious code.

The core implementation is not included to protect the author's intellectual property.

---

## 🔍 About the Plugin

**AntiSpam** provides powerful chat protection while giving administrators full control over moderation, punishments, and logging.

Thanks to its modular design, smart pagination reports, and optional web interface, it can be easily adapted to any server setup.

---

## ✨ Main Features

### 🛡️ Chat Protection

* **Percentage-Based Anti-Spam:** Compares message similarity in percentages (configurable, e.g., 65%) to block repetitive content and prevent clever bypasses
* **Anti-Flood & Character Limits:** Blocks messages sent too fast and limits excessive character repetition (e.g., `ssssssssss`)
* **Blocked Words:** A fully customizable blacklist to prevent offensive language, which reliably catches blacklisted words even when hidden inside other text
* **Configurable Delays:** Global chat cooldowns and repeat message delays to prevent rapid spam

### 📈 Ladder Punishment System

* Fully configurable escalating punishments for each category (Ladder)
* Configures a sequence where repeated violations trigger progressively harsher penalties (e.g., 1st offense = 30m mute, 2nd = 2h mute, 3rd = 10d mute, 4th = ban)

### 📋 Smart Chat Reporting & Book GUI

* Players can report toxic users using `/chatreport <player>`
* The plugin automatically archives recent messages from the reported player
* Administrators can review reports directly in-game through an interactive book featuring clean pagination and distinct colored message separators

### 🤖 Enhanced Discord Webhooks

* Receive modern, real-time Discord notifications whenever a report is created
* Features thumbnails, custom footers, icons, clean formatting, and a clickable title link

### ⚙️ Modular System

* Each feature can be enabled or disabled individually
* Easy customization via `config.yml`
* Suitable for both small servers and large networks

---

## ⚠️ Automatic Player Punishments

AntiSpam includes an advanced **automatic punishment system** for players who repeatedly violate chat rules.

* Ability to enable or disable automatic punishments
* Configurable number of violations (threshold) before a punishment is applied
* Fully configurable punishment commands (`mute`, `warn`, `tempmute`, `ban`, etc.) or ladder sequences
* Customizable punishment executor name (e.g. `AntiSpam` or `Console`)
* Every punishment is automatically recorded

---

## 🆔 Unique Punishment IDs

Each punishment is assigned a **unique ID** that allows clear identification of chat rule violations.

* The ID is displayed when the punishment is issued
* The punishment can be searched later using an in-game command
* The ID is also used in the web interface

This system provides administrators with a clear overview and full control over punishment history.

---

## 🌐 Web Interface

AntiSpam includes a built-in **web interface** that allows administrators to search for punishments by their unique ID and view detailed information about chat rule violations.

### Security

* Secure login system to prevent access by regular players
* The first registered user becomes the main administrator
* Administrators can add, edit, and remove web users

⚠️ The web interface **requires a MySQL database**.

---

## 💾 Database Support

AntiSpam supports multiple data storage methods:

* **SQLite (`data.db`)** – suitable for smaller servers
* **MySQL** – recommended for larger servers and required for the web interface

All database settings are fully configurable in the configuration file.

---

## ⌨️ Commands

### `/antispam reload`

Reloads the plugin configuration and language files without restarting the server.

**Permission:** `antispam.reload`

### `/antispam check <ID>`

Searches for a specific punishment using its unique ID.

**Permission:** `antispam.admin`

### `/chatreport <player>`

Reports a player for chat violations.

**Permission:** `antispam.chatreport`

### `/chatreportlogs`

Displays recent chat reports.

**Permission:** `antispam.admin`

---

## 🔑 Permissions

* `antispam.reload` – Allows usage of the `/antispam reload` command
* `antispam.admin` – Access to reports, punishment lookup, and administrative features
* `antispam.chatreport` – Allows players to report others
* `antispam.bypass` – Players with this permission are completely ignored by AntiSpam
* `antispam.punishnotify` – Allows staff members to receive in-game notifications whenever a player is automatically punished
* `antispam.notify` – Allows receiving notifications about plugin updates

---

## 🌍 Multi-language Support

The plugin includes three default language files:

* English
* Czech
* German

Language files can be freely edited or translated into any custom language.

---

## 🛠️ Configuration

* Clean and easy-to-edit `config.yml`
* Ability to enable or disable individual features
* Configuration of limits, delays, thresholds, and punishment ladders
* No source code modifications required

---

## ⚖️ License

This project may contain **partial source code for transparency purposes only**.

You are **NOT allowed to copy, modify, or redistribute** the source code without explicit permission.

See the `LICENSE` file for full details.

---

© 2026 **Kijler** — All rights reserved

🌐 [https://kijler.eu](https://kijler.eu)
