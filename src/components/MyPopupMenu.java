package components;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class MyPopupMenu extends JPopupMenu{
	JMenuItem popupMenu_view,popupMenu_copy,popupMenu_move,popupMenu_delete,
	popupMenu_reName,popupMenu_property;
	public MyPopupMenu(){
		popupMenu_view = new JMenuItem("预览");
		popupMenu_copy = new JMenuItem("复制到");
		popupMenu_move = new JMenuItem("移动到");
		popupMenu_delete = new JMenuItem("删除");
		popupMenu_reName = new JMenuItem("重命名");
		popupMenu_property = new JMenuItem("属性");
		
		this.add(popupMenu_view);
		this.add(popupMenu_copy);
		this.add(popupMenu_move);
		this.add(popupMenu_delete);
		this.add(popupMenu_reName);
		this.add(popupMenu_property);
	}
	public JMenuItem getPopupMenu_view() {
		return popupMenu_view;
	}
	public JMenuItem getPopupMenu_copy() {
		return popupMenu_copy;
	}
	public JMenuItem getPopupMenu_move() {
		return popupMenu_move;
	}
	public JMenuItem getPopupMenu_delete() {
		return popupMenu_delete;
	}
	public JMenuItem getPopupMenu_reName() {
		return popupMenu_reName;
	}
	public JMenuItem getPopupMenu_property() {
		return popupMenu_property;
	}
	
}
