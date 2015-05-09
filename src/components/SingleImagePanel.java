package components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import utils.FileUtils;
import utils.OperateUtils;
import filetree.FileNode;
import filetree.FileTree;

@SuppressWarnings("serial")
public class SingleImagePanel extends JPanel implements MouseListener {
	File picture;
	MyPopupMenu popupMenu;// 右键菜单
	JLabel imageLabel;
	JTextField describeTextField;
	ArrayList<File> pictures;
	JCheckBox checkBox;//复选框
	boolean isShowCheckBox ;//是否显示复选框
	boolean isSelected;//标志是否被选中
	String reName_OldName;//重命名时用来保存原来的文件名
	JPanel checkBoxPanel;
	public SingleImagePanel(File picture, ArrayList<File> pictures,boolean isShowCheckBox) {
		super();
		this.picture = picture;
		this.pictures = pictures;
		this.isShowCheckBox = isShowCheckBox;
		if(isShowCheckBox){//根据isShowCheckBox是否显示复选框
			checkBox = new JCheckBox();
			checkBox.setBackground(Color.white);
		}
		popupMenu = new MyPopupMenu();
		this.setPreferredSize(new Dimension(100, 150));
		this.setBackground(Color.white);
		this.addMouseListener(this);
		this.addListenerToComponents();
		this.setComponentPopupMenu(popupMenu);
		// *************************************

		this.setLayout(new BorderLayout());
		@SuppressWarnings("rawtypes")
		Builder builder = Thumbnails.of(picture).size(90, 120);
		BufferedImage image = null;
		try {
			image = builder.asBufferedImage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ImageIcon imageIcon = new ImageIcon(image);
		imageLabel = new JLabel(imageIcon);
		describeTextField = new JTextField(picture.getName());
		this.describeTextField.setOpaque(true);
		this.describeTextField.setBackground(new Color(255, 255, 255));
		this.describeTextField.setBorder(BorderFactory.createEmptyBorder());
		this.describeTextField.setEditable(false);
		
		if(checkBox != null){
			checkBoxPanel = new JPanel();
			checkBoxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			checkBoxPanel.setBackground(Color.white);
			checkBoxPanel.add(checkBox);
			this.add(checkBoxPanel,BorderLayout.NORTH);
		}
		this.add(imageLabel, BorderLayout.CENTER);
		this.add(describeTextField, BorderLayout.SOUTH);

		this.describeTextField.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) { // TODO 自动生成的方法存根
				if (e.getClickCount() == 2) {// 双击重命名
					reName_OldName = describeTextField.getText();//保存旧名字
					describeTextField.setEditable(true);
					//describeTextField.setFocusable(true);
					describeTextField.selectAll();
					describeTextField.setBorder(BorderFactory
							.createLineBorder(Color.lightGray));
					}
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
		});
	}
	
	// 给右键菜单添加监听器
	private void addListenerToComponents() {
		if (checkBox != null) {
			this.checkBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO 自动生成的方法存根
					if (checkBox.isSelected()) {
						isSelected = true;
					} else {
						isSelected = false;
					}
				}
			});
		}
		// 复制
		this.popupMenu.getPopupMenu_copy().addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						try {// 没选择文件时处理异常
							JFileChooser fileChooser = OperateUtils
									.getDefaultFileChooser("复制到");
							String destination;
							try{
								destination = fileChooser.getSelectedFile().getAbsolutePath();
							}catch(Exception e1){
								return;
							}
							try {
								//复制文件
								FileUtils.copyFile(destination+ "\\"+picture.getName(),
										picture.getAbsolutePath());
								//更新对应目录树节点
								try{
									sychronizeFileTree_Copy(destination,new File(destination+ "\\"+picture.getName()));
								}catch(Exception e1){
									OperateUtils.showTips("复制失败!", 2);
									return;
								}
								// 弹窗提示
								OperateUtils.showTips("复制成功！", 2);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								// 弹窗提示
								OperateUtils.showTips("复制失败！", 2);
							}
						} catch (Exception e1) {
							// TODO Auto-generated catch block
						}
					}
				});
		// 移动
		this.popupMenu.getPopupMenu_move().addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						try {// 没选择文件时处理异常
							JFileChooser fileChooser = OperateUtils
									.getDefaultFileChooser("移动到");
							String destination;
							try{
								destination = fileChooser.getSelectedFile().getAbsolutePath();
							}catch(Exception e1){
								return;
							}
							try {
								//删除目录树对应节点
								sychronizeFileTree_Move();
								//移动文件
								FileUtils.moveFile(destination+"\\"+picture.getName(),
										picture.getAbsolutePath());
								// 删除pictures中的该记录
								pictures.remove(picture);
								//更新对应目录树节点
								try{
									sychronizeFileTree_Copy(destination+ "\\"+picture.getName(),new File(destination+ "\\"+picture.getName()));
								}catch(Exception e1){
									OperateUtils.showTips("移动失败!", 2);
									return;
								}
								((ShowPicturePanel) getParent()).doShowPictures(pictures);
								// 弹窗提示
								OperateUtils.showTips("移动成功！", 2);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								// 弹窗提示
								OperateUtils.showTips("移动失败！", 2);
							}
						} catch (Exception e1) {
							// TODO Auto-generated catch block
						}
					}

				});
		// 重命名
		this.popupMenu.getPopupMenu_reName().addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						reName_OldName = describeTextField.getText();//保存旧名字
						describeTextField.setEditable(true);
						// describeTextField.setFocusable(true);
						describeTextField.selectAll();
						describeTextField.setBorder(BorderFactory
								.createLineBorder(Color.lightGray));
					}

				});
		// c查看属性
		this.popupMenu.getPopupMenu_property().addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						new PropertyDialog(picture);
					}

				});

		// 删除
		this.popupMenu.getPopupMenu_delete().addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub

						try {
							//删除目录树对应节点
							sychronizeFileTree_Delete();
							//移动文件
							FileUtils.deleteFile(picture.getAbsolutePath());
							pictures.remove(picture);
							((ShowPicturePanel) getParent())
									.doShowPictures(pictures);
							// 弹窗提示
							OperateUtils.showTips("删除成功！", 2);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							// 弹窗提示
							OperateUtils.showTips("删除失败！", 2);
						}
					}

				});
		// 全屏查看
		this.popupMenu.getPopupMenu_view().addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						ViewerDialog dialog = new ViewerDialog(pictures, picture);
					}

				});
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getClickCount() == 1) {// 单击一次确认重命名
			if(!describeTextField.isEditable()){
				this.describeTextField.setBorder(null);
				this.describeTextField.setEditable(false);
				return;
			}
			this.describeTextField.setBorder(null);
			this.describeTextField.setEditable(false);
			try {
				//重命名对应目录树节点
				ShowPicturePanel showPicturePanel = (ShowPicturePanel)getParent();
				FileTree fileTree = showPicturePanel.getFileTree();
				DefaultMutableTreeNode currentTreeNode = showPicturePanel.getCurrentTreeNode();
				Enumeration childrens = currentTreeNode.children();
				while(childrens.hasMoreElements()){
					DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) childrens.nextElement();
					FileNode fileNode = (FileNode) childNode.getUserObject();
					//判断是否是重命名的文件(节点名字和重命名前的名字一致)
					if(fileNode.getFile().getName().equals(reName_OldName)){
						//重命名文件
						FileUtils.renameFile(picture.getAbsolutePath(),describeTextField.getText());
						//重命名目录树对应节点
						int index = picture.getAbsolutePath().lastIndexOf("\\");
						String newPath = picture.getAbsolutePath().substring(0,index+1)+describeTextField.getText();
						File newPicture = new File(newPath);
						showPicturePanel.pictures.remove(picture);
						picture = newPicture;
						fileNode.setFile(picture);
						showPicturePanel.pictures.add(picture);
						fileNode.setName(FileSystemView.getFileSystemView().getSystemDisplayName(picture));
						break;
					}
				}
				DefaultTreeModel model = (DefaultTreeModel) fileTree.getModel();
				model.nodeStructureChanged(currentTreeNode);
				
				OperateUtils.showTips("重命名成功", 2);
			} catch (Exception e1) {
			}
		} else if (e.getClickCount() == 2) {// 双击取消进入查看单张图片的模式
			ViewerDialog dialog = new ViewerDialog(pictures, picture);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		this.setBackground(new Color(184,224,243));
		this.describeTextField.setBackground(new Color(184,224,243));
		if(checkBoxPanel != null){
			this.checkBoxPanel.setBackground(new Color(184,224,243));
			this.checkBox.setBackground(new Color(184,224,243));
		}
		//this.setBorder(BorderFactory.createLineBorder(new Color(229, 243, 251)));
	}

	@Override
	public void mouseExited(MouseEvent e) {
		this.setBackground(Color.white);
		this.describeTextField.setBackground(Color.white);
		if(checkBoxPanel != null){
			this.checkBoxPanel.setBackground(Color.white);
			this.checkBox.setBackground(Color.white);
		}
	}

	public File getPicture() {
		return picture;
	}

	public void setPicture(File picture) {
		this.picture = picture;
	}

	public MyPopupMenu getPopupMenu() {
		return popupMenu;
	}

	public void setPopupMenu(MyPopupMenu popupMenu) {
		this.popupMenu = popupMenu;
	}

	public JLabel getImageLabel() {
		return imageLabel;
	}

	public void setImageLabel(JLabel imageLabel) {
		this.imageLabel = imageLabel;
	}

	public JTextField getDescribeTextField() {
		return describeTextField;
	}

	public void setDescribeTextField(JTextField describeTextField) {
		this.describeTextField = describeTextField;
	}

	public JCheckBox getCheckBox() {
		return checkBox;
	}

	public void setCheckBox(JCheckBox checkBox) {
		this.checkBox = checkBox;
	}

	public boolean isShowCheckBox() {
		return isShowCheckBox;
	}

	public void setShowCheckBox(boolean isShowCheckBox) {
		this.isShowCheckBox = isShowCheckBox;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public ArrayList<File> getPictures() {
		return pictures;
	}
	public void setPictures(ArrayList<File> pictures) {
		this.pictures = pictures;
	}


	public void sychronizeFileTree_Delete() {
		ShowPicturePanel showPicrurePanel = (ShowPicturePanel)getParent();
		FileTree fileTree = showPicrurePanel.getFileTree();
		DefaultMutableTreeNode currentTreeNode = showPicrurePanel.getCurrentTreeNode();
		Enumeration childrens =  currentTreeNode.children();
		while(childrens.hasMoreElements()){
			DefaultMutableTreeNode childNode = (DefaultMutableTreeNode)childrens.nextElement();
			FileNode fileNode = (FileNode)childNode.getUserObject();
			if(picture.getAbsolutePath().equals(fileNode.getFile().getAbsolutePath())){
				currentTreeNode.remove(childNode);
			}
		}
		DefaultTreeModel model = (DefaultTreeModel) fileTree.getModel();
		model.nodeStructureChanged(currentTreeNode);
	}
	public void sychronizeFileTree_Move() {
		sychronizeFileTree_Delete();
	}
	public void sychronizeFileTree_Copy(String destination,File picture) {
		ShowPicturePanel showPicrurePanel = (ShowPicturePanel)getParent();
		FileTree fileTree = showPicrurePanel.getFileTree();
		DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) fileTree.getModel().getRoot();//得到根节点
		visitAllNodes(rootNode,picture,destination);
	}
	public void visitAllNodes(DefaultMutableTreeNode startNode,File picture,String destination){
		
		if(!startNode.isRoot()){
			if(process(startNode,picture,destination)){//找到节点,处理成功
				ShowPicturePanel showPicrurePanel = (ShowPicturePanel)getParent();
				FileTree fileTree = showPicrurePanel.getFileTree();
				DefaultTreeModel model = (DefaultTreeModel)fileTree.getModel();
				//通知模型数据已改变
				model.nodeStructureChanged(startNode);
				return;
			}
		}
		if(startNode.getChildCount() > 0){
			for(Enumeration e = startNode.children();e.hasMoreElements();){
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
				visitAllNodes(node,picture,destination);
			}
		}
	}
	private boolean process(DefaultMutableTreeNode node,File picture,String destination) {
		FileNode fileNode = (FileNode) node.getUserObject();
		//判断节点的userobject的file路径是否为destination，是则给该节点添加子节点
		if(destination.equals(fileNode.getFile().getAbsolutePath())){
			FileSystemView fileSystemView = FileSystemView.getFileSystemView();
			
				FileNode childFileNode = new FileNode(fileSystemView.getSystemDisplayName(picture),
						picture, fileSystemView.getSystemIcon(picture),
						false, new ArrayList<File>());
				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(childFileNode);
				node.add(childNode);
				fileNode.getPictures().add(picture);
			
			return true;
		}
		return false;
	}
	
}