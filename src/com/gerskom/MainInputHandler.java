package com.gerskom;

import java.awt.event.*;
import java.io.IOException;

public class MainInputHandler implements KeyListener, MouseListener, MouseMotionListener {
    SimulationPanel simulationPanel;
    PaintTaskMaker paintTaskMaker;
    private static int mouseButton = -1;

    private final float brushSize = 45f;
    private final float brushDensity = 14f;

    public MainInputHandler(SimulationPanel simulationPanel) {
        this.simulationPanel = simulationPanel;
    }

    public MainInputHandler(PaintTaskMaker paintTaskMaker) {
        this.paintTaskMaker = paintTaskMaker;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        switch (e.getKeyChar()) {
            //case '\n' -> {
            //    if (!simulationPanel.started) {
            //        System.out.println("\nSTART");
            //        simulationPanel.startTheGasSimulation(1, simulationPanel.map.width);
            //        simulationPanel.started = true;
            //    }
            //}
            case 'r' -> {
                simulationPanel.map.initRandomCells();
                simulationPanel.map.dataCopier();
                simulationPanel.repaint();
            }
            case 'f' -> {
                try {
                    simulationPanel.exportImage("lattice_gas");
                    System.out.println("\nFRAME EXPORTED");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            case 'p' -> {
                //System.out.println(Arrays.deepToString(simulationPanel.tmpData));
                System.out.println("Number of particles: " + simulationPanel.countParticles());
            }
            case 'o' -> {
                simulationPanel.fpsCounter();
            }
            //case'k' -> simulationPanel.kasienka();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = simulationPanel.getMousePosition().x;
        int y = simulationPanel.getMousePosition().y;

        switch (e.getButton()) {
            case 1 -> {
                mouseButton = 1;
                simulationPanel.map.addWallSquare(x, y, 8);
            }
            case 2 -> {
                mouseButton = 2;
                simulationPanel.map.removeSquareOfParticles(x, y, 100);
            }
            case 3 -> {
                mouseButton = 3;
                simulationPanel.map.addBrushOfParticles(x, y, brushSize, brushDensity);
            }

        }
        simulationPanel.map.dataCopier();
        simulationPanel.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
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

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = simulationPanel.getMousePosition().x;
        int y = simulationPanel.getMousePosition().y;

        switch (mouseButton) {
            case 1 -> simulationPanel.map.addWallSquare(x, y, 10);
            case 2 -> simulationPanel.map.initCell(x, y);
            case 3 -> simulationPanel.map.addBrushOfParticles(x, y, brushSize, brushDensity);

        }
        simulationPanel.map.dataCopier();
        simulationPanel.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
