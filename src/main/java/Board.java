import java.util.Random;

public class Board {
    private GridSpace[][] grid;
    private int r;
    private int c;
    private int difficulty;

    public Board(int difficulty) {
        int r = makeR(difficulty);
        int c = makeC(difficulty);
        int mines = makeMines(difficulty);
        this.r = r;
        this.c = c;
        this.difficulty = difficulty;
        grid = new GridSpace[r][c];
        for (int row=0; row<r; row++) {
            for (int col = 0; col<c; col++) {
                grid[row][col] = new GridSpace(row, col,0);
            }
        }
        double a=0;
        double b=0;
        for (int i=0; i<mines; i++) {
            int row;
            int col;
            do {
                a = new Random().nextDouble();
                b = new Random().nextDouble();
                row = (int)(a*r);
                col = (int)(b*c);
            } while (grid[row][col].getId() == 9);
            setAsMine(row, col);
        }
        addNumbers();
        System.out.println(this);
    }

    private void addNumbers() {
        for (int row=0; row<r; row++) {
            for (int col=0; col<c; col++) {
                int newNumber=0;
                if (grid[row][col].getId() != 9) {
                    for (int i=-1; i<2; i++) {
                        for (int j=-1; j<2; j++) {
                            try {
                                if (grid[row + i][col + j].getId() == 9) {
                                    newNumber++;
                                }
                            }
                            catch (IndexOutOfBoundsException ignored) {}
                        }
                    }
                    grid[row][col].setId(newNumber);
                }
            }
        }
    }

    public int getDifficulty() {return difficulty;}

    private int makeR(int difficulty) {
        if (difficulty == 0) {return 9;}
        if (difficulty == 1 || difficulty == 2) {return 16;}
        return 1;
    }

    private int makeC(int difficulty) {
        if (difficulty == 0) {return 9;}
        if (difficulty == 1) {return 16;}
        if (difficulty == 2) {return 30;}
        return 1;
    }

    private int makeMines(int difficulty) {
        if (difficulty == 0) {return 10;}
        if (difficulty == 1) {return 40;}
        if (difficulty == 2) {return 99;}
        return 1;
    }

    private void setAsMine(int r, int c) {
        grid[r][c].setAsMine();
    }

    public int flagNum() {
        return makeMines(difficulty);
    }

    public GridSpace get(int r, int c) {
        return grid[r][c];
    }

    public void flagUpdate(int r, int c) {
        grid[r][c].flagUpdate();
    }

    public boolean isFlagged(int r, int c) {
        return grid[r][c].isFlagged();
    }

    public int getId(int r, int c) {
        return grid[r][c].getId();
    }

    public void uncover(int r, int c) {
        grid[r][c].uncover();
    }

    public boolean isCovered(int r, int c) {
        return grid[r][c].isCovered();
    }

    public boolean isToUncover(int r, int c) {
        return grid[r][c].getZeroToUncover();
    }

    public void queueToUncover(int r, int c) {
        grid[r][c].queueToUncover();
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        for (int row=0; row<r; row++) {
            for (int col = 0; col < c; col++) {
                out.append(grid[row][col]);
            }
            out.append("\n");
        }
        out.append("\n");
        for (int row=0; row<r; row++) {
            for (int col = 0; col < c; col++) {
                out.append(grid[row][col].getId()).append(" ");
            }
            out.append("\n");
        }
        return out.toString();
    }
}