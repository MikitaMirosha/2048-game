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

public class SetAudio { // ����� ��� ������ �� ������

	private static SetAudio setter;
	private HashMap<String, Clip> audio; // ������ ��� �����, Clip ��� ��������� �����

	private SetAudio() {  // ������� audio � HashMap ��� �������� ���� ��������
		audio = new HashMap<String, Clip>(); // � ����������� ��������� �� �����
	}

	public static SetAudio getInstance() {
		if (setter == null) { // ���������, ���� �� ����� ���������, ������ null
			setter = new SetAudio(); // �� ������� ���
		}
		return setter;
	}
	
	public void playAudio(String name, int loopAudio) {
		if(audio.get(name).isRunning()) { // ���� ���� ���������������, ������������ �������� �� ������
			audio.get(name).stop(); // ������������� ��� � ������ ������
		}
		audio.get(name).setFramePosition(0); // ������������� ���� �� ������ ������
		audio.get(name).loop(loopAudio); // �����������
	}
	
	public void adjustVolume(String name, int value) { // ��� ����������� ���������
		FloatControl floatControl = (FloatControl)audio.get(name).getControl(FloatControl.Type.MASTER_GAIN);
		floatControl.setValue(value);
	}

	public void loadAudio(String path, String name) {
		
		URL res = SetAudio.class.getClassLoader().getResource(path); // ������������� ���� � �����

		// �������� ����� ���� �� ����� � �������� ������� 
		AudioInputStream input = null; // null �.�. ����� try-catch
		try {
			input = AudioSystem.getAudioInputStream(res); // �������� �������� ������ � �����
		} catch (UnsupportedAudioFileException ex) { // unsupported audio
			ex.printStackTrace();
		} catch (IOException ex) {  // input/output exception
			ex.printStackTrace();
		}
		
		// ��������� �������� �� ������ PCM, � �� MP3 (WAV)
		AudioFormat formatPCM = input.getFormat();
		if(formatPCM.getEncoding() == AudioFormat.Encoding.PCM_SIGNED) { // ���� PCM
			try{
				Clip clip = AudioSystem.getClip(); // ������� clip (clip - ��� interface)
				clip.open(input); // ���������
				audio.put(name, clip); // ������� � hash map
				return;
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
		// ���� ������ �� PCM, �� ���������� ��� ������
		AudioFormat decode = new AudioFormat(
				AudioFormat.Encoding.PCM_SIGNED, // ��������� ��� �������������
				formatPCM.getSampleRate(), // ������� ������������� 
				16, // ������ ������� � �����
				formatPCM.getChannels(), // �������� ������
				formatPCM.getChannels() * 2, // ������ ����� 
				formatPCM.getSampleRate(), // ������� ����� �����
				false);

		// ������� ����� ������� ����� ��� ������ ������� 
		AudioInputStream decodedNewFormat = AudioSystem.getAudioInputStream(decode, input);

		Clip clip = null;
		try {
		    clip = AudioSystem.getClip();  // ������� clip
			clip.open(decodedNewFormat); // ��������� � �������������� �������
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		audio.put(name, clip); // ������� � hash map
	}
}