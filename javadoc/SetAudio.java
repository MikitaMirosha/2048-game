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
 * Класс для работы со звуком
 * @author Mirosha
 * @version 1.0
 */
public class SetAudio {

	/** Поле установка звука*/
	private static SetAudio setter;
	
	/** Поле для хранения всех звуков в hash map*/
	private HashMap<String, Clip> audio; 

	/** 
     * Конструктор - создание нового объекта звука для занесения в hash map
     */
	private SetAudio() {  
		audio = new HashMap<String, Clip>(); 
	}

	/** Метод добавление нового экземпляра (Singleton)*/
	public static SetAudio getInstance() {
		if (setter == null) { 
			setter = new SetAudio(); 
		}
		return setter;
	}
	
	/**
     * Процедура воспроизведение аудио
     * @param name - название звука
     * @param loopAudio - зацикливание звука
     */
	public void playAudio(String name, int loopAudio) {
		if(audio.get(name).isRunning()) { 					// если звук воспроизводится, накладываясь эффектом на эффект,
			audio.get(name).stop(); 						// то останавливаем его в случае ошибки
		}
		audio.get(name).setFramePosition(0); 				// установка звука на начало фрейма
		audio.get(name).loop(loopAudio); 
	}
	
	/**
     * Процедура регулировка громкости звука
     * @param name - название звука
     * @param value - величина громкости
     */
	public void adjustVolume(String name, int value) { 
		FloatControl floatControl = (FloatControl)audio.get(name).getControl(FloatControl.Type.MASTER_GAIN);
		floatControl.setValue(value);
	}

	/**
     * Процедура загрузки аудио в игру из файла
     * @param path - путь к файлу с аудио
     * @param name - название аудио
     */
	public void loadAudio(String path, String name) {
		
		URL res = SetAudio.class.getClassLoader().getResource(path); // установка пути к файлу

		AudioInputStream input = null; 
		try {
			input = AudioSystem.getAudioInputStream(res); 	// получение доступа к аудио
		} catch (UnsupportedAudioFileException ex) { 
			System.out.println("Произошла ошибка при работе с аудио");
			ex.getMessage();
		} catch (IOException ex) {  
			System.out.println("Произошла ошибка при работе с аудио");
			ex.getMessage();
		}
		
		// проверка на то является ли формат PCM, а не MP3 (WAV) 
		AudioFormat formatPCM = input.getFormat();
		if(formatPCM.getEncoding() == AudioFormat.Encoding.PCM_SIGNED) { // если PCM
			try{
				Clip clip = AudioSystem.getClip(); // создание Clip (Clip - это interface)
				clip.open(input); 
				audio.put(name, clip); 
				return;
			}
			catch(Throwable ex) {
				System.out.println("Произошла ошибка при работе с аудио");
				ex.getMessage();
			}
		}
		
		// если формат не PCM, то происходит декодирование формата
		AudioFormat decode = new AudioFormat(
				AudioFormat.Encoding.PCM_SIGNED, // кодировка для использования
				formatPCM.getSampleRate(), 		 // частота дискретизации 
				16, 						     // размер выборки в битах
				formatPCM.getChannels(),         // получение каналов
				formatPCM.getChannels() * 2,     // размер кадра 
				formatPCM.getSampleRate(),       // частота смены кадра
				false);

		// создание нового входного потока для нового формата 
		AudioInputStream decodedNewFormat = AudioSystem.getAudioInputStream(decode, input);

		Clip clip = null;
		try {
		    clip = AudioSystem.getClip();  
			clip.open(decodedNewFormat); 		// открытие в декодированном формате
		} catch (Throwable ex) {
			System.out.println("Произошла ошибка при работе с аудио");
			ex.getMessage();
		}
		audio.put(name, clip); 
	}
}