
import javafx.util.Pair;

import java.io.*;
import java.util.*;

/**
 * Created by nafee on 10/5/17.
 */


class Move
{
    static final int LEFT = 1;
    static final int RIGHT = 2;
    static final int UP = 3;
    static final int DOWN = 4;
    static final int ROW = 5;
    static final int COL = 6;



    int rowOrCol;
    int rowColNum;
    int typ;

    public Move(int rowOrCol, int rowColNum, int typ)
    {
        this.rowOrCol = rowOrCol;
        this.rowColNum = rowColNum;
        this.typ = typ;
    }
}




class Grid implements Comparable<Grid>
{



    int size;
    int ar[][];


    Grid getChild( Move move )
    {
        int rowOrCol = move.rowOrCol;
        int rowColNum = move.rowColNum;
        int typ = move.typ;
        Grid ret = new Grid(this.size, this.ar);

        if ( rowOrCol == Move.ROW )
        {
            if ( typ == Move.LEFT )
            {
                int save = ret.ar[rowColNum][0];
                for (int i = 0; i < size-1; i++)
                {
                    ret.ar[rowColNum][i] = ret.ar[rowColNum][i+1];
                }
                ret.ar[rowColNum][size-1] = save;
            }
            else if ( typ == Move.RIGHT )
            {
                int save = ret.ar[rowColNum][size-1];
                for (int i = size-1; i > 0; i--)
                {
                    ret.ar[rowColNum][i] = ret.ar[rowColNum][i-1];
                }
                ret.ar[rowColNum][0] = save;
            }
        }
        else if ( rowOrCol == Move.COL )
        {
            if (typ==Move.UP)
            {
                int save = ret.ar[0][rowColNum];
                for (int i = 0; i < size-1; i++)
                {
                    ret.ar[i][rowColNum] = ret.ar[i+1][rowColNum];
                }
                ret.ar[size-1][rowColNum] = save;
            }
            else if ( typ==Move.DOWN )
            {
                int save = ret.ar[size-1][rowColNum];
                for (int i = size-1; i > 0; i--)
                {
                    ret.ar[i][rowColNum] = ret.ar[i-1][rowColNum];
                }
                ret.ar[0][rowColNum] = save;
            }
        }

        return ret;
    }


    ArrayList<Pair<Grid, Move>> getAllChildren(  )
    {
        ArrayList<Pair<Grid, Move>> ret = new ArrayList<Pair<Grid, Move>>();

        for ( int i = 0; i < this.size; i++ )
        {
            Move move;
            Grid child;

            move = new Move(Move.ROW, i, Move.LEFT);
            child = this.getChild( move );
            ret.add( new Pair<Grid, Move>(child, move) );

            move = new Move(Move.ROW, i, Move.RIGHT);
            child = this.getChild( move );
            ret.add( new Pair<Grid, Move>(child, move) );

            move = new Move(Move.COL, i, Move.UP);
            child = this.getChild( move );
            ret.add( new Pair<Grid, Move>(child, move) );

            move = new Move(Move.COL, i, Move.DOWN);
            child = this.getChild( move );
            ret.add( new Pair<Grid, Move>(child, move) );





//            ret.add( this.getChild( new Move(Move.ROW, i, Move.RIGHT) ) );
//            ret.add( this.getChild( new Move(Move.COL, i, Move.UP) ) );
//            ret.add( this.getChild( new Move(Move.COL, i, Move.DOWN) ) );

        }

        return ret;
    }




    public Grid(int size, int[][] ar) {
        this.size = size;
        this.ar = new int[size][size];
        for (int i = 0; i < ar.length; i++ )
        {
            for ( int j = 0; j < ar[i].length; j++ )
            {
                this.ar[i][j] = ar[i][j];
            }
        }
    }



    boolean isRowAligned()
    {
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size-1; j++)
            {
                if ( ar[i][j] != ar[i][j+1] )
                {
                    return  false;
                }
            }
        }
        return true;
    }

    boolean isColumnAligned()
    {
        for (int i = 0; i < size-1; i++)
        {
            for (int j = 0; j < size; j++)
            {
                if ( ar[i][j] != ar[i+1][j] )
                {
                    return  false;
                }
            }
        }
        return true;
    }

    int getManhatanDistance(Grid goalGrid)
    {
        int ret = 0;
        if ( size != goalGrid.size )
        {
            System.out.println( "size doesn't match in getManhattanDistance" );
            System.exit(0);
        }


        for (int i = 0; i < size; i++ )
        {
            for (int j = 0; j < size; j++)
            {
                int addee = Integer.MAX_VALUE;

                for (int k = 0; k < size; k++)
                {
                    if ( this.ar[k][j] == goalGrid.ar[k][j] )
                    {
                        addee = Integer.min( addee, Math.abs( k-i ) );
                    }

                    if ( this.ar[i][k] == goalGrid.ar[i][k] )
                    {
                        addee = Integer.min( addee, Math.abs( k-j ) );
                    }
                }

                ret += addee;
            }
        }

        return ret;
    }

    double getHeuristics( Grid goalGrid )
    {
        int manDis = this.getManhatanDistance(goalGrid);
        double ret = manDis / 1.0 * this.size;
        return ret;
    }


    @Override
    public int hashCode()
    {
        return Arrays.deepHashCode( ar );
    }

    @Override
    public String toString() {
        return "Grid{" +
                "size=" + size +
                ", ar=" + Arrays.deepToString(ar) +
                '}';
    }

    @Override
    public int compareTo(Grid o) {
        if ( this.size == o.size )
        {
            for (int i = 0; i < size; i++)
            {
                for (int j = 0; j < size; j++)
                {
                    if ( this.ar[i][j] != o.ar[i][j] )
                    {
                        return this.ar[i][j]-o.ar[i][j];
                    }
                }
            }
        }
        return this.size-o.size;
    }

    //    @Override
