package bubble.test.ex07;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

//메인스레드 바쁨 - 키보드 이벤트를 처리하기 바쁘다. 
//background에서 계쏙 관찰함!!
public class BackgroundPlayerService implements Runnable {

	private BufferedImage image;
	private Player player;

	public BackgroundPlayerService(Player player) {
		this.player = player;

		try {
			image = ImageIO.read(new File("image/backgroundMapService.png"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void run() {

		while (true) {
			Color leftcolor = new Color(image.getRGB(player.getX() - 10, player.getY() + 25));
			Color rightcolor = new Color(image.getRGB(player.getX() + 50 + 15, player.getY() + 25));

			// System.out.println("left색상 = " + leftcolor);
			// System.out.println("rigth색상 = " + rightcolor);

			if (leftcolor.getRed() == 255 && leftcolor.getGreen() == 0 && leftcolor.getBlue() == 0) {
				System.out.println("왼쪽벽 충돌");
				player.setLeftWallCrash(true);
				player.setLeft(false);
			} else if (rightcolor.getRed() == 255 && rightcolor.getGreen() == 0 && rightcolor.getBlue() == 0) {
				System.out.println("오른쪽벽 충돌");
				player.setRightWallCrash(true);
				player.setRight(false);
			} else {
				player.setLeftWallCrash(false);
				player.setRightWallCrash(false);
			}
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

}
