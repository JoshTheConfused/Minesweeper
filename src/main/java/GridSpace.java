public class GridSpace {
    private final int r;
    private final int c;
    private int id;
    boolean flagged = false;
    private boolean isCovered = true;
    private boolean zeroToUncover = false;

    public GridSpace(int r, int c, int id) {
        this.r = r;
        this.c = c;
        this.id = id;
    }

    public void setAsMine() {
        id = 9;
    }

    public void uncover() {
        isCovered = false;
        flagged = false;
    }

    public boolean isCovered() {
        return isCovered;
    }

    public void flagUpdate() {
        if (isCovered) {
            flagged = !flagged;
        }
    }

    public boolean getZeroToUncover() {
        return zeroToUncover;
    }

    public void queueToUncover() {
        if (isCovered) {
            zeroToUncover = true;
        }
        flagged = false;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("{Row: ").append(r).append(", Column: ").append(c);
        if (id == 9) {
            out.append(" BOOM");
        }
        else {
            out.append(" ID:").append(id);
        }
        out.append("} ");
        return out.toString();
    }
}