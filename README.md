# FamilyBudgetManager

### Описание

**FamilyBudgetManager** — это мобильное приложение для ОС Android для учета и ведения семейного бюджета.

### Стек технологий

- Язык программирования: **Kotlin**
- Платформа: **Android**
- Архитектура: **MVVM**
- Инструменты сборки: **Gradle**

### Установка и запуск

1. Клонируйте репозиторий:
   ```bash
   git clone https://github.com/christmaaas/family-budget-manager.git
   ```
2. Откройте проект в Android Studio.
3. Синхронизируйте проект с Gradle файлами.
4. Запустите приложение на эмуляторе или физическом устройстве Android.

### Структура проекта

```
TasksAndProjectsManager/
│
├── app/                   # Основной модуль приложения
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/      # Исходный код Kotlin
│   │   │   └── res/       # Ресурсы приложения
│   │   └── test/          # Тесты
│   └── build.gradle       # Конфигурация сборки модуля
│
├── gradle/
├── .gitattributes
├── .gitignore
├── build.gradle.kts       # Главный файл конфигурации сборки
├── gradle.properties
├── gradlew
├── gradlew.bat
├── local.properties
├── settings.gradle.kts
└── README.md              # Документация проекта
```

### Требования

- Android Studio 4.0+
- JDK 8+
- Android SDK с API level 21+

### Контрибуции

Приветствуются любые предложения по улучшению проекта! Если у вас есть идеи или вы нашли баг, пожалуйста, создайте issue или отправьте pull request.

### Спецификация требований программного обеспечения
Ознакомиться со спецификаций требований программного обеспечения приложения можно [тут](https://github.com/christmaaas/family-budget-manager/blob/main/docs/requirements/SRS.md)