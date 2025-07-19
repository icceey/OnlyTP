#!/bin/bash

# OnlyTP Multi-Loader Build Script

set -e

echo "OnlyTP Multi-Loader Build Script"
echo "================================="

case "$1" in
    "forge")
        echo "Building for Forge..."
        ./gradlew clean build -PbuildTarget=forge --no-daemon
        echo "✓ Forge build completed!"
        ;;
    "neoforge")
        echo "Building for NeoForge..."
        ./gradlew clean build -PbuildTarget=neoforge --no-daemon
        echo "✓ NeoForge build completed!"
        ;;
    "all"|"")
        echo "Building for both platforms..."
        echo ""
        echo "1. Building for Forge..."
        ./gradlew clean build -PbuildTarget=forge --no-daemon
        echo "✓ Forge build completed!"
        echo ""
        echo "2. Building for NeoForge..."
        ./gradlew clean build -PbuildTarget=neoforge --no-daemon
        echo "✓ NeoForge build completed!"
        echo ""
        echo "🎉 All builds completed successfully!"
        ;;
    *)
        echo "Usage: $0 [forge|neoforge|all]"
        echo ""
        echo "  forge    - Build only for Forge"
        echo "  neoforge - Build only for NeoForge"
        echo "  all      - Build for both platforms (default)"
        exit 1
        ;;
esac

echo ""
echo "Build artifacts:"
ls -la build/libs/*.jar 2>/dev/null || echo "No JAR files found in build/libs/"