package Drawer;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class Graph extends Frame {
    static int h = 400;
    static int w = 100;
    static int width = 500;
    static int height = 500;
    static int tile = 50;
    static float scale = 1;
    Vector<Linear> lines = new Vector<>();
    Vector<Quadratic> quadratics = new Vector<>();
    Vector<Function> functions = new Vector<>();
    List<Color> colors = new ArrayList<>();
    public Graph() {
        this.colors = new ArrayList<>();
        this.setVisible(true);
        this.setSize(width, height);
        this.setIconImage((new ImageIcon(Objects.requireNonNull(getClass().getResource("icon.png")))).getImage());
        this.setTitle("Graph drawer");
        FuncPanel fp = new FuncPanel(this);
        fp.setVisible(true);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                fp.dispose();
                System.exit(0);
            }

        });
        this.addMouseWheelListener(e -> {
            if (e.getWheelRotation() == -1) {
                tile += 5;
            } else {
                tile = Math.max(10, tile - 5);
            }
            Graph.this.paint(Graph.this.getGraphics());
        });
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouse_x = e.getX(), mouse_y = e.getY();
                JOptionPane.showMessageDialog(null, String.format("%f %f", (float)(mouse_x - w) / tile, (float)(h - mouse_y) / tile), "Position Mouse", JOptionPane.PLAIN_MESSAGE);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    moveX(-10);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    moveX(10);
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    moveY(-10);
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    moveY(10);
                } else if (e.getKeyCode() == KeyEvent.VK_R) {
                    setCenter();
                } else if (e.getKeyCode() == KeyEvent.VK_F) {
                    new FuncPanel(Graph.this);
                }
            }
        });
    }

    public void moveX(int x) {
        w += x;
        this.paint(this.getGraphics());
    }

    public void moveY(int y) {
        h += y;
        this.paint(this.getGraphics());
    }

    public void addLine(Linear line) {
        lines.add(line);
        this.paint(this.getGraphics());
    }

    public void addQuadratic(Quadratic quad) {
        quadratics.add(quad);
        this.paint(this.getGraphics());
    }

    public void addFunction(Function f) {
        functions.add(f);
    }

    public void setCenter() {
        h = height / 2 / tile * tile;
        w = width / 2 / tile * tile;
        this.paint(this.getGraphics());
    }

    public void clearAll() {
        this.functions.clear();
        this.lines.clear();
        this.quadratics.clear();
        this.paint(this.getGraphics());
    }

    public void paint(Graphics gr) {
        gr.setColor(new Color(0, 250, 255));
        gr.fillRect(0, 0, width, height);
        gr.setColor(Color.red);
        gr.fillRect(0, h - 1, width, 2);
        gr.fillRect(w - 1, 0, 2, height);
        gr.setColor(Color.black);
        gr.fillOval(w-5, h-5, 10, 10);
        gr.drawChars("0".toCharArray(), 0, 1, w+5, h+10);
        width = this.getWidth();
        height = this.getHeight();
        for (int x = 0; x <= width; x += tile) {
            gr.drawLine(x + w - w / tile * tile, 0, x + w - w / tile * tile, height);
        }
        for (int y = 0; y <= height; y += tile) {
            gr.drawLine(0, y + h - h / tile * tile,  width, y + h - h / tile * tile);
        }
        gr.setColor(new Color(200, 150, 0));
        for (Linear line : lines) {
            for (int b = -1; b <= 1; b++) {
                gr.drawLine(0, h - (int) (line.y(((float) -w / tile * scale)) * tile / scale) + b, width, h - (int) (line.y(((float) (width - w) / tile * scale)) * tile / scale) + b);
            }
        }
        int i = 0;
        for (Function function : functions) {
            gr.setColor(colors.get(i));
            int y_p = h - (int) (function.get((double)(-w) / tile * scale) * tile / scale);
            double y1 = function.get((float) (-w) / tile * scale);
            int x = 1;
            while (x <= width && !function.isDefinition((double) (x-w) / tile * scale)) {
                y_p = h - (int) (function.get((double) (x - w) / tile * scale) * tile / scale);
                y1 = function.get((double) (x - w) / tile * scale);
                x++;
            }
            for (; x <= width; x++) {
                function.get((double) (x - w) / tile * scale);
                if (StringFunc.except > 0) continue;
                if (!function.isDefinition((double) (x-w) / tile * scale)) continue;
                if (Math.abs(function.get((double) (x - w) / tile * scale) - y1) <= 100.f) {
                    for (int b = -function.width; b <= function.width; b++) {
                        gr.drawLine(x - 1, y_p + b, x, h - (int) (function.get((double) (x - w) / tile * scale) * tile / scale) + b);
                    }
                } else {
                    gr.drawOval(x, h - (int) (function.get((double) (x - w) / tile * scale) * tile / scale), 1, 1);
                }
                y_p = h-(int)(function.get((double) (x-w) / tile * scale) * tile / scale);
                y1 = function.get((double) (x - w) / tile * scale);
            }
            i++;
        }
        for (Quadratic quad : quadratics) {
            for (int y = 0; y <= height; y++) {
                if (quad.D((float) (h - y) / tile * scale) > 0) {
                    for (int b = -1; b <= 1; b++) {
                        gr.drawOval((int) (quad.x((float) (h - y) / tile * scale)[0] * tile / scale) + w, y + b, 1, 1);
                        gr.drawOval((int) (quad.x((float) (h - y) / tile * scale)[1] * tile / scale) + w, y + b, 1, 1);
                    }
                } else if (quad.D((float) (h - y) / tile * scale) == 0) {
                    for (int b = -1; b <= 1; b++) {
                        gr.drawOval((int) (quad.x((float) (h - y) / tile * scale)[0] * tile / scale) + w, y + b, 1, 1);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Graph graph = new Graph();
        graph.setCenter();
    }
}
