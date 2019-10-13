import java.io.*;
import java.net.*;
import java.util.Scanner;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Frame extends JFrame implements Runnable, MouseListener
{
    private ConnectFourGame game = null;
    private ObjectOutputStream os = null;
    private BufferedImage buffer = null;
    private char letter;
    private long closeTimerStart = -1;

    public Frame(ConnectFourGame game,ObjectOutputStream os, char letter)
    {
        super("ConnectFourNetworking");
        this.letter = letter;
        setIgnoreRepaint(true);
        setSize(700,600);
        addMouseListener(this);
        setVisible(true);
        buffer = new BufferedImage(getWidth(),getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        Thread t = new Thread(this);
        t.start();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.game 	= game;
        this.os		= os;
    }
    public void paint(Graphics m)
    {
        Graphics g = buffer.getGraphics();
        g.setColor(Color.ORANGE);
        g.fillRect(0,0,700,600);
        for(int x = 0; x < 7;x++)
        {
            for(int y = 0; y < 6;y++)
            {
                if(game.getBoard()[y][x] == ' ')
                {
                    g.setColor(new Color(151, 151, 151));
                    g.fillOval((x*100)+20,(y*100)+20,60,60);
                }
                else if(game.getBoard()[y][x] == 'R')
                {
                    g.setColor(Color.RED);
                    g.fillOval((x*100)+20,(y*100)+20,60,60);
                }
                else if(game.getBoard()[y][x] == 'B')
                {
                    g.setColor(Color.BLACK);
                    g.fillOval((x*100)+20,(y*100)+20,60,60);
                }

            }
        }

        Font f = new Font("Times New Roman",Font.BOLD,25);
        g.setFont(f);

        int textX = 200;
        int textY = 550;

        g.setColor(Color.CYAN);
        System.out.println(""+game.getStatus());
        if(game.getStatus() == game.WAITING_FOR_B)
            g.drawString("Waiting for BLACK to connect",textX,textY);
            //1
        else if(game.getStatus() == game.PLAYING && game.getTurn() == 'R'
                && letter=='R')
            g.drawString("Your turn.",textX,textY);
            //2
        else if(game.getStatus() == game.PLAYING && game.getTurn() == 'R'
                && letter=='B')
            g.drawString("RED's Move.",textX,textY);
            //3
        else if(game.getStatus() == game.PLAYING && game.getTurn() == 'B'
                && letter=='B')
            g.drawString("Your turn.",textX,textY);
            //4
        else if(game.getStatus() == game.PLAYING && game.getTurn() == 'B'
                && letter=='R')
            g.drawString("BLACK's Turn.",textX,textY);
            //5
        else if(game.getStatus() == game.RED_WINS && letter=='R')
            g.drawString("Your Win! Right click for new Game.",textX-100,textY);
            //6
        else if(game.getStatus() == game.RED_WINS && letter=='B')
            g.drawString("Your Lose! Right click for new Game.",textX-100,textY);
            //7
        else if(game.getStatus() == game.BLACK_WINS && letter=='B')
            g.drawString("Your Win! Right click for new Game.",textX-100,textY);
            //8
        else if(game.getStatus() == game.BLACK_WINS && letter=='R')
            g.drawString("Your Lose! Right click for new Game.",textX-100,textY);
            //9
        else if(game.getStatus() == game.DRAW)
            g.drawString("Tie Game. Right click for new Game.",textX-100,textY);
            //10
        else if(game.getStatus()==game.WAITING_RESTART_B && letter=='B')
            g.drawString("RED is ready, right click for new Game.",textX-100,textY);
            //11
        else if(game.getStatus()==game.WAITING_RESTART_R && letter=='R')
            g.drawString("BLACK is ready, right click for new Game.",textX-100,textY);
            //12
        else if(game.getStatus()==game.WAITING_RESTART_B && letter=='R')
            g.drawString("Waiting for BLACK to agree to a new game.",textX-100,textY);
            //13
        else if(game.getStatus()==game.WAITING_RESTART_R && letter=='B')
            g.drawString("Waiting for RED to agree to a new game.",textX-100,textY);
            //14
        else if(game.getStatus()==game.PLAYER_LEFT && letter=='B')
            g.drawString("RED quit. Shuting Down in: "
                    +(5-(System.nanoTime()-closeTimerStart)/1000000000L),textX-150,textY);
            //15
        else if(game.getStatus()==game.PLAYER_LEFT && letter=='R')
            g.drawString("BLACK quit. Shuting Down in: "
                    +(5-(System.nanoTime()-closeTimerStart)/1000000000L),textX-150,textY );
            // WRONG
        else
            g.drawString("Program Fails!",textX,textY);

        m.drawImage(buffer,0,0,null);
    }

    public void run()
    {
        try
        {
            while(true)
            {
                Thread.sleep(50);
                paint(this.getGraphics());

                if(game.getStatus()== ConnectFourGame.PLAYER_LEFT && closeTimerStart==-1)
                {
                    closeTimerStart=System.nanoTime();
                }
                else if(game.getStatus()==ConnectFourGame.PLAYER_LEFT && (5-(System.nanoTime()-closeTimerStart)/1000000000L)<=0)
                {
                    System.exit(4);
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Error in Frame run: "+e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        try
        {
            if(e.getButton()==e.BUTTON1)
            {

                int x = e.getX();

                if( x>0 && x<100)
                {
                    os.writeObject(new Command_To_Server(Command_To_Server.MOVE,game.row(0),0));
                    os.reset();
                }
                else if(x>100 && x<200)
                {
                    os.writeObject(
                            new Command_To_Server(Command_To_Server.MOVE,game.row(1),1));
                    os.reset();
                }
                else if(x>200 && x<300)
                {
                    os.writeObject(
                            new Command_To_Server(Command_To_Server.MOVE,game.row(2),2));
                    os.reset();
                }
                else if(x>300 && x<400)
                {
                    os.writeObject(
                            new Command_To_Server(Command_To_Server.MOVE,game.row(3),3));
                    os.reset();
                }
                else if(x>400 && x<500)
                {
                    os.writeObject(
                            new Command_To_Server(Command_To_Server.MOVE,game.row(4),4));
                    os.reset();
                }
                else if(x>500 && x<600 )
                {
                    os.writeObject(
                            new Command_To_Server(Command_To_Server.MOVE,game.row(5),5));
                    os.reset();
                }
                else if(x>600 && x<700 )
                {
                    os.writeObject(
                            new Command_To_Server(Command_To_Server.MOVE,game.row(6),6));
                    os.reset();
                }
            }
            else if(e.getButton()==e.BUTTON3)
            {
                os.writeObject(
                        new Command_To_Server(Command_To_Server.NEW_GAME));
                os.reset();
            }
        }
        catch(Exception ex)
        {
            System.out.println("Error in Frame-KeyTyped: "+ex.getMessage());
            ex.printStackTrace();
        }
        /*if(game.getStatus() == game.PLAYING)
        {
            int c = 0;
            int x = e.getX();
            c = (x-20)/100;
            if(game.dropPiece(c,game.getTurn()))
                game.changeTurn();
        }*/
        /*else if(game.status() == game.PLAYING && game.getMode() == 1)
        {
            if(game.getTurn() == 1)
            {
                int c = 0;
                int x = e.getX();
                c = (x-20)/100;
                if(game.dropPiece(c,game.getTurn()))
                    game.changeTurn();
            }
            else
            {
                int c = (int)(Math.random()*6);
                if(game.dropPiece(c,game.getTurn()))
                    game.changeTurn();
            }

        }*/
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
}

