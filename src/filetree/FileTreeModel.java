package filetree;

import java.io.File;
import java.util.ArrayList;

import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

/*
 * JTree ģ��
 */
public class FileTreeModel extends DefaultTreeModel {

	public FileTreeModel(TreeNode root) {
		super(root);
		FileSystemView fileSystemView = FileSystemView.getFileSystemView();
		/* �õ�ϵͳ���и�Ŀ¼ */
		File[] files = fileSystemView.getRoots();
		for (File file : files) {

			/*
			 * public FileNode(String name, File file, Icon icon, boolean
			 * isDummyRoot)
			 */
			FileNode fileNode = new FileNode(
					fileSystemView.getSystemDisplayName(file), file,
					fileSystemView.getSystemIcon(file), false,
					new ArrayList<File>());
			/* ��FileNode����һ���ڵ� */
			DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(
					fileNode);
			/* ���һ���ڵ� */
			((DefaultMutableTreeNode) root).add(childNode);
		}
	}

	@Override
	public boolean isLeaf(Object node) {
		// TODO Auto-generated method stub
		// ���ݴ�������nodeȡ��fileNode����
		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
		FileNode fileNode = (FileNode) treeNode.getUserObject();
		if (fileNode.isDummyRoot()) { // �����Ŀ¼����false
			return false;
		}
		return fileNode.getFile().isFile();
	}

}
