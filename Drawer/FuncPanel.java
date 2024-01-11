package Drawer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Vector;

public class FuncPanel extends JFrame {
    JButton set = new JButton("Set");
    Font arial = new Font("arial", Font.BOLD, 20);
    JButton add = new JButton("+");
    JButton sub = new JButton("-");
    Graph gr;
    Vector<JTextField> textFields;
    Vector<JButton> colorButtons;
    JPanel panel;
    SpringLayout layout;
    FuncPanel(Graph gr) {
        this.gr = gr;
        this.setVisible(true);
        this.setBounds(gr.getBounds().x + gr.getBounds().width + 10, gr.getBounds().y, 300, 300);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                FuncPanel.this.dispose();
            }
        });
        this.panel = new JPanel();
        this.layout = new SpringLayout();
        this.panel.setLayout(layout);
        this.set.addActionListener(new Listener(1));
        this.add.addActionListener(new Listener(2));
        this.sub.addActionListener(new Listener(3));
        this.set.setFont(arial);
        this.panel.add(add);
        this.panel.add(set);
        this.panel.add(sub);
        this.layout.putConstraint(SpringLayout.WEST, add, 40, SpringLayout.WEST, panel);
        this.layout.putConstraint(SpringLayout.NORTH, add, 10, SpringLayout.NORTH, panel);
        this.layout.putConstraint(SpringLayout.WEST, sub, 120, SpringLayout.WEST, panel);
        this.layout.putConstraint(SpringLayout.NORTH, sub, 10, SpringLayout.NORTH, panel);
        this.layout.putConstraint(SpringLayout.WEST, set, 40, SpringLayout.WEST, panel);
        this.layout.putConstraint(SpringLayout.NORTH, set, 50, SpringLayout.NORTH, panel);
        this.setContentPane(this.panel);
        this.textFields = new Vector<>();
        this.colorButtons = new Vector<>();
    }

    class Listener implements ActionListener {
        int type;
        int par;
        Listener(int type) {
            this.type = type;
            this.par = 0;
        }
        Listener(int type, int par) {
            this.type = type;
            this.par = par;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            FuncPanel fp = FuncPanel.this;
            if (this.type == 1) {
                func isD = new func() {
                    public boolean bF(double x) {
                        return true;
                    }
                };
                fp.gr.clearAll();
                for (JTextField tf : textFields) {
                    func st = new StringFunc(tf.getText()).GetFunc();
                    Function f = new Function(st, isD, 1);
                    fp.gr.addFunction(f);
                }
                fp.gr.paint(fp.gr.getGraphics());
            } else if (this.type == 2) {
                JLabel label = new JLabel(String.format("%d. y =", textFields.size()+1));
                label.setFont(arial);
                JTextField x = new JTextField();
                JButton color_but = new JButton();
                color_but.setBackground(settings.colors.get(textFields.size() % 4));
                color_but.setPreferredSize(new Dimension(30, 30));
                color_but.addActionListener(new Listener(4, textFields.size()));
                x.setFont(arial);
                x.setPreferredSize(new Dimension(150, 30));
                x.addKeyListener(new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {

                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            System.out.println("ok");
                            func isD = new func() {
                                public boolean bF(double x) {
                                    return true;
                                }
                            };
                            FuncPanel.this.gr.clearAll();
                            for (JTextField tf : textFields) {
                                func st = new StringFunc(tf.getText()).GetFunc();
                                Function f = new Function(st, isD, 1);
                                FuncPanel.this.gr.addFunction(f);
                            }
                            FuncPanel.this.gr.paint(FuncPanel.this.gr.getGraphics());
                        }
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {

                    }
                });
                fp.textFields.add(x);
                fp.panel.add(x);
                fp.panel.add(label);
                fp.panel.add(color_but);
                fp.colorButtons.add(color_but);
                fp.layout.putConstraint(SpringLayout.WEST, x, 20 + label.getPreferredSize().width, SpringLayout.WEST, fp.panel);
                fp.layout.putConstraint(SpringLayout.NORTH, x, 90 + 40 * (fp.textFields.size() - 1), SpringLayout.NORTH, panel);
                fp.layout.putConstraint(SpringLayout.WEST, label, 10, SpringLayout.WEST, fp.panel);
                fp.layout.putConstraint(SpringLayout.NORTH, label, 90 + 40 * (fp.textFields.size() - 1), SpringLayout.NORTH, panel);
                fp.layout.putConstraint(SpringLayout.WEST, color_but, 30 + label.getPreferredSize().width + x.getPreferredSize().width, SpringLayout.WEST, fp.panel);
                fp.layout.putConstraint(SpringLayout.NORTH, color_but, 90 + 40 * (fp.textFields.size() - 1), SpringLayout.NORTH, panel);
                fp.setContentPane(fp.panel);
                fp.gr.colors.add(settings.colors.get(fp.gr.colors.size() % 4));
            } else if (this.type == 3) {
                if (fp.textFields.isEmpty()) return;
                fp.textFields.remove(fp.textFields.lastElement());
                fp.colorButtons.remove(fp.colorButtons.lastElement());
                fp.panel.remove(fp.panel.getComponents().length-1);
                fp.panel.remove(fp.panel.getComponents().length-1);
                fp.panel.remove(fp.panel.getComponents().length-1);
                fp.setContentPane(fp.panel);
            } else if (this.type == 4) {
                Color c = JColorChooser.showDialog(fp, "Choose a color", settings.colors.get(textFields.size() % 4));
                fp.gr.colors.set(par, c);
                fp.colorButtons.get(par).setBackground(c);
            }
        }
    }
}
