### Inventory Update Event Plugin

Just add `PlayerInventoryUpdateAsyncEvent` to listen player update event

If you want use it, just paste this in your build.gradle(.kts)

```kotlin
repositories {
    maven("https://repo.spliterash.ru/group/")
}

dependencies {
    compileOnly("ru.spliterash:inventory-update-event-plugin:1.0.0")
}
```
