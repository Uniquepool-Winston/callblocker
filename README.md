# CallBlocker — Android Studio project (debug APK)

В этом архиве — минимальный Android Studio проект (Kotlin + Jetpack Compose) для локальной сборки debug APK и/или использования с GitHub Actions.

Файлы:
- app/ : модуль приложения
- .github/workflows/android-build.yml : workflow для сборки debug APK на GitHub Actions

Как использовать:
1. Распакуй проект.
2. Открой в Android Studio и собери (Build > Build APK(s)).
3. Или создай репозиторий на GitHub, закинь сюда файлы и запусти workflow (Actions -> android-build).

Примечание: проект собирает debug APK. Для release-подписи нужно создать keystore и настроить сигнование.
