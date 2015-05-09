package components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.Timer;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import utils.OperateUtils;
//查看图片窗口
public class ViewerDialog extends JDialog {
    
    //工具条
     JToolBar toolBar;
    //按纽
    JButton nextPicture, previousPicture, play, accelerate, decelerate;
    JButton enlarge, decrease, rotateR, rotateL;
    JButton type, fullScreen;
    //显示图片的标签
    PictureLabel pictureLabel;
    //信息面板
    JTextField MessageLabel = new JTextField();
    //图像列表
    List<Image> imageList = new ArrayList();
    //图片文件列表
    List<File> pictures;
    //计时器
    Timer timer;
    Timer timer2;
    //图片播放速度
    int JumpSpeed = 3000;//正常模式下的速度
    int MoveSpeed = 20;//胶片模式下的速度
    //播放模式
    int model = 0;
    //旋转角度
    int rotation = 0;
    //正在旋转的图片在图片列表里的下标
    int rotateIndex = -1;
    //子窗口(全屏查看)
    ViewerChildDialog viewerChildDialog;

    public ViewerDialog(List<File> pictures, File currentPicture) {
        this.pictures = pictures;
        System.out.println(this.imageList);
        //this.setSize(new Dimension(1366, 700));
        this.setSize(new Dimension(1024, 400));
        initComponent();
        initImageList(pictures);
        initImage(pictures.indexOf(currentPicture));
        System.out.println(this.imageList);
        timer = new Timer(JumpSpeed, new TimerListener());
        timer2 = new Timer(MoveSpeed, new TimerListener2());

        this.addListener();

        this.setResizable(false);
        this.setVisible(true);
        this.setModal(false);
        this.setTitle("图片查看器");
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    //初始化图像列表
    public void initImageList(List<File> pictures) {
        for (int i = 0; i < pictures.size(); i++) {
            try {
                Image img = ImageIO.read(pictures.get(i));
                if (img != null) {
                    imageList.add(img);
                } else {
                    pictures.remove(i);
                }
            } catch (IOException ex) {
            	 OperateUtils.showTips("图片加载失败!", 2);
            }
        }
    }

    //设置第一张图片并将图片标签加到窗口上
    public void initImage(int index) {
        if (index < 0) {
            index = 0;
        }
        Image img = imageList.get(index);
        MessageLabel.setText(pictures.get(index).getAbsolutePath());
        pictureLabel = PictureLabel.getInstanceLabel(img);
        //设置图片默认大小
        int w = img.getWidth(null);
        int h = img.getHeight(null);
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
        pictureLabel.repaint();

        this.getRootPane().add(pictureLabel, BorderLayout.CENTER);
    }

    //初始化组件
    public void initComponent() {
        toolBar = new JToolBar();
        nextPicture = new JButton("下一张");
        
        play = new JButton("开始播放");
        previousPicture = new JButton("上一张");
        accelerate = new JButton("加速播放");
        decelerate = new JButton("减速播放");
        accelerate.setEnabled(false);
        decelerate.setEnabled(false);
        enlarge = new JButton("放大");
        decrease = new JButton("减小");
        type = new JButton("胶片模式");
        type.setEnabled(false);
        fullScreen = new JButton("全屏显示");
        rotateR = new JButton("顺时针旋转");
        rotateL = new JButton("逆时针旋转");
        toolBar.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolBar.add(type);
        toolBar.add(rotateR);
        toolBar.add(enlarge);
        toolBar.add(decelerate);
        toolBar.add(previousPicture);
        toolBar.add(play);
        toolBar.add(nextPicture);
        toolBar.add(accelerate);
        toolBar.add(decrease);
        toolBar.add(rotateL);
        toolBar.add(fullScreen);
        
        toolBar.setBackground(Color.blue);
   

        //获得最底层面板
        JRootPane rootPane = this.getRootPane();
        rootPane.setLayout(new BorderLayout());
        //将工具条放在窗口主面板的北面
        rootPane.add(toolBar, BorderLayout.NORTH);
        //将信息面板放在窗口的南面
        rootPane.add(MessageLabel, BorderLayout.SOUTH);
        
        MessageLabel.setForeground(Color.orange);
        MessageLabel.setBackground(Color.gray);
        
        rootPane.setBackground(Color.LIGHT_GRAY);
        MessageLabel.setFont(new Font("楷体",Font.BOLD,16));
        for(int i=0;i<toolBar.getComponents().length;i++){
            toolBar.getComponent(i).setFont(new Font("楷体",Font.ITALIC,18));
            toolBar.getComponent(i).setBackground(Color.PINK);
        }     
        
    }

    //顺时针转90度
    public void rotateRight() {
        try {
            rotation += 90;
            if (rotation == 360) {
                rotation = 0;
            }
            //第一次旋转，从标签里得到图片，再得到图片在图片列表的下标
            if (rotateIndex == -1) {
                rotateIndex = imageList.indexOf(pictureLabel.getImage());
            }
            //根据图片在图片列表的下标从路径列表中得到该图片的路径
            Builder b = Thumbnails.of(pictures.get(rotateIndex).getAbsoluteFile())
                    .scale(1.0)
                    .rotate(rotation);
            pictureLabel.setImage(new ImageIcon(b.asBufferedImage()).getImage());
            int wh = pictureLabel.getImageHeight();
            pictureLabel.setImageHeight(pictureLabel.getImageWidth());
            pictureLabel.setImageWidth(wh);
            pictureLabel.setIx(pictureLabel.getWidth() / 2 - pictureLabel.getImageWidth() / 2);
            pictureLabel.setIy(pictureLabel.getHeight() / 2 - pictureLabel.getImageHeight() / 2);
            pictureLabel.repaint();
        } catch (IOException ex) {
        	 OperateUtils.showTips("图片加载失败!", 2);
        }
    }

    //逆时针转90度
    public void rotateLeft() {
        try {
            rotation -= 90;
            if (rotation == -360) {
                rotation = 0;
            }
            //第一次旋转，从标签里得到图片，再得到图片在图片列表的下标
            if (rotateIndex == -1) {
                rotateIndex = imageList.indexOf(pictureLabel.getImage());
            }
            //根据图片在图片列表的下标从路径列表中得到该图片的路径
            Builder b = Thumbnails.of(pictures.get(rotateIndex).getAbsoluteFile())
                    .scale(1.0)
                    .rotate(rotation);
            Image img = new ImageIcon(b.asBufferedImage()).getImage();
            pictureLabel.setImage(img);
            int wh = pictureLabel.getImageHeight();
            pictureLabel.setImageHeight(pictureLabel.getImageWidth());
            pictureLabel.setImageWidth(wh);
            pictureLabel.setIx(pictureLabel.getWidth() / 2 - pictureLabel.getImageWidth() / 2);
            pictureLabel.setIy(pictureLabel.getHeight() / 2 - pictureLabel.getImageHeight() / 2);
            pictureLabel.repaint();
        } catch (IOException ex) {
        	OperateUtils.showTips("图片加载失败!", 2);
        }
    }

    //停止播放
    public void SetStop() {
        timer.stop();
        timer2.stop();
        play.setText("开始播放");
        previousPicture.setEnabled(true);
        nextPicture.setEnabled(true);
        accelerate.setEnabled(false);
        decelerate.setEnabled(false);
        rotateR.setEnabled(true);
        rotateL.setEnabled(true);
        //恢复缩放功能
        enlarge.setEnabled(true);
        decrease.setEnabled(true);
        pictureLabel.setChangeSize(true);
        //恢复拖动功能
        pictureLabel.setCanMove(true);
        type.setEnabled(false);
        updateImageLocation();
        //将图片2 设为空
        pictureLabel.setImage2(null);
        pictureLabel.repaint();
        if (MessageLabel.getText().contains(" ")) {
            String text = MessageLabel.getText().substring(0, MessageLabel.getText().indexOf(" "));
            if (model == 1) {
                //改变信息面板的内容
                if (MessageLabel.getText().contains("~")) {
                    text = text.substring(0, text.indexOf("~"));
                    MessageLabel.setText(text);
                }
            } 
            else {
                MessageLabel.setText(text);
            }
        } else {            
            if (MessageLabel.getText().contains("~")) {
                String text =MessageLabel.getText().substring(0, MessageLabel.getText().indexOf("~"));
                MessageLabel.setText(text);
            }
        }
    }

    //开始播放
    public void SetPlay() {
        if (model == 0) {
            timer.start();
            //正常模式下设置图片大小和位置
            updateImageLocation();
        } else {
            timer2.start();
            //胶片模式下要另外设置图片大小和位置
            pictureLabel.setImageWidth(pictureLabel.getWidth());
            pictureLabel.setImageHeight(pictureLabel.getHeight());
            pictureLabel.setIx(0);
            pictureLabel.setIy(0);
        }
        play.setText("停止播放");
        type.setEnabled(true);
        previousPicture.setEnabled(false);
        nextPicture.setEnabled(false);
        accelerate.setEnabled(true);
        decelerate.setEnabled(true);
        rotateR.setEnabled(false);
        rotateL.setEnabled(false);
        //禁用缩放功能
        enlarge.setEnabled(false);
        decrease.setEnabled(false);
        pictureLabel.setChangeSize(false);
        //禁用拖动功能
        pictureLabel.setCanMove(false);
    }

    //设置图片大小及位置
    public void updateImageLocation() {
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
        //恢复图片默认旋转
        rotation = 0;
        //重绘图片
        pictureLabel.repaint();
    }

    private void addListener() {
        // 显示前一张
        previousPicture.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int front;
                if (rotateIndex >= 0) {
                    front = rotateIndex - 1;
                    rotateIndex = -1;
                } else {
                    front = imageList.indexOf(pictureLabel.getImage()) - 1;
                }
                if (front < 0) {
                    front = imageList.size() - 1;
                }
                pictureLabel.setImage(imageList.get(front));
                MessageLabel.setText(pictures.get(front).getAbsolutePath());
                updateImageLocation();
            }

        });
        //显示后一张
        nextPicture.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int next;
                //图片已发生旋转，只能从图片列表里得到图片
                if (rotateIndex >= 0) {
                    next = rotateIndex + 1;
                    rotateIndex = -1;
                } else {
                    next = imageList.indexOf(pictureLabel.getImage()) + 1;
                }
                if (next >= imageList.size()) {
                    next = 0;
                }
                pictureLabel.setImage(imageList.get(next));
                MessageLabel.setText(pictures.get(next).getAbsolutePath());

                updateImageLocation();
            }
        });
        //自动播放
        play.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if ("开始播放".equals(play.getText())) {
                    if (rotateIndex >= 0) {
                        pictureLabel.setImage(imageList.get(rotateIndex));
                        rotateIndex = -1;
                    }
                    SetPlay();
                } else {
                    SetStop();
                }
            }
        }
        );
        //加速播放
        accelerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                decelerate.setEnabled(true);
                if (model == 0) {
                    if (JumpSpeed > 500) {
                        JumpSpeed -= 500;
                    } else {
                        JOptionPane.showMessageDialog(null, "不能再加速了！");
                        accelerate.setEnabled(false);
                    }
                    timer.setDelay(JumpSpeed);
                } else if (model == 1) {
                    if (MoveSpeed > 2) {
                        MoveSpeed -= 2;
                    } else {
                    	OperateUtils.showTips("已达最大速度!", 2);
                        accelerate.setEnabled(false);
                    }
                    timer2.setDelay(MoveSpeed);
                }
            }
        }
        );
        //减速播放
        decelerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accelerate.setEnabled(true);
                if (model == 0) {
                    if (JumpSpeed < 6000) {
                        JumpSpeed += 500;
                    } else {
                    	JOptionPane.showMessageDialog(null, "不能再减速了！");
                        decelerate.setEnabled(false);
                    }
                    timer.setDelay(JumpSpeed);
                } else if (model == 1) {
                    if (MoveSpeed < 30) {
                        MoveSpeed += 2;
                    } else {
                    	OperateUtils.showTips("已达最小速度!", 2);
                        decelerate.setEnabled(false);
                    }
                    timer2.setDelay(MoveSpeed);
                }
            }
        }
        );
        //放大
        enlarge.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //放大图像
                pictureLabel.EnlargePicture();
                //重绘图像
                pictureLabel.repaint();
            }
        }
        );
        //减小
        decrease.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        pictureLabel.DecreasePicture();
                        pictureLabel.repaint();
                    }
                }
        );
        //播放模式切换
        type.addActionListener( new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e ) {
                        if (model == 0) {
                            //设置成胶片模式
                            model = 1;
                            type.setText("正常模式");
                            pictureLabel.setImageWidth(pictureLabel.getWidth());
                            pictureLabel.setImageHeight(pictureLabel.getHeight());
                            pictureLabel.setIx(0);
                            pictureLabel.setIy(0);
                            //停止计时器1并将其延时复位，开启计时器2
                            timer.stop();
                            timer2.start();
                            //把加减速按纽恢复使用
                            accelerate.setEnabled(true);
                            decelerate.setEnabled(true);
                            pictureLabel.repaint();
                        } else if (model == 1) {
                        	updateImageLocation();
                            //停止计时器2并将其延时复位，开启计时器1
                            timer2.stop();
                            timer.start();
                            //设置成正常模式
                            model = 0;
                            type.setText("胶片模式");
                            //改变信息面板的内容
                            if(MessageLabel.getText().contains("~")){
                            String text = MessageLabel.getText().substring(0, MessageLabel.getText().indexOf("~"));
                            MessageLabel.setText(text+"     当前播放刷新频率"+JumpSpeed+"毫秒");
                            }
                            //把加减速按纽恢复使用
                            accelerate.setEnabled(true);
                            decelerate.setEnabled(true);
                            //将图片2 设为空
                            pictureLabel.setImage2(null);
                        }
                    }

                }
        );
        //顺时针旋转
        rotateR.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e
                    ) {
                        rotateRight();
                    }

                }
        );
        //逆时针旋转
        rotateL.addActionListener(  new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e
                    ) {
                        rotateLeft();
                    }

                }
        );

        //全屏显示
        fullScreen.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                    	//如果当前正处于播放状态下，则先停止播放
                    	if(play.getText()=="停止播放")
                    		SetStop();
                    	else{
                    		 //将旋转的图片复位
                            if (rotateIndex >= 0) {
                                pictureLabel.setImage(imageList.get(rotateIndex));
                                rotateIndex = -1;
                            }
                            //恢复图片大小和位置
                            //updateImageLocation();
                    	}
                    		
                        //第一次打开子窗口，则先创建子窗口
                        if (viewerChildDialog == null) {
                            viewerChildDialog = ViewerChildDialog.getInstanceDialog(imageList, pictureLabel);
                          System.out.println("create new dialog");
                        }                       
                       
                        //将图像标签加到子窗口上，并初始化
                        viewerChildDialog.initImage();
                        //显示子窗口
                        viewerChildDialog.setVisible(true);
                    }

                }
        );

        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowActivated(WindowEvent e) {
            		initImage(imageList.indexOf(pictureLabel.getImage()));    
            		
            		 getRootPane().add(pictureLabel, BorderLayout.CENTER);
            		System.out.println("get focus");
            		pictureLabel.setImage(imageList.get(imageList.indexOf(pictureLabel.getImage()) ));
                    updateImageLocation();
            }
            @Override
            public void windowClosed(WindowEvent e){
            	pictureLabel=null;
            }
        });
    }

    //时钟监听器1
    private class TimerListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int next = imageList.indexOf(pictureLabel.getImage()) + 1;
            System.out.println(next);
            if (next >= imageList.size()) {
                next = 0;
            }
            pictureLabel.setImage(imageList.get(next));
            MessageLabel.setText(pictures.get(next).getAbsolutePath()+"     当前播放刷新频率"+JumpSpeed+"毫秒");

            //设置图片默认大小
            updateImageLocation();
        }
    }

    //时钟监听器2
    private class TimerListener2 implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            //如果图片2为空，则将下一张图片给图片2
            if (pictureLabel.getImage2() == null) {
                int next = imageList.indexOf(pictureLabel.getImage()) + 1;
                if (next >= imageList.size()) {
                    next = 0;
                }
                pictureLabel.setImage2(imageList.get(next));
                pictureLabel.setImageWidth(pictureLabel.getWidth());
                pictureLabel.setImageHeight(pictureLabel.getHeight());
                pictureLabel.setIx(0);
                pictureLabel.setIy(0);
                if (MessageLabel.getText().contains(" ")) {
                    String t = MessageLabel.getText().substring(0, MessageLabel.getText().indexOf(" "));
                    MessageLabel.setText(t + "~~~~~~~~~" + pictures.get(next).getAbsolutePath() + "     当前播放刷新频率" + MoveSpeed + "毫秒");
                }
                else{
                    MessageLabel.setText(MessageLabel.getText() + "~~~~~~~~~" + pictures.get(next).getAbsolutePath() + "     当前播放刷新频率" + MoveSpeed + "毫秒");
                }
            } //如果图片1已经走完，则将图片2 图片1，再将下一张图片给图片2
            else if (pictureLabel.getIx() + pictureLabel.getImageWidth() < 0) {
                int next = imageList.indexOf(pictureLabel.getImage2()) + 1;
                if (next >= imageList.size()) {
                    next = 0;
                }
                pictureLabel.setImage(pictureLabel.getImage2());
                pictureLabel.setImage2(imageList.get(next));
                //将图片1 的坐标复位
                pictureLabel.setIx(0);
                String text1 = MessageLabel.getText().substring(MessageLabel.getText().lastIndexOf("~") + 1,MessageLabel.getText().indexOf(" "));
                MessageLabel.setText(text1 + "~~~~~~~~~" + pictures.get(next).getAbsolutePath()+"     当前播放刷新频率"+MoveSpeed+"毫秒");
            }
            pictureLabel.setIx(pictureLabel.getIx() - 5);
            pictureLabel.repaint();
        }

    }
}
