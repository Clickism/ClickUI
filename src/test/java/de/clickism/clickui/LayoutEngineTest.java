package de.clickism.clickui;

import de.clickism.clickui.elements.Box;
import de.clickism.clickui.layout.Align;
import de.clickism.clickui.layout.LayoutEngine;
import de.clickism.clickui.layout.Rect;
import de.clickism.clickui.layout.Sizing;
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
                            .growWidth()
                            .growHeight(),

                        box()
                            .growWidth()
                            .growHeight()
                    ),

                box()
                    .horizontal()
                    .padding(10)
                    .childGap(10)
                    .growWidth()
                    .mainAlign(Align.CENTER)
                    .crossAlign(Align.CENTER)
                    .children(
                        box()
                            .width(100)
                            .height(150),

                        box()
                            .width(100)
                            .height(100),

                        box()
                            .height(Sizing.grow())
                            .childGap(4)
                            .padding(4)
                            .children(
                                box()
                                    .absolute(600, 400)
                                    .height(20)
                                    .width(20),

                                box()
                                    .relative(30, -30)
                                    .height(20)
                                    .width(20),

                                box()
                                    .width(20)
                                    .height(20),

                                box()
                                    .width(20)
                                    .height(40)
                            )
                    ),

                box()
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

    @Test
    void renderSimpleGrid() {
        int size = 50;
        renderScreen(grid(3)
            .childGap(20)
            .children(
                box()
                    .width(size)
                    .height(size),
                box()
                    .width(size)
                    .height(size),
                box()
                    .width(size)
                    .height(size),
                box()
                    .width(size)
                    .height(size),
                box()
                    .width(size)
                    .height(size),
                box()
                    .width(size)
                    .height(size),
                box()
                    .width(size)
                    .height(size),
                box()
                    .width(size)
                    .height(size),
                box()
                    .width(size)
                    .height(size)
            ));
    }

    @Test
    void renderWrappedChildren() {
        renderScreen(box()
            .padding(10)
            .children(
                box()
                    .horizontal()
                    .width(300)
                    .wrapChildren(true)
                    .childGap(10)
                    .crossAlign(Align.CENTER)
                    .children(
                        box()
                            .width(100)
                            .height(100),
                        box()
                            .width(100)
                            .height(100),
                        box()
                            .width(100)
                            .height(100),
                        box()
                            .width(100)
                            .height(100)
                    ),

                box()
                    .width(200)
                    .height(30)
            )
        );
    }

    @Test
    void renderWrapped() {
        renderScreen(box()
            .padding(10)
            .width(400)
            .height(800)
            .childGap(10)
            .crossAlign(Align.CENTER)
            .children(
                box().size(200),

                box()
                    .horizontal()
                    .wrapChildren(true)
                    .childGap(5)
                    .padding(10)
                    .alignCenter()
                    .children(
                        box().size(100),
                        box().size(100),
                        box().size(100),
                        box().size(100),
                        box().size(100),
                        box().size(100),
                        box().size(100),
                        box().size(100)
                    )
            )
        );
    }

    @Test
    void renderCrossAligned() {
        renderScreen(box()
            .width(600)
            .height(300)
            .crossAlign(Align.CENTER)
            .childGap(10)
            .children(
                box()
                    .width(100)
                    .height(100),

                box()
                    .width(200)
                    .height(50),

                box()
                    .width(200)
                    .height(50),

                box()
                    .width(150)
                    .height(50)
            ));
    }

    @Test
    void renderGrowHorizontal() {
        renderScreen(box()
            .width(600)
            .height(400)
            .padding(20)
            .children(
                box()
                    .horizontal()
                    .padding(10)
                    .grow()
                    .children(
                        box().size(100),
                        box().size(100)
                    )
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