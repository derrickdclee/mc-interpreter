import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Test extends JFrame{
	JPanel [][] squares;
	
	public Test() {
		Container c = getContentPane();
        c.setLayout(new GridLayout(10,20, 1 , 1)); 
        squares = new JPanel[10][20];
        for(int i=0; i<10; i++)
        {
            for(int j=0; j<20; j++)
            {
                squares[i][j] = new JPanel();
                squares[i][j].setBackground(Color.white);
                c.add(squares[i][j]);
            }
        }
	}
	
	public void addPiece(String piece_name,int i, int j)
    {
		File file = new File("./images/cat.jpg");
        ImageIcon img = new ImageIcon(file.getAbsolutePath());
        JLabel label = new JLabel("", img, JLabel.CENTER);        
        squares[i][j].add(label, BorderLayout.CENTER);
    }
	
	public static void main(String[] args) {
		Test test = new Test();
        test.addPiece("images/cat.jpg",0,0);
        test.setSize(900,900);
        test.setResizable(true);
        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        test.setVisible(true);
	}

}
