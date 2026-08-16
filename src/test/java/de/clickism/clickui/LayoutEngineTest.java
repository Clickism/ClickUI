package de.clickism.clickui;

import de.clickism.clickui.elements.Box;
import de.clickism.clickui.layout.Layout;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

class LayoutEngineTest {

    @Test
    void renderLayout() {
        var root = new Box()
                .vertical()
                .padding(20)
                .childGap(10)
                .children(
                        new Box()
                                .width(300)
                                .horizontal()
                                .padding(10)
                                .childGap(10)
                                .children(
                                        new Box()
                                                .width(30)
                                                .height(30),

                                        new Box()
                                                .width(Sizing.grow())
                                                .height(Sizing.grow()),

                                        new Box()
                                                .width(Sizing.grow())
                                                .height(Sizing.grow())
                                ),

                        new Box()
                                .horizontal()
                                .padding(10)
                                .childGap(10)
                                .width(Sizing.grow())
                                .children(
                                        new Box()
                                                .width(100)
                                                .height(150),

                                        new Box()
                                                .width(100)
                                                .height(100),

                                        new Box()
                                                .width(20)
                                                .height(50)
                                ),

                        new Box()
                                .width(300)
                                .height(40)
                );

        new Layout()
                .width(100)
                .height(100);

        LayoutEngine engine = new LayoutEngine();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("ClickUI Layout Test");

            JPanel panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);

                    engine.layout(new Box()
                            .width(800)
                            .height(500)
                            .children(root));

                    renderElement(g, root, 0);
                }
            };

            frame.setContentPane(panel);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
        });

        // Keep the JUnit test alive while developing.
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException ignored) {
        }
    }

    private static void renderElement(
            Graphics g,
            Element<?> element,
            int depth
    ) {
        Rect bounds = element.bounds();

        // Draw the element's bounds.
        g.drawRect(
                bounds.x(),
                bounds.y(),
                bounds.width(),
                bounds.height()
        );

        // Draw its name/depth for debugging.
        g.drawString(
                "Element " + depth,
                bounds.x() + 4,
                bounds.y() + 15
        );

        for (var child : element.children()) {
            renderElement(g, child, depth + 1);
        }
    }
}