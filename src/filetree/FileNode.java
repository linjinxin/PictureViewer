package filetree;

import java.io.File;
import java.util.ArrayList;

import javax.swing.Icon;

public class FileNode {
	private String name;
	private File file;
	private Icon icon;
	private boolean isInit;
	private boolean isDummyRoot;
	private ArrayList<File> pictures;
	public FileNode(String name, File file, Icon icon, boolean isDummyRoot,ArrayList<File> pictures) {
		super();
		this.name = name;
		this.file = file;
		this.icon = icon;
		this.isDummyRoot = isDummyRoot;
		this.pictures = pictures;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public Icon getIcon() {
		return icon;
	}
	public void setIcon(Icon icon) {
		this.icon = icon;
	}
	public boolean isInit() {
		return isInit;
	}
	public void setInit(boolean isInit) {
		this.isInit = isInit;
	}
	public boolean isDummyRoot() {
		return isDummyRoot;
	}
	public void setDummyRoot(boolean isDummyRoot) {
		this.isDummyRoot = isDummyRoot;
	}
	public ArrayList<File> getPictures() {
		return pictures;
	}
	public void setPictures(ArrayList<File> pictures) {
		this.pictures = pictures;
	}
	
}
