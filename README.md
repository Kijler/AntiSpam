# AntiSpam

AntiSpam is a fully configurable Minecraft plugin that monitors chat activity and effectively prevents spam, repeated messages, blocked words, and other unwanted player behavior.  
The plugin is designed with performance, clarity, and flexibility in mind, making it suitable for both small servers and large networks.

This repository may contain **partial source code only**, published for transparency purposes, allowing users to verify that the plugin is safe and does not contain any malicious code.  
The core implementation is not included to protect the author's intellectual property.

---

## 🔍 About the Plugin

**AntiSpam** provides powerful chat protection while giving administrators full control over moderation, punishments, and logging.  
Thanks to its modular design and optional web interface, it can be easily adapted to any server setup.

---

## ✨ Main Features

### 🛡️ Chat Protection
- Protection against repeated or very similar messages  
- Blocking of messages containing forbidden words or phrases  
- Detection of excessive character repetition (e.g. `!!!!`, `aaaaaa`)  
- Configurable chat cooldown to prevent fast spam  

### ⚙️ Modular System
- Each feature can be enabled or disabled individually  
- Easy customization via `config.yml`  
- Suitable for both small servers and large networks  

---

## ⚠️ Automatic Player Punishments

AntiSpam includes an advanced **automatic punishment system** for players who repeatedly violate chat rules.

- Ability to enable or disable automatic punishments  
- Configurable number of violations before a punishment is applied  
- Fully configurable punishment commands (`mute`, `warn`, `tempmute`, `ban`, etc.)  
- Customizable punishment executor name (e.g. `AntiSpam` or `Console`)  
- Every punishment is automatically recorded  

---

## 🆔 Unique Punishment IDs

Each punishment is assigned a **unique ID** that allows clear identification of chat rule violations.

- The ID is displayed when the punishment is issued  
- The punishment can be searched later using an in-game command  
- The ID is also used in the web interface  

This system provides administrators with a clear overview and full control over punishment history.

---

## 🌐 Web Interface

AntiSpam includes a built-in **web interface** that allows administrators to search for punishments by their unique ID and view detailed information about chat rule violations.

### Security
- Secure login system to prevent access by regular players  
- The first registered user becomes the main administrator  
- Administrators can add, edit, and remove web users  

⚠️ The web interface **requires a MySQL database**.

---

## 💾 Database Support

AntiSpam supports multiple data storage methods:

- **SQLite (`data.db`)** – suitable for smaller servers  
- **MySQL** – recommended for larger servers and required for the web interface  

All database settings are fully configurable in the configuration file.

---

## ⌨️ Commands

### `/antispam reload`
Reloads the plugin configuration and language files without restarting the server.  
**Permission:** `antispam.reload`

### `/antispam check <ID>`
Searches for a specific punishment using its unique ID.  
**Permission:** `antispam.check`

---

## 🔑 Permissions

- `antispam.reload` – Allows usage of the `/antispam reload` command  
- `antispam.check` – Allows usage of the `/antispam check <ID>` command  
- `antispam.bypass` – Players with this permission are completely ignored by AntiSpam  
- `antispam.notify` – Allows receiving notifications about plugin updates  

---

## 🌍 Multi-language Support

The plugin includes three default language files:

- English  
- Czech  
- German  

Language files can be freely edited or translated into any custom language.

---

## 🛠️ Configuration

- Clean and easy-to-edit `config.yml`  
- Ability to enable or disable individual features  
- Configuration of limits, delays, and punishments  
- No source code modifications required  

---

## ⚖️ License

This project may contain **partial source code for transparency purposes only**.  
You are **NOT allowed to copy, modify, or redistribute** the source code without explicit permission.

See the `LICENSE` file for full details.

---

© 2025 **Kijler** — All rights reserved  
🌐 https://kijler.eu
