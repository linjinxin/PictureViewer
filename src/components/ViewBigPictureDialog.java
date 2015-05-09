package components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileSystemView;

//查看图片窗口
public class ViewBigPictureDialog extends JDialog {
	JToolBar toolBar;
	JButton nextPicture, previousPicture, play;
	JLabel pictureLabel;
	ArrayList<File> pictures;
	File currentPicture;
	boolean isPlay = false;

	public ViewBigPictureDialog(ArrayList<File> pictures, File currentPicture) {
		super();
		this.pictures = pictures;
		this.currentPicture = currentPicture;

		pictureLabel = new JLabel(new ImageIcon(
				currentPicture.getAbsolutePath()));

		toolBar = new JToolBar();
		nextPicture = new JButton("下一张");
		play = new JButton("开始播放");
		previousPicture = new JButton("上一张");

		this.addListener();
		toolBar.setLayout(new FlowLayout(FlowLayout.CENTER));
		toolBar.add(previousPicture);
		toolBar.add(play);
		toolBar.add(nextPicture);

		JRootPane rootPane = this.getRootPane();
		rootPane.setLayout(new BorderLayout());
		rootPane.add(new JScrollPane(pictureLabel), BorderLayout.CENTER);
		rootPane.add(toolBar, BorderLayout.SOUTH);
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		this.setModal(true);
		this.setSize((int)screen.getWidth(), (int)screen.getHeight());
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	private void addListener() {
		// TODO Auto-generated method stub
		previousPicture.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int currentPictureIndex = pictures.indexOf(currentPicture);
				File changedPicture;
				if (currentPictureIndex > 0) {
					changedPicture = pictures.get(currentPictureIndex - 1);
				} else {
					changedPicture = pictures.get(currentPictureIndex
							+ pictures.size() - 1);
				}
				pictureLabel.setIcon(new ImageIcon(changedPicture
						.getAbsolutePath()));
				currentPicture = changedPicture;
			}

		});
		nextPicture.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int currentPictureIndex = pictures.indexOf(currentPicture);
				File changedPicture;
				if (currentPictureIndex < pictures.size() - 1) {
					changedPicture = pictures.get(currentPictureIndex + 1);
				} else {
					changedPicture = pictures.get(currentPictureIndex
							- pictures.size() + 1);
				}
				pictureLabel.setIcon(new ImageIcon(changedPicture
						.getAbsolutePath()));
				currentPicture = changedPicture;
			}

		});
		play.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Thread thread = new Thread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						int currentPictureIndex = pictures.indexOf(currentPicture);
						for (int i = currentPictureIndex; i < pictures.size(); i++) {
							/* if (isPlay) { */
							
							pictureLabel.setIcon(new ImageIcon(pictures.get(i)
									.getAbsolutePath()));
							try {
								System.out.println("before sleep");
								Thread.sleep(2000);
								System.
								out.println("after sleep");
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							if (i + 1 == pictures.size()) {
								i = 0;
							}
						}
					}
					
				});
				thread.start();
			}
		});
	}
}
