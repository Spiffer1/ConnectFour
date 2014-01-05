public class EvalBoardTester
{
    public static void main(String[] args)
    {
        BoardModel testBoard = new BoardModel();
        int[][] b = 
            {
                {1, 0, 0, 2, 0, 0, 0},
                {1, 0, 0, 2, 0, 0, 0},
                {1, 0, 0, 2, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
            };
        testBoard.setBoard(b);
        System.out.println(testBoard);
        int value = testBoard.evaluateBoard();
        System.out.println(value);
    }
}
