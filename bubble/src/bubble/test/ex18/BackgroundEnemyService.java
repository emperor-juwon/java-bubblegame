package bubble.test.ex18;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

//메인스레드 바쁨 - 키보드 이벤트를 처리하기 바쁘다. 
//background에서 계쏙 관찰함!!
public class BackgroundEnemyService implements Runnable {

	private BufferedImage image;
	private Enemy enemy;

	// player, bubble
	public BackgroundEnemyService(Enemy enemy) {
		this.enemy = enemy;
		try {
			image = ImageIO.read(new File("image/backgroundMapService.png"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void run() {
		while (enemy.getState() == 0) {

			// 색상 확인
			Color leftcolor = new Color(image.getRGB(enemy.getX() - 10, enemy.getY() + 25));
			Color rightcolor = new Color(image.getRGB(enemy.getX() + 50 + 15, enemy.getY() + 25));

			// -2가 나온다는 뜻은 바닥에 색깔이 없이 흰색이라는 뜻!!
			int bottomcolor = image.getRGB(enemy.getX() + 10, enemy.getY() + 50 + 5)
					+ image.getRGB(enemy.getX() + 50 - 10, enemy.getY() + 50 + 5);

			// 바닥 충돌 확인
			if (bottomcolor != -2) {
				// System.out.println("bottom" + bottomcolor);
				// System.out.println("바닥충돌함");
				enemy.setDown(false);
			} else { // 오른쪽 왼쪽으로만 이동할 때,,!! (bottomcolor가 -2일 때!)
				if (!enemy.isUp() && !enemy.isDown()) {
					enemy.down();
				}
			}

			// 외벽 충돌 확인
			if (leftcolor.getRed() == 255 && leftcolor.getGreen() == 0 && leftcolor.getBlue() == 0) {
				enemy.setLeft(false);
				if (!enemy.isRight()) {
					enemy.right();
				}
			} else if (rightcolor.getRed() == 255 && rightcolor.getGreen() == 0 && rightcolor.getBlue() == 0) {
				enemy.setRight(false);
				if (!enemy.isLeft()) {
					enemy.left();
				}
			}
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

}
