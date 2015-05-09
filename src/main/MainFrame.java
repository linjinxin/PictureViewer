package main;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;

import components.MyMenuBar;
import components.MyStatusBar;
import components.MyToolBar;
import components.ShowPicturePanel;
import filetree.FileNode;
import filetree.FileTree;
import filetree.FileTreeModel;
import filetree.FileTreeRenderer;
/* 
 * ������ 
 */
public class MainFrame extends JFrame{
	MyMenuBar menuBar;
	MyStatusBar statusBar;//״̬��
	MyToolBar toolBar;//������
	ShowPicturePanel showPicturePanel;//��ʾͼƬ�����
	
	public MainFrame(){
		
		menuBar = new MyMenuBar();
		statusBar = new MyStatusBar();
		toolBar = new MyToolBar();
		showPicturePanel = new ShowPicturePanel(toolBar,menuBar);
		
		/* ��ʼ����Ŀ¼�� */
		FileTree fileTree = establishFiletree();
		/* ��������Ŀ¼�� */
		showPicturePanel.setFileTree(fileTree);
		
		JScrollPane jsp = new JScrollPane(fileTree);
		jsp.setPreferredSize(new Dimension(200,590));
		jsp.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		
		Container contentPane = this.getContentPane();
		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerSize(8);
		contentPane.add(splitPane);
		splitPane.setLeftComponent(jsp);
		JScrollPane jsp1 = new JScrollPane(showPicturePanel);
		jsp1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		splitPane.setRightComponent(jsp1);
		this.addListenerToMenuBar();//��MenuBar���������Ӽ���
		this.setJMenuBar(menuBar);
		this.add(statusBar,BorderLayout.SOUTH);
		this.add(toolBar,BorderLayout.NORTH);
		
		this.setTitle("PictureViewer");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(900, 700);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
	}
	private FileTree establishFiletree() {
		FileTree fileTree = new FileTree(showPicturePanel,statusBar);
		FileTreeModel model = new FileTreeModel(
				new DefaultMutableTreeNode(new FileNode("root", null,
						null, true,new ArrayList<File>())));
		FileTreeRenderer renderer = new FileTreeRenderer();
		fileTree.setModel(model);
		fileTree.setCellRenderer(renderer);
		return fileTree;
	}
	public void addListenerToMenuBar(){
		//�˳�
		this.menuBar.getFile_exit().addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});
		//����
		this.menuBar.getEdit_copy().addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		
		//�ƶ�
		this.menuBar.getEdit_move().addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			}
			});
		
		//ɾ��
		this.menuBar.getEdit_delete().addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			}
			});
		
		//������
		this.menuBar.getEdit_rename().addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			}
			});
		}
	
	public static void main(String[] args) {
		try {
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
			new MainFrame();
			
	}
}
