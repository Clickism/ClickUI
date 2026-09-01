package de.clickism.clickui.testmod;

import de.clickism.clickui.Ref;
import de.clickism.clickui.UiBuilder;
import de.clickism.clickui.UiColor;
import de.clickism.clickui.UiScreen;
import de.clickism.clickui.elements.input.NumberField;
import de.clickism.clickui.style.Border;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TestMod implements ClientModInitializer, UiBuilder {
    @Override
    public void onInitializeClient() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            openTestScreen();
        });
    }

    private void openTestScreen() {
        var screen = UiScreen.asScreen(box()
            .vertical()
            .childGap(10)
            .padding(30)
            .children(
                text("Hello, this is a test screen!")
                    .padding(20)
                    .style(style()
                        .fontScale(2.0f)
                        .backgroundColor(UiColor.BLUE)
                        .borderColor(UiColor.YELLOW)
                        .borderWidth(5)),
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
                    .style(style()
                        .backgroundColor(UiColor.GREEN)
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

        Ref<NumberField> numberRef = ref();

        var newScreen = UiScreen.asScreen(box()
            .alignCenter()
            .grow()
            .style(style()
                .backgroundColor(UiColor.WHITE_A50)
                .alpha(0.5f))
            .children(
                box()
                    .width(300)
                    .style(style()
                        .borderColor(UiColor.LIGHT_GRAY)
                        .backgroundColor(UiColor.BLACK)
                        .alpha(0.5f))
                    .padding(8)
                    .childGap(8)
                    .alignCenter()
                    .scrollable(false)
                    .children(
                        h1("New Screen!"),
                        text("There are some important info here!"),
                        h3("For example:"),
                        box()
                            .scrollable(true)
                            .width(300)
                            .padding(16)
                            .height(100)
                            .childGap(10)
                            .style(style().borderColor(UiColor.GREEN))
                            .children(
                                text("Scrollable content line 1"),
                                text("Scrollable content line 2"),
                                text("Scrollable content line 3"),
                                text("Scrollable content line 4"),
                                button("Drag me!")
                                    .onDragStart(event -> {
                                        event.player().sendSystemMessage(Component.literal("Drag started!"));
                                    })
                                    .onDragEnd(event -> {
                                        event.player().sendSystemMessage(Component.literal("Drag ended!"));
                                    })
                                    .onDrag(event -> {
                                        event.player().sendSystemMessage(Component.literal("Dragging!"));
                                    }),
                                text("Scrollable content line 5"),
                                text("Scrollable content line 6"),
                                text("Scrollable content line 7"),
                                button("Whaat?"),
                                text("Scrollable content line 8"),
                                text("Scrollable content line 9"),
                                button("Far down!?"),
                                // Test borders
                                box()
                                    .width(50)
                                    .height(50)
                                    .style(style()
                                        .backgroundColor(UiColor.WHITE_A30)
                                        .whenHovered(style()
                                            .borderPosition(Border.Position.CENTER)
                                            .borderColor(UiColor.RED)
                                            .borderWidth(3))),

                                box()
                                    .width(50)
                                    .height(50)
                                    .style(style()
                                        .backgroundColor(UiColor.WHITE_A30)
                                        .whenHovered(style()
                                            .borderPosition(Border.Position.INSIDE)
                                            .borderColor(UiColor.BLUE))),

                                box()
                                    .width(50)
                                    .height(50)
                                    .style(style()
                                        .backgroundColor(UiColor.WHITE_A30))
                            ),
                        text("You can go back to the previous screen by clicking the button below.\n\nAlternatively, you can also press the §lESC §rkey to go back.")
                            .alignTextCenter()
                            .padding(4),
                        text("veyrlongwordthatshouldwrapandbreakintomultiplelinesbecauseitistoolongbutitkindadoesntithink")
                            .alignTextCenter()
                            .padding(4),
                        new Counter(),
                        textField("Type something...")
                            .tooltip(box()
                                .size(20)
                                .style(style()
                                    .backgroundColor(UiColor.YELLOW)
                                    .borderColor(UiColor.RED)))
                            .maxLength(32)
                            .suggest("hello", "bye", "heat"),
                        numberField("Type a number...")
                            .padding(0)
                            .tooltip(image(
                                ResourceLocation.tryBuild("minecraft", "textures/block/stone.png"),
                                32,
                                32
                            ))
                            .ref(numberRef),
                        button("Print Number")
                            .tooltip(new Counter())
                            .onClick(event -> {
                                var number = numberRef.get().doubleValue();
                                event.player().sendSystemMessage(Component.literal("Number: " + number));
                            }),
                        button("Go back")
                            .tooltip("Click to go back to the previous screen")
                            .onClick(event -> {
                                event.ui().close();
                            })
                    )
            )
        );

        newScreen
            .debug(false)
            .open();
    }
}