package de.clickism.clickui.testmod;

import de.clickism.clickui.UiBuilder;
import de.clickism.clickui.UiScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

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
                    .style(style -> style
                        .fontScale(2.0f)
                        .background(Color.BLUE)
                        .border(Color.YELLOW)
                        .borderWidth(5))
                    .padding(20),
                text(Component.literal("Whaaat?").withStyle(ChatFormatting.BOLD)),
                button("Click me!")
                    .width(200)
                    .disabled(true)
                    .onClick(event -> {
                        event.player().sendSystemMessage(Component.literal("Button clicked!"));
                    })
                    .onRelease(event -> {
                        event.player().sendSystemMessage(Component.literal("Button released!"));
                    })
                    .onKeyPress(event -> {
                        event.player().sendSystemMessage(Component.literal("Key pressed on button!"));
                    }),
                box()
                    .height(200)
                    .vertical()
                    .style(style -> style
                        .background(Color.GREEN)
                        .alpha(0.5f))
                    .children(
                        text("This is a box")
                            .padding(10),
                        button("I don't feel like I fit in")
                            .width(50),
                        button("I do though"),
                        button("Click me too!")
                            .padding(20)
                    )
            )
        );

        var client = Minecraft.getInstance();
        client.execute(() -> client.setScreen(screen));
    }
}