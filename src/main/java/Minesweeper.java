import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Minesweeper extends JFrame {
    public static final int EASY = 0;
    public static final int MEDIUM = 1;
    public static final int HARD = 2;
    static Board board = new Board(EASY);
    public static boolean started = true;
    static ArrayList<JLabel> spaces = new ArrayList<>();
    public static int flagsLeft = board.flagNum();
    public static JLabel flagNum;
    public static JLabel winText;
    public static JLabel failText;

    public Minesweeper() {
        super("Minesweeper");

        DrawPane drawPane = new DrawPane();
        setContentPane(drawPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1925, 1000);
        setVisible(true);
    }

    public static void zeroUncover(int y, int x) {
        int r=0, c=0, repeats;
        if (board.getId(y,x) == 0) {
            board.queueToUncover(y,x);
            switch (board.getDifficulty()) {
                case 0:
                    r=9;
                    c=9;
                    break;
                case 1:
                    r=16;
                    c=16;
                    break;
                case 2:
                    r=16;
                    c=30;
                    break;
            }
            repeats = r+c;
            int iterations=0;
            while (iterations<repeats) {
                for (int i = 0; i < r; i++) {
                    for (int j = 0; j < c; j++) {
                        if (board.isToUncover(i, j)) {
                            for (int a=-1; a<2; a++) {
                                for (int b=-1; b<2; b++) {
                                    try {
                                        if (board.getId(a + i, b + j) == 0) {
                                            board.queueToUncover(a+i,b+j);
                                        }
                                        else {
                                            board.uncover(a + i, b + j);
                                        }
                                    } catch (ArrayIndexOutOfBoundsException ignored) {}
                                }
                            }
                            board.uncover(i, j);
                        }
                    }
                }
                iterations++;
            }
            int flags = 0;
            for (int i = 0; i < r; i++) {
                for (int j = 0; j < c; j++) {
                    if (board.isCovered(i,j) && board.isFlagged(i,j)) {
                        flags++;
                    }
                }
            }
            flagsLeft = board.flagNum()-flags;
            flagNum.setText("" + flagsLeft);
        }
    }

    public static void visualUncover() {
        int r=0, c=0;
        switch (board.getDifficulty()) {
            case 0:
                r=9;
                c=9;
                break;
            case 1:
                r=16;
                c=16;
                break;
            case 2:
                r=16;
                c=30;
                break;
        }
        for (int i=0; i<r; i++) {
            for (int j=0; j<c; j++) {
                if (!board.isCovered(i,j)) {
                    spaces.get((j*r)+i).setIcon(new ImageIcon(""+board.getId(i,j)+".png"));
                }
            }
        }
    }

    public static boolean winCheck() {
        int r=0, c=0, total=0;
        switch (board.getDifficulty()) {
            case 0:
                r=9;
                c=9;
                break;
            case 1:
                r=16;
                c=16;
                break;
            case 2:
                r=16;
                c=30;
                break;
        }
        for (int i=0; i<r; i++) {
            for (int j=0; j<c; j++) {
                if (!board.isCovered(i,j)) {
                    total++;
                }
                if (board.getId(i,j)==9 && board.isFlagged(i,j)) {
                    total++;
                }
            }
        }
        System.out.println(total);
        return total >= r * c;
    }

    public static void addSideElements(JFrame frame) {
        JLabel flagCountI = new JLabel((new ImageIcon("flag.png")));
        frame.getContentPane().add(flagCountI);
        flagCountI.setBounds(1820, 100, 50, 50);

        flagNum = new JLabel("" + flagsLeft);
        frame.getContentPane().add(flagNum);
        flagNum.setFont(new Font("Serif", Font.PLAIN, 24));
        flagNum.setBounds(1830, 150, 50, 50);

        JLabel reset = new JLabel(new ImageIcon("reset.png"));
        frame.getContentPane().add(reset);
        reset.setBounds(1820,300,50,50);

        JLabel easy = new JLabel("EASY");
        frame.getContentPane().add(easy);
        easy.setFont(new Font("Serif", Font.PLAIN, 20));
        easy.setBounds(1820, 500, 100, 50);

        JLabel medium = new JLabel("MED");
        frame.getContentPane().add(medium);
        medium.setFont(new Font("Serif", Font.PLAIN, 20));
        medium.setBounds(1820, 550, 100, 50);

        JLabel hard = new JLabel("HARD");
        frame.getContentPane().add(hard);
        hard.setFont(new Font("Serif", Font.PLAIN, 20));
        hard.setBounds(1820, 600, 100, 50);

        winText = new JLabel("WIN");
        winText.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 250));
        winText.setBounds(650,200, 500, 500);

        failText = new JLabel("DEATH");
        failText.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 250));
        failText.setBounds(550,0, 1100, 800);
    }

    class DrawPane extends JPanel {
        public void paintComponent(Graphics g) {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0,0,1925,1000);
            getContentPane().setLayout(null);
            int rShift = 0, cShift = 0, r = 0, c = 0, boxTop = 0;
            switch (board.getDifficulty()) {
                case 0:
                    r = 9;
                    c = 9;
                    rShift = 650;
                    cShift = 200;
                    boxTop = 515;
                    break;
                case 1:
                    r = 16;
                    c = 16;
                    rShift = 400;
                    cShift = 5;
                    boxTop = 565;
                    break;
                case 2:
                    r = 30;
                    c = 16;
                    rShift = 5;
                    cShift = 5;
                    boxTop = 615;
                    break;
            }
            for (int row = 0; row < r; row++) {
                for (int col = 0; col < c; col++) {
                    if (spaces.size() < (r*c)) {
                        spaces.add(new JLabel(new ImageIcon("covered.png")));
                        getContentPane().add(spaces.get(spaces.size() - 1));
                        spaces.get(spaces.size() - 1).setBounds((row * 60) + rShift, (col * 60) + cShift, 50, 50);
                    }
                }
            }
            g.setColor(Color.BLACK);
            g.drawRect(1815, boxTop, 65, 25);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new Minesweeper();
        addSideElements(frame);

        frame.addMouseListener(new MouseListener() {
            public void mousePressed(MouseEvent me) { }
            public void mouseReleased(MouseEvent me) { }
            public void mouseEntered(MouseEvent me) { }
            public void mouseExited(MouseEvent me) { }
            public void mouseClicked(MouseEvent me) {
                if (me.getX() >= 1820 && me.getX() <= 1880) {
                    if (me.getY() >= 300 && me.getY() <= 380) {
                        int diff = board.getDifficulty();
                        board = null;
                        board = new Board(diff);
                        for (JLabel j : spaces) {
                            j.setIcon(new ImageIcon("covered.png"));
                        }
                        started = true;
                        flagNum.setText("" + board.flagNum());
                        flagsLeft = board.flagNum();

                        frame.getContentPane().remove(winText);
                        frame.getContentPane().remove(failText);
                        frame.getContentPane().repaint();
                    }
                    else if (me.getY() >= 545 && me.getY() <=670) {
                        board = null;

                        if (me.getY() >= 545 && me.getY() <= 570)
                            board = new Board(EASY);
                        else if (me.getY() >= 595 && me.getY() <= 620)
                            board = new Board(MEDIUM);
                        else if (me.getY() >= 615 && me.getY() <= 670)
                            board = new Board(HARD);

                        frame.getContentPane().removeAll();
                        spaces.clear();
                        addSideElements(frame);

                        int diff = board.getDifficulty();
                        board = null;
                        board = new Board(diff);
                        for (JLabel j : spaces) {
                            j.setIcon(new ImageIcon("covered.png"));
                        }

                        frame.getContentPane().remove(winText);
                        frame.getContentPane().remove(failText);
                        frame.getContentPane().repaint();

                        started = true;
                        flagNum.setText("" + board.flagNum());
                        flagsLeft = board.flagNum();
                    }
                }
                if (started) {
                    int x = 0, y = 0, xMultiply = 0;
                    switch (board.getDifficulty()) {
                        case 0:
                            x = (me.getX()-650)/60;
                            y = (me.getY()-225)/60;
                            xMultiply = 9;
                            break;
                        case 1:
                            x = (me.getX()-400)/60;
                            y = (me.getY()-30)/60;
                            xMultiply = 16;
                            break;
                        case 2:
                            x = (me.getX()-5)/60;
                            y = (me.getY()-30)/60;
                            xMultiply = 16;
                            break;
                    }
                    if (me.getButton() == MouseEvent.BUTTON3) {
                        try {
                            if (board.isCovered(y,x)) {
                                if (board.isFlagged(y,x)) {
                                    spaces.get((x * xMultiply) + y).setIcon(new ImageIcon("covered.png"));
                                    flagsLeft++;
                                }
                                else {
                                    if (flagsLeft > 0) {
                                        spaces.get((x * xMultiply) + y).setIcon(new ImageIcon("flag.png"));
                                        flagsLeft--;
                                    }
                                }
                                if (flagsLeft >= 0)
                                    board.flagUpdate(y, x);
                                System.out.println("Flag update at row " + y + ", column " + x);
                            }
                            flagNum.setText("" + flagsLeft);
                        }
                        catch (IndexOutOfBoundsException ignored) {}
                        if (winCheck()) {
                            frame.getContentPane().add(winText);
                            frame.getContentPane().setComponentZOrder(winText, 1);
                            frame.getContentPane().repaint();
                        }
                        System.out.println(winCheck());
                    }
                    else if (me.getButton() == MouseEvent.BUTTON1) {
                        try {
                            if (!board.isFlagged(y, x)) {
                                try {
                                    spaces.get((x * xMultiply) + y).setIcon(new ImageIcon("" + board.getId(y, x) + ".png"));
                                    if (board.getId(y, x) == 9) {
                                        started = false;
                                        frame.getContentPane().add(failText);
                                        frame.getContentPane().setComponentZOrder(failText, 1);
                                        frame.getContentPane().repaint();
                                    } else {
                                        spaces.get((x * xMultiply) + y).setIcon(new ImageIcon("" + board.getId(y, x) + ".png"));
                                        if (board.getId(y, x) == 0) {
                                            zeroUncover(y, x);
                                            visualUncover();
                                        }
                                        board.uncover(y, x);
                                        if (winCheck()) {
                                            frame.getContentPane().add(winText);
                                            frame.getContentPane().setComponentZOrder(winText, 1);
                                            frame.getContentPane().repaint();
                                        }
                                        System.out.println(winCheck());
                                    }
                                } catch (IndexOutOfBoundsException ignored) {}
                            }
                        }
                        catch (ArrayIndexOutOfBoundsException ignored) {}
                    }
                }
            }
        })
    ;}
}