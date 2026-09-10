package de.clickism.clickui.testmod;

import de.clickism.clickui.UiColor;
import de.clickism.clickui.UiScreen;
import net.minecraft.network.chat.Component;

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
