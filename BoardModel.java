
/*
 * Database for the position of all tokens. Tokens are stored as "1"s for computer and "2"s
 * for Human player.
 */

import java.util.Arrays;
public class BoardModel
{
    private int[][] board;  //[row], [column]; row 0 is bottom
    private int whoseTurn;
    private int[] moveRecord;
    private int movesCompleted;

    public BoardModel()
    {
        board = new int[6][7];
        whoseTurn = 2;  // human gets first move
        moveRecord = new int[42];
        movesCompleted = 0;
    }

    public BoardModel(BoardModel original)      // Constructs a copy of the original board and its state
    {
        board = new int[6][7];
        for (int i = 0; i < 6; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                board[i][j] = original.getColor(i, j);
            }
        }
        whoseTurn = original.getWhoseTurn();
        moveRecord = new int[42];
        movesCompleted = original.getMovesCompleted();
        for (int move = 0; move < movesCompleted; move++)
        {
            moveRecord[move] = original.getMove(move);
        }
    }

    public void switchTurn()
    {
        if (whoseTurn == 1)
        {
            whoseTurn = 2;
        }
        else
        {
            whoseTurn = 1;
        }
    }

    // Updates the board when a column is selected. Returns the row number of the lowest empty hole 
    // in the selected column.
    public int pickColumn(int col)
    {
        if (col < 0 || col > 6)
        {
            return -1;
        }
        for (int row = 0; row < 6; row++)
        {
            if (board[row][col] == 0)
            {
                board [row][col] = whoseTurn;
                moveRecord[movesCompleted] = col;
                movesCompleted++;
                System.out.print("Moves: ");
                for (int move = 0; move < movesCompleted; move++)
                {
                    System.out.print(moveRecord[move] + "  ");
                }
                System.out.println();
                return row;
            }
        }
        return -1;
    }

    public void undoMove()
    {
        // Determine the coordinates of the last move made (lastRow, lastColumn)

        int lastColumn = moveRecord[movesCompleted - 1];
        int lastRow = 5;
        while (lastRow >= 0 && board[lastRow][lastColumn] == 0)
        {
            lastRow--;
        }
        board[lastRow][lastColumn] = 0;
        movesCompleted--;
        switchTurn();
    }

    public int getColor(int row, int col)
    {
        return board[row][col];
    }

    public void setColor(int row, int col, int player)
    {
        board[row][col] = player;
    }

    public void setBoard(int[][] b)
    {
        board = b;
    }

    public int getWhoseTurn()
    {
        return whoseTurn;
    }

    public void setWhoseTurn(int player)
    {
        whoseTurn = player;
    }

    public int getMove(int moveNum)
    {
        return moveRecord[moveNum];
    }

    public int getMovesCompleted()
    {
        return movesCompleted;
    }

    // Find winner by calculating product of four squares in a row. If all are 1's, product 
    // is 1 (player 1 wins). If product is 16, all are 2's and player 2 wins. Any other 
    // product, doesn't affect the default selection of winner = 0.
    public int findWinner()
    {
        int winner = 0;
        // Look for horizontal wins
        for (int i = 0; i < 6; i++)
        {
            for (int start = 0; start <= 3; start++)
            {
                int product = 1;
                for (int j = start; j < start + 4; j++)
                {
                    product *= board[i][j];
                }
                if (product == 1)
                {
                    winner = 1;
                }
                else if (product == 16)
                {
                    winner = 2;
                }
            }
        }
        // Look for vertical wins
        for (int j = 0; j < 7; j++)
        {
            for (int start = 0; start <= 2; start++)
            {
                int product = 1;
                for (int i = start; i < start + 4; i++)
                {
                    product *= board[i][j];
                }
                if (product == 1)
                {
                    winner = 1;
                }
                else if (product == 16)
                {
                    winner = 2;
                }
            }
        }
        // Look for ascending diagonal wins
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                int product = 1;
                for (int count = 0; count < 4; count++)
                {
                    product *= board[i + count][j + count];
                }
                if (product == 1)
                {
                    winner = 1;
                }
                else if (product == 16)
                {
                    winner = 2;
                }
            }
        }
        // Look for descending diagonal wins
        for (int i = 5; i >= 3; i--)
        {
            for (int j = 0; j < 4; j++)
            {
                int product = 1;
                for (int count = 0; count < 4; count++)
                {
                    product *= board[i - count][j + count];
                }
                if (product == 1)
                {
                    winner = 1;
                }
                else if (product == 16)
                {
                    winner = 2;
                }
            }
        }
        return winner;
    }

    public String toString()
    {
        String str = "";
        for (int i = 5; i >= 0; i--)
        {
            for (int j = 0; j < 7; j++)
            {
                str += board[i][j] + " ";
            }
            str += "\n";
        }
        return str;
    } 

    public int evaluateBoard()
    {
        // define patterns to look for as arrays within a 2-d array
        int[][] pattern = 
            {
                {2, 2, 2, 2},
                {1, 1, 1, 1},
                {0, 2, 2, 2},
                {2, 0, 2, 2},
                {2, 2, 0, 2},
                {2, 2, 2, 0},
                {0, 1, 1, 1},
                {1, 0, 1, 1},
                {1, 1, 0, 1},
                {1, 1, 1, 0}
            };

        boolean nowFound31 = false;     // When three "1"s are in a 4 member sub-array and the blank could get filled next turn
        int foundRow = -1;
        int foundCol = -1;

        int result = 0;
        // loop through rows
        for (int row = 0; row < 6; row++)
        {
            int lineScore = 0;
            // loop through single row to form 4-digit sub-arrays
            int[] subArr = new int[4];
            for (int firstCol = 0; firstCol < 4; firstCol++)
            {
                // build the sub-array
                for (int col = firstCol; col < firstCol + 4; col++)
                {
                    subArr[col - firstCol] = board[row][col];
                }
                // loop through patterns, testing each one against the sub-array
                if (Arrays.equals(subArr, pattern[0]))
                {
                    return -1001;    // You have already lost the game
                }
                if (Arrays.equals(subArr, pattern[1]))
                {
                    return 1000;    // Your move will win the game
                }
                // loop through remaining patterns of 2's
                for (int pat = 2; pat < 6; pat++)
                {
                    if (Arrays.equals(subArr, pattern[pat]))
                    {
                        lineScore -= 2;
                        int zeroPos = (pat - 2) % 4 + firstCol;
                        // find height of first blank hole
                        int firstBlankRow = 0;
                        while (firstBlankRow < 6 && board[firstBlankRow][zeroPos] != 0)
                        {
                            firstBlankRow++;
                        }
                        if (firstBlankRow == row)
                        {
                            lineScore -=400;    // opponent will win on next turn
                        }
                    }
                }
                // loop through remaining patterns of "1"s
                for (int pat = 6; pat < pattern.length; pat++)
                {
                    if (Arrays.equals(subArr, pattern[pat]))
                    {
                        lineScore += 2;
                        int zeroPos = (pat - 2) % 4 + firstCol;
                        // find height of first blank hole
                        int firstBlankRow = 0;
                        while (firstBlankRow < 6 && board[firstBlankRow][zeroPos] != 0)
                        {
                            firstBlankRow++;
                        }
                        if (firstBlankRow == row)
                        {
                            lineScore--;
                            if (nowFound31 && (row != foundRow || zeroPos != foundCol))
                            {
                                lineScore += 40;    // a double attack on your next turn
                            }
                            else
                            {
                                nowFound31 = true;
                                foundRow = row;
                                foundCol = zeroPos;
                            }
                        }
                    }
                }   // end checking patterns
            }   // end checking sub-arrays in that row
            result += lineScore;
        }   // end checking all rows

        // loop through columns
        for (int col = 0; col < 7; col++)
        {
            int lineScore = 0;
            // loop through single column to form 4-digit sub-arrays
            int[] subArr = new int[4];
            for (int firstRow = 0; firstRow < 3; firstRow++)
            {
                // build the sub-array
                for (int row = firstRow; row < firstRow + 4; row++)
                {
                    subArr[row - firstRow] = board[row][col];
                }
                // loop through patterns, testing each one against the sub-array 
                // There is some unecessary checking here, since several patterns can't actually exist in a column.
                if (Arrays.equals(subArr, pattern[0]))
                {
                    return -1001;    // You have already lost the game
                }
                if (Arrays.equals(subArr, pattern[1]))
                {
                    return 1000;    // Your move will win the game
                }
                // loop through remaining patterns of 2's
                for (int pat = 2; pat < 6; pat++)
                {
                    if (Arrays.equals(subArr, pattern[pat]))
                    {
                        lineScore -= 2;
                        int zeroPos = (pat - 2) % 4 + firstRow;
                        // find height of first blank hole
                        // These ought to be the same for a pattern in a column...
                        int firstBlankRow = 0;
                        while (firstBlankRow < 6 && board[firstBlankRow][col] != 0)
                        {
                            firstBlankRow++;
                        }
                        if (firstBlankRow == zeroPos)
                        {
                            lineScore -=400;    // opponent will win on next turn
                        }
                    }
                }
                // loop through remaining patterns of "1"s
                for (int pat = 6; pat < pattern.length; pat++)
                {
                    if (Arrays.equals(subArr, pattern[pat]))
                    {
                        lineScore += 2;
                        int zeroPos = (pat - 2) % 4 + firstRow;
                        // find height of first blank hole
                        int firstBlankRow = 0;
                        while (firstBlankRow < 6 && board[firstBlankRow][col] != 0)
                        {
                            firstBlankRow++;
                        }
                        if (firstBlankRow == zeroPos)
                        {
                            lineScore--;
                            if (nowFound31 && (zeroPos != foundRow || col != foundCol))
                            {
                                lineScore += 40;    // a double attack on your next turn
                            }
                            else
                            {
                                nowFound31 = true;
                                foundRow = zeroPos;
                                foundCol = col;
                            }
                        }
                    }
                }   // end checking patterns
            }   // end checking sub-arrays in that column
            result += lineScore;
        }   // end checking all columns

        // loop through ascending diagonals. (row, col) is the lower left corner of the diagonal
        for (int row = 0; row < 3; row++)     // coordinates of a square on the diagonal are (row + count, col + count)
        {
            int lineScore = 0;
            // loop through single diagonal to form 4-digit sub-arrays
            int[] subArr = new int[4];
            for (int col = 0; col < 4; col++)
            {
                // build the sub-array
                for (int count = 0; count < 4; count++)
                {
                    subArr[count] = board[row + count][col + count];
                }
                // loop through patterns, testing each one against the sub-array
                if (Arrays.equals(subArr, pattern[0]))
                {
                    return -1001;    // You have already lost the game
                }
                if (Arrays.equals(subArr, pattern[1]))
                {
                    return 1000;    // Your move will win the game
                }
                // loop through remaining patterns of 2's
                for (int pat = 2; pat < 6; pat++)
                {
                    if (Arrays.equals(subArr, pattern[pat]))
                    {
                        lineScore -= 2;
                        int zeroPos = (pat - 2) % 4;
                        int zeroCol = zeroPos + col;
                        // find height of first blank hole
                        int firstBlankRow = 0;
                        while (firstBlankRow < 6 && board[firstBlankRow][zeroCol] != 0)
                        {
                            firstBlankRow++;
                        }
                        if (firstBlankRow == row + zeroPos)
                        {
                            lineScore -=400;    // opponent will win on next turn
                        }
                    }
                }
                // loop through remaining patterns of "1"s
                for (int pat = 6; pat < pattern.length; pat++)
                {
                    if (Arrays.equals(subArr, pattern[pat]))
                    {
                        lineScore += 2;
                        int zeroPos = (pat - 2) % 4;
                        int zeroCol = zeroPos + col;
                        // find height of first blank hole
                        int firstBlankRow = 0;
                        while (firstBlankRow < 6 && board[firstBlankRow][zeroCol] != 0)
                        {
                            firstBlankRow++;
                        }
                        if (firstBlankRow == row + zeroPos)
                        {
                            lineScore--;
                            if (nowFound31 && (row + zeroPos != foundRow || zeroCol != foundCol))
                            {
                                lineScore += 40;    // a double attack on your next turn
                            }
                            else
                            {
                                nowFound31 = true;
                                foundRow = row + zeroPos;
                                foundCol = zeroCol;
                            }
                        }
                    }
                }   // end checking patterns for that sub-array
            }   // end checking sub-arrays in that diagonal
            result += lineScore;
        }   // end checking all diagonals

        // loop through descending diagonals; (row, col) is the upper left corner of the diagonal
        for (int row = 5; row >= 3; row--)     // coordinates of a square on the diagonal are (row - count, col + count)
        {
            int lineScore = 0;
            // loop through single diagonal to form 4-digit sub-arrays
            int[] subArr = new int[4];
            for (int col = 0; col < 4; col++)
            {
                // build the sub-array
                for (int count = 0; count < 4; count++)
                {
                    subArr[count] = board[row - count][col + count];
                }
                // loop through patterns, testing each one against the sub-array
                if (Arrays.equals(subArr, pattern[0]))
                {
                    return -1001;    // You have already lost the game
                }
                if (Arrays.equals(subArr, pattern[1]))
                {
                    return 1000;    // Your move will win the game
                }
                // loop through remaining patterns of 2's
                for (int pat = 2; pat < 6; pat++)
                {
                    if (Arrays.equals(subArr, pattern[pat]))
                    {
                        lineScore -= 2;
                        int zeroPos = (pat - 2) % 4;
                        int zeroCol = zeroPos + col;
                        // find height of first blank hole
                        int firstBlankRow = 0;
                        while (firstBlankRow < 6 && board[firstBlankRow][zeroCol] != 0)
                        {
                            firstBlankRow++;
                        }
                        if (firstBlankRow == row - zeroPos)
                        {
                            lineScore -=400;    // opponent will win on next turn
                        }
                    }
                }
                // loop through remaining patterns of "1"s
                for (int pat = 6; pat < pattern.length; pat++)
                {
                    if (Arrays.equals(subArr, pattern[pat]))
                    {
                        lineScore += 2;
                        int zeroPos = (pat - 2) % 4;
                        int zeroCol = zeroPos + col;
                        // find height of first blank hole
                        int firstBlankRow = 0;
                        while (firstBlankRow < 6 && board[firstBlankRow][zeroCol] != 0)
                        {
                            firstBlankRow++;
                        }
                        if (firstBlankRow == row - zeroPos)
                        {
                            lineScore--;
                            if (nowFound31 && (row - zeroPos != foundRow || zeroCol != foundCol))
                            {
                                lineScore += 40;    // a double attack on your next turn
                            }
                            else
                            {
                                nowFound31 = true;
                                foundRow = row - zeroPos;
                                foundCol = zeroCol;
                            }
                        }
                    }
                }   // end checking patterns for that sub-array
            }   // end checking sub-arrays in that diagonal
            result += lineScore;
        }   // end checking all diagonals
        return result;
    }
}
