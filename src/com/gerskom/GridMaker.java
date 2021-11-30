package com.gerskom;

import java.util.Random;

public record GridMaker(Grid map, int x, int y, int opt) implements Runnable {

    @Override
    public void run() {
        switch (opt){
            case 1 -> inOperation();
            case 2 -> dataCopier();
            case 3 -> outOperation();
        }
    }

    public void inOperation() {
        map.cellTable[x][y][0][0] = map.cellTable[x][y - 1][1][2];
        map.cellTable[x][y][0][1] = map.cellTable[x + 1][y][1][3];
        map.cellTable[x][y][0][2] = map.cellTable[x][y + 1][1][0];
        map.cellTable[x][y][0][3] = map.cellTable[x - 1][y][1][1];
    }

    public void outOperation() {
        Random random = new Random();
        int r = random.nextInt(100);

        map.cellTable[x][y][1][0] = 0;
        map.cellTable[x][y][1][1] = 0;
        map.cellTable[x][y][1][2] = 0;
        map.cellTable[x][y][1][3] = 0;

        if (map.tmpCellTable[x][y][0][1] == 1 && map.tmpCellTable[x][y][0][3] == 1 && map.tmpCellTable[x][y][0][0] == 1 && map.tmpCellTable[x][y][0][2] == 1) {
            map.cellTable[x][y][1][0] = 1;
            map.cellTable[x][y][1][1] = 1;
            map.cellTable[x][y][1][2] = 1;
            map.cellTable[x][y][1][3] = 1;
        } else if (map.tmpCellTable[x][y][0][1] == 1 && map.tmpCellTable[x][y][0][3] == 1 && map.tmpCellTable[x][y][0][0] == 1) {
            map.cellTable[x][y][1][0] = 1;
            map.cellTable[x][y][1][2] = 1;
            if (r < 50) map.cellTable[x][y][1][1] = 1;
            else map.cellTable[x][y][1][3] = 1;
        } else if (map.tmpCellTable[x][y][0][3] == 1 && map.tmpCellTable[x][y][0][0] == 1 && map.tmpCellTable[x][y][0][2] == 1) {
            map.cellTable[x][y][1][1] = 1;
            map.cellTable[x][y][1][3] = 1;
            if (r < 50) map.cellTable[x][y][1][2] = 1;
            else map.cellTable[x][y][1][0] = 1;
        } else if (map.tmpCellTable[x][y][0][1] == 1 && map.tmpCellTable[x][y][0][3] == 1 && map.tmpCellTable[x][y][0][2] == 1) {
            map.cellTable[x][y][1][0] = 1;
            map.cellTable[x][y][1][2] = 1;
            if (r < 50) map.cellTable[x][y][1][3] = 1;
            else map.cellTable[x][y][1][1] = 1;
        } else if (map.tmpCellTable[x][y][0][0] == 1 && map.tmpCellTable[x][y][0][2] == 1 && map.tmpCellTable[x][y][0][1] == 1) {
            map.cellTable[x][y][1][1] = 1;
            map.cellTable[x][y][1][3] = 1;
            if (r < 50) map.cellTable[x][y][1][0] = 1;
            else map.cellTable[x][y][1][2] = 1;
        } else if (map.tmpCellTable[x][y][0][1] == 1 && map.tmpCellTable[x][y][0][3] == 1) {
            map.cellTable[x][y][1][0] = 1;
            map.cellTable[x][y][1][2] = 1;
        } else if (map.tmpCellTable[x][y][0][0] == 1 && map.tmpCellTable[x][y][0][2] == 1) {
            map.cellTable[x][y][1][1] = 1;
            map.cellTable[x][y][1][3] = 1;
        } else if (map.tmpCellTable[x][y][0][0] == 1 && map.tmpCellTable[x][y][0][1] == 1) {
            map.cellTable[x][y][1][2] = 1;
            map.cellTable[x][y][1][3] = 1;
        } else if (map.tmpCellTable[x][y][0][1] == 1 && map.tmpCellTable[x][y][0][2] == 1) {
            map.cellTable[x][y][1][3] = 1;
            map.cellTable[x][y][1][0] = 1;
        } else if (map.tmpCellTable[x][y][0][2] == 1 && map.tmpCellTable[x][y][0][3] == 1) {
            map.cellTable[x][y][1][0] = 1;
            map.cellTable[x][y][1][1] = 1;
        } else if (map.tmpCellTable[x][y][0][3] == 1 && map.tmpCellTable[x][y][0][0] == 1) {
            map.cellTable[x][y][1][1] = 1;
            map.cellTable[x][y][1][2] = 1;
        } else if (map.tmpCellTable[x][y][0][0] == 1) map.cellTable[x][y][1][2] = 1;
        else if (map.tmpCellTable[x][y][0][1] == 1) map.cellTable[x][y][1][3] = 1;
        else if (map.tmpCellTable[x][y][0][2] == 1) map.cellTable[x][y][1][0] = 1;
        else if (map.tmpCellTable[x][y][0][3] == 1) map.cellTable[x][y][1][1] = 1;

        if (map.cellTable[x][y][1][0] == 1 && map.wallTable[x][y - 1] == Grid.wall) {
            map.cellTable[x][y][1][0] = 0;
            map.cellTable[x][y][1][2] = 1;
        } else if (map.cellTable[x][y][1][1] == 1 && map.wallTable[x + 1][y] == Grid.wall) {
            map.cellTable[x][y][1][1] = 0;
            map.cellTable[x][y][1][3] = 1;
        } else if (map.cellTable[x][y][1][2] == 1 && map.wallTable[x][y + 1] == Grid.wall) {
            map.cellTable[x][y][1][2] = 0;
            map.cellTable[x][y][1][0] = 1;
        } else if (map.cellTable[x][y][1][3] == 1 && map.wallTable[x - 1][y] == Grid.wall) {
            map.cellTable[x][y][1][3] = 0;
            map.cellTable[x][y][1][1] = 1;
        }
    }

    public void dataCopier() {
        for (int i = 0; i < 2; i++) {
            System.arraycopy(map.cellTable[x][y][i], 0, map.tmpCellTable[x][y][i], 0, 4);
        }
    }
}
