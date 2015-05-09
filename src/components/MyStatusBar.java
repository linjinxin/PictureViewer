package components;

import javax.swing.JLabel;
import javax.swing.JToolBar;
//¹¤¾ßÀ¸
public class MyStatusBar extends JToolBar{
	JLabel label;
	public MyStatusBar(){
		label = new JLabel("No Thing Selected");
		this.add(label);
	}
	public JLabel getLabel() {
		return label;
	}
	public void setLabel(JLabel label) {
		this.label = label;
	}
}
