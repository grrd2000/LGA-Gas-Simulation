package com.gerskom;

import java.awt.*;

public record PaintTaskMaker(SimulationPanel sP, int xp, int xk, int yp, int yk) implements Runnable {

    static final Object lock = new Object();

    @Override
    public void run() {
        paintTask(sP.g2D);
    }

    public void paintTask(Graphics g) {
        synchronized (lock) {
            Graphics2D g2D = (Graphics2D) g;
            for (int x = xp; x < xk; x++) {
                for (int y = yp; y < yk; y++) {
                    if (sP.tmpData[x][y] == 1)
                        g2D.setColor(SimulationPanel.particleColor);
                    else if (sP.tmpData[x][y] == -1)
                        g2D.setColor(SimulationPanel.wallColor);
                    else
                        g2D.setColor(SimulationPanel.bgColor);

                    g2D.fillRect(x, y, 1, 1);
                }
            }
            g2D.dispose();
        }
    }
}
