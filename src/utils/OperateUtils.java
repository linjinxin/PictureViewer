package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileSystemView;

import components.TipDialog;

public class OperateUtils {
	public static void showTips(String tipText, int tipExitsTime) {
		// 弹窗提示
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				TipDialog dialog = new TipDialog(tipText);
				try {
					Thread.sleep(1000 * tipExitsTime);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				dialog.setVisible(false);
				dialog.dispose();
				dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			}
		});
		thread.start();
	}

	public static JFileChooser getDefaultFileChooser(String dialogTitle) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle(dialogTitle);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setCurrentDirectory(FileSystemView.getFileSystemView()
				.getHomeDirectory());// 设置打开的默认目录为桌面
		fileChooser.showSaveDialog(null);// 显示选择窗口
		fileChooser.setVisible(true);
		return fileChooser;
	}

	public static <T> Object deepClone(T object) throws Exception {//深克隆
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(object);
		// 从流里读回来
		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(bis);
		return ois.readObject();
	}
}
