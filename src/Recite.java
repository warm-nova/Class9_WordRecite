import javafx.scene.control.Slider;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.List;
import java.util.ArrayList;

public class Recite extends JFrame{ 
	JLabel lblWord = new JLabel("word"); 
	JLabel lblMeaning = new JLabel("meaning");
	//Speed control
	JLabel speedTips = new JLabel("Word Speed(word/s):");
	JSlider wordSpeed = new JSlider(1,10,1);

	JButton startrecite = new JButton("Start");
	JButton endrecite = new JButton("End");
	Thread wordlist;
	int DelaySet = 1000;
	public volatile boolean exit = false;

	public void init() {
		setSize( 400,200 );
		//更换布局方式
		setLayout(new FlowLayout());
		Box basebox,wordsline,speedline,tagline;
		basebox = Box.createVerticalBox();
		wordsline = Box.createHorizontalBox();
		speedline = Box.createHorizontalBox();
		tagline = Box.createHorizontalBox();

		basebox.add(wordsline);
		basebox.add(speedline);
		basebox.add(tagline);
		wordsline.add(lblWord);
		wordsline.add(Box.createGlue());
		wordsline.add(lblMeaning);
		speedline.add(speedTips);
		speedline.add(wordSpeed);
		tagline.add(startrecite);
		tagline.add(Box.createGlue());
		tagline.add(endrecite);

		/**
		getContentPane().add(lblWord);
		getContentPane().add(lblMeaning);
		getContentPane().add(speedTips);
		getContentPane().add(wordSpeed,"Word Speed:");
		*/
		wordSpeed.setMajorTickSpacing(1);
		wordSpeed.setPaintLabels(true);
		wordSpeed.setPaintTicks(true);
		wordSpeed.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				DelaySet=wordSpeed.getValue()*1000;
				System.out.println("当前值: " + DelaySet);
			}
		});

		startrecite.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				start(DelaySet);
			}
		});



		getContentPane().add(basebox);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}


	List<String> words = new ArrayList<>();
	List<String> meanings = new ArrayList<>();
	int current = 0;


	public void start(int DelaySet) {

		wordlist = new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					readAll();
				}catch(IOException ex){}
				Timer sd =new Timer(DelaySet, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						lblWord.setText(words.get(current));
						lblMeaning.setText(meanings.get(current));
						current++;
					}
				});
				sd.start();
				endrecite.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						sd.stop();
						//System.out.println("STOP");
					}
				});
			}
		});
		wordlist.run();

	}



	public void readAll( ) throws IOException{
		String fileName = "College_Grade4.txt";
		String charset = "GB2312";
		BufferedReader reader = new BufferedReader(
			new InputStreamReader(
				new FileInputStream(fileName), charset)); 
		String line; 
		while ((line = reader.readLine()) != null) { 
			line = line.trim();
			if( line.length() == 0 ) continue;
			int idx = line.indexOf("\t");
			System.out.println(idx);
			int idx2 = line.lastIndexOf("\t");
			words.add( line.substring(0, idx));
			meanings.add( line.substring(idx+1,idx2));

		} 
		reader.close();
	}

	public static void main( String[] args){
		Recite f = new Recite();
		f.init();
		//f.start();
	}
} 
