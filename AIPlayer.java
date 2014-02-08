public class AIPlayer
{
    public int chooseMove(BoardModel board)
    {
        BoardModel copy = new BoardModel(board);
        int[][][] value = new int[7][7][7];  // how good the board is after I play, he plays, I play (depth 3)
        /*
         * A large positive value[][][] number indicates that the play is good for me (the computer); a big 
         * negative value[][][]number is good for him (the human player). 
         * 
         * As each sequence of three moves is considered, its value is compare against the previous best value
         * for that depth. The best value for depth 1 gets stored in value[0][0][0]; best values for each depth 
         * 2 get stored in value[d1][0][0]; for depth 3 they are stored in each of the value[d1][d2][0]. Normally
         * the value[0][0][0] and the other best values would get set as all of the possible plays are evaluated.
         * But, if column 0 is already full, then the move sequence (0, 0, 0) could not be carried out, so 
         * value[0][0][0] could not be evaluated. Therefore, these best values must be initialized.
         * Initial values are chosen as extremes so that any other valid move would be better. This means that 
         * value[0][0][0] and the value[d1][d2][0] are set to low numbers (-10000) while the value[d1][0][0] are
         * set to high numbers
         */
        // Initialize best values:
        for (int i = 0; i < 7; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                value[i][j][0] = -10000;
            }
            value[i][0][0] = 10000;
        }
        value[0][0][0] = -10000;

        int bestChoice = 0;
        // verify that bestChoice is a valid column; i.e. that there is at least one open hole in that column
        while (copy.getColor(5, bestChoice) != 0)
        {
            bestChoice++;
        }
        for (int d1 = 0; d1 < 7; d1++)
        {
            int pickedRow = copy.pickColumn(d1);
            // if there are no more spaces in that row, pickedRow will be -1
            if (pickedRow >= 0)     // else, if pickedRow = -1, then nothing happens in this iteration and d1 is increased
            {
                copy.switchTurn();
                if (copy.evaluateBoard() == 1000)  // if next move wins, don't bother looking further
                {
                    bestChoice = d1;
                    break;
                }
                for (int d2 = 0; d2 < 7; d2++)
                {
                    pickedRow = copy.pickColumn(d2);
                    if (pickedRow >= 0)
                    {
                        copy.switchTurn();
                        if (copy.evaluateBoard() == -1001) // if opponent's next move would win... 
                        {
                            value[d1][0][0] = -1001;        // ...it's value is very low...
                        }
                        else                                    // ... and you can't make a move after it.
                        {
                            for (int d3 = 0; d3 < 7; d3++)
                            {
                                pickedRow = copy.pickColumn(d3);
                                if (pickedRow >= 0)
                                {
                                    copy.switchTurn();
                                    System.out.println(copy);
                                    value[d1][d2][d3] = copy.evaluateBoard();
                                    System.out.println("Value[" + d1 + "][" + d2 + "][" + d3 + "] = " + value[d1][d2][d3]);
                                    // Put your best 2nd move's value into value[d1][d2][0]
                                    if (value[d1][d2][d3] > value[d1][d2][0])
                                    {
                                        value[d1][d2][0] = value[d1][d2][d3];
                                    }
                                    copy.undoMove();
                                }
                            }
                            // put oponent's best move's value into value[d1][0][0]
                            if (value[d1][d2][0] < value[d1][0][0])
                            {
                                value[d1][0][0] = value[d1][d2][0];
                            }
                        }
                        copy.undoMove();
                    }
                }
                if (value[d1][0][0] > value[0][0][0])
                {
                    bestChoice = d1;
                    value[0][0][0] = value[d1][0][0];
                }
                System.out.println("Value: " + value[0][0][0]);
                copy.undoMove();
            }
        }
        return bestChoice;
    }
}
