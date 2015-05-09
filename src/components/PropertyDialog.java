package components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
/**
 * 查看属性窗口
 * @author lixiaohui
 *
 */
public class PropertyDialog extends JDialog {
	//文件名，类型，位置，大小，创建时间，修改时间，分表率，宽，高
	JTextField fileName,fileType, fileLocation, fileSize, createTime, lastModifiedTime,resolution ,width,height;

	public PropertyDialog(File picture) {
		fileName = new JTextField(picture.getName());
		fileType = new JTextField(picture.getAbsolutePath().substring(picture.getAbsolutePath().lastIndexOf(".")));
		fileLocation = new JTextField(picture.getAbsolutePath());
		fileSize = new JTextField(picture.length() + "字节");
		createTime = new JTextField(this.getCreateTime(picture.getAbsolutePath()));
		lastModifiedTime = new JTextField(new Date(picture.lastModified()).toLocaleString());
		resolution = new JTextField(new ImageIcon(picture.getAbsolutePath()).getIconWidth() + "*" + new ImageIcon(picture.getAbsolutePath()).getIconHeight());
		width = new JTextField(new ImageIcon(picture.getAbsolutePath()).getIconWidth() + "像素");
		height = new JTextField(new ImageIcon(picture.getAbsolutePath()).getIconHeight() + "像素");
		//setEditable(false);
		fileName.setEditable(false);
		fileType.setEditable(false);
		fileSize.setEditable(false);
		fileLocation.setEditable(false);
		createTime.setEditable(false);
		lastModifiedTime.setEditable(false);
		resolution.setEditable(false);
		width.setEditable(false);
		height.setEditable(false);
		//.setBorder(null);
		fileName.setBorder(null);
		fileType.setBorder(null);
		fileSize.setBorder(null);
		fileLocation.setBorder(null);
		createTime.setBorder(null);
		lastModifiedTime.setBorder(null);
		resolution.setBorder(null);
		width.setBorder(null);
		height.setBorder(null);
		//setPreferredSize(new Dimension(280,30));
		fileName.setPreferredSize(new Dimension(300,30));
		fileType.setPreferredSize(new Dimension(300,30));
		fileSize.setPreferredSize(new Dimension(300,30));
		fileLocation.setPreferredSize(new Dimension(300,30));
		createTime.setPreferredSize(new Dimension(300,30));
		lastModifiedTime.setPreferredSize(new Dimension(300,30));
		resolution.setPreferredSize(new Dimension(300,30));
		width.setPreferredSize(new Dimension(300,30));
		height.setPreferredSize(new Dimension(300,30));
		//设置光标类型
		
		fileName.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		fileType.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		fileSize.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		fileLocation.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		createTime.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		lastModifiedTime.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		resolution.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		width.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		height.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		
		//this.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
		JLabel label1 = new JLabel("文件名:", JLabel.CENTER);
		JLabel label2 = new JLabel("文件类型:", JLabel.CENTER);
		JLabel label3 = new JLabel("文件位置:", JLabel.CENTER);
		JLabel label4 = new JLabel("文件大小:", JLabel.CENTER);
		JLabel label5 = new JLabel("创建时间:", JLabel.CENTER);
		JLabel label6 = new JLabel("最后修改时间:", JLabel.CENTER);
		JLabel label7 = new JLabel("分辨率:", JLabel.CENTER);
		JLabel label8 = new JLabel("宽度:", JLabel.CENTER);
		JLabel label9 = new JLabel("高度:", JLabel.CENTER);
		label1.setPreferredSize(new Dimension(100,30));
		label2.setPreferredSize(new Dimension(100,30));
		label3.setPreferredSize(new Dimension(100,30));
		label4.setPreferredSize(new Dimension(100,30));
		label5.setPreferredSize(new Dimension(100,30));
		label6.setPreferredSize(new Dimension(100,30));
		label7.setPreferredSize(new Dimension(100,30));
		label8.setPreferredSize(new Dimension(100,30));
		label9.setPreferredSize(new Dimension(100,30));
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
		panel.add(label1);
		panel.add(fileName);
		panel.add(label2);
		panel.add(fileType);
		panel.add(label3);
		panel.add(fileLocation);
		panel.add(label4);
		panel.add(fileSize);
		panel.add(label5);
		panel.add(createTime);
		panel.add(label6);
		panel.add(lastModifiedTime);
		panel.add(label7);
		panel.add(resolution);
		panel.add(label8);
		panel.add(width);
		panel.add(label9);
		panel.add(height);
		
		this.add(panel);
		this.setTitle(picture.getName() + "的属性");
		this.setModal(true);
		this.setSize(450, 450);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	private String getCreateTime(String absoluteFile) {
		// TODO Auto-generated method stub
		String strTime = null;
		try {
			Process p = Runtime.getRuntime().exec(
					"cmd /C dir " + absoluteFile + "/tc");
			InputStream is = p.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.endsWith(".txt")) {
					strTime = line.substring(0, 17);
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strTime;
	}
}
