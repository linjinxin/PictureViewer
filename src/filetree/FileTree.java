package filetree;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;

import components.MyStatusBar;
import components.ShowPicturePanel;

public class FileTree extends JTree {
	FileSystemView fileSystemView = FileSystemView.getFileSystemView();
	ShowPicturePanel showPicturesPanel;
	MyStatusBar statusBar;
	public FileTree(ShowPicturePanel showPicturesPanel, MyStatusBar statusBar) {
		this.showPicturesPanel = showPicturesPanel;
		this.statusBar = statusBar;
		this.setRootVisible(false);
		/* ��Ӽ�������ʵ�ֵ��ĳ�����ڵ�ʱ�ż�������ڵ�ĺ��ӽڵ㣬�����ջ��� */
		this.addTreeWillExpandListener(new TreeWillExpandListener() {

			@Override
			public void treeWillExpand(TreeExpansionEvent event)
					throws ExpandVetoException {
				// TODO Auto-generated method stub
				DefaultMutableTreeNode lastTreeNode = (DefaultMutableTreeNode) event
						.getPath().getLastPathComponent();
				FileNode fileNode = (FileNode) lastTreeNode.getUserObject();
				/* �ж��Ƿ��Ѿ����ع��������ظ����� */
				if (!fileNode.isInit()) {
					File[] files;
					if (fileNode.isDummyRoot()) {
						files = fileSystemView.getRoots();
					} else {
						files = fileSystemView.getFiles(fileNode.getFile(),
								false);
						for (File file : files) {
							// �ж�file�Ƿ�ΪͼƬ���������pictures
							if (file.isFile()) {
								String fileName = file.getName();
								if (fileName.endsWith(".jpg")
										|| fileName.endsWith(".JPG")
										|| fileName.endsWith(".gif")
										|| fileName.endsWith(".GIF")
										|| fileName.endsWith(".bmp")
										|| fileName.endsWith(".BMP")
										|| fileName.endsWith(".jpeg")
										|| fileName.endsWith(".JPEG")
										|| fileName.endsWith(".png")
										|| fileName.endsWith(".PNG")) {
									FileNode childFileNode = new FileNode(
											fileSystemView
													.getSystemDisplayName(file),
											file, fileSystemView
													.getSystemIcon(file),
											false, new ArrayList<File>());
									DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(
											childFileNode);
									lastTreeNode.add(childNode);
									fileNode.getPictures().add(file);
								}
							}
							// ���ļ�����ֻ��ӽڵ�
							if (file.isDirectory()) {
								FileNode childFileNode = new FileNode(
										fileSystemView
												.getSystemDisplayName(file),
										file, fileSystemView
												.getSystemIcon(file), false,
										new ArrayList<File>());
								DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(
										childFileNode);
								lastTreeNode.add(childNode);
							}
						}
						/* ֪ͨģ�ͷ����仯 */
						DefaultTreeModel changedTreeModel = (DefaultTreeModel) getModel();
						changedTreeModel.nodeStructureChanged(lastTreeNode);

						/* ����FileNode��isInit ,�����ظ����� */
						fileNode.setInit(true);

						DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) getLastSelectedPathComponent();
						if (selectedNode == lastTreeNode) {
							showPicturesPanel.doShowPictures(fileNode
									.getPictures());
							showPicturesPanel.setCurrentTreeNode(lastTreeNode);// ���ݵ�ǰѡ��ڵ�

							String filePath = fileNode.getFile()
									.getAbsolutePath();
							JLabel label = statusBar.getLabel();
							label.setText("Location: " + filePath + "   "
									+ fileNode.getPictures().size() + "��ͼƬ");
						}
					}
				}
			}

			@Override
			public void treeWillCollapse(TreeExpansionEvent event)
					throws ExpandVetoException {
				// TODO Auto-generated method stub

			}
		});

		this.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent event) {
				// TODO Auto-generated method stub
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						DefaultMutableTreeNode selectedTreeNode = (DefaultMutableTreeNode) event
								.getPath().getLastPathComponent();
						FileNode fileNode = (FileNode) selectedTreeNode
								.getUserObject();
						if (fileNode.getFile().isFile()) {
							return;
						}
						ArrayList<File> pictures = fileNode.getPictures();
						showPicturesPanel.doShowPictures(pictures);
						showPicturesPanel.setCurrentTreeNode(selectedTreeNode);

						String filePath = fileNode.getFile().getAbsolutePath();
						JLabel label = statusBar.getLabel();
						label.setText("Location: " + filePath + "   "
								+ pictures.size() + "��ͼƬ");
					}
				});
				thread.start();
			}
		});
	}
}
