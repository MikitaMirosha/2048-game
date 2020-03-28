package mirosha.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import mirosha.game.DisplayObject;
import mirosha.game.Game;
import mirosha.game.Leaders;

public class PanelScores extends PanelButton { // ����� �������� ������� �����
	
	private Leaders scores;
	// ������������ ����������� ����:
	private int buttonW = 100;
	private int buttonH = 50;
	private int buttonY = 120;
	private int scoresX = 130;
	private int scoresY = buttonY + buttonH + 90;
	private int buttonDistance = 20;
	private int backButtonW = 220; 
	
	// ����� �����:
	private String header = "����";
	private Font headerFont = Game.main.deriveFont(45f);
	private Font scoreFont = Game.main.deriveFont(32f);
	private State currentState = State.SCORE;
	
	private enum State { SCORE, CUBE }
	
	public PanelScores() { // � ������������ ������ ����� ��������� � �������������� ������
		super(); 
		scores = Leaders.getInstance();
		scores.loadScores();

		// �������� � ��������� ������ ����
		Button cube = new Button(Game.WIDTH / 2 - buttonW / 2, buttonY, buttonW, buttonH);
		cube.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				currentState = State.CUBE;
			}
		});
		cube.setText("����");
		addButton(cube);
		
		// �������� � ��������� ������ ����
		Button score = new Button(Game.WIDTH / 2 - buttonW / 2 - cube.getWidth() - buttonDistance, buttonY, buttonW, buttonH);
		score.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				currentState = State.SCORE;
			}
		});
		score.setText("����");
		addButton(score);
		
		// �������� � ��������� ������ �����
		Button back = new Button(Game.WIDTH / 2 - backButtonW / 2, 500, backButtonW, 60);
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Screen.getInstance().setPanel("Menu"); // ����� � ������� ����
			}
		});
		back.setText("�����");
		addButton(back);
	}
	
	// ������������ ����� � ������: 
	private ArrayList<String> convertToStrings(ArrayList<? extends Number> listNum) {
		ArrayList<String> get = new ArrayList<String>();
		for(Number num : listNum){
			get.add(num.toString());
		}
		return get;
	}
	
	private void printScores(Graphics2D graphics) { // ��������� �����
		ArrayList<String> strings = new ArrayList<String>();
		if(currentState == State.SCORE) {
			strings = convertToStrings(scores.getTopScores());
		}
		else if(currentState == State.CUBE) {
			strings = convertToStrings(scores.getTopCubes());
		}
		graphics.setColor(Color.white);
		graphics.setFont(scoreFont);
		
		for(int i = 0; i < strings.size(); i++) { // �������� ������ � �������
			String str = (i + 1) + ". " + strings.get(i);
			graphics.drawString(str, scoresX, scoresY + i * 40);
		}
	}
	
	@Override
	public void renderPanel(Graphics2D graphics) { // �������� ������
		super.renderPanel(graphics);
		graphics.setColor(Color.black);
		graphics.drawString(header, Game.WIDTH / 2 - DisplayObject.getObjectWidth(header, headerFont, graphics) / 2, DisplayObject.getObjectHeight(header, headerFont, graphics) + 40);
		printScores(graphics);
	}
	
	@Override
	public void updatePanel() {}
}
