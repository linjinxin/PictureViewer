package components;

import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JLabel;

public class TipDialog extends JDialog{
	public TipDialog(String tipText){
		super();
		this.setUndecorated(true);
		JLabel label = new JLabel(tipText,JLabel.CENTER);
		label.setFont(new Font("ËÎÌו",Font.BOLD,15));
		this.add(label);
		this.setSize(140, 70);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
	}
}
