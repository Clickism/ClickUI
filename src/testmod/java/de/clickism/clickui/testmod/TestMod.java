package de.clickism.clickui.testmod;

import de.clickism.clickui.UiBuilder;
import de.clickism.clickui.UiScreen;
import de.clickism.clickui.style.Style;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.client.Minecraft;

import java.awt.*;

public class TestMod implements ClientModInitializer, UiBuilder {
    @Override
    public void onInitializeClient() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            openTestScreen();
        });
    }

    private void openTestScreen() {
        var screen = UiScreen.create(box()
            .vertical()
            .childGap(10)
            .padding(30)
            .children(
                text("Hello, this is a test screen!")
                    .style(Style.empty()
                        .background(Color.BLUE)
                        .border(Color.YELLOW)
                        .borderWidth(5))
                    .padding(20),
                text("Whaaat?"),
                button("Click me!")
                    .width(200),
                box()
                    .height(200)
                    .horizontal()
                    .style(Style.empty()
                        .background(Color.GREEN)
                        .alpha(0.5f))
                    .children(
                        text("This is a box")
                            .padding(10),
                        button("Click me too!")
                            .padding(20)
                    )
            )
        );

        var client = Minecraft.getInstance();
        client.execute(() -> client.setScreen(screen));
    }
}