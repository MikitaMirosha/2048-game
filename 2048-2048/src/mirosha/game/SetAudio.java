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

public class SetAudio { // класс дл€ работы со звуком

	private static SetAudio setter;
	private HashMap<String, Clip> audio; // хранит все звуки, Clip дл€ обработки звука

	private SetAudio() {  // заносим audio в HashMap дл€ хранени€ всех эффектов
		audio = new HashMap<String, Clip>(); // и возможности обращени€ по имени
	}

	public static SetAudio getInstance() {
		if (setter == null) { // провер€ет, если мы имеем экземпл€р, равный null
			setter = new SetAudio(); // то создаем его
		}
		return setter;
	}
	
	public void playAudio(String name, int loopAudio) {
		if(audio.get(name).isRunning()) { // если звук возпроизводитс€, накладыва€сь эффектом на эффект
			audio.get(name).stop(); // останавливаем его в случае ошибки
		}
		audio.get(name).setFramePosition(0); // устанавливаем звук на начало фрейма
		audio.get(name).loop(loopAudio); // зацикливаем
	}
	
	public void adjustVolume(String name, int value) { // дл€ регулировки громкости
		FloatControl floatControl = (FloatControl)audio.get(name).getControl(FloatControl.Type.MASTER_GAIN);
		floatControl.setValue(value);
	}

	public void loadAudio(String path, String name) {
		
		URL res = SetAudio.class.getClassLoader().getResource(path); // устанавливаем путь к файлу

		// получить аудио вход из файла и базового формата 
		AudioInputStream input = null; // null т.к. далее try-catch
		try {
			input = AudioSystem.getAudioInputStream(res); // пытаемс€ получить доступ к аудио
		} catch (UnsupportedAudioFileException ex) { // unsupported audio
			ex.printStackTrace();
		} catch (IOException ex) {  // input/output exception
			ex.printStackTrace();
		}
		
		// провер€ем €вл€етс€ ли формат PCM, а не MP3 (WAV)
		AudioFormat formatPCM = input.getFormat();
		if(formatPCM.getEncoding() == AudioFormat.Encoding.PCM_SIGNED) { // если PCM
			try{
				Clip clip = AudioSystem.getClip(); // создаем clip (clip - это interface)
				clip.open(input); // открываем
				audio.put(name, clip); // заносим в hash map
				return;
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
		// если формат не PCM, то декодируем наш формат
		AudioFormat decode = new AudioFormat(
				AudioFormat.Encoding.PCM_SIGNED, // кодировка дл€ использовани€
				formatPCM.getSampleRate(), // частота дискретизации 
				16, // размер выборки в битах
				formatPCM.getChannels(), // получить каналы
				formatPCM.getChannels() * 2, // размер кадра 
				formatPCM.getSampleRate(), // частота смены кадра
				false);

		// создаем новый входной поток дл€ нового формата 
		AudioInputStream decodedNewFormat = AudioSystem.getAudioInputStream(decode, input);

		Clip clip = null;
		try {
		    clip = AudioSystem.getClip();  // создаем clip
			clip.open(decodedNewFormat); // открываем в декодированном формате
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		audio.put(name, clip); // заносим в hash map
	}
}