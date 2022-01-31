package com.gerskom;

import java.util.Random;

public class Grid {
    public int width;
    public int height;
    public int nMax = 50000;

    private final int borderSize = 10;
    public int i = 0;

    public int[][][][] cellTable;
    public int[][][][] tmpCellTable;
    private final float density = 20f;
    public int[][] wallTable;
    public final static int wall = -1;

    Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.cellTable = new int[width][height][2][4];
        initCellsOfSimArea();
        this.tmpCellTable = new int[width][height][2][4];
        dataCopier();
        this.wallTable = new int[width][height];
        initComputeArea();
    }

    public void startIterations() {
        inOperation();
        dataCopier();
        outOperation();
        i++;
    }

    public void startIterations(int x, int y) {
        inOperation(x, y);
        dataCopier(x, y);
        outOperation(x, y);
        i++;
    }

    public void inOperation() {
        for(int x = 1; x < width - 1; x++)
            for(int y = 1; y < height - 1; y++) {
                cellTable[x][y][0][0] = cellTable[x][y - 1][1][2];
                cellTable[x][y][0][1] = cellTable[x + 1][y][1][3];
                cellTable[x][y][0][2] = cellTable[x][y + 1][1][0];
                cellTable[x][y][0][3] = cellTable[x - 1][y][1][1];
            }
    }

    public void outOperation() {
        for(int x = 1; x < width - 1; x++)
            for(int y = 1; y < height - 1; y++) {
                Random random = new Random();
                int r = random.nextInt(100);

                cellTable[x][y][1][0] = 0;
                cellTable[x][y][1][1] = 0;
                cellTable[x][y][1][2] = 0;
                cellTable[x][y][1][3] = 0;

                if(tmpCellTable[x][y][0][1] == 1 && tmpCellTable[x][y][0][3] == 1 && tmpCellTable[x][y][0][0] == 1 && tmpCellTable[x][y][0][2] == 1) {
                    cellTable[x][y][1][0] = 1;
                    cellTable[x][y][1][1] = 1;
                    cellTable[x][y][1][2] = 1;
                    cellTable[x][y][1][3] = 1;
                }
                else if(tmpCellTable[x][y][0][1] == 1 && tmpCellTable[x][y][0][3] == 1 && tmpCellTable[x][y][0][0] == 1) {
                    cellTable[x][y][1][0] = 1;
                    cellTable[x][y][1][2] = 1;
                    if(r < 50)  cellTable[x][y][1][1] = 1;
                    else        cellTable[x][y][1][3] = 1;
                }
                else if(tmpCellTable[x][y][0][3] == 1 && tmpCellTable[x][y][0][0] == 1 && tmpCellTable[x][y][0][2] == 1) {
                    cellTable[x][y][1][1] = 1;
                    cellTable[x][y][1][3] = 1;
                    if(r < 50)  cellTable[x][y][1][2] = 1;
                    else        cellTable[x][y][1][0] = 1;
                }
                else if(tmpCellTable[x][y][0][1] == 1 && tmpCellTable[x][y][0][3] == 1 && tmpCellTable[x][y][0][2] == 1) {
                    cellTable[x][y][1][0] = 1;
                    cellTable[x][y][1][2] = 1;
                    if(r < 50)  cellTable[x][y][1][3] = 1;
                    else        cellTable[x][y][1][1] = 1;
                }
                else if(tmpCellTable[x][y][0][0] == 1 && tmpCellTable[x][y][0][2] == 1 && tmpCellTable[x][y][0][1] == 1) {
                    cellTable[x][y][1][1] = 1;
                    cellTable[x][y][1][3] = 1;
                    if(r < 50)  cellTable[x][y][1][0] = 1;
                    else        cellTable[x][y][1][2] = 1;
                }
                else if(tmpCellTable[x][y][0][1] == 1 && tmpCellTable[x][y][0][3] == 1) {
                    cellTable[x][y][1][0] = 1;
                    cellTable[x][y][1][2] = 1;
                }
                else if(tmpCellTable[x][y][0][0] == 1 && tmpCellTable[x][y][0][2] == 1)  {
                    cellTable[x][y][1][1] = 1;
                    cellTable[x][y][1][3] = 1;
                }
                else if(tmpCellTable[x][y][0][0] == 1 && tmpCellTable[x][y][0][1] == 1)  {
                    cellTable[x][y][1][2] = 1;
                    cellTable[x][y][1][3] = 1;
                }
                else if(tmpCellTable[x][y][0][1] == 1 && tmpCellTable[x][y][0][2] == 1)  {
                    cellTable[x][y][1][3] = 1;
                    cellTable[x][y][1][0] = 1;
                }
                else if(tmpCellTable[x][y][0][2] == 1 && tmpCellTable[x][y][0][3] == 1)  {
                    cellTable[x][y][1][0] = 1;
                    cellTable[x][y][1][1] = 1;
                }
                else if(tmpCellTable[x][y][0][3] == 1 && tmpCellTable[x][y][0][0] == 1)  {
                    cellTable[x][y][1][1] = 1;
                    cellTable[x][y][1][2] = 1;
                }
                else if(tmpCellTable[x][y][0][0] == 1) cellTable[x][y][1][2] = 1;
                else if(tmpCellTable[x][y][0][1] == 1) cellTable[x][y][1][3] = 1;
                else if(tmpCellTable[x][y][0][2] == 1) cellTable[x][y][1][0] = 1;
                else if(tmpCellTable[x][y][0][3] == 1) cellTable[x][y][1][1] = 1;

                if(cellTable[x][y][1][0] == 1 && wallTable[x][y - 1] == wall)  {
                    cellTable[x][y][1][0] = 0;
                    cellTable[x][y][1][2] = 1;
                }
                else if(cellTable[x][y][1][1] == 1 && wallTable[x + 1][y] == wall) {
                    cellTable[x][y][1][1] = 0;
                    cellTable[x][y][1][3] = 1;
                }
                else if(cellTable[x][y][1][2] == 1 && wallTable[x][y + 1] == wall)  {
                    cellTable[x][y][1][2] = 0;
                    cellTable[x][y][1][0] = 1;
                }
                else if(cellTable[x][y][1][3] == 1 && wallTable[x - 1][y] == wall)  {
                    cellTable[x][y][1][3] = 0;
                    cellTable[x][y][1][1] = 1;
                }
            }
    }

    public void inOperation(int x, int y) {
        cellTable[x][y][0][0] = cellTable[x][y - 1][1][2];
        cellTable[x][y][0][1] = cellTable[x + 1][y][1][3];
        cellTable[x][y][0][2] = cellTable[x][y + 1][1][0];
        cellTable[x][y][0][3] = cellTable[x - 1][y][1][1];
    }

    public void outOperation(int x, int y) {
        Random random = new Random();
        int r = random.nextInt(100);

        cellTable[x][y][1][0] = 0;
        cellTable[x][y][1][1] = 0;
        cellTable[x][y][1][2] = 0;
        cellTable[x][y][1][3] = 0;

        if(tmpCellTable[x][y][0][1] == 1 && tmpCellTable[x][y][0][3] == 1 && tmpCellTable[x][y][0][0] == 1 && tmpCellTable[x][y][0][2] == 1) {
            cellTable[x][y][1][0] = 1;
            cellTable[x][y][1][1] = 1;
            cellTable[x][y][1][2] = 1;
            cellTable[x][y][1][3] = 1;
        }
        else if(tmpCellTable[x][y][0][1] == 1 && tmpCellTable[x][y][0][3] == 1 && tmpCellTable[x][y][0][0] == 1) {
            cellTable[x][y][1][0] = 1;
            cellTable[x][y][1][2] = 1;
            if(r < 50)  cellTable[x][y][1][1] = 1;
            else        cellTable[x][y][1][3] = 1;
        }
        else if(tmpCellTable[x][y][0][3] == 1 && tmpCellTable[x][y][0][0] == 1 && tmpCellTable[x][y][0][2] == 1) {
            cellTable[x][y][1][1] = 1;
            cellTable[x][y][1][3] = 1;
            if(r < 50)  cellTable[x][y][1][2] = 1;
            else        cellTable[x][y][1][0] = 1;
        }
        else if(tmpCellTable[x][y][0][1] == 1 && tmpCellTable[x][y][0][3] == 1 && tmpCellTable[x][y][0][2] == 1) {
            cellTable[x][y][1][0] = 1;
            cellTable[x][y][1][2] = 1;
            if(r < 50)  cellTable[x][y][1][3] = 1;
            else        cellTable[x][y][1][1] = 1;
        }
        else if(tmpCellTable[x][y][0][0] == 1 && tmpCellTable[x][y][0][2] == 1 && tmpCellTable[x][y][0][1] == 1) {
            cellTable[x][y][1][1] = 1;
            cellTable[x][y][1][3] = 1;
            if(r < 50)  cellTable[x][y][1][0] = 1;
            else        cellTable[x][y][1][2] = 1;
        }
        else if(tmpCellTable[x][y][0][1] == 1 && tmpCellTable[x][y][0][3] == 1) {
            cellTable[x][y][1][0] = 1;
            cellTable[x][y][1][2] = 1;
        }
        else if(tmpCellTable[x][y][0][0] == 1 && tmpCellTable[x][y][0][2] == 1)  {
            cellTable[x][y][1][1] = 1;
            cellTable[x][y][1][3] = 1;
        }
        else if(tmpCellTable[x][y][0][0] == 1 && tmpCellTable[x][y][0][1] == 1)  {
            cellTable[x][y][1][2] = 1;
            cellTable[x][y][1][3] = 1;
        }
        else if(tmpCellTable[x][y][0][1] == 1 && tmpCellTable[x][y][0][2] == 1)  {
            cellTable[x][y][1][3] = 1;
            cellTable[x][y][1][0] = 1;
        }
        else if(tmpCellTable[x][y][0][2] == 1 && tmpCellTable[x][y][0][3] == 1)  {
            cellTable[x][y][1][0] = 1;
            cellTable[x][y][1][1] = 1;
        }
        else if(tmpCellTable[x][y][0][3] == 1 && tmpCellTable[x][y][0][0] == 1)  {
            cellTable[x][y][1][1] = 1;
            cellTable[x][y][1][2] = 1;
        }
        else if(tmpCellTable[x][y][0][0] == 1) cellTable[x][y][1][2] = 1;
        else if(tmpCellTable[x][y][0][1] == 1) cellTable[x][y][1][3] = 1;
        else if(tmpCellTable[x][y][0][2] == 1) cellTable[x][y][1][0] = 1;
        else if(tmpCellTable[x][y][0][3] == 1) cellTable[x][y][1][1] = 1;

        if(cellTable[x][y][1][0] == 1 && wallTable[x][y - 1] == wall)  {
            cellTable[x][y][1][0] = 0;
            cellTable[x][y][1][2] = 1;
        }
        else if(cellTable[x][y][1][1] == 1 && wallTable[x + 1][y] == wall) {
            cellTable[x][y][1][1] = 0;
            cellTable[x][y][1][3] = 1;
        }
        else if(cellTable[x][y][1][2] == 1 && wallTable[x][y + 1] == wall)  {
            cellTable[x][y][1][2] = 0;
            cellTable[x][y][1][0] = 1;
        }
        else if(cellTable[x][y][1][3] == 1 && wallTable[x - 1][y] == wall)  {
            cellTable[x][y][1][3] = 0;
            cellTable[x][y][1][1] = 1;
        }
    }

    public void initCell(int xCor, int yCor) {
        if(xCor > borderSize && xCor < width - borderSize && yCor > borderSize && yCor < height - borderSize) {
            Random ran1 = new Random();
            float p1 = 100 * ran1.nextFloat();
            if (density >= p1) cellTable[xCor][yCor][1][0] = 1;

            Random ran2 = new Random();
            float p2 = 100 * ran2.nextFloat();
            if (density >= p2) cellTable[xCor][yCor][1][1] = 1;

            Random ran3 = new Random();
            float p3 = 100 * ran3.nextFloat();
            if (density >= p3) cellTable[xCor][yCor][1][2] = 1;

            Random ran4 = new Random();
            float p4 = 100 * ran4.nextFloat();
            if (density >= p4) cellTable[xCor][yCor][1][3] = 1;
        }
    }

    public void initCell(int xCor, int yCor, int dir) {
        if(wallTable[xCor][yCor] != wall)
            cellTable[xCor][yCor][1][dir] = 1;
    }

    public void initWallCell(int xCor, int yCor) {
        wallTable[xCor][yCor] = wall;
    }

    public void addWallSquare(int xCor, int yCor, int size) {
        for(int x = xCor - (size / 2); x < xCor + (size / 2); x++)
            for(int y = yCor - (size / 2); y < yCor + (size / 2); y++) {
                removeParticle(x, y);
                initWallCell(x, y);
            }
    }

    public void addBrushOfParticles(int xCor, int yCor, float size, float density) {
        for (double r = 4; r <= size / 2; r += 1.5) {
            for (double a = 0; a < 2 * Math.PI; a += 0.05) {
                Random random = new Random();
                float rand = 100 * random.nextFloat();

                int x = (int)(r * Math.cos(a));
                int y = (int)(r * Math.sin(a));

                if (density >= rand)
                    if ((x + xCor) >= 0 && (x + xCor) <= width && (y + yCor) >= 0 && (y + yCor) <= height)
                        initCell(x + xCor, y + yCor);
            }
        }
    }

    private void removeParticle(int xCor, int yCor) {
        if(xCor > 0 && xCor < width && yCor > 0 && yCor < height) {
            if (wallTable[xCor][yCor] != wall) {
                cellTable[xCor][yCor][1][0] = 0;
                cellTable[xCor][yCor][1][1] = 0;
                cellTable[xCor][yCor][1][2] = 0;
                cellTable[xCor][yCor][1][3] = 0;
            }
        }
    }

    public void removeSquareOfParticles(int xCor, int yCor, int size) {
        for(int x = xCor - (size / 2); x < xCor + (size / 2); x++)
            for(int y = yCor - (size / 2); y < yCor + (size / 2); y++)
                removeParticle(x, y);
    }

    public void initRandomCells() {
        for(int c = 0; c < nMax; c++) {
            Random ran = new Random();
            int x = ran.nextInt(width - 10) + 5;
            int y = ran.nextInt(height - 10) + 5;
            initCell(x, y);
        }
    }

    public void initRandomCells(int nMax) {
        for(int c = 0; c < nMax; c++) {
            Random ran = new Random();
            int x = ran.nextInt(width - 10) + 5;
            int y = ran.nextInt(height - 10) + 5;
            initCell(x, y);
        }
    }

    private void initComputeArea() {
        for(int x = 0; x < width; x++)
            for(int y = 0; y < height; y++) {
                if(x < borderSize || x >= width - borderSize || y < borderSize || y >= height - borderSize) {
                    //|| (x > 50 && x < 65) && (y < height / 2 - 20 && y > height / 2 + 20))
                    initWallCell(x, y);
                }
            }
        dataCopier();
    }

    private void initCellsOfSimArea() {
        for(int x = 0; x < width; x++)
            for(int y = 0; y < height; y++) {
                for(int i = 0; i < 2; i++)
                    for (int j = 0; j < 4; j++)
                        cellTable[x][y][i][j] = 0;
            }
    }

    public void dataCopier() {
        for(int x = 0; x < width; x++)
            for(int y = 0; y < height; y++) {
                for(int i = 0; i < 2; i++)
                    System.arraycopy(cellTable[x][y][i], 0, tmpCellTable[x][y][i], 0, 4);
            }
    }

    public void dataCopier(int xCor, int yCor) {
        for(int i = 0; i < 2; i++) {
            System.arraycopy(cellTable[xCor][yCor][i], 0, tmpCellTable[xCor][yCor][i], 0, 4);
        }
    }
}