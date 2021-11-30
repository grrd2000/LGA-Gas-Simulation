package com.gerskom;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationPanel extends JPanel {
    public final Grid map;
    public int[][] tmpData;
    public boolean started = false;

    private static final int THREADS = 6;
    private static final int XTASKS = 6;
    private static final int YTASKS = 6;
    private final int deltaTime = 12;
    public long firstTime;
    public long secondTime;
    private double maxDiff = 0;
    private double minDiff = Integer.MAX_VALUE;
    private final ArrayList<Double> diffs = new ArrayList<>();
    private final ArrayList<Double> fps = new ArrayList<>();
    public int i = 0;
    public Graphics2D g2D;

    private static final int densityCellSize = 20;
    private static final int halfSize = densityCellSize / 2;
    private static final int colorRes = 15;
    private static final int colorPart = 255 / colorRes;

    public static final Color particleColor = new Color(170, 170, 170);
    public static final Color bgColor = new Color(70, 70, 70);
    public static final Color wallColor = new Color(50, 50, 50);

    public SimulationPanel (Grid grid) {
        super();
        this.map = grid;
        dataConversion();

        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addMouseListener(new MainInputHandler(this));
        this.addMouseMotionListener(new MainInputHandler(this));
        this.addKeyListener(new MainInputHandler(this));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2D = (Graphics2D) g.create();

        /*int taskSizeX = (int)Math.ceil((double)((map.width - 1) - 1) / XTASKS);
        int taskSizeY = (int)Math.ceil((double)((map.height - 1) - 1) / YTASKS);

        ExecutorService executor = Executors.newFixedThreadPool(THREADS);

        for(int i = 0; i < XTASKS; i++) {
            for(int j = 0; j < YTASKS; j++) {
                //new PaintTaskMaker(this, taskSizeX * i, taskSizeX * (i + 1), taskSizeY * j, taskSizeY * (j + 1)).run();
                Runnable worker = new PaintTaskMaker(this, taskSizeX * i, taskSizeX * (i + 1), taskSizeY * j, taskSizeY * (j + 1));
                executor.execute(worker);
            }
        }

        executor.shutdown();*/

        //RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //g2D.setRenderingHints(hints);

        for(int x = halfSize; x < map.width - halfSize; x += halfSize + 1) {
            for(int y = halfSize; y < map.height - halfSize; y += halfSize + 1) {
                int counter = countParticles(x - halfSize, x + halfSize, y - halfSize, y + halfSize);
                int color = counter * colorRes / colorPart;
                //System.out.println(color);
                if(color < 256)
                    g2D.setColor(new Color(color, color, color));
                else
                    g2D.setColor(Color.WHITE);
                g2D.fillRect(x, y, densityCellSize, densityCellSize);
            }
        }

        /*for(int x = 0; x < map.width; x++){
            for(int y = 0; y < map.height; y++){
                if(tmpData[x][y] == 1)
                    g2D.setColor(particleColor);
                else if(tmpData[x][y] == 0)
                    g2D.setColor(bgColor);
                else
                    g2D.setColor(wallColor);

                g2D.fillRect(x, y, 1, 1);
            }
        }*/
        g2D.dispose();
        //this.requestFocusInWindow();
    }

    public void startTheParallelGasSimulation(){
        started = true;
        Timer timer;

        int taskSizeX = (int)Math.ceil((double)((map.width - 1) - 1) / XTASKS);
        int taskSizeY = (int)Math.ceil((double)((map.height - 1) - 1) / YTASKS);

        timer = new Timer(deltaTime, e -> {
            firstTime = System.nanoTime();
            i++;

            dataConversion();
            ExecutorService executor = Executors.newFixedThreadPool(THREADS);
            for(int i = 0; i < XTASKS; i++) {
                for(int j = 0; j < YTASKS; j++) {
                    Runnable worker = new GridTaskMaker(map, taskSizeX * i, taskSizeX * (i + 1), taskSizeY * j, taskSizeY * (j + 1), 1);
                    executor.execute(worker);
                }
            }
            for(int i = 0; i < XTASKS; i++) {
                for(int j = 0; j < YTASKS; j++) {
                    Runnable worker = new GridTaskMaker(map, taskSizeX * i, taskSizeX * (i + 1), taskSizeY * j, taskSizeY * (j + 1), 2);
                    executor.execute(worker);
                }
            }
            for(int i = 0; i < XTASKS; i++) {
                for(int j = 0; j < YTASKS; j++) {
                    Runnable worker = new GridTaskMaker(map, taskSizeX * i, taskSizeX * (i + 1), taskSizeY * j, taskSizeY * (j + 1), 3);
                    executor.execute(worker);
                }
            }
            executor.shutdown();
            repaint();

            secondTime = System.nanoTime();
        });
        timer.start();
    }

    public void exportImage(String fileName) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(map.width, map.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2D = bufferedImage.createGraphics();

        for(int x = 0; x < map.width; x++){
            for(int y = 0; y < map.height; y++){
                if(tmpData[x][y] == 1)
                    g2D.setColor(particleColor);
                else if(tmpData[x][y] == -1)
                    g2D.setColor(wallColor);
                else
                    g2D.setColor(bgColor);

                g2D.fillRect(x, y, 1, 1);
            }
        }
        g2D.dispose();

        String formatName = "bmp";
        File file;

        if (map.i != 0)
            file = new File("output/" + fileName + "_" + map.i + "." + formatName);
        else
            file = new File("output/test_gas." + formatName);

        ImageIO.write(bufferedImage, formatName, file);
    }

    private void dataConversion() {
        this.tmpData = new int[map.width][map.height];
        for(int i = 0; i < map.width; i++)
            for(int j = 0; j < map.height; j++) {
                //if(map.cellTable[i][j][1][0] != -1)
                    tmpData[i][j] = 0;
            }

        for(int x = 1; x < map.width - 1; x++)
            for(int y = 1; y < map.height - 1; y++) {
                for(int i = 0; i < 2; i++)
                    for (int j = 0; j < 4; j++) {
                        if(map.cellTable[x][y][1][0] == 1)
                            tmpData[x][y - 1] = 1;
                        if(map.cellTable[x][y][1][1] == 1)
                            tmpData[x + 1][y] = 1;
                        if(map.cellTable[x][y][1][2] == 1)
                            tmpData[x][y + 1] = 1;
                        if(map.cellTable[x][y][1][3] == 1)
                            tmpData[x - 1][y] = 1;
                        if(map.wallTable[x][y] == -1)
                            tmpData[x][y] = -1;
                    }
            }
    }

    public int countParticles() {
        int counter = 0;

        for(int i = 0; i < map.width; i++)
            for(int j = 0; j < map.height; j++)
                if(tmpData[i][j] == 1)
                    counter++;

       return counter;
    }

    public int countParticles(int xp, int xk, int yp, int yk) {
        int counter = 0;

        for(int i = xp; i < xk; i++)
            for(int j = yp; j < yk; j++)
                if(tmpData[i][j] == 1)
                    counter++;

        return counter;
    }

    public void fpsCounter() {
        double diff = secondTime - firstTime;
        double fps = (1 / (diff / 1000000000D));
        if(maxDiff <= diff) maxDiff = diff;
        else if(minDiff > diff) minDiff = diff;
        System.out.println("\nbefore: " + firstTime + "\tafter: " + secondTime +
                "\ndelay: " + deltaTime + "ms" +
                "\ndiff: " + new DecimalFormat("##.###").format(diff / 1000000D) + "ms" +
                "\nFPS: " + new DecimalFormat("###.##").format(fps) +
                "\nmax diff: " + new DecimalFormat("##.###").format(maxDiff / 1000000D) + "ms" +
                "\nmin diff: " + new DecimalFormat("##.###").format(minDiff / 1000000D) + "ms");
        averageResults(diff, fps);
    }

    public void averageResults(double newDiff, double newFPS) {
        int n = 5;
        double diffAverage = 0;
        double fpsAverage = 0;
        diffs.add(newDiff);
        fps.add(newFPS);
        for(int i = 0; i < diffs.size(); i++) {
            diffAverage += diffs.get(i) / diffs.size();
            fpsAverage += fps.get(i) / fps.size();
        }
        System.out.println("average diff: " + new DecimalFormat("##.###").format(diffAverage / 1000000D)
                + "ms" + "\naverage FPS: " + new DecimalFormat("###.##").format(fpsAverage));
    }
}
