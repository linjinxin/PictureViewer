package components;

import javax.swing.JCheckBox;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MyMenuBar extends JMenuBar{
	JMenu file,edit,setting;
	JMenuItem file_exit;
	JMenuItem edit_copy,edit_move,edit_delete,edit_rename,edit_selectAll;
	JCheckBox setting_checkBox;
	boolean isCheckBoxSelected = false;
	
	public MyMenuBar(){
		file = new JMenu("�ļ�(F)");
		edit = new JMenu("�༭(E)");
		setting = new JMenu("����(S)");
		file.setMnemonic('F');
		edit.setMnemonic('E');
		setting.setMnemonic('S');
		
		file_exit = new JMenuItem("�˳�");
		edit_copy = new JMenuItem("����");
		edit_move = new JMenuItem("�ƶ�");
		edit_delete = new JMenuItem("ɾ��");
		edit_rename = new JMenuItem("������");
		edit_selectAll = new JMenuItem("ȫѡ");
		setting_checkBox = new JCheckBox("��Ŀ��ѡ��");
		file.add(file_exit);
		edit.add(edit_selectAll);
		edit.add(edit_copy);
		edit.add(edit_move);
		edit.add(edit_delete);
		edit.add(edit_rename);
		
		setting.add(setting_checkBox);
		
		this.add(file);
		this.add(edit);
		this.add(setting);
	}
	public JMenu getFile() {
		return file;
	}
	public void setFile(JMenu file) {
		this.file = file;
	}
	public JMenu getEdit() {
		return edit;
	}
	public void setEdit(JMenu edit) {
		this.edit = edit;
	}
	
	public JMenu getSetting() {
		return setting;
	}
	public void setSetting(JMenu setting) {
		this.setting = setting;
	}
	public JMenuItem getEdit_copy() {
		return edit_copy;
	}
	public void setEdit_copy(JMenuItem edit_copy) {
		this.edit_copy = edit_copy;
	}
	public JMenuItem getEdit_move() {
		return edit_move;
	}
	public void setEdit_move(JMenuItem edit_move) {
		this.edit_move = edit_move;
	}
	public JMenuItem getEdit_delete() {
		return edit_delete;
	}
	public void setEdit_delete(JMenuItem edit_delete) {
		this.edit_delete = edit_delete;
	}
	public JMenuItem getFile_exit() {
		return file_exit;
	}
	public void setFile_exit(JMenuItem file_exit) {
		this.file_exit = file_exit;
	}
	public JMenuItem getEdit_rename() {
		return edit_rename;
	}
	public void setEdit_rename(JMenuItem edit_rename) {
		this.edit_rename = edit_rename;
	}
	public JCheckBox getSetting_checkBox() {
		return setting_checkBox;
	}
	public void setSetting_checkBox(JCheckBox setting_checkBox) {
		this.setting_checkBox = setting_checkBox;
	}
	public JMenuItem getEdit_selectAll() {
		return edit_selectAll;
	}
	public void setEdit_selectAll(JMenuItem edit_selectAll) {
		this.edit_selectAll = edit_selectAll;
	}
	public boolean isCheckBoxSelected() {
		return isCheckBoxSelected;
	}
	public void setCheckBoxSelected(boolean isCheckBoxSelected) {
		this.isCheckBoxSelected = isCheckBoxSelected;
	}
	
}
