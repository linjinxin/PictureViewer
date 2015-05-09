package filetree;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class FileTreeRenderer extends DefaultTreeCellRenderer{

	public FileTreeRenderer() {
		super();
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		FileTree fileTree = (FileTree)tree;
		JLabel label = (JLabel)super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
		FileNode fileNode = (FileNode)node.getUserObject();
		label.setText(fileNode.getName());
		label.setIcon(fileNode.getIcon());
		return label;
	}

	@Override
	protected void firePropertyChange(String propertyName, Object oldValue,
			Object newValue) {
		// TODO Auto-generated method stub
		super.firePropertyChange(propertyName, oldValue, newValue);
	}

	
	
}
