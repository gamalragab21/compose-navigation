# compose-navigation

[![](https://jitpack.io/v/mvarnagiris/compose-navigation.svg)](https://jitpack.io/#mvarnagiris/compose-navigation)

- Navigate completely within Jetpack Compose
- Use `Router` as any other composable
- Nest `Router`s however you like to support split screens or bottom tabs
- Each `Router` you add will have its own back stack
- Back stack is handled for you no matter how complex your navigation is

## Download
Add Jitpack repository to your top level `build.gradle`:
```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
Add dependency in your app `build.gradle`:
```groovy
implementation 'com.github.mvarnagiris.compose-navigation:navigation:{latest_version}'
```
## How to use

Define your navigation routes. Best way to do that is to use a `sealed class`. Each route can contain all necessary parameters for your screens.
```kotlin
sealed class AppRoute {
    object SplashRoute : AppRoute()
    object HomeRoute : AppRoute()
    data class DetailsRoute(val id: String) : AppRoute()
}
```

Wherever you want routing to happen add `Router`:
```kotlin
@Composable
fun AppRoot() {
    Router<AppRoute>(start = SplashRoute) { currentRoute ->
        // this lambda has this signature: BackStack<AppRoute>.(Route<AppRoute>) -> Unit
        // this is BackStack<AppRoute>. So you can call BackStack methods easily like push(...), pop(), etc.
        // currentRoute is Route<AppRoute> that contains current AppRoute as data and other metadata like back stack identifier
        when (val route = currentRoute.data) {
            SplashRoute -> SplashScreen(onInitialized = { replace(HomeRoute) })
            HomeRoute -> HomeScreen(onDetailsSelected = { id -> push(DetailsRoute(id)) })
            is DetailsRoute -> DetailsScreen(route.id)
        }
    }
}

@Composable
fun SplashScreen(onInitialized: () -> Unit) {
    // ...
}

@Composable
fun HomeScreen(onDetailsSelected: (String) -> Unit) {
    // ...
}

@Composable
fun DetailsScreen(id: String) {
    // ...
}
```

BackStack operations:
- **push** - push one or more routes into the back stack
- **pop** - remove top most route from the back stack. It will not remove last route
- **replace** - replace top most route with zero or more routes
- **replaceRoute** - replace specific route with zero or more routes
- **popUntil** - pop routes until predicate returns true or until last route
- **popUntilAndPush** - pop routes until predicate returns true or until last route then push given routes into the back stack
- **popAll** - pops all routes until only 1 remains

`backStackController.pop()` will pop from global back stack. Each `Router` has it's own `BackStack` and all of them are controlled by `backStackController` which has global back stack.

For more examples check out the `sample` app
