# SunLootManager — Система управления лутом для Spigot/Paper

Плагин для удобного управления системой лута в Minecraft с возможностью создания, редактирования и генерации различных наборов предметов.

---

## 📦 Подключение SunLootManager к вашему проекту

### Для Maven проектов

1. Добавьте репозиторий JitPack в `pom.xml`:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

2. Добавьте зависимость SunLootManager:
```xml
<dependencies>
    <dependency>
        <groupId>com.github.7byloper</groupId>
        <artifactId>SunLootManager</artifactId>
        <version>ВЕРСИЯ</version> <!-- Укажите актуальную версию -->
        <scope>provided</scope>
    </dependency>
</dependencies>
```

### Для Gradle проектов

1. Добавьте в `build.gradle`:
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.7byloper:SunLootManager:ВЕРСИЯ'
}
```

### Для Kotlin DSL (build.gradle.kts)

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.7byloper:SunLootManager:ВЕРСИЯ")
}
```

---

## 🎮 **Использование API**

### Получение менеджера лута
```java
LootManager lootManager = SunLootManager.getInstance().getLootManager();
```

### Создание нового набора лута
```java
boolean success = lootManager.createLoot("rare_items");
if (success) {
    // Набор успешно создан
}
```

### Добавление предметов в лут
```java
Optional<Loot> lootOptional = lootManager.getLoot("rare_items");
if (lootOptional.isPresent()) {
    Loot loot = lootOptional.get();
    
    ItemStack diamond = new ItemStack(Material.DIAMOND);
    LootItem lootItem = new LootItem("diamond", diamond, 25, 1, 3);
    
    loot.addItem("diamond", lootItem);
    loot.save(); // Сохранение изменений
}
```

### Генерация лута
```java
List<ItemStack> generatedLoot = lootManager.generateLoot("rare_items", 10);
// generatedLoot содержит до 10 случайных предметов согласно их шансам
```

### Получение всех наборов
```java
Collection<Loot> allLoots = lootManager.getAllLoots();
Set<String> lootNames = lootManager.getLootNames();
```

---

## 📊 **Структура файлов лута**

### Пример файла `loots/rare_items.yml`:
```yaml
items:
  diamond:
    item:
      ==: org.bukkit.inventory.ItemStack
      type: DIAMOND
    chance: 25
    minAmount: 1
    maxAmount: 3
  emerald:
    item:
      ==: org.bukkit.inventory.ItemStack
      type: EMERALD
    chance: 15
    minAmount: 1
    maxAmount: 2
  gold_ingot:
    item:
      ==: org.bukkit.inventory.ItemStack
      type: GOLD_INGOT
    chance: 60
    minAmount: 2
    maxAmount: 8
```

---

## 🔧 **Классы API**

### `LootManager`
Основной класс для управления всеми наборами лута.

**Основные методы:**
- `createLoot(String name)` - создание нового набора
- `deleteLoot(String name)` - удаление набора
- `getLoot(String name)` - получение набора по имени
- `generateLoot(String lootName, int amount)` - генерация лута
- `getAllLoots()` - получение всех наборов
- `reload()` - перезагрузка всех наборов
- `saveAll()` - сохранение всех наборов

### `Loot`
Представляет отдельный набор лута.

**Основные методы:**
- `addItem(String id, LootItem item)` - добавление предмета
- `removeItem(String id)` - удаление предмета
- `generateLoot(int amount)` - генерация предметов из набора
- `save()` - сохранение набора в файл

### `LootItem`
Представляет предмет в наборе лута.

**Характеристики:**
- `chance` - шанс выпадения (0-100%)
- `minAmount` - минимальное количество
- `maxAmount` - максимальное количество
- `itemStack` - сам предмет

**Методы:**
- `shouldDrop()` - проверка, должен ли предмет выпасть
- `getRandomAmount()` - получение случайного количества
- `getItemStackRandomAmount()` - получение предмета со случайным количеством

---

## 📁 **Структура директорий**

```
plugins/
└── SunLootManager/
    ├── config.yml
    └── loots/
        ├── common.yml
        ├── rare.yml
        ├── epic.yml
        └── custom_loot.yml
```

---

## ⚙️ **Зависимости**

- **Spigot/Paper** 1.16.5+
- **Java** 16+
- **SunCore**

---

## 🔄 **Команды и права**

Плагин предоставляет команду `/lootmanager` для управления лутом через чат.

---

## 📝 **Лицензия**

MIT License. Разработано для удобного управления системой лута в Minecraft плагинах.
