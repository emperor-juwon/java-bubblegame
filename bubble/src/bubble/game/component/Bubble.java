package bubble.game.component;

import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import bubble.game.BubbleFrame;
import bubble.game.Moveable;
import bubble.game.service.BackgroundBubbleService;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Bubble extends JLabel implements Moveable {

	// 의존성 컴포지션
	private Player player;
	private BackgroundBubbleService backgroundBubbleService;
	private BubbleFrame mContext;
	private List<Enemy> enemys;
	private Enemy removeEnemy = null;

	// 위치 상태
	private int x;
	private int y;

	// 움직임 상태
	private boolean left;
	private boolean right;
	private boolean up;

	// 적군을 맞춘 상태
	private int state; // 0(물방울), 1(적을 가둔 물방울)
	private ImageIcon bubble; // 물방울
	private ImageIcon bubbled; // 적을 가둔 물방울
	private ImageIcon bomb; // 물방울이 터진 상태

	public Bubble(BubbleFrame mContext) {
		this.mContext = mContext;
		this.player = mContext.getPlayer();
		this.enemys = mContext.getEnemys();
		initObject();
		initSetting();
	}

	private void initObject() {
		bubble = new ImageIcon("image/bubble.png");
		bubbled = new ImageIcon("image/bubbled.png");
		bomb = new ImageIcon("image/bomb.png");

		backgroundBubbleService = new BackgroundBubbleService(this);
	}

	private void initSetting() {
		up = false;
		left = false;
		right = false;

		x = player.getX();
		y = player.getY();

		setIcon(bubble);
		setSize(50, 50);

		state = 0;

	}

	@Override
	public void left() {
		left = true;
		for (int i = 0; i < 400; i++) {
			x--;
			setLocation(x, y);

			if (backgroundBubbleService.leftWall()) {
				left = false;
				break;
			}

			for (Enemy e : enemys) {
				if (Math.abs(x - e.getX()) < 10 && Math.abs(y - e.getY()) > 0 && Math.abs(y - e.getY()) < 50) {
					if (e.getState() == 0) {
						attack(e);
						break;
					}
				}
			}

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		up();
	}

	@Override
	public void right() {
		right = true;
		for (int i = 0; i < 400; i++) {
			x++;
			setLocation(x, y);

			if (backgroundBubbleService.rightWall()) {
				right = false;
				break;
			}

			for (Enemy e : enemys) {
				if (Math.abs(x - e.getX()) < 10 && Math.abs(y - e.getY()) > 0 && Math.abs(y - e.getY()) < 50) {
					if (e.getState() == 0) {
						attack(e);
						break;
					}
				}
			}

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		up();
	}

	@Override
	public void up() {
		up = true;
		while (up) {
			y--;
			setLocation(x, y);

			if (backgroundBubbleService.topWall()) {
				up = false;
				break;
			}

			try {
				if (state == 0) { // 기본 물방울
					Thread.sleep(1);
				} else { // 적을 가둔 물방울
					Thread.sleep(10);
				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (state == 0) {
			clearBubble(); // up이 끝나고 버블이 천장에 도착하고 나서 3초 후 메모리에서 소멸시키자
		}
	}

	@Override
	public void attack(Enemy e) {
		state = 1;
		e.setState(1);
		setIcon(bubbled);
		removeEnemy = e;
		mContext.remove(e); // memory에서 사라지게!!
		mContext.repaint();
	}

	// 행위 -> clear -> bubble
	private void clearBubble() {
		try {
			Thread.sleep(3000);
			setIcon(bomb);
			Thread.sleep(2000);
			// 버블 객체 메모리에서 날리기
			mContext.getPlayer().getBubbleList().remove(this);
			mContext.remove(this); // bubbleframe의 bubble이 메모리에서 소멸
			mContext.repaint(); // bubbleframe의 전체를 다시 그림(메모리에 없는건 안그림)
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clearBubbled() {
		new Thread(() -> {
			System.out.println("clearBubbled");
			try {
				up = false;
				setIcon(bomb);
				Thread.sleep(1000);
				// 버블 객체 메모리에서 날리기
				mContext.getPlayer().getBubbleList().remove(this);
				mContext.getEnemys().remove(removeEnemy);
				mContext.remove(this);
				mContext.repaint();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}
}
