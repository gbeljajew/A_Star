/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin;

import bin.pane.MapPane;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author gbeljajew
 */
public class Start {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        JFrame frame = new JFrame("A-Star");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(640, 481));
        
        MapPane map = new MapPane();
        map.setBounds(0, 0, 481, 481);
        panel.add(map);
        
        frame.add(panel);
        frame.pack();
        
        final JButton switchButton = new JButton("Wall");
        switchButton.setBounds(490, 10, 140, 20);
        panel.add(switchButton);
        
        switchButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                map.clickState=(map.clickState+1)%3;
                
                switch(map.clickState)
                {
                    case MapPane.WALL:
                        switchButton.setText("Wall");
                        break;
                    case MapPane.START:
                        switchButton.setText("Start");
                        break;
                    case MapPane.END:
                        switchButton.setText("End");
                        break;
                }
                
            }
            
        });
        
        JButton startButton = new JButton("START");
        startButton.setBounds(490, 40, 140, 20);
        panel.add(startButton);
        
        startButton.addMouseListener(new MouseAdapter() 
        {

            @Override
            public void mouseClicked(MouseEvent e) {
                
                if(!map.runing)
                    map.init();
                
            }
            
        });
        
        
        
        
        frame.setVisible(true);
        
        Timer t = new Timer(10, new ActionListener() {
            int i=0;
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                if(map.runing && i%50==0)
                {
                    if(!map.found)
                        map.update();
                    else
                        map.getPath();
                }
                
                
                frame.repaint();
                i++;
            }
        });
        
        
        t.start();
    }
    
}
