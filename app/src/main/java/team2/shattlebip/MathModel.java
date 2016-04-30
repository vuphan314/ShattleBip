package team2.shattlebip;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Zach
 */
public class MathModel {
    private static MathModel ourInstance = new MathModel();
    private static AdapterBoard board;
    private static int x, y, cols;
    private static Random random = new Random();

    public static MathModel getInstance() {
        if (ourInstance == null)
            ourInstance = new MathModel();
        return ourInstance;
    }

    private MathModel()
    {
        x = 0;
        y = 0;
        cols = 1;
    }

    private static void setXYFromPos(int cols, int pos)
    {
        x = pos % cols;
        y = pos / cols;
    }

    private static int getPosFromXY() {
        return x * cols + y;
    }

    public static int getPosFromXY(int x, int y) {
        return x * cols + y;
    }

    //find random cell to initialize from
    private static void getEmptyCell() {
        do {
            x = random.nextInt(cols);
            y = random.nextInt(cols);
        }
        while (board.getItem(getPosFromXY()).getStatus() == Cell.Status.OCCUPIED);
    }

    //
    private static boolean isNorthValid(int size)
    {
        if (y - size < 0)
            return false;

        int source = getPosFromXY(x, y);
        for (int i = 0; i < size; i++)
            if (board.getItem(source - i).status == BoardCellStatus.OCCUPIED)
                return false;

        return true;
    }

    private static void setNorthPlacement(int size) {
        for (int i = 0; i < size; i++)
            board.getItem(getPosFromXY(x, y - i)).setStatus(Cell.Status.OCCUPIED);
    }

    private static boolean isEastValid(int size)
    {
        if (x + size > cols)
            return false;

        for (int i = 0; i < size; i++)
            if (board.getItem(getPosFromXY(x + i, y)).status == BoardCellStatus.OCCUPIED)
                return false;

        return true;
    }

    private static void setEastPlacement(int size) {
        for (int i = 0; i < size; i++)
            board.getItem(getPosFromXY(x + i, y)).setStatus(Cell.Status.OCCUPIED);
    }

    private static boolean isSouthValid(int size)
    {
        if (y + size > cols)
            return false;

        for (int i = 0; i < size; i++)
            if (board.getItem(getPosFromXY(x, y + i)).status == BoardCellStatus.OCCUPIED)
                return false;

        return true;
    }

    private static void setSouthPlacement(int size) {
        for (int i = 0; i < size; i++)
            board.getItem(getPosFromXY(x, y + i)).setStatus(Cell.Status.OCCUPIED);
    }

    private static boolean isWestValid(int size)
    {
        if (x - size < 0)
            return false;

        for (int i = 0; i < size; i++)
        {
            int nextX = x - i;
            if (nextX < 0 || board.getItem(getPosFromXY(nextX, y)).getStatus() == Cell.Status.OCCUPIED)
                return false;
        }

        return true;
    }

    private static void setWestPlacement(int size) {
        for (int i = 0; i < size; i++)
            board.getItem(getPosFromXY(x - i, y)).setStatus(Cell.Status.OCCUPIED);
    }

    //performs random sampling
    private static void setPlacement(int size) {
        boolean notPlaced = true;

        while (notPlaced) {
            List<Integer> sample = new ArrayList<>(4);
            sample.add(0);
            sample.add(1);
            sample.add(2);
            sample.add(3);

            getEmptyCell();

            while (!sample.isEmpty()) {
                int i = random.nextInt(sample.size());
                switch (sample.get(i)) {
                    case 0:
                        if (isNorthValid(size)) {
                            setNorthPlacement(size);
                            notPlaced = false;
                        } else
                            sample.remove(i);
                        break;
                    case 1:
                        if (isEastValid(size)) {
                            setEastPlacement(size);
                            notPlaced = false;
                        } else
                            sample.remove(i);
                        break;
                    case 2:
                        if (isSouthValid(size)) {
                            setSouthPlacement(size);
                            notPlaced = false;
                        } else
                            sample.remove(i);
                        break;
                    case 3:
                        if (isWestValid(size)) {
                            setWestPlacement(size);
                            notPlaced = false;
                        } else
                            sample.remove(i);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    //the main public function. this places ships in available squares.
    public static void generateShipPlacement(AdapterBoard adapterBoard, int dim) {
        board = adapterBoard;
        cols = dim;

        setPlacement(2); //small
        setPlacement(3); //medium
        setPlacement(5); //large
        //testPlacement();

        board.notifyDataSetChanged();
    }

    private static void testPlacement()
    {
        getEmptyCell();
        //x = 0;
        //y = 0;
        if (isEastValid(5))
        {
            setEastPlacement(5);
        }
        if (isWestValid(5))
        {
            setWestPlacement(5);
        }

    }
}
