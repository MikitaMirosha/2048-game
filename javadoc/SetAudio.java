package mirosha.game;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * ����� ��� ������ �� ������
 * @author Mirosha
 * @version 1.0
 */
public class SetAudio {

	/** ���� ��������� �����*/
	private static SetAudio setter;
	
	/** ���� ��� �������� ���� ������ � hash map*/
	private HashMap<String, Clip> audio; 

	/** 
     * ����������� - �������� ������ ������� ����� ��� ��������� � hash map
     */
	private SetAudio() {  
		audio = new HashMap<String, Clip>(); 
	}

	/** ����� ���������� ������ ���������� (Singleton)*/
	public static SetAudio getInstance() {
		if (setter == null) { 
			setter = new SetAudio(); 
		}
		return setter;
	}
	
	/**
     * ��������� ��������������� �����
     * @param name - �������� �����
     * @param loopAudio - ������������ �����
     */
	public void playAudio(String name, int loopAudio) {
		if(audio.get(name).isRunning()) { 					// ���� ���� ���������������, ������������ �������� �� ������,
			audio.get(name).stop(); 						// �� ������������� ��� � ������ ������
		}
		audio.get(name).setFramePosition(0); 				// ��������� ����� �� ������ ������
		audio.get(name).loop(loopAudio); 
	}
	
	/**
     * ��������� ����������� ��������� �����
     * @param name - �������� �����
     * @param value - �������� ���������
     */
	public void adjustVolume(String name, int value) { 
		FloatControl floatControl = (FloatControl)audio.get(name).getControl(FloatControl.Type.MASTER_GAIN);
		floatControl.setValue(value);
	}

	/**
     * ��������� �������� ����� � ���� �� �����
     * @param path - ���� � ����� � �����
     * @param name - �������� �����
     */
	public void loadAudio(String path, String name) {
		
		URL res = SetAudio.class.getClassLoader().getResource(path); // ��������� ���� � �����

		AudioInputStream input = null; 
		try {
			input = AudioSystem.getAudioInputStream(res); 	// ��������� ������� � �����
		} catch (UnsupportedAudioFileException ex) { 
			System.out.println("��������� ������ ��� ������ � �����");
			ex.getMessage();
		} catch (IOException ex) {  
			System.out.println("��������� ������ ��� ������ � �����");
			ex.getMessage();
		}
		
		// �������� �� �� �������� �� ������ PCM, � �� MP3 (WAV) 
		AudioFormat formatPCM = input.getFormat();
		if(formatPCM.getEncoding() == AudioFormat.Encoding.PCM_SIGNED) { // ���� PCM
			try{
				Clip clip = AudioSystem.getClip(); // �������� Clip (Clip - ��� interface)
				clip.open(input); 
				audio.put(name, clip); 
				return;
			}
			catch(Throwable ex) {
				System.out.println("��������� ������ ��� ������ � �����");
				ex.getMessage();
			}
		}
		
		// ���� ������ �� PCM, �� ���������� ������������� �������
		AudioFormat decode = new AudioFormat(
				AudioFormat.Encoding.PCM_SIGNED, // ��������� ��� �������������
				formatPCM.getSampleRate(), 		 // ������� ������������� 
				16, 						     // ������ ������� � �����
				formatPCM.getChannels(),         // ��������� �������
				formatPCM.getChannels() * 2,     // ������ ����� 
				formatPCM.getSampleRate(),       // ������� ����� �����
				false);

		// �������� ������ �������� ������ ��� ������ ������� 
		AudioInputStream decodedNewFormat = AudioSystem.getAudioInputStream(decode, input);

		Clip clip = null;
		try {
		    clip = AudioSystem.getClip();  
			clip.open(decodedNewFormat); 		// �������� � �������������� �������
		} catch (Throwable ex) {
			System.out.println("��������� ������ ��� ������ � �����");
			ex.getMessage();
		}
		audio.put(name, clip); 
	}
}