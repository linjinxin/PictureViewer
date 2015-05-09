package filetree;

import java.io.File;
import java.util.ArrayList;

import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

/*
 * JTree 模型
 */
public class FileTreeModel extends DefaultTreeModel {

	public FileTreeModel(TreeNode root) {
		super(root);
		FileSystemView fileSystemView = FileSystemView.getFileSystemView();
		/* 得到系统所有根目录 */
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
			/* 以FileNode创建一个节点 */
			DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(
					fileNode);
			/* 添加一个节点 */
			((DefaultMutableTreeNode) root).add(childNode);
		}
	}

	@Override
	public boolean isLeaf(Object node) {
		// TODO Auto-generated method stub
		// 根据传进来的node取得fileNode对象
		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
		FileNode fileNode = (FileNode) treeNode.getUserObject();
		if (fileNode.isDummyRoot()) { // 虚拟根目录返回false
			return false;
		}
		return fileNode.getFile().isFile();
	}

}
