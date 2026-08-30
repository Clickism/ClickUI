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
                    .style(s -> s
                        .border(UiColor.LIGHT_GRAY.alpha(0.5f))
                        .background(UiColor.BLACK.alpha(0.5f))),
                box()
                    .grow()
                    .crossAlign(Align.CENTER)
                    .maxWidth(400)
                    .padding(8)
                    .style(s -> s
                        .border(UiColor.LIGHT_GRAY.alpha(0.5f))
                        .background(UiColor.BLACK.alpha(0.5f))
                    )
                    .scrollable(true)
                    .children(
                        box()
                            .scrollable(true)
                            .size(200)
                            .style(s -> s
                                .border(UiColor.LIGHT_GRAY.alpha(0.5f))
                                .background(UiColor.BLACK))
                            .children(
                                box()
                                    .width(50)
                                    .height(400)
                                    .overrideStyle(Style.empty()
                                        .background(UiColor.RED)),

                                box()
                                    .width(50)
                                    .height(500)
                                    .overrideStyle(Style.empty()
                                        .background(UiColor.BEIGE))
                            ),

                        box()
                            .size(200)
                            .style(s -> s
                                .background(UiColor.CYAN)),

                        box()
                            .size(200)
                            .style(s -> s
                                .background(UiColor.MAGENTA)),

                        box()
                            .size(200)
                            .style(s -> s
                                .background(UiColor.TEAL))
                    )
            ));
    }
}
