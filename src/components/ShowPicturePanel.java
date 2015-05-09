package components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import utils.FileUtils;
import utils.OperateUtils;
import filetree.FileNode;
import filetree.FileTree;

public class ShowPicturePanel extends JPanel {
	ArrayList<SingleImagePanel> singleImagePanels = new ArrayList<>();// 包含的Panel
	MyToolBar toolBar;// 工具栏
	MyMenuBar menuBar;// 菜单栏
	boolean isShowCheckBox = false;// 是否显示复选框
	ArrayList<File> pictures;
	boolean isSelectAll = false;
	FileTree fileTree;
	DefaultMutableTreeNode currentTreeNode;

	public ShowPicturePanel(MyToolBar toolBar, MyMenuBar menuBar) {
		this.toolBar = toolBar;
		this.menuBar = menuBar;
		this.fileTree = fileTree;
		this.addListenerToComponents();
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		this.setBackground(Color.white);
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
	}

	public void doShowPictures(ArrayList<File> pictures) {
		this.pictures = pictures;
		this.removeAll();// 删除面板原有图片
		if (pictures.size() == 0) {
			this.updateUI();
			return;
		}
		int height = 125 * (pictures.size() / 5);
		if (pictures.size() % 5 != 0){
			height = height + 200;
		}
		this.setPreferredSize(new Dimension(650, height));
		singleImagePanels.clear();
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
		for (File picture : pictures) {
			// 采用多线程(线程池)，每个图片分配一个线程，加快显示速度(不能按顺序显示)
			fixedThreadPool.execute((new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						SingleImagePanel singleImagePanel = new SingleImagePanel(
								picture, pictures, isShowCheckBox);
						if(isShowCheckBox){
							if (isSelectAll) {
								singleImagePanel.getCheckBox().setSelected(true);
								singleImagePanel.isSelected = true;
							}
						}
						add(singleImagePanel);
						singleImagePanels.add(singleImagePanel);
						updateUI();
					} catch (Exception e) {
					}
				}
			}));
			
		}
		this.updateUI();
	}
	
	private void addListenerToComponents() {
		// TODO Auto-generated method stub
		this.menuBar.getSetting_checkBox().addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						try {
							if (!isShowCheckBox) {
								isShowCheckBox = true;
								doShowPictures(pictures);
								updateUI();
							} else {
								isShowCheckBox = false;
								doShowPictures(pictures);
								updateUI();
							}
						} catch (Exception e1) {
							// TODO Auto-generated catch block
						}
					}
				});

		this.toolBar.getCopy().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (isShowCheckBox) {// 有复选框的情况
					// 判断是否有文件被选择
					boolean isFileSelected = false;
					for (SingleImagePanel panel : singleImagePanels) {
						if (panel.isSelected) {
							isFileSelected = true;
							break;
						}
					}
					if(!isFileSelected){
						OperateUtils.showTips("未选择文件！", 2);
						return;
					}
					// 弹出对话框选择保存的文件夹
					JFileChooser fileChooser = OperateUtils
							.getDefaultFileChooser("复制到");
					String destination;
					try{
						destination = fileChooser.getSelectedFile().getAbsolutePath();
					}catch(Exception e1){
						return;
					}
					try {
						ArrayList<File> movedList = new ArrayList<File>();
						for (int i = 0; i < singleImagePanels.size(); i++) {// 遍历所有面板，看是否被选中，选中则复制
							if (singleImagePanels.get(i).isSelected) {
								FileUtils.copyFile(destination +  "\\" + singleImagePanels.get(i).picture.getName(),
										singleImagePanels.get(i).picture.getAbsolutePath());
								movedList.add(new File(destination + "\\" + singleImagePanels.get(i).picture.getName()));
							} 
						}
						try {
							//新增目录树节点
							synchronizeFileTree_Copy(movedList,destination);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							OperateUtils.showTips("复制出错！", 2);
							return;
						}
						OperateUtils.showTips("复制成功！", 2);
					} catch (Exception e2) {
						e2.printStackTrace();
						OperateUtils.showTips("复制失败！", 2);
					}
				}
			}
		});

		this.toolBar.getMove().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (isShowCheckBox) {
					// 判断是否有文件被选择
					boolean isFileSelected = false;
					for (SingleImagePanel panel : singleImagePanels) {
						if (panel.isSelected) {
							isFileSelected = true;
							break;
						}
					}
					if(!isFileSelected){
						OperateUtils.showTips("未选择文件！", 2);
						return;
					}
					// 弹出对话框选择保存的文件夹
					JFileChooser fileChooser = OperateUtils
							.getDefaultFileChooser("移动到");
					String destination;
					try{
						destination = fileChooser.getSelectedFile().getAbsolutePath();
					}catch(Exception e1){
						return;
					}
					try {
						ArrayList<File> movedList = new ArrayList<File> ();
						for (SingleImagePanel panel : singleImagePanels) {// 遍历所有面板，看是否被选中，选中则移动
							if (panel != null) {
								if (panel.isSelected) {
									//删除目录树的节点
									synchronizeFileTree_Move(panel);
									//删除文件
									FileUtils.moveFile(destination + "\\"+ panel.picture.getName(),
											panel.picture.getAbsolutePath());
									pictures.remove(panel.picture);
									movedList.add(new File(destination + "\\"+ panel.picture.getName()));
								}
							}
						}
						try{
							//新增目录树节点
							synchronizeFileTree_Copy(movedList, destination);
						}catch(Exception e1){
							OperateUtils.showTips("移动失败！", 2);
							return;
						}
						doShowPictures(pictures);
						// 弹窗提示
						OperateUtils.showTips("移动成功！", 2);
					} catch (ConcurrentModificationException e3) {
						OperateUtils.showTips("移动成功！", 2);
					} catch (Exception e2) {
						// 弹窗提示
						OperateUtils.showTips("移动失败！", 2);
					}
				}
			}

		});

		this.toolBar.getDelete().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (isShowCheckBox) {
					try {
						boolean isSelectFile = false;
						for (SingleImagePanel panel : singleImagePanels) {// 遍历所有面板，看是否被选中，选中则删除
							if (panel.isSelected) {
								//删除目录树中对应节点
								synchronizeFileTree_Delete(panel);
								//删除文件
								pictures.remove(panel.picture);
								FileUtils.deleteFile(panel.picture
										.getAbsolutePath());
								isSelectFile = true;
							}
						}
						//没有选择文件则退出
						if (!isSelectFile) {
							OperateUtils.showTips("未选择文件！", 2);
							return;
						}
						//重新显示图片
						doShowPictures(pictures);
						OperateUtils.showTips("删除成功！", 2);
					} catch (ConcurrentModificationException e3) {
						OperateUtils.showTips("删除成功！", 2);
					} catch (Exception e2) {
						OperateUtils.showTips("删除失败！", 2);
					}
				}
			}
		});

		this.toolBar.getRename().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (isShowCheckBox) {
					for (SingleImagePanel panel : singleImagePanels) {
						if (panel.isSelected) {
							break;
						}
						OperateUtils.showTips("未选择文件！", 2);
						return;
					}
					MyDialog myDialog = new MyDialog();
				}
			}
		});

		this.toolBar.getSelectAll().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (isShowCheckBox) {
					if (!isSelectAll) {
						isSelectAll = true;
						toolBar.getSelectAll().setText("反选");
						if(singleImagePanels.size() == 0){
							return;
						}
						for (SingleImagePanel panel : singleImagePanels) {
							panel.isSelected = false;
						}
					} else {
						isSelectAll = false;
						toolBar.getSelectAll().setText("全选");
						if(singleImagePanels.size() == 0){
							return;
						}
						for (SingleImagePanel panel : singleImagePanels) {
							panel.isSelected = true;
						}
					}
					doShowPictures(pictures);
				} else {
				}
			}
		});

		this.menuBar.getEdit_copy().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (isShowCheckBox) {// 有复选框的情况
					// 判断是否有文件被选择
					boolean isFileSelected = false;
					for (SingleImagePanel panel : singleImagePanels) {
						if (panel.isSelected) {
							isFileSelected = true;
							break;
						}
					}
					if(!isFileSelected){
						OperateUtils.showTips("未选择文件！", 2);
						return;
					}
					// 弹出对话框选择保存的文件夹
					JFileChooser fileChooser = OperateUtils
							.getDefaultFileChooser("复制到");
					String destination;
					try{
						destination = fileChooser.getSelectedFile().getAbsolutePath();
					}catch(Exception e1){
						return;
					}
					try {
						ArrayList<File> movedList = new ArrayList<File> ();//保存复制后的目标文件夹下的被复制文件
						for (int i = 0; i < singleImagePanels.size(); i++) {// 遍历所有面板，看是否被选中，选中则复制
							if (singleImagePanels.get(i).isSelected) {
								FileUtils.copyFile(destination + "\\" + singleImagePanels.get(i).picture.getName(),
										singleImagePanels.get(i).picture.getAbsolutePath());
								movedList.add(new File(destination + singleImagePanels.get(i).getName()));
							}
						}
						try {
							//新增目录树节点
							synchronizeFileTree_Copy(movedList,destination);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							OperateUtils.showTips("复制出错！", 2);
							return;
						}
						OperateUtils.showTips("复制成功！", 2);
					} catch (Exception e2) {
						OperateUtils.showTips("复制失败！", 2);
					}
				}
			}

		});

		this.menuBar.getEdit_move().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (isShowCheckBox) {
					// 判断是否有文件被选择
					boolean isFileSelected = false;
					for (SingleImagePanel panel : singleImagePanels) {
						if (panel.isSelected) {
							isFileSelected = true;
							break;
						}
					}
					if(!isFileSelected){
						OperateUtils.showTips("未选择文件！", 2);
						return;
					}
					// 弹出对话框选择保存的文件夹
					JFileChooser fileChooser = OperateUtils
							.getDefaultFileChooser("移动到");
					String destination;
					try{
						destination = fileChooser.getSelectedFile().getAbsolutePath();
					}catch(Exception e1){
						return;
					}
					try {
						ArrayList<File> movedList = new ArrayList<File> ();
						for (SingleImagePanel panel : singleImagePanels) {// 遍历所有面板，看是否被选中，选中则移动
							if (panel != null) {
								if (panel.isSelected) {
									//删除目录树的节点
									synchronizeFileTree_Move(panel);
									//删除文件
									FileUtils.moveFile(destination + "\\"+ panel.picture.getName(),
											panel.picture.getAbsolutePath());
									pictures.remove(panel.picture);
									movedList.add(new File(destination + "\\" + panel.picture.getName()));
								}
							}
							
						}
						try{
							synchronizeFileTree_Copy(movedList, destination);
						}catch(Exception e1){
							OperateUtils.showTips("移动失败!", 2);
							return;
						}
						doShowPictures(pictures);
						// 弹窗提示
						OperateUtils.showTips("移动成功！", 2);
					} catch (ConcurrentModificationException e3) {
						OperateUtils.showTips("移动成功！", 2);
					} catch (Exception e2) {
						// 弹窗提示
						OperateUtils.showTips("移动失败！", 2);
					}
				}
			}

		});

		this.menuBar.getEdit_rename().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (isShowCheckBox) {
					for (SingleImagePanel panel : singleImagePanels) {
						if (panel.isSelected) {
							break;
						}
						OperateUtils.showTips("未选择文件！", 2);
						return;
					}
					MyDialog myDialog = new MyDialog();
				}
			}
		});

		this.menuBar.getEdit_delete().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (isShowCheckBox) {
					try {
						boolean isSelectFile = false;
						for (SingleImagePanel panel : singleImagePanels) {// 遍历所有面板，看是否被选中，选中则删除
							if (panel.isSelected) {
								//删除目录树中对应节点
								synchronizeFileTree_Delete(panel);
								//删除文件
								FileUtils.deleteFile(panel.picture
										.getAbsolutePath());
								pictures.remove(panel.picture);
								isSelectFile = true;
							}
						}
						doShowPictures(pictures);

						if (!isSelectFile) {
							OperateUtils.showTips("未选择文件！", 2);
							return;
						}
						OperateUtils.showTips("删除成功！", 2);
					} catch (ConcurrentModificationException e3) {
						OperateUtils.showTips("删除成功！", 2);
					} catch (Exception e2) {
						OperateUtils.showTips("删除失败！", 2);
					}
				}
			}
		});
	
		this.menuBar.getEdit_selectAll().addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (isShowCheckBox) {
					if (!isSelectAll) {
						isSelectAll = true;
						toolBar.getSelectAll().setText("反选");
						if(singleImagePanels.size() == 0){
							return;
						}
						for (SingleImagePanel panel : singleImagePanels) {
							panel.isSelected = false;
						}
					} else {
						isSelectAll = false;
						toolBar.getSelectAll().setText("全选");
						if(singleImagePanels.size() == 0){
							return;
						}
						for (SingleImagePanel panel : singleImagePanels) {
							panel.isSelected = true;
						}
					}
					doShowPictures(pictures);
				} else {
				}
			}
			
		});
		
	}

	class MyDialog extends JDialog {// 批量重命名对话框
		JTextField nameField, startField,bitField;
		JLabel nameLabel, startLabel,bitLabel;
		JButton submit;

		public MyDialog() {
			JPanel jp = new JPanel();
			jp.setLayout(new FlowLayout(FlowLayout.LEFT));

			nameLabel = new JLabel( "文件名前缀：", JLabel.CENTER);
			startLabel = new JLabel("起 始 编 号:", JLabel.CENTER);
			bitLabel = new JLabel(  "编 号 位  数",JLabel.CENTER);
			nameField = new JTextField(25);
			startField = new JTextField(25);
			bitField = new JTextField(25);

			jp.add(nameLabel);
			jp.add(nameField);
			jp.add(startLabel);
			jp.add(startField);
			jp.add(bitLabel);
			jp.add(bitField);

			this.add(jp);

			submit = new JButton("确定");
			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout(FlowLayout.CENTER));
			panel.add(submit);
			this.add(panel, BorderLayout.SOUTH);

			this.setSize(300, 160);
			// this.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
			//this.setModal(true);
			this.setLocationRelativeTo(null);
			this.setVisible(true);

			submit.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO 自动生成的方法存根

					String newName = nameField.getText();
					if (newName.equals("")) {
						OperateUtils.showTips("文件名不能为空!", 2);
						return;
					}
					int start;//起始编号
					int bitNum;//位数
					try {
						start = Integer.parseInt(startField.getText());
						bitNum = Integer.parseInt(bitField.getText());
						//编号+图片数 长度大于 位数则出错
						if((start + pictures.size() + "").length() > bitNum){
							OperateUtils.showTips("编号,位数不合要求", 2);
							return;
						}
					} catch (Exception e1) {
						OperateUtils.showTips("请输入整数!", 2);
						e1.printStackTrace();
						return;
					}
					
					try {
						for(SingleImagePanel panel : singleImagePanels){
							if(panel.isSelected){
								int index = panel.picture.getName().lastIndexOf(".");
								String type = panel.picture.getName().substring(index);
								
								Enumeration childrens = currentTreeNode.children();
								while(childrens.hasMoreElements()){
									DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) childrens.nextElement();
									FileNode fileNode = (FileNode) childNode.getUserObject();
									if(fileNode.getFile().getName().equals(panel.getDescribeTextField().getText())){
										String number = (((int)Math.pow(10, bitNum) + start)+"").substring(1);//编号
										//重命名文件
										FileUtils.renameFile(panel.picture.getAbsolutePath(), newName + number + type);
										//设置显示值
										panel.describeTextField.setText(newName + number + type);
										//重命名对应目录树节点
										String newPath = panel.picture.getAbsolutePath().substring(0,panel.picture.getAbsolutePath().lastIndexOf("\\")+1)+newName+number+type;
										File newPicture = new File(newPath);
										((FileNode)currentTreeNode.getUserObject()).getPictures().remove(panel.picture);
										panel.setPicture(newPicture);
										fileNode.setFile(panel.picture);
										((FileNode)currentTreeNode.getUserObject()).getPictures().add(panel.picture);
										fileNode.setName(FileSystemView.getFileSystemView().getSystemDisplayName(panel.picture));
									
										start++;
										break;
									}
								}
							}
							
						}
						DefaultTreeModel model = (DefaultTreeModel) fileTree.getModel();
						model.nodeStructureChanged(currentTreeNode);
						// 弹窗提示
						OperateUtils.showTips("重命名成功！", 2);
						setVisible(false);
						dispose();
						setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
					} catch (Exception e2) {
						// 弹窗提示
						OperateUtils.showTips("重命名失败！", 2);
						e2.printStackTrace();
					}
				}

			});

		}
	}

	
	public DefaultMutableTreeNode getCurrentTreeNode() {
		return currentTreeNode;
	}

	public void setCurrentTreeNode(DefaultMutableTreeNode currentTreeNode) {
		this.currentTreeNode = currentTreeNode;
	}

	public FileTree getFileTree() {
		return fileTree;
	}

	public void setFileTree(FileTree fileTree) {
		this.fileTree = fileTree;
	}
	//删除时更新目录树中对应节点
	public void synchronizeFileTree_Delete(SingleImagePanel panel) {
		Enumeration childrens =  currentTreeNode.children();
		while(childrens.hasMoreElements()){
			DefaultMutableTreeNode childNode = (DefaultMutableTreeNode)childrens.nextElement();
			FileNode fileNode = (FileNode)childNode.getUserObject();
			//判断是否是要被删除的节点
			if(fileNode.getFile().getAbsolutePath().equals(panel.picture.getAbsolutePath())){
				currentTreeNode.remove(childNode);
			}
		}
		DefaultTreeModel model = (DefaultTreeModel) fileTree.getModel();
		model.nodeStructureChanged(currentTreeNode);
	}
	//移动时更新对应目录树节点
	public void synchronizeFileTree_Move(SingleImagePanel panel) {
		synchronizeFileTree_Delete(panel);
	}
	//复制时更新对应目录树节点
	public void synchronizeFileTree_Copy(ArrayList<File> fileList,String destination) {
		DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) fileTree.getModel().getRoot();
		visitAllNodes(rootNode,fileList,destination);
	}
	
	public void visitAllNodes(DefaultMutableTreeNode startNode,ArrayList<File> fileList,String destination){
		
		if(!startNode.isRoot()){
			if(process(startNode,fileList,destination)){//找到节点,处理成功
				DefaultTreeModel model = (DefaultTreeModel)fileTree.getModel();
				model.nodeStructureChanged(startNode);
				return;
			}
		}
		if(startNode.getChildCount() > 0){
			for(Enumeration e = startNode.children();e.hasMoreElements();){
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
				visitAllNodes(node,fileList,destination);
			}
		}
	}

	private boolean process(DefaultMutableTreeNode node,ArrayList<File> fileList,String destination) {
		FileNode fileNode = (FileNode) node.getUserObject();
		//判断节点的userobject的file路径是否为destination，是则给该节点添加子节点
		if(destination.equals(fileNode.getFile().getAbsolutePath())){
			FileSystemView fileSystemView = FileSystemView.getFileSystemView();
			for(File file : fileList){
				FileNode childFileNode = new FileNode(fileSystemView.getSystemDisplayName(file),
						file, fileSystemView.getSystemIcon(file),
						false, new ArrayList<File>());
				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(childFileNode);
				node.add(childNode);
				fileNode.getPictures().add(file);
			}
			return true;
		}
		return false;
	}
}
