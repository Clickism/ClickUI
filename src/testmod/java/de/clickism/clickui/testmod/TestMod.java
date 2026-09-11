package de.clickism.clickui.testmod;

import de.clickism.clickui.BaseComponents;
import de.clickism.clickui.Ref;
import de.clickism.clickui.UiColor;
import de.clickism.clickui.UiScreen;
import de.clickism.clickui.elements.input.NumberField;
import de.clickism.clickui.style.Border;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

//? if fabric {
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
//?} else {
/*import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import static net.minecraftforge.api.distmarker.Dist.CLIENT;import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD;
*///?}

//? if fabric {
public class TestMod implements ClientModInitializer, BaseComponents {
 //?} else {
/*@Mod("clickui-test-mod")
@Mod.EventBusSubscriber(modid = "clickui-test-mod", bus = MOD, value = CLIENT)
public class TestMod implements BaseComponents {
*///?}

    //? if fabric {
    @Override
    public void onInitializeClient() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            openTestScreen();
        });
    }

    //?} else {
    /*private static TestMod instance;

    public TestMod() {
        instance = this;
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer().level().isClientSide()) {
            instance.openTestScreen();
        }
    }
    *///?}

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
                    .width(800)
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
                                        .backgroundColor(UiColor.WHITE_A30)),

                                // Test headers, random text
                                box().children(
                                    h1("Header 1"),
                                    text("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."),
                                    h2("Header 2"),
                                    text("Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."),
                                    h3("Header 3"),
                                    text("Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."),
                                    h4("Header 4"),
                                    text("Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."),
                                    h5("Header 5"),
                                    text("Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo."),
                                    h6("Header 6"),
                                    text("Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem.")
                                )
                            ),
                        text("You can go back to the previous screen by clicking the button below. Alternatively, you can also press the §lESC §rkey to go back.")
                            .tooltip("Test boksdjl fdsjkl jfslkd jfklsddfgdfgdfgdfgfdsfsdj jk jdfkl sjkl fsd kfds kljfsl kjfkl sdj klsjdkf jsdlk jfklsdl")
                            .alignTextCenter()
                            .padding(5),
                        new Counter()
                            .onKeyPress(event -> {
                                event.player().sendSystemMessage(Component.literal("Counter key pressed!"));
                            }),
                        numberField("Type something...")
                            .ref(numberRef)
                            .tooltip(box()
                                .size(20)
                                .style(style()
                                    .backgroundColor(UiColor.YELLOW)
                                    .borderColor(UiColor.RED)))
                            .maxLength(32)
                            .highlightInvalid(true)
                            .suggest("hello", "bye", "heat"),
                        button("Print Number")
                            .tooltip(new Counter())
                            .onClick(event -> {
                                var number = numberRef.get().doubleValue();
                                event.player().sendSystemMessage(Component.literal("Number: " + number));
                            }),
                        box()
                            .size(20)
                            .style(style()
                                .backgroundColor(UiColor.BLACK)
                                .whenHovered(style()
                                    .borderWidth(3)
                                    .borderColor(UiColor.WHITE_A50))),
                        button("Go back")
                            .tooltip("Click to go back to the previous screen")
                            .onClick(event -> {
                                event.screen().close();
                            })
                    )
            )
        );

        newScreen
            .debug(false)
            .open();
    }
}