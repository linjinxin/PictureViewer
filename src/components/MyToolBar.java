package components;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JToolBar;

public class MyToolBar extends JToolBar{
	JButton copy,move,delete,selectAll,rename;
	boolean isSelectAll=false;
	
	public MyToolBar(){
		
		selectAll=new JButton("全选");
		copy = new JButton("复制");
		move = new JButton("移动");
		delete = new JButton("删除");
		rename = new JButton("重命名"); 
				
		this.setLayout(new FlowLayout(5));
		this.add(selectAll);
		this.add(copy);
		this.add(move);
		this.add(delete);
		this.add(rename);
	}

	public JButton getCopy() {
		return copy;
	}

	public void setCopy(JButton copy) {
		this.copy = copy;
	}

	public JButton getMove() {
		return move;
	}

	public void setMove(JButton move) {
		this.move = move;
	}

	public JButton getDelete() {
		return delete;
	}

	public void setDelete(JButton delete) {
		this.delete = delete;
	}

	public JButton getSelect() {
		return selectAll;
	}

	public void setSelect(JButton selectAll) {
		this.selectAll = selectAll;
	}

	public JButton getSelectAll() {
		return selectAll;
	}

	public void setSelectAll(JButton selectAll) {
		this.selectAll = selectAll;
	}

	public Boolean getIsSelectAll() {
		return isSelectAll;
	}

	public void setIsSelectAll(Boolean isSelectAll) {
		this.isSelectAll = isSelectAll;
	}

	public JButton getRename() {
		return rename;
	}

	public void setRename(JButton rename) {
		this.rename = rename;
	}

	public void setSelectAll(boolean isSelectAll) {
		this.isSelectAll = isSelectAll;
	}

	
}
