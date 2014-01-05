public class ChooseMoveTester
{
    public static void main(String[] args)
    {
        BoardModel testBoard = new BoardModel();
        int[][] b = 
            {
                {1, 2, 0, 0, 0, 0, 0},
                {1, 2, 0, 0, 0, 0, 0},
                {0, 2, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
            };
        testBoard.setBoard(b);
        System.out.println(testBoard);
        AIPlayer computer = new AIPlayer();
        int move = computer.chooseMove(testBoard);
        System.out.println(move);
    }
}
