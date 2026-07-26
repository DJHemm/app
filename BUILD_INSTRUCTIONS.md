# Loyalty Cards App - Build Instructions

## 📱 Hoe een .APK bestand te maken

Er zijn twee manieren om een .apk bestand te genereren:

---

## 🔧 Methode 1: Met Android Studio (Aanbevolen)

### Stappen:

1. **Open het project in Android Studio**
   ```
   File → Open → Selecteer de /workspace/DJHemm__app map
   ```

2. **Wacht tot Gradle sync voltooid is**
   - Android Studio downloadt automatisch alle benodigde dependencies
   - Dit kan enkele minuten duren

3. **Bouw de debug .apk**
   ```
   Build → Build Bundle(s) / APK(s) → Build APK
   ```
   - De .apk wordt gegenereerd in: `app/build/outputs/apk/debug/app-debug.apk`

4. **Bouw de release .apk (voor publicatie)**
   ```
   Build → Generate Signed Bundle / APK → APK
   ```
   - Selecteer "APK" in plaats van "Android App Bundle"
   - Kies een keystore of maak er een nieuwe
   - De .apk wordt gegenereerd in: `app/build/outputs/apk/release/app-release.apk`

---

## 💻 Methode 2: Met Command Line (Terminal)

### Vereisten:
- Java JDK 17 (of hoger)
- Android SDK (met build-tools, platform-tools, en Android 14 SDK)
- Android NDK (optioneel)

### Stappen:

#### 1. Installeer Android SDK

Download en installeer Android Studio of alleen de command line tools:
- [Android Command Line Tools](https://developer.android.com/studio#command-tools)

#### 2. Stel omgevingsvariabelen in

```bash
# Voor Linux/Mac
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/platform-tools
export PATH=$PATH:$ANDROID_HOME/tools
export PATH=$PATH:$ANDROID_HOME/tools/bin

# Voor Windows (in cmd)
set ANDROID_HOME=C:\Users\<username>\AppData\Local\Android\Sdk
set PATH=%PATH%;%ANDROID_HOME%\platform-tools
set PATH=%PATH%;%ANDROID_HOME%\tools
set PATH=%PATH%;%ANDROID_HOME%\tools\bin
```

#### 3. Installeer benodigde SDK packages

```bash
# Lijst beschikbare packages
sdkmanager --list

# Installeer benodigde packages
sdkmanager "platforms;android-34"
sdkmanager "build-tools;34.0.0"
sdkmanager "platform-tools"
sdkmanager "emulator"
sdkmanager "patcher;v4"
```

#### 4. Bouw de debug .apk

```bash
# Navigeer naar de projectmap
cd /workspace/DJHemm__app

# Maak gradlew uitvoerbaar (alleen eerste keer)
chmod +x gradlew

# Bouw debug APK
./gradlew assembleDebug

# De .apk vind je hier:
# app/build/outputs/apk/debug/app-debug.apk
```

#### 5. Bouw de release .apk

Eerst een keystore maken (alleen eerste keer):

```bash
# Maak een nieuwe keystore
keytool -genkey -v -keystore loyaltycards.keystore \
    -alias loyaltycards \
    -keyalg RSA \
    -keysize 2048 \
    -validity 10000

# Vul de gevraagde informatie in
# Onthoud het wachtwoord en alias!
```

Bouw dan de release APK:

```bash
# Kopieer de keystore naar de app map
cp loyaltycards.keystore app/

# Bouw release APK
./gradlew assembleRelease

# De .apk vind je hier:
# app/build/outputs/apk/release/app-release.apk
```

---

## 📁 Waar vind ik de .APK?

Na het bouwen vind je de .apk bestanden in:

```
app/build/outputs/apk/
├── debug/
│   └── app-debug.apk          # Voor testen en ontwikkeling
└── release/
    └── app-release.apk        # Voor publicatie (ondertekend)
```

---

## 🚀 Snelle Start (Debug APK)

Voor snelle testing kun je de **debug APK** gebruiken:

1. Open Android Studio
2. Open dit project
3. Druk op **Build → Build APK**
4. De debug APK wordt automatisch gegenereerd
5. Installeer op je Android telefoon:
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

---

## ⚠️ Belangrijke Notities

### Over de Debug APK:
- ✅ Werkt op alle Android apparaten (min SDK 24)
- ✅ Volledige functionaliteit
- ❌ Niet geschikt voor Google Play Store
- ❌ Heeft debug handtekening

### Over de Release APK:
- ✅ Geschikt voor Google Play Store
- ✅ Geoptimaliseerd en verkleind
- ✅ Ondertekend met je eigen keystore
- ⚠️ Moet zelf ondertekend worden

### Benodigde Permissies:
De app heeft de volgende permissies nodig:
- **Camera**: Voor barcode scannen
- **Opslag**: Voor database (automatisch verwerkt door Room)

---

## 🔍 Problemen Oplossen

### Probleem: "Gradle sync failed"
**Oplossing:**
- Zorg dat je internetverbinding werkt
- Klik op "Try Again" in Android Studio
- Of voer uit: `./gradlew --refresh-dependencies`

### Probleem: "SDK not found"
**Oplossing:**
- Installeer Android SDK via Android Studio
- Stel `ANDROID_HOME` omgevingsvariabele in

### Probleem: "Java not found"
**Oplossing:**
- Installeer Java JDK 17 of hoger
- Stel `JAVA_HOME` omgevingsvariabele in

### Probleem: "Build failed"
**Oplossing:**
- Check de error message in de terminal
- Meestal ontbrekende dependencies: voer `./gradlew --refresh-dependencies` uit

---

## 📞 Ondersteuning

Als je problemen hebt met het bouwen, kun je:

1. De error message hier posten
2. Controleren of alle dependencies geïnstalleerd zijn
3. Zorg dat je de nieuwste versie van Android Studio gebruikt

---

## 🎯 Snelle Command Line Bouw (als alles geïnstalleerd is)

```bash
cd /workspace/DJHemm__app
./gradlew clean assembleDebug
```

De debug APK is dan klaar in: `app/build/outputs/apk/debug/app-debug.apk`
