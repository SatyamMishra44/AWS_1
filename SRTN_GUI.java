import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class SRTN_GUI extends JFrame implements ActionListener {

    private int[] p, bt, w, t, at, rt;
    private int n;
    private float awt, atat;

    private JTextField processField;
    private JButton run, clear;

    SRTN_GUI() {
        setTitle("Shortest Remaining Time Next (SRTN)");
        setSize(600, 400);
        setVisible(true);
        setLocation(350, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel processLabel = new JLabel("Number Of Processes: ");
        processLabel.setFont(new Font("Osward", Font.BOLD, 15));
        processLabel.setBounds(20, 30, 200, 30);
        add(processLabel);

        processField = new JTextField();
        processField.setBounds(180, 170, 80, 30);
        add(processField);

        getContentPane().setBackground(Color.WHITE);

        run = new JButton("Run");
        run.setBounds(280, 170, 80, 30);
        run.setVisible(true);
        //run.setBackground(Color.BLACK);
        //run.setForeground(Color.WHITE);
        run.addActionListener(this);
        add(run);

        clear = new JButton("Clear");
        clear.setBounds(280, 200, 100, 30);
        clear.setVisible(true);
        //clear.setBackground(Color.BLACK);
        //clear.setForeground(Color.WHITE);
        clear.addActionListener(this);
        add(clear);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals("Clear")) {
            processField.setText("");
        }
        if (ae.getActionCommand().equals("Run")) {
            String numOfProcessesStr = processField.getText();
            n = Integer.parseInt(numOfProcessesStr);

            p = new int[n];
            bt = new int[n];
            w = new int[n];
            t = new int[n];
            at = new int[n];
            rt = new int[n];

            for (int i = 0; i < n; i++) {
                bt[i] = Integer.parseInt(JOptionPane.showInputDialog("Enter burst time for Process " + (i + 1) + ":"));
                at[i] = Integer.parseInt(JOptionPane.showInputDialog("Enter arrival time for Process " + (i + 1) + ":"));
                p[i] = i + 1;
                rt[i] = bt[i];
            }

            int complete = 0, shortest = 0, min = Integer.MAX_VALUE;
            int time = 0, finish_time;
            boolean check = false;//it is boolean uswed to idicate whether the process was foud for 
                                // execution or not.

            while (complete != n) {
                for (int j = 0; j < n; j++) {
                    if ((at[j] <= time) && (rt[j] < min) && rt[j] > 0) {
                        min = rt[j]; 
                        shortest = j;
                        check = true;
                    }
                }

                if (!check) {
                    time++;
                    continue;
                }

                rt[shortest]--;

                min = rt[shortest];
                if (min == 0)
                    min = Integer.MAX_VALUE;

                if (rt[shortest] == 0) {
                    complete++;
                    check = false;
                    finish_time = time + 1;
                    w[shortest] = finish_time - bt[shortest] - at[shortest];
                    if (w[shortest] < 0)
                        w[shortest] = 0;
                }
                time++;
            }

            for (int i = 0; i < n; i++) {
                t[i] = bt[i] + w[i];
                atat += t[i];
                awt += w[i];
            }

            awt /= n;
            atat /= n;

            StringBuilder output = new StringBuilder("\nProcess \t\t Burst_Time \t Wait_Time \t Turn_Around_Time \t Arrival_Time\n");

            for (int i = 0; i < n; i++) {
                output.append("\n   ").append(p[i]).append("\t\t\t                 ").append(bt[i])
                        .append("\t\t\t\t                           ").append(w[i]).append("\t\t\t\t                     ")
                        .append(t[i]).append("\t\t\t\t                      ").append(at[i]);
            }

            output.append("\n\n Average Wait Time : ").append(awt);
            output.append("\n Average Turn Around Time : ").append(atat);

            JOptionPane.showMessageDialog(this, output.toString());

            // Draw Gantt Chart
            drawGanttChart();
        }
    }

    private void drawGanttChart() {
        JFrame ganttFrame = new JFrame("Gantt Chart");
        ganttFrame.setSize(600, 150);
        ganttFrame.setLocation(350, 450);

        JPanel ganttPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                int startX = 50;
                int startY = 50;
                int scale = 20;

                for (int i = 0; i < n; i++) {
                    g.drawRect(startX, startY, bt[i] * scale, 30);
                    g.drawString("P" + p[i], startX + 5, startY + 20);
                    startX += bt[i] * scale;
                }
            }
        };

        ganttFrame.add(ganttPanel);
        ganttFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new SRTN_GUI();
    }
}
