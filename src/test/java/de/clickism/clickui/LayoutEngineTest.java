package de.clickism.clickui;

import de.clickism.clickui.elements.Box;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

class LayoutEngineTest implements UiBuilder {

    @Test
    void renderSimple() {
        renderScreen(box()
            .vertical()
            .padding(20)
            .childGap(10)
            .children(
                box()
                    .width(400)
                    .horizontal()
                    .padding(10)
                    .childGap(10)
                    .children(
                        box()
                            .width(30)
                            .height(30),

                        box()
                            .width(Sizing.grow())
                            .height(Sizing.grow()),

                        box()
                            .width(Sizing.grow())
                            .height(Sizing.grow())
                    ),

                box()
                    .horizontal()
                    .padding(10)
                    .childGap(10)
                    .width(Sizing.grow())
                    .mainAlign(Align.CENTER)
                    .crossAlign(Align.CENTER)
                    .children(
                        new Box()
                            .width(100)
                            .height(150),

                        new Box()
                            .width(100)
                            .height(100),

                        new Box()
                            .width(20)
                            .height(Sizing.grow())
                            .children(
                                box()
                                    .absolute(600, 400)
                                    .height(20)
                                    .width(20),
                                box()
                                    .relative(-20, 0)
                                    .height(20)
                                    .width(20)
                            )
                    ),

                new Box()
                    .width(300)
                    .height(40)
            )
        );
    }

    @Test
    void renderGrid() {
        renderScreen(box()
            .horizontal()
            .height(Sizing.grow())
            .width(Sizing.grow())
            .children(
                // First panel
                box()
                    .width(300)
                    .height(Sizing.grow())
                    .padding(10),

                // Space
                box()
                    .width(Sizing.grow())
                    .height(Sizing.grow())
                    .mainAlign(Align.END)
                    .padding(20)
                    .children(
                        box()
                            .width(Sizing.grow())
                            .height(50)
                    ),

                // Second panel
                box()
                    .width(300)
                    .height(Sizing.grow())
            )
        );
    }

    private void renderScreen(Element<?> root) {
        LayoutEngine engine = new LayoutEngine();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("ClickUI Layout Test");

            JPanel panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);

                    int width = getWidth();
                    int height = getHeight();

                    // The root represents the actual available window space.
                    Box screen = new Box()
                        .width(width)
                        .height(height)
                        .children(root);

                    engine.layout(screen);

                    renderElement(g, root, 0);
                }
            };

            panel.addComponentListener(new java.awt.event.ComponentAdapter() {
                @Override
                public void componentResized(java.awt.event.ComponentEvent e) {
                    panel.repaint();
                }
            });

            frame.setContentPane(panel);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
        });

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