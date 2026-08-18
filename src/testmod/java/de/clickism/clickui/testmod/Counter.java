package de.clickism.clickui.testmod;

import de.clickism.clickui.Component;
import de.clickism.clickui.reactivity.State;

public class Counter extends Component<Counter> {
    private final State<Integer> count = state(0);

    @Override
    protected void build() {
        add(text("Count: " + count.get()));
        add(button("Increment")
            .onClick(event -> count.update(c -> c + 1)));
    }
}
