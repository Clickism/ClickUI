# ClickUI

> _A UI framework like no other. Some might even say, React in Minecraft?_

ClickUI is a client-side declarative UI framework designed for Minecraft, completely reimplementing the UI system from scratch.
It provides a **powerful layout engine**, a range of predefined UI elements/components, alongside
a flexible styling system, and easy event handling.

Thanks to the layout engine, you no longer need to manually position widgets,
or create many unnecessary subclasses for every widget and style variation you want!

The layouting system is inspired by web development, and allows you to create complex layouts with ease.

## Using ClickUI

You can create a screen by extending the `UiScreen` class and implementing the `build()` method.

```java
public class MyScreen extends UiScreen<MyScreen> {
    @Override
    protected void build() {
        children(
            h1("Welcome to ClickUI"),
            text("ClickUI is an easy to use UI library for Minecraft mods."),
            box()
                .horizontal()
                .growWidth()
                .childGap(8)
                .children(
                    button("Click Me")
                        .onClick(event -> {
                            event.player().sendSystemMessage(Component.literal("You clicked the button!"));
                        })
                ),
            box()
                .scrollable(true)
                .children(
                    text("Easy scrollable containers!"),
                    box()
                        .width(200)
                        .height(400)
                        .padding(16)
                        .alignCenter()
                        .style(style()
                            .backgroundColor(UiColor.BLACK_A50)
                            .borderColor(UiColor.LIGHT_GRAY))
                        .children(
                            text("Scrollable content here."),
                            text("Pretty neat!")
                                .style(style()
                                    .textColor(UiColor.OLIVE)
                                    .fontScale(2.0f))
                        )
                )
        );
    }
}
```

And to open a screen is as easy as:

```java
new MyScreen().open();
```

## Reactivity and Components

ClickUI has a rich reactivity system, allowing you to create reactive components that automatically
rebuild themselves when their state changes.

```java
public class Counter extends UiComponent<Counter> {
    private final State<Integer> count = state(0);

    @Override
    protected void build() {
        alignCenter();
        children(
            text("Count: " + count.get()),
            button("Increment")
                .onClick(event -> count.update(c -> c + 1))
        );
    }
}
```

### Refs

You can use refs to reference other components, allowing you to access them easily while keeping
the clean declarative style of the framework.

```java
@Override
protected void build() {
    Ref<Counter> counterRef = ref();
    children(
        new Counter()
            .ref(counterRef),
        button("Reset Counter")
            .onClick(event -> counterRef.get().reset())
    );
}
```

### Memoization

Some components may be expensive to build, or have complex state that shouldn't be change often (such as input fields).
For these cases, there is a built-in memoization system that allows you to cache a component.

There are two ways to use memoization:

`memo(() -> new MyComponent())` - This will keep the component cached forever.
`memo("key", () -> new MyComponent())` - This will keep the component cached until the key changes.

```java
@Override
protected void build() {
    Ref<Counter> counterRef = ref();
    children(
        memo(() -> new Counter()
            .ref(counterRef)),
        button("Reset Counter")
            .onClick(event -> counterRef.get().reset())
    );
}
```
