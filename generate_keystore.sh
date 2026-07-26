#!/bin/bash

# Loyalty Cards Keystore Generator
# Dit script helpt je een keystore te maken voor release builds

echo "=========================================="
echo "Loyalty Cards Keystore Generator"
echo "=========================================="
echo ""

# Vraag om informatie
read -p "Voer je keystore wachtwoord in (onthoud dit!): " keystore_password
read -p "Voer je alias in (bv. loyaltycards): " alias
read -p "Voer je alias wachtwoord in: " alias_password
read -p "Voer je naam in: " name
read -p "Voer je organisatie eenheid in: " organizational_unit
read -p "Voer je organisatie in: " organization
read -p "Voer je stad in: " city
read -p "Voer je provincie in: " state
read -p "Voer je land code in (bv. NL): " country_code

echo ""
echo "Aan het genereren van keystore..."
echo ""

# Genereer de keystore
keytool -genkey -v \
    -keystore loyaltycards.keystore \
    -alias "$alias" \
    -keyalg RSA \
    -keysize 2048 \
    -validity 10000 \
    -storepass "$keystore_password" \
    -keypass "$alias_password" \
    -dname "CN=$name, OU=$organizational_unit, O=$organization, L=$city, ST=$state, C=$country_code"

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Keystore succesvol gegenereerd!"
    echo ""
    echo "Bestand: loyaltycards.keystore"
    echo "Alias: $alias"
    echo ""
    echo "Verplaats de keystore naar de app map:"
    echo "  mv loyaltycards.keystore app/"
    echo ""
    echo "Voeg deze toe aan je build.gradle:"
    echo "  signingConfigs {"
    echo "      release {"
    echo "          storeFile file('loyaltycards.keystore')"
    echo "          storePassword '$keystore_password'"
    echo "          keyAlias '$alias'"
    echo "          keyPassword '$alias_password'"
    echo "      }"
    echo "  }"
    echo ""
    echo "Bouw dan de release APK met:"
    echo "  ./gradlew assembleRelease"
else
    echo "❌ Fout bij het genereren van de keystore"
    exit 1
fi
