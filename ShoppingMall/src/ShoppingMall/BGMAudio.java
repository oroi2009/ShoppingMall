package ShoppingMall;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.sound.sampled.*;
import java.io.*;

public class BGMAudio extends JPanel {
	JButton control;
	private Clip clip;
	BGMAudio() {
		setBackground(new Color(40, 40, 40));
		control = new JButton(" |   | ");
		control.addActionListener(new ControlMusic());
		control.setBackground(new Color(255, 255, 255));
		control.setOpaque(true);
		add(control);
		loadAudio("./BGM.wav");
		clip.start();
	}
	public void close() { clip.close(); }
	// 오디오 정지 및 리소스 해제
    public void stopAudio() {
        if (clip != null) {
            clip.stop();
            clip.close();
            clip = null;
        }
    }
	private void loadAudio(String path) {
		try {
			stopAudio(); // 기존 클립 정리
			clip = AudioSystem.getClip();
			File audioFile = new File(path);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			clip.open(audioStream);
			// 무한 반복 재생 설정
            clip.loop(Clip.LOOP_CONTINUOUSLY);
		}
		catch (LineUnavailableException e) { e.printStackTrace(); }
		catch (UnsupportedAudioFileException e) { e.printStackTrace(); }
		catch (IOException e) { e.printStackTrace(); }
	}
	class ControlMusic implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("▶︎")) {
				clip.start();
				JButton control = (JButton) e.getSource();
				control.setText(" |   | ");
			}
			else {
				clip.stop();
				JButton control = (JButton) e.getSource();
				control.setText("▶︎");
			}
		}
	}
}