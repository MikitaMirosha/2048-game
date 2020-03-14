package com.mirosha.game;

import java.net.URL;
import java.util.HashMap;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class SetAudio {
	private static SetAudio setter;
	private HashMap<String, Clip> sound; // ������ ���� �����, Clip ��� ��������� �����
	
	private SetAudio() {
		sound = new HashMap<String, Clip>(); // ������ ��� ������� � ��������� �� �����
	}
	
	public static SetAudio getSample() {
		if(setter == null) {
			setter = new SetAudio();
		}
		return setter;
	}
	
	public void controlVolume(String audioName, int value) {
		FloatControl fControl = (FloatControl)sound.get(audioName).getControl(FloatControl.Type.MASTER_GAIN);
		fControl.setValue(value);
	}
	
	public void play(String audioName, int loopCount) {
		if(sound.get(audioName).isRunning()) {
			sound.get(audioName).stop();
		}
		sound.get(audioName).setFramePosition(0); // ���� �� ������ ������
		sound.get(audioName).loop(loopCount); // loopCount
	}
	
	public void loadSound(String resourcePath, String audioName) {
		// ��������� �������
		URL resource = SetAudio.class.getClassLoader().getResource(resourcePath);
		
		AudioInputStream inputAudio = null;
		try {
			inputAudio = AudioSystem.getAudioInputStream(resource);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		AudioFormat baseFormat = inputAudio.getFormat(); // � PCM ������
		
		if(baseFormat.getEncoding() == AudioFormat.Encoding.PCM_SIGNED) {
			try {
				Clip clip = AudioSystem.getClip();
				clip.open(inputAudio);
				sound.put(audioName, clip);
				return;
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		// ���� ��� �� PCM, �� ����� ������������
		AudioFormat decodedFormat = new AudioFormat (
					AudioFormat.Encoding.PCM_SIGNED,
					baseFormat.getSampleRate(),
					16,
					baseFormat.getChannels(),
					baseFormat.getChannels() * 2,
					baseFormat.getSampleRate(),
					false
				);
		
		AudioInputStream decode = AudioSystem.getAudioInputStream(decodedFormat, inputAudio);
		
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(decode);
			sound.put(audioName, clip);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
