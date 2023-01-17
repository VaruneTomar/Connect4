import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Connect4 {
    JFrame frame = new JFrame("Connect 4");
    JPanel titlepanel = new JPanel();
    JLabel textfield = new JLabel();
    public Connect4() {
        //frame creation
        frame.setSize(1000, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(frame.getSize());
        frame.setLayout(new BorderLayout());
        frame.add(new GamePlay(), BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        //textfield contents
        textfield.setBackground(Color.black);
        textfield.setForeground(new Color(255, 255, 255));
        textfield.setFont(new Font("Futura", Font.BOLD, 75));
        textfield.setHorizontalAlignment(JLabel.CENTER);
        textfield.setText("CONNECT 4");
        textfield.setOpaque(true);

        //title panel creation
        titlepanel.setLayout(new BorderLayout());
        titlepanel.setBounds(0, 0, 800, 100);
        titlepanel.add(textfield);

        //adding panel to Frame
        frame.add(titlepanel, BorderLayout.NORTH);
    }

    public static void main(String[] args) {
        Connect4 connect4 = new Connect4();
    }
    public static class GamePlay extends JPanel implements MouseListener {

        String[][] boardArray = new String[6][7];
        String currentTurn;
        String winningPlayer = "";
        int totalTurns = 0;
        int boardUnit = 100;
        boolean winner;
        boolean gameDraw = false;
        public GamePlay() {
            //Randomizing which user gets the first turn
            Random random = new Random();
            int randomTurn = random.nextInt(2);
            if (randomTurn == 0){
                currentTurn = "Yellow";
            } else {
                currentTurn = "Red";
            }

            addMouseListener(this);

            //creating the array where placement of pieces is stored
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 7; j++) {
                    boardArray[i][j] = "null";
                }
            }
        }

        public void paintComponent(Graphics g){
            Graphics2D gPaint = (Graphics2D) g;
            gPaint.setFont(new Font("Futura", Font.BOLD, 35));
            gPaint.setColor(new Color(0, 0, 0));
            gPaint.fillRect(0,0,getWidth(),getHeight());

            int initialX = 100;
            int initialY = 100;

            //generating gui for the Connect 4 board
            for (int i = 0; i < boardArray.length; i++) {
                for (int j = 0; j < boardArray[0].length; j++) {
                    gPaint.setColor(Color.blue);
                    gPaint.fillRect(initialX,initialY, boardUnit, boardUnit);

                    if (boardArray[i][j].equals("null")) {
                        gPaint.setColor(Color.white);
                    } else if (boardArray[i][j].equals("Red")){
                        gPaint.setColor(Color.red);
                    } else {
                        gPaint.setColor(Color.YELLOW);
                    }
                    gPaint.fillOval(initialX + 5,initialY + 5, boardUnit -10, boardUnit -10);

                    initialX += boardUnit;
                }
                initialY += boardUnit;
                initialX = 100;
            }

            //Procedures for when there is a winning user or a draw
            if (winner){
                gPaint.setColor(Color.white);
                if (winningPlayer.equals("Red")) {
                    gPaint.drawString("RED IS THE WINNER!", 270, 795);
                } else {
                    gPaint.drawString("YELLOW IS THE WINNER!", 235, 795);
                }

            } else if (gameDraw) {
                gPaint.setColor(Color.white);
                gPaint.drawString("No winner was decided, it ends in a draw!", 65, 795);
            }

            //Displaying whose turn it is
            if (currentTurn.equals("Red") && (!winner) && !gameDraw){
                gPaint.setColor(Color.RED);
                gPaint.drawString("RED'S TURN", 350, 795);

            } else if ((!winner) && (!gameDraw)){
                gPaint.setColor(Color.YELLOW);
                gPaint.drawString("YELLOW'S TURN", 310, 795);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        static boolean validMove(String userTurn, int columnSelection, String boardArray[][]) {
            //checking if the move is valid, such as column being full
            for (int i = 5; i >= 0; i--){
                if (boardArray[i][columnSelection].equals("null")){
                    boardArray[i][columnSelection] = userTurn;
                    return true;
                }
            }
            return false;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if ((!winner) && (!gameDraw)) {
                //finding position of the click and corresponding column on the board
                int xCoordinate = e.getX();
                int column = xCoordinate / boardUnit;

                //Checks if move is valid and places piece in the column
                if ((1 <= column) && (column <= 7)) {
                    if (currentTurn.equals("Red")) {
                         if (validMove(currentTurn, column - 1, boardArray)) {
                            totalTurns += 1;
                            winnerCheck(currentTurn, boardArray);
                            currentTurn = "Yellow";

                            //42 total moves for a draw
                            if (totalTurns == 42) {
                                 gameDraw = true;
                                 repaint();
                            }
                         }

                    } else {
                        if (validMove(currentTurn, column - 1, boardArray)) {
                            totalTurns += 1;
                            winnerCheck(currentTurn, boardArray);
                            currentTurn = "Red";

                            if (totalTurns == 42) {
                                gameDraw = true;
                                repaint();
                            }
                        }
                    }
                    repaint();
                }

            } else {
                //Winner or draw declared, thus user can play again with another press
                playAgain();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        static boolean verticalWinner(String[][] boardArray) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 7; j++) {
                    if(boardArray[i][j].equals("Red") || boardArray[i][j].equals("Yellow")){
                        if ((boardArray[i][j] == boardArray[i + 1][j]) && (boardArray[i][j] == boardArray[i + 2][j]) && (boardArray[i][j] == boardArray[i+3][j])) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        static Boolean horizontalWinner(String[][] boardArray) {
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 4; j++) {
                    if(boardArray[i][j].equals("Red") || boardArray[i][j].equals("Yellow")){
                        if ((boardArray[i][j] == boardArray[i][j+1]) && (boardArray[i][j] == boardArray[i][j+2]) && (boardArray[i][j] == boardArray[i][j+3])) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        static Boolean leftDiagonalWinner(String[][] boardArray) {
            for (int i = 0; i < boardArray.length - 3; i++) {
                for (int j = 0; j < boardArray[0].length - 3; j++) {
                    if(boardArray[i][j].equals("Red") || boardArray[i][j].equals("Yellow")){
                        if ((boardArray[i][j] == boardArray[i+1][j+1]) && (boardArray[i][j] == boardArray[i+2][j+2]) && (boardArray[i][j] == boardArray[i+3][j+3])) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        static Boolean rightDiagonalWinner(String[][] boardArray) {
            for (int i = 3; i < boardArray.length; i++) {
                for (int j = 0; j < boardArray[0].length - 3; j++) {
                    if(boardArray[i][j].equals("Red") || boardArray[i][j].equals("Yellow")){
                        if ((boardArray[i][j] == boardArray[i-1][j+1]) && (boardArray[i][j] == boardArray[i-2][j+2]) && (boardArray[i][j] == boardArray[i-3][j+3])) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        public void winnerCheck(String currentPlayer, String[][] boardArray){
            //Checks for winner in all directions
            if (verticalWinner(boardArray)){
                winner = true;
                winningPlayer = currentPlayer;

            } else if (horizontalWinner(boardArray)){
                winner = true;
                winningPlayer = currentPlayer;

            } else if (leftDiagonalWinner(boardArray)){
                winner = true;
                winningPlayer = currentPlayer;

            } else if (rightDiagonalWinner(boardArray)){
                winner = true;
                winningPlayer = currentPlayer;
            }

        }

        public void playAgain(){
            //resets the board and variables to start state to play again
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 7; j++) {
                    boardArray[i][j] = "null";
                }
            }
            totalTurns = 0;

            gameDraw = false;

            winner = false;

            repaint();
        }
    }
}
