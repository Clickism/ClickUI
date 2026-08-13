package de.clickism.clickui;

import org.junit.jupiter.api.Test;

public class UiTest {
    @Test
    public void testUi() {

//        var test = Ui.box()
//                .width(FIT)
//                .height(GROW)
//                .children(
//                        MinecraftUi.text("Hello")
//                                .width(FIT)
//
//                );

        var root = new Element()
                .padding(8)
                .children(
                        new Element()
                                .width(32)
                                .height(32)
                );

        new Component() {
            @Override
            public Element build() {
                return box()
                        .vertical()
                        .children(
                                box()
                        );
            }
        };
    }
}
