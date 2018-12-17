package ivanludvig.livecounting;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.DefaultCaret;

class GUI extends JFrame {

	JButton run;
	JLabel progress;
	ArrayList<JCheckBox> checkboxes = new ArrayList<JCheckBox>(21);
	String stats[] = {"Pairs", "Favourite counter", "HoE(+HoS, Ho3k)", "Hours", "Bars", "Odd/even", "Pincus", 
			"20k Days", "Day streak","First counts", "1k Streaks", "Top streaks", "Pee", "Count percentage", 
			"K's participation", "Counts", "Gets", "Assists", "Days", "HoP", 
			"10kto100k",  "Average Counts", ""};
	int n = 22;
	Main main;
    public int a[]=new int[n];
	public GUI(Main m){
		main = m;
		/*
		setTitle("Livecounting stats");
		setSize(800, 480);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(5, 5));

        JPanel checkBoxPanel = new JPanel();
        for(int i = 0; i<18; i++) {
        	checkboxes.add(new JCheckBox(stats[i]));
            checkBoxPanel.add(checkboxes.get(i));
        }

        mainPanel.add(checkBoxPanel);

        add(mainPanel);

        //pack();
        setLocationByPlatform(true);
        setVisible(true); */
		
		setTitle("Livecounting stats");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 560);
		JPanel pnPanel0;

		JPanel pnPanel2;

		JPanel pnPanel3;

		JPanel pnPanel5;

		progress = new JLabel("Lore mimpsun dolor sit amet \n \n \n");
		pnPanel0 = new JPanel();
		GridBagLayout gbPanel0 = new GridBagLayout();
		GridBagConstraints gbcPanel0 = new GridBagConstraints();
		pnPanel0.setLayout( gbPanel0 );

		pnPanel2 = new JPanel();
		pnPanel2.setBorder( BorderFactory.createTitledBorder( "Select Stats" ) );
		GridBagLayout gbPanel2 = new GridBagLayout();
		GridBagConstraints gbcPanel2 = new GridBagConstraints();
		pnPanel2.setLayout( gbPanel2 );

		
        for(int i = 0; i<n; i++) {
        	checkboxes.add(new JCheckBox(stats[i]));
    		gbcPanel2.gridx = i/8;
    		gbcPanel2.gridy = i%8;
    		gbcPanel2.gridwidth = 1;
    		gbcPanel2.gridheight = 1;
    		gbcPanel2.fill = GridBagConstraints.BOTH;
    		gbcPanel2.weightx = 1;
    		gbcPanel2.weighty = 0;
    		gbcPanel2.anchor = GridBagConstraints.NORTH;
    		gbPanel2.setConstraints(checkboxes.get(i), gbcPanel2 );
    		pnPanel2.add(checkboxes.get(i));
        }

		gbcPanel0.gridx = 7;
		gbcPanel0.gridy = 3;
		gbcPanel0.gridwidth = 10;
		gbcPanel0.gridheight = 8;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets( 20,20,20,20 );
		gbPanel0.setConstraints( pnPanel2, gbcPanel0 );
		pnPanel0.add( pnPanel2 );

		pnPanel3 = new JPanel();
		GridBagLayout gbPanel3 = new GridBagLayout();
		GridBagConstraints gbcPanel3 = new GridBagConstraints();
		pnPanel3.setLayout( gbPanel3 );

		run = new JButton( "Run"  );
		gbcPanel3.gridx = 1;
		gbcPanel3.gridy = 4;
		gbcPanel3.gridwidth = 1;
		gbcPanel3.gridheight = 1;
		gbcPanel3.fill = GridBagConstraints.BOTH;
		gbcPanel3.weightx = 1;
		gbcPanel3.weighty = 0;
		gbcPanel3.anchor = GridBagConstraints.CENTER;
		gbPanel3.setConstraints( run, gbcPanel3 );
		pnPanel3.add( run );
		gbcPanel0.gridx = 17;
		gbcPanel0.gridy = 3;
		gbcPanel0.gridwidth = 3;
		gbcPanel0.gridheight = 8;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets( 0,0,0,30 );
		gbPanel0.setConstraints( pnPanel3, gbcPanel0 );
		pnPanel0.add( pnPanel3 );

