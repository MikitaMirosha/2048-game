package mirosha.game;
  
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Класс для работы с лучшими результатами игры
 * @author Mirosha
 * @version 1.0
 */
public class Leaders { 

	/** Поле лучшие результаты игры*/
	private static Leaders leads;
	
	/** Поле путь к файлу*/
	private String path; 
	
	/** Поле максимальные очки*/
	private String highScores;
	
	/** Поле хранение счета лучших очков*/
	private ArrayList<Integer> topScores;
	
	/** Поле хранение счета лучших кубов*/
	private ArrayList<Integer> topCubes;
	
	/** 
     * Конструктор - создание нового объекта лучшие результаты игры
     */
	private Leaders() {
		try {
			path = new File("").getAbsolutePath();	// абсолютный путь к файлу
			System.out.println(path);
		} catch (Throwable ex) {
			System.out.println("Произошла ошибка при работе с файлом");
			ex.getMessage();
		} 
		highScores = "Scores";
		topScores = new ArrayList<Integer>();
		topCubes = new ArrayList<Integer>();
	}
	
	/** Метод добавление нового экземпляра (Singleton)*/
	public static Leaders getInstance() { 
		if(leads == null){
			leads = new Leaders();
		}
		return leads;
	}
	
	/**
     * Процедура добавление лучшего результата очков
     * @param score - счет
     */
	public void addScore(int score) { 
		for(int i = 0; i < topScores.size(); i++) {
			if(score >= topScores.get(i)) {
				topScores.add(i, score);
				topScores.remove(topScores.size() - 1);
				return;
			}
		}
	}

	/**
     * Процедура добавление лучшего результата кубов
     * @param cubeValue - значение куба
     */
	public void addCube(int cubeValue) { 
		for(int i = 0; i < topCubes.size(); i++) {
			if(cubeValue >= topCubes.get(i)) {
				topCubes.add(i, cubeValue);
				topCubes.remove(topCubes.size() - 1);
				return;
			}
		}
	}
	
	/**
     * Процедура формирование данных результата в файле
     */
	private void formSaveData() { 
		try {
			File file = new File(path, highScores);
			FileWriter output = new FileWriter(file);
			BufferedWriter writer = new BufferedWriter(output);
			writer.write("0-0-0-0-0"); 		// заполнение нулями
			writer.newLine();
			writer.write("0-0-0-0-0");
			writer.newLine(); // установка максимального значения счета: 
			writer.write(Integer.MAX_VALUE + "-" + Integer.MAX_VALUE + "-" + Integer.MAX_VALUE + "-" + Integer.MAX_VALUE + "-" + Integer.MAX_VALUE);
			writer.close();
		} catch (Throwable ex) {
			System.out.println("Произошла ошибка при работе с файлом");
			ex.getMessage();
		}
	}
	
	/**
     * Процедура сохранение результатов игры в файл
     */
	public void saveScores() { 
		FileWriter output = null;
		try {
			File file = new File(path, highScores);
			output = new FileWriter(file);
			BufferedWriter writer = new BufferedWriter(output);
			// добавление нового результата между лучшим и худшим результатом, разделяя минусами:
			writer.write(topScores.get(0) + "-" + topScores.get(1) + "-" + topScores.get(2) + "-" + topScores.get(3) + "-" + topScores.get(4));
			writer.newLine();
			writer.write(topCubes.get(0) + "-" + topCubes.get(1) + "-" + topCubes.get(2) + "-" + topCubes.get(3) + "-" + topCubes.get(4));
			writer.newLine();
			writer.close();
		} catch (Throwable ex) {
			System.out.println("Произошла ошибка при работе с файлом");
			ex.getMessage();
		}
	}
	
	/**
     * Процедура загрузка счета в таблицу
     */
	public void loadScores() { 
		try {
			File file = new File(path, highScores);
			if (!file.isFile()) {
				formSaveData();
			}
			// чтение текста из потока ввода символов
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			topScores.clear(); // очистка предыдущего счета
			topCubes.clear();
			
			String[] scores = reader.readLine().split("-");
			String[] cubes = reader.readLine().split("-");

			for (int i = 0; i < scores.length; i++) {
				topScores.add(Integer.parseInt(scores[i]));
			}
			for (int i = 0; i < cubes.length; i++) {
				topCubes.add(Integer.parseInt(cubes[i]));
			}
	
			reader.close();
		} catch (Throwable ex) {
			System.out.println("Произошла ошибка при работе с файлом");
			ex.getMessage();
		}
	}
	
	/**
     * Функция получения значения поля {@link #topScores}
     * @return возвращает лучший счет игры
     */
	public ArrayList<Integer> getTopScores() {
		return topScores;
	}

	/**
     * Функция получения значения поля {@link #topCubes}
     * @return возвращает лучшее значение куба
     */
	public ArrayList<Integer> getTopCubes() {
		return topCubes;
	}
  
	/**
     * Функция получения значения поля {@link #highScores}
     * @return возвращает максимальные очки игры
     */
	public int getHighScore() {
		return topScores.get(0);
	}  
}
