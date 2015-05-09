package components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.Timer;

import utils.OperateUtils;

/**
 *
 * @author Administrator
 */
public class ViewerChildDialog extends JDialog {

    //显示图片的标签
    PictureLabel pictureLabel;
    //图片列表
    List<Image> imageList;
    //计时器
    Timer timer;
    //图片播放速度
    int speed = 1000;
    //菜单
    JPopupMenu menuBar = new JPopupMenu();
    JMenuItem pause = new JMenuItem("暂停");
    JMenuItem accelerate = new JMenuItem("加速");
    JMenuItem decerate = new JMenuItem("减速");
    JMenuItem exit = new JMenuItem("退出");

    public static ViewerChildDialog dialog;

    public static ViewerChildDialog getInstanceDialog(List<Image> imageList, PictureLabel pictureLabel) {
        if (dialog == null) {
            dialog = new ViewerChildDialog(imageList, pictureLabel);
        }
        return dialog;
    }

    private ViewerChildDialog(List<Image> imageList, PictureLabel pictureLabel) {
        this.pictureLabel = pictureLabel;
        this.imageList = imageList;        
        timer = new Timer(speed, new TimerListener());
        initImage();
        pause.setIcon(new ImageIcon("fullscreen.png"));        
        menuBar.add(pause);
        menuBar.add(accelerate);
        menuBar.add(decerate);
        menuBar.add(exit);

        addListener();

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        this.setUndecorated(true);
        this.setSize(d.width, d.height);
        this.setVisible(true);
        this.setModal(true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
    }
    public void initImage(){
        
        //获得最顶层面板（内容层）        
        this.getContentPane().add(this.pictureLabel,BorderLayout.CENTER);
        this.getContentPane().setBackground(Color.black);
        pictureLabel.setImage(imageList.get(imageList.indexOf(pictureLabel.getImage()) ));
        updateImageLocation2();
      //移除缩放图片，拖动图片的监听器
        pictureLabel.removeMouseWheelListener(pictureLabel.EnlargeDcrease);
        pictureLabel.removeMouseMotionListener(pictureLabel.move);
        timer.start();
    }

    private void addListener() {
        //显示菜单
        pictureLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    menuBar.show(e.getComponent(), e.getX(), e.getY());
                }
            }

        });

        //退出
        exit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                pictureLabel.addMouseMotionListener(pictureLabel.move);
                pictureLabel.addMouseWheelListener(pictureLabel.EnlargeDcrease);
                timer.stop();
                //隐藏子窗口
                dialog.setVisible(false);
            }

        });

        //暂停/播放
        pause.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if ("暂停".equals(pause.getText())) {
                    timer.stop();
                    pause.setText("播放");
                } else {
                    timer.start();
                    pause.setText("暂停");
                }
            }

        });

        //加速
        accelerate.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (speed > 200) {
                    speed -= 200;
                } else {
                    accelerate.setEnabled(false);
                    OperateUtils.showTips("已达最大速度!", 2);
                }
                timer.setDelay(speed);
            }

        });

        //减速
        decerate.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                accelerate.setEnabled(true);
                speed += 200;
                timer.setDelay(speed);
            }

        });
    }
    //设置图片大小及位置
    public void updateImageLocation2() {
        //设置图片默认大小
        int w = pictureLabel.getImage().getWidth(null);
        int h = pictureLabel.getImage().getHeight(null);
        if (w > pictureLabel.getWidth() || h > pictureLabel.getHeight()) {
            double p = 1.0 * h / w;
            w = pictureLabel.getWidth() * 3 / 5;
            h = (int) (w * p);
        }
        pictureLabel.setImageWidth(w);
        pictureLabel.setImageHeight(h);
        //设置图片默认位置
        pictureLabel.setIx(pictureLabel.getWidth() / 2 - pictureLabel.getImageWidth() / 2);
        pictureLabel.setIy(pictureLabel.getHeight() / 2 - pictureLabel.getImageHeight() / 2);
        //重绘图片
        pictureLabel.repaint();
    }
    //时钟监听器
    private class TimerListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int next = imageList.indexOf(pictureLabel.getImage()) + 1;
            if (next >= imageList.size()) {
                next = 0;
            }
            pictureLabel.setImage(imageList.get(next));
            updateImageLocation2();
        }
    }

}
