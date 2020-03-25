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


public class SetAudio { // класс для работы со звуком

	private static SetAudio setter;
	private HashMap<String, Clip> sound; // хранит все звуки, Clip для обработки звука

	private SetAudio() { // заносим sound в HashMap для хранения всех эффектов
		sound = new HashMap<String, Clip>(); // и возможности обращения по имени
	}

	public static SetAudio getSample() {
		if (setter == null) { // проверяет, если мы имеем экземпляр, равный null
			setter = new SetAudio(); // то создаем его
		}
		return setter;
	}
	
	public void playSound(String name, int loopSound) { 
		if(sound.get(name).isRunning()) { // если звук возпроизводится, накладываясь эффектом на эффект
			sound.get(name).stop(); // останавливаем его в случае ошибки
		}
		sound.get(name).setFramePosition(0); // устанавливаем звук на начало фрейма
		sound.get(name).loop(loopSound); // зацикливаем
	}
	
	public void controlVolume(String name, int value) { // для регулировки громкости
		FloatControl fControl = (FloatControl)sound.get(name).getControl(FloatControl.Type.MASTER_GAIN);
		fControl.setValue(value); 
	}

	public void loadSound(String path, String name) {
		URL res = SetAudio.class.getClassLoader().getResource(path); // устанавливаем путь к файлу

		// получить аудио вход из файла и базового формата 
		AudioInputStream input = null; // null т.к. далее try-catch
		try {
			input = AudioSystem.getAudioInputStream(res); // пытаемся получить доступ к аудио
		} catch (UnsupportedAudioFileException ex) { // unsupported audio
			ex.printStackTrace();
		} catch (IOException ex) { // input/output exception
			ex.printStackTrace();
		}
		
		// проверяем является ли формат PCM, а не MP3 (WAV)
		AudioFormat formatPCM = input.getFormat(); 
		if(formatPCM.getEncoding() == AudioFormat.Encoding.PCM_SIGNED) { // если PCM
			try {
				Clip clip = AudioSystem.getClip(); // создаем clip (clip - это interface)
				clip.open(input); // открываем
				sound.put(name, clip); // заносим в hash map
				return;
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		// если формат не PCM, то декодируем наш формат
		AudioFormat decode = new AudioFormat(
				AudioFormat.Encoding.PCM_SIGNED, // кодировка для использования
				formatPCM.getSampleRate(), // частота дискретизации 
				16, // размер выборки в битах
				formatPCM.getChannels(), // получить каналы
				formatPCM.getChannels() * 2, // размер кадра 
				formatPCM.getSampleRate(), // частота смены кадра
				false);

		// создаем новый входной поток для нового формата 
		AudioInputStream decodedNewFormat = AudioSystem.getAudioInputStream(decode, input);

		Clip clip = null;
		try {
		    clip = AudioSystem.getClip(); // создаем clip
			clip.open(decodedNewFormat); // открываем в декодированном формате
		} catch (Exception e) {
			e.printStackTrace();
		}
		sound.put(name, clip); // заносим в hash map
	}
}
