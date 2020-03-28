package mirosha.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import mirosha.game.DisplayObject;
import mirosha.game.Game;

public class PanelMenu extends PanelButton { // ����� �������� ������� ����

	private String header = "2048"; 
	private int buttonW = 220;
	private Font headerFont = Game.main.deriveFont(100f); // ������ ������ 2048
	
	public PanelMenu() { // ����������� ������ ����
		super(); 
		Button play = new Button(Game.WIDTH / 2 - buttonW / 2, 220, buttonW, 60); // ������� ������ ����
		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Screen.getInstance().setPanel("Play");
			}
		});
		play.setText("����"); 
		addButton(play); // ��������� ������
		
		Button scores = new Button(Game.WIDTH / 2 - buttonW / 2, 310, buttonW, 60); // ������� ������ ����
		scores.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Screen.getInstance().setPanel("Leaderboards");
			}
		});
		scores.setText("����");
		addButton(scores); // ��������� ������
		
		Button quitButton = new Button(Game.WIDTH / 2 - buttonW / 2, 400, buttonW, 60);  // ������� ������ �����
		quitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		quitButton.setText("�����");
		addButton(quitButton); // ��������� ������
	}

	@Override
	public void renderPanel(Graphics2D graphics) { // �������� ���������� (������, ��������)
		super.renderPanel(graphics);
		graphics.setFont(headerFont);
		graphics.setColor(Color.white);
		graphics.drawString(header, Game.WIDTH / 2 - DisplayObject.getObjectWidth(header, headerFont, graphics) / 2, 150);
	}
}