		pnPanel5 = new JPanel();
		pnPanel5.setBorder( BorderFactory.createTitledBorder( "Progress" ) );
		GridBagLayout gbPanel5 = new GridBagLayout();
		GridBagConstraints gbcPanel5 = new GridBagConstraints();
		pnPanel5.setLayout( gbPanel5 );
		JScrollPane scpPanel5 = new JScrollPane( pnPanel5 );
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 11;
		gbcPanel0.gridwidth = 20;
		gbcPanel0.gridheight = 9;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 1;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbcPanel0.insets = new Insets( 0,10,30,10 );
		gbPanel0.setConstraints( scpPanel5, gbcPanel0 );
		progress.setText("<html></html>");
		progress.setFont(new Font("Consolas",Font.PLAIN, 12));
		pnPanel5.add(progress);
		pnPanel0.add( scpPanel5 );
		
		add(pnPanel0);
		listen();
		setVisible(true);
  }
	public void updateProgress(String update) {
		progress.setText(progress.getText().substring(0, progress.getText().length()-6)+"<br>"+update+"</html>");
		progress.paintComponents(progress.getGraphics());
		//System.out.println("label "+progress.getText());
	}
	
	public void setGreenColour() {
		progress.setForeground(Color.GREEN);
	}
	
	private void listen() {
		for(int i = 14; i<19; i++) {
	        checkboxes.get(i).addItemListener(new ItemListener() {
	            @Override
	            public void itemStateChanged(ItemEvent e) {
	                if(e.getStateChange() == ItemEvent.DESELECTED) {
            			checkboxes.get(19).setSelected(false);
	                }
	            }
	        });
		}
        checkboxes.get(19).addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                	for(int m = 14; m<19; m++) {
            			checkboxes.get(m).setSelected(true);
                	}
                }
            }
        });
        checkboxes.get(n-2).addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                	for(int i = 0; i<n; i++) {
                		if((i!=(n-1))&&(i!=(n-2))) {
                			checkboxes.get(i).setSelected(false);
                			checkboxes.get(i).setEnabled(false);
                		}
                	}
                }else {
                	for(int i = 0; i<n; i++) {
                		if((i!=(n-1))&&(i!=(n-2))) {
                			checkboxes.get(i).setEnabled(true);
                		}
                	}
                }
            }
        });
        checkboxes.get(n-2).addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                	for(int i = 0; i<n; i++) {
                		if((i!=(n-1))&&(i!=(n-2))) {
                			checkboxes.get(i).setSelected(false);
                			checkboxes.get(i).setEnabled(false);
                		}
                	}
                }else {
                	for(int i = 0; i<n; i++) {
                		if((i!=(n-1))&&(i!=(n-2))) {
                			checkboxes.get(i).setEnabled(true);
                		}
                	}
                }
            }
        });
        checkboxes.get(n-1).addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                	for(int i = 0; i<n; i++) {
                		if((i!=(n-1))&&(i!=(n-2))) {
                			checkboxes.get(i).setSelected(false);
                			checkboxes.get(i).setEnabled(false);
                		}
                	}
                }else {
                	for(int i = 0; i<n; i++) {
                		if((i!=(n-1))&&(i!=(n-2))) {
                			checkboxes.get(i).setEnabled(true);
                		}
                	}
                }
            }
        });
        
        run.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            	for(int i = 0; i<n; i++) {
            		if(checkboxes.get(i).isSelected()) {
            			a[i]=1;
            		}else {
            			a[i]=0;
            		}
            		checkboxes.get(i).setEnabled(false);
            	}
            	run.setEnabled(false);
            	new Thread(main).start();
            }
        });
	}

}