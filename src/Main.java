
import javafx.util.Pair;

import java.io.*;
import java.lang.reflect.Array;
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

    @Override
    public String toString() {
        return "Move{" +
                "rowOrCol=" + rowOrCol +
                ", rowColNum=" + rowColNum +
                ", typ=" + typ +
                '}';
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

//        System.out.println( "in getManHatanDistance" );
//        System.out.println( "this = " + this );
//        System.out.println("goalGrid = " + goalGrid);

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
                int addee = size/2;

                for (int k = 0; k < size; k++)
                {
                    if ( this.ar[i][j] == goalGrid.ar[k][j] )
                    {
                        int dis = Math.abs( k-i );
                        dis = Integer.min(dis, size-dis);
                        addee = Integer.min( addee, dis );
                    }

                    if ( this.ar[i][j] == goalGrid.ar[i][k] )
                    {
                        int dis = Math.abs( k-j );
                        dis = Integer.min(dis, size-dis);
                        addee = Integer.min( addee, dis );
                    }
                }

                if ( addee >= Integer.MAX_VALUE )
                {
                    System.out.println("addee " + addee);
                    System.out.println("i = " + i + ", j= " + j);

                    System.out.println( " this.ar[i][j] = " + this.ar[i][j] );

                    for (int k = 0; k < size; k++)
                    {

                        System.out.println("k = " + k + ",j = " + j );
                        System.out.println( " this.ar[k][j] = " + this.ar[k][j] );
                        System.out.println("goalGrid.ar[k][j] =  " + goalGrid.ar[k][j] );
                        if ( this.ar[i][j] == goalGrid.ar[k][j] )
                        {
                            System.out.println("k = " + k + "j = " + j);
//                            addee = Integer.min( addee, Math.abs( k-i ) );
                        }

                        if ( this.ar[i][j] == goalGrid.ar[i][k] )
                        {
//                            addee = Integer.min( addee, Math.abs( k-j ) );
                        }
                    }


                    System.exit(0);
                }

                ret += addee;
            }
        }

        if ( ret < 0 )
        {
            System.out.println(" getManhatanDistance trying to return negative value ");
            System.out.println("ret =  " + ret);
            System.exit(0);
        }

        return ret;
    }

    double getHeuristics( Grid goalGrid )
    {
        int manDis = this.getManhatanDistance(goalGrid);
        double ret = manDis / ( 1.0 * this.size);
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


class Node  implements Comparable<Node>
{
    double fVal;
    Grid grid;
    Node(){}

    public Node(double fVal, Grid grid) {
        if ( fVal < 0 )
        {
            System.out.println("fVal < 0");
            System.exit(0);
        }
        this.fVal = fVal;
        this.grid = grid;
    }

    @Override
    public int compareTo(Node o) {

        if ( Double.compare(this.fVal, o.fVal) == 0 )
        {
            return this.grid.compareTo( o.grid );
        }
        return Double.compare(this.fVal, o.fVal);


//        if ( this.fVal == o.fVal )
//        {
//            return this.grid.compareTo( o.grid );
//        }
//
//        if ( this.fVal < o.fVal )
//        {
//            return -1;
//        }
//        else if ( this.fVal > o.fVal )
//        {
//            return 1;
//        }

//        return 0;
    }

    @Override
    public String toString() {
        return "Node{" +
                "fVal=" + fVal +
                ", grid=" + grid +
                '}';
    }
}


class Solver
{



    int size;
    Grid initialGrid;
    Grid goalGrid;
    static Scanner scanner = new Scanner(System.in);


    void printPQ( PriorityQueue<Node> toPrint )
    {
        toPrint = new PriorityQueue<Node>(toPrint);
        while ( toPrint.size() > 0 )
        {
            Node node = toPrint.poll();
            System.out.println(node);
        }
    }

    void checkPQ( PriorityQueue<Node> toCheck )
    {
        toCheck = new PriorityQueue<Node>(toCheck);
        double curFVal = Double.MIN_VALUE;
        while ( toCheck.size() > 0 )
        {
            Node node = toCheck.poll();
            if ( node.fVal < curFVal )
            {
                System.out.println(" Priority Queu is not working properly ");
                System.exit( 0 );
            }
            curFVal = Double.max(curFVal, node.fVal);
//            System.out.println(node);
        }
    }


    int aStar()
    {
//        Hashtable<Grid, Integer> closedTable = new Hashtable<Grid, Integer>();

//        Hashtable<Grid, Integer> openTable = new Hashtable<Grid, Integer>();
//        openTable.put(initialGrid, 1);

        System.out.println("start of aStar");

        PriorityQueue<Node> priorityQueue = new PriorityQueue<Node>();
        Node firstNode = new Node( initialGrid.getHeuristics(goalGrid), initialGrid );
        priorityQueue.add( firstNode );

        System.out.println("priority queue is initialized");


        Hashtable<Grid, Move> cameFrom = new Hashtable<Grid, Move>();

        Hashtable<Grid, Integer> gScore = new Hashtable<Grid, Integer>();
        gScore.put(initialGrid, 0);

        Hashtable<Grid, Double> fScore = new Hashtable<Grid, Double>();
        fScore.put( initialGrid, initialGrid.getHeuristics(goalGrid) );

        while ( priorityQueue.size() > 0 )
        {

//            System.out.println( "priorityQueue.size() = " + priorityQueue.size() );
//            System.out.println("printing priorityQueue");
//            printPQ( priorityQueue );

//            checkPQ( priorityQueue );


            Node currentNode = priorityQueue.poll();

//            System.out.println( "currentNode = " + currentNode );
//
//            System.out.println(  );

            Grid currentGrid = currentNode.grid;
            double currentFScore = currentNode.fVal;

//            assert  currentFScore >= 0  : "currentFScore can't be < 0 ";

            int currentGScore = gScore.get( currentGrid );

            if ( currentFScore > fScore.get( currentGrid ) )
            {
                continue;
            }

            if ( currentGrid.getHeuristics( goalGrid ) == 0 )
            {
                System.out.println(" Total move needed = " + gScore.get(currentGrid) );
                System.out.println( "fScore of goalGrid = " + fScore.get(currentGrid) );
                return gScore.get(currentGrid);
            }


            ArrayList<Pair<Grid,Move>> neighborAr = currentGrid.getAllChildren();
//            System.out.println( "NeighborAr = " + neighborAr );
//            System.out.println(  " neighborAr.size() = " + neighborAr.size() );
            for ( Pair<Grid,Move> pair : neighborAr )
            {
                Grid child = pair.getKey();
                Move move = pair.getValue();
                int tentativeGScore = currentGScore+1;
                double childHVal = child.getHeuristics(goalGrid);
                double tentativeFScore = tentativeGScore + childHVal;
                if ( gScore.containsKey(child) && gScore.get(child)<=tentativeGScore )
                {
                    continue;
                }


                tentativeFScore = Double.max(tentativeFScore, currentFScore);
                if ( fScore.containsKey(child) && fScore.get(child)<=tentativeFScore )
                {
                    continue;
                }


//                if ( Double.compare(tentativeFScore, currentFScore) < 0 )
//                {
//                    System.out.println(" tentativeFScore can't be smaller than currentFScore ");
//                    System.out.println( "tentativeFScore = " + tentativeFScore );
//                    System.out.println("currentFScore = " + currentFScore);
//                    System.exit(0);
//                }

                priorityQueue.add( new Node(tentativeFScore, child) );
                cameFrom.put(child, move);


                if ( child.compareTo(goalGrid)==0 )
                {
                    System.out.println("currentGScore = " + currentGScore);
                    System.out.println( "currentFScore = " + currentFScore );
                    System.out.println( "currentHVal = " + currentGrid.getHeuristics(goalGrid) );
                    System.out.println( "currentGrid = " + currentGrid );


                    System.out.println( "tentativeGScore = " + tentativeGScore );
                    System.out.println("tentativeFScore = " + tentativeFScore);
                    System.out.println("childHVal = " + childHVal);
                }


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
                System.out.println( grid[i][j] );
                grid[i][j]--;
            }
        }
        initialGrid = new Grid(size, grid);

        System.out.println("initial grid input tatken");
        System.out.println( initialGrid );


        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                grid[i][j] = scanner.nextInt();
                grid[i][j]--;
            }
        }
        goalGrid = new Grid(size, grid);

        System.out.println("goalGrid input taken");
        System.out.println( goalGrid );

        assert false : "forced assertion";

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