//    public String toString() {
//        String ret = "";
//        ret += "size + " + size + "\n";
//
//        for ( int a[] : ar )
//        {
//            for (int j : a)
//            {
////                System.out.print(j + " ");
//                ret += j + " ";
//            }
//            ret += "\n";
//        }
//        return  ret;
//
//    }
}


class Node implements Comparable<Node>
{
    double fVal;
    Grid grid;
    Node(){}

    public Node(double fVal, Grid grid) {
        this.fVal = fVal;
        this.grid = grid;
    }

    @Override
    public int compareTo(Node o) {
        if ( this.fVal == o.fVal )
        {
            return this.grid.compareTo( o.grid );
        }

        if ( this.fVal < o.fVal )
        {
            return -1;
        }
        else if ( this.fVal > o.fVal )
        {
            return 1;
        }

        return 0;
    }
}


class Solver
{



    int size;
    Grid initialGrid;
    Grid goalGrid;
    static Scanner scanner = new Scanner(System.in);

    int aStar()
    {
//        Hashtable<Grid, Integer> closedTable = new Hashtable<Grid, Integer>();

//        Hashtable<Grid, Integer> openTable = new Hashtable<Grid, Integer>();
//        openTable.put(initialGrid, 1);

        PriorityQueue<Node> priorityQueue = new PriorityQueue<Node>();
        priorityQueue.add( new Node( -initialGrid.getHeuristics(goalGrid), initialGrid ) );



        Hashtable<Grid, Move> cameFrom = new Hashtable<Grid, Move>();

        Hashtable<Grid, Integer> gScore = new Hashtable<Grid, Integer>();
        gScore.put(initialGrid, 0);

        Hashtable<Grid, Double> fScore = new Hashtable<Grid, Double>();
        fScore.put( initialGrid, initialGrid.getHeuristics(goalGrid) );

        while ( priorityQueue.size() > 0 )
        {
            Node currentNode = priorityQueue.poll();

            Grid currentGrid = currentNode.grid;
            double currentFScore = -currentNode.fVal;

            int currentGScore = gScore.get( currentGrid );

            if ( currentFScore > fScore.get( currentGrid ) )
            {
                continue;
            }

            if ( currentGrid.getHeuristics( goalGrid ) == 0 )
            {
                System.out.println(" Total move needed = " + gScore.get(currentGrid) );
                return gScore.get(currentGrid);
            }


            ArrayList<Pair<Grid,Move>> NeighborAr = currentGrid.getAllChildren();
            for ( Pair<Grid,Move> pair : NeighborAr )
            {
                Grid child = pair.getKey();
                Move move = pair.getValue();
                int tentativeGScore = currentGScore+1;
                double tentativeFScore = tentativeGScore + child.getHeuristics(goalGrid);
                if ( gScore.containsKey(child) && gScore.get(child)<=tentativeGScore )
                {
                    continue;
                }

                cameFrom.put(child, move);
                gScore.put(child, tentativeGScore);
                fScore.put( child, tentativeFScore );

            }
        }

        return -1;

    }




    public Solver() {}

    void takeInput()
    {
        System.out.println("In takeInput ");
//        Scanner scanner = new Scanner(System.in);
//
//
//        System.out.println("new scanner created ");
        size = scanner.nextInt();

        System.out.println("size = " + size);




        if (size == 0)
        {
            System.out.println("size = 0");
            System.out.println("Terminating the program");
            System.exit( 0 );
        }


        int[][] grid = new int[size][size];

        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                grid[i][j] = scanner.nextInt();
                grid[i][j]--;
            }
        }
        initialGrid = new Grid(size, grid);

        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                grid[i][j] = scanner.nextInt();
                grid[i][j]--;
            }
        }
        goalGrid = new Grid(size, grid);

    }

    void printInput()
    {
        System.out.println( "size = " + size );


        System.out.println( "initial Grid" );
        System.out.println(initialGrid.toString());


        System.out.println("Goal grid");
        System.out.println( goalGrid.toString() );
    }




    String solve()
    {
        System.out.println( "In solver" );
        takeInput();
        printInput();

        int ans = aStar();


        String ret = "" + ans;
        return  ret;
    }
}


public class Main {

    static void redirectIO()
    {
        String inputFileName = "input.txt";
        String outputFileName = "output.txt";


        try
        {
            System.setIn(new FileInputStream(new File("input.txt")));
        }
        catch (Exception e)
        {
            System.out.println("Can't redirect input to " + inputFileName);
            System.exit(0);
        }



//        try
//        {
//            System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(outputFileName))));
//        }
//        catch (Exception e)
//        {
//            System.out.println("Can't redirect output to " + outputFileName);
//            System.exit(0);
//        }
    }


    public static void main(String[] args) {
        redirectIO();

        while (true)
        {
            Solver solver = new Solver();

            String toPrint = solver.solve();
            System.out.println(toPrint);
        }


    }



}
