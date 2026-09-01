package de.clickism.clickui.testmod;

import de.clickism.clickui.UiColor;
import de.clickism.clickui.UiScreen;
import de.clickism.clickui.layout.Align;
import de.clickism.clickui.style.Style;

public class OverflowScreen extends UiScreen<OverflowScreen> {

    @Override
    public void build() {
        add(box()
            .alignCenter()
            .grow()
            .padding(8)
            .childGap(8)
            .children(
                h4("Select Symbol")
                    .padding(6, 12)
                    .style(style()
                        .borderColor(UiColor.LIGHT_GRAY.alpha(0.5f))
                        .backgroundColor(UiColor.BLACK.alpha(0.5f))),
                box()
                    .grow()
                    .crossAlign(Align.CENTER)
                    .maxWidth(400)
                    .padding(8)
                    .style(style()
                        .borderColor(UiColor.LIGHT_GRAY.alpha(0.5f))
                        .backgroundColor(UiColor.BLACK.alpha(0.5f))
                    )
                    .scrollable(true)
                    .children(
                        box()
                            .scrollable(true)
                            .size(200)
                            .style(style()
                                .borderColor(UiColor.LIGHT_GRAY.alpha(0.5f))
                                .backgroundColor(UiColor.BLACK))
                            .children(
                                box()
                                    .width(50)
                                    .height(400)
                                    .overrideStyle(Style.empty()
                                        .backgroundColor(UiColor.RED)),

                                box()
                                    .width(50)
                                    .height(500)
                                    .overrideStyle(Style.empty()
                                        .backgroundColor(UiColor.BEIGE))
                            ),

                        box()
                            .size(200)
                            .style(style()
                                .backgroundColor(UiColor.CYAN)),

                        box()
                            .size(200)
                            .style(style()
                                .backgroundColor(UiColor.MAGENTA)),

                        box()
                            .size(200)
                            .style(style()
                                .backgroundColor(UiColor.TEAL))
                    )
            ));
    }
}
