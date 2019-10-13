import java.io.Serializable;

public class ConnectFourGame implements Serializable
{
    private int status                          = 0;
    private char turn                           = 'R';
    public static final int WAITING_FOR_B 		= 0;
    public static final int WAITING_RESTART_B 	= 6;
    public static final int WAITING_RESTART_R 	= 7;
    public static final int PLAYER_LEFT			= 8;
    public static final int PLAYING             = 1;
    public static final int RED_WINS            = 5;
    public static final int BLACK_WINS          = 2;
    public static final int DRAW                = 4;
    private char[][] board               = null;

    public ConnectFourGame(int status)
    {
        this.status = status;
        turn = 'R';
        board = new char[6][7];
        for(int r=0;r<board.length; r++) {
            for (int c = 0; c < board[0].length; c++)
                board[r][c] = ' ';
        }
    }

    public void update(ConnectFourGame other)
    {
        this.status = other.getStatus();
        turn = other.getTurn();

        for(int r=0;r<board.length; r++)
            for(int c=0; c<board[0].length; c++)
                board[r][c]=other.getSpot(r,c);
    }

    public char board(int r, int c)
    {
        if(r<0 || c<0 || c>=getNumberOfCols() || r>=getNumberOfRows())
            return '*';
        else
            return board[r][c];
    }
    public boolean changeSpot(int r, int c, char letter)
    {
        if(r<0 || c<0 || c>=getNumberOfCols() || r>=getNumberOfRows() || board[r][c]!=' ')
            return false;
        else
        {
            board[r][c]=letter;
            changeTurns();
            status();
            return true;
        }
    }

    public int getStatus()
    {	return status;	}

    public char getTurn()
    {	return turn;	}

    public void changeTurns()
    {
        if(getTurn()=='R')
            setTurn('B');
        else
            setTurn('R');
    }

    public void setStatus(int status)
    {
        this.status = status;	}

    public void setTurn(char turn)
    {	this.turn = turn;	}

    public char getSpot(int r, int c)
    {	return board[r][c];	}

    public void setSpot(int r, int c, char letter)
    {	board[r][c] = letter;	}

    public void reset()
    {
        turn = 'R';
        status = PLAYING;
        board = new char[6][7];
        for(int r=0;r<board.length; r++)
            for(int c=0; c<board[0].length; c++)
                board[r][c]=' ';
    }

    public int getNumberOfRows()
    {
        return board.length;
    }

    public int getNumberOfCols()
    {
        return board[0].length;
    }


    public int row(int c) {
        for(int r = 5; r >= 0; --r) {
            if (this.board[r][c] == ' ') {
                return r;
            }
        }
        return -1;
    }
    public int status() {

        int r;
        int c;
        for(r = 0; r < 6; ++r) {
            for(c = 0; c <= 3; ++c) {
                if (this.board[r][c] == 'R' && this.board[r][c + 1] == 'R' && this.board[r][c + 2] == 'R' && this.board[r][c + 3] == 'R') {
                    status  = RED_WINS;
                    System.out.println("RED wins");
                    return status;
                }

                if (this.board[r][c] == 'B' && this.board[r][c + 1] == 'B' && this.board[r][c + 2] == 'B' && this.board[r][c + 3] == 'B') {
                    status  = BLACK_WINS;
                    System.out.println("BLACK wins");
                    return status;
                }
            }
        }

        for(r = 0; r <= 2; ++r) {
            for(c = 0; c < 7; ++c) {
                if (this.board[r][c] == 'R' && this.board[r + 1][c] == 'R' && this.board[r + 2][c] == 'R' && this.board[r + 3][c] == 'R') {
                    status  = RED_WINS;
                    System.out.println("RED wins");
                    return status;
                }

                if (this.board[r][c] == 'B' && this.board[r + 1][c] == 'B' && this.board[r + 2][c] == 'B' && this.board[r + 3][c] == 'B') {
                    status  = BLACK_WINS;
                    System.out.println("BLACK wins");
                    return status;
                }
            }
        }

        for(r = 0; r <= 2; ++r) {
            for(c = 3; c < 7; ++c) {
                if (this.board[r][c] == 'R' && this.board[r + 1][c - 1] == 'R' && this.board[r + 2][c - 2] == 'R' && this.board[r + 3][c - 3] == 'R') {
                    status  = RED_WINS;
                    System.out.println("RED wins");
                    return status;
                }

                if (this.board[r][c] == 'B' && this.board[r + 1][c - 1] == 'B' && this.board[r + 2][c - 2] == 'B' && this.board[r + 3][c - 3] == 'B') {
                    status  = BLACK_WINS;
                    System.out.println("BLACK wins");
                    return status;
                }
            }
        }

        for(r = 0; r <= 2; ++r) {
            for(c = 0; c <= 3; ++c) {
                if (this.board[r][c] == 'R' && this.board[r + 1][c + 1] == 'R' && this.board[r + 2][c + 2] == 'R' && this.board[r + 3][c + 3] == 'R') {
                    status  = RED_WINS;
                    System.out.println("RED wins");
                    return status;
                }

                if (this.board[r][c] == 'B' && this.board[r + 1][c + 1] == 'B' && this.board[r + 2][c + 2] == 'B' && this.board[r + 3][c + 3] == 'B') {
                    status  = BLACK_WINS;
                    System.out.println("BLACK wins");
                    return status;
                }
            }
        }

        boolean empty = false;
        for(r = 0; r < 6; ++r) {
            for(c = 0; c < 7; ++c) {
                if (this.board[r][c] == ' ') {
                    empty = true;
                }
            }
        }
        if(empty == true)
            return PLAYING;
        else
        {
            status  = DRAW;
            return status;
        }

    }
    public void draw() {
        for(int r = 0; r < 6; ++r) {
            System.out.print("|");

            for(int c = 0; c < 7; ++c) {
                System.out.print(" ");
                if (this.board[r][c] == 0) {
                    System.out.print(" ");
                } else if (this.board[r][c] == 1) {
                    System.out.print("R");
                } else {
                    System.out.print("B");
                }

                System.out.print(" |");
            }

            System.out.print("\n");
        }

        System.out.print("-----------------------------\n");
    }

    public char[][] getBoard() {
        return board;
    }

    public void setBoard(char[][] board) {
        this.board = board;
    }

    public static int getDRAW() {
        return DRAW;
    }

    public static int getRedWins() {
        return RED_WINS;
    }

    public static int getBlackWins() {
        return BLACK_WINS;
    }

    public static int getPLAYING() {
        return PLAYING;
    }

    public void changeTurn()
    {
        if(turn == 1)
            turn =2;
        else if(turn ==2)
            turn =1;
    }

}
