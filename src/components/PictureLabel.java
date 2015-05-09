package components;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
//图片面板类
public class PictureLabel extends JLabel implements Cloneable {

    private Image image;
    private Image image2;
    //图片大小
    private static int imageWidth;
    private static int imageHeight;
    //图片坐标
    private static int ix;//此处千万不能定义成x和y 
    private static int iy;
    //图片拖动时与鼠标的坐标差
    private static int vx;
    private static int vy;
    private static boolean startMove = false;//是否开始拖动
    private boolean changeSize = true;//是否可以缩放
    private boolean canMove = true;//是否可以拖动
    public static PictureLabel pictureLabel;//单例模式
    //鼠标滚轮监听器
    public MouseWheelListenerImpl EnlargeDcrease = new MouseWheelListenerImpl();
    //鼠标拖动监听器
    public MouseMotionListenerImpl move = new MouseMotionListenerImpl();

    //获得图片标签实例
    public static PictureLabel getInstanceLabel(Image image) {
        if (getPictureLabel() == null) {
            setPictureLabel(new PictureLabel(image));
       }
        return getPictureLabel();
    }

    private PictureLabel(Image image) {
        this.setImage(image);
        //拖动图片
        this.addMouseMotionListener(move);
        //缩放图片
        this.addMouseWheelListener(EnlargeDcrease);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getImage() != null) {
            //模式1：只显示一张图片
            if (getImage2() == null) {
                g.drawImage(getImage(), getIx(), getIy(), getImageWidth(), getImageHeight(), this);
            } //模式2：显示两张图片
            else {
                g.drawImage(getImage(), getIx(), getIy(), getImageWidth(), getImageHeight(), this);
                g.drawImage(image2, getIx() + getImageWidth(), getIy(), getImageWidth(), getImageHeight(), this);
            }
        }
    }

    //放大图像
    public boolean EnlargePicture() {
        //放大图像并设置坐标
        if (isChangeSize() == true) {
            if (pictureLabel.getImageHeight() < pictureLabel.getHeight() * 12) {
                pictureLabel.setImageHeight(pictureLabel.getImageHeight() * 5 / 4);
                pictureLabel.setImageWidth(pictureLabel.getImageWidth() * 5 / 4);
                pictureLabel.setIx(pictureLabel.getWidth() / 2 - pictureLabel.getImageWidth() / 2);
                pictureLabel.setIy(pictureLabel.getHeight() / 2 - pictureLabel.getImageHeight() / 2);
            } else {
               // JOptionPane.showMessageDialog(null, "已经太大了！");
                return false;
            }
        }
        return true;
    }

    //减小图像
    public boolean DecreasePicture() {
        if (isChangeSize() == true) {
            if (pictureLabel.getImageHeight() > pictureLabel.getHeight() / 12) {
                pictureLabel.setImageHeight(pictureLabel.getImageHeight() * 3 / 4);
                pictureLabel.setImageWidth(pictureLabel.getImageWidth() * 3 / 4);
                pictureLabel.setIx(pictureLabel.getWidth() / 2 - pictureLabel.getImageWidth() / 2);
                pictureLabel.setIy(pictureLabel.getHeight() / 2 - pictureLabel.getImageHeight() / 2);
            } else {
               // JOptionPane.showMessageDialog(null, "已经够小了！");
                return false;
            }
        }
        return true;
    }

    //鼠标拖拽监听器
    private class MouseMotionListenerImpl implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {
            if (canMove) {
                //判断是否处于拖动状态（是否刚按下鼠标）
                {
                    if (isStartMove() == false) {
                        //如果刚按下鼠标，则重置图片坐标与鼠标坐标之差
                        setVx(getIx() - e.getX());
                        setVy(getIy() - e.getY());
                        setStartMove(true);
                    }
                }
                //图片在正常范围内
                if ((getVy() + e.getY() > (getPictureLabel().getHeight() - getImageHeight())) && (getVy() + e.getY() < 0)) {
                    //用图片与刚按下时鼠标的坐标差加上此刻的鼠标坐标表示图片此刻的坐标
                    setIy(getVy() + e.getY());
                    setCursor(new Cursor(Cursor.MOVE_CURSOR));
                }
                if ((getVx() + e.getX() > getPictureLabel().getWidth() - getImageWidth()) && getVx() + e.getX() < 0 && isStartMove() == true) {
                    //用图片与刚按下时鼠标的坐标差加上此刻的鼠标坐标表示图片此刻的坐标
                    setIx(getVx() + e.getX());
                    setCursor(new Cursor(Cursor.MOVE_CURSOR));
                }

                repaint();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            setStartMove(false);
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    //鼠标滚轮监听器
    private class MouseWheelListenerImpl implements MouseWheelListener {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (e.getWheelRotation() < 0) {
                EnlargePicture();
            } else {
                DecreasePicture();
            }
            repaint();
        }
    }

    /**
     * @return the imageWidth
     */
    public int getImageWidth() {
        return imageWidth;
    }

    /**
     * @param imageWidth the imageWidth to set
     */
    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    /**
     * @return the imageHeight
     */
    public int getImageHeight() {
        return imageHeight;
    }

    /**
     * @param imageHeight the imageHeight to set
     */
    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    /**
     * @return the ix
     */
    public int getIx() {
        return ix;
    }

    /**
     * @param ix the ix to set
     */
    public void setIx(int ix) {
        this.ix = ix;
    }

    /**
     * @return the iy
     */
    public int getIy() {
        return iy;
    }

    /**
     * @param iy the iy to set
     */
    public void setIy(int iy) {
        this.iy = iy;
    }

    /**
     * @return the image
     */
    public Image getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * @return the image2
     */
    public Image getImage2() {
        return image2;
    }

    /**
     * @param image2 the image2 to set
     */
    public void setImage2(Image image2) {
        this.image2 = image2;
    }

    /**
     * @return the vx
     */
    public static int getVx() {
        return vx;
    }

    /**
     * @param aVx the vx to set
     */
    public static void setVx(int aVx) {
        vx = aVx;
    }

    /**
     * @return the vy
     */
    public static int getVy() {
        return vy;
    }

    /**
     * @param aVy the vy to set
     */
    public static void setVy(int aVy) {
        vy = aVy;
    }

    /**
     * @return the startMove
     */
    public static boolean isStartMove() {
        return startMove;
    }

    /**
     * @param aStartMove the startMove to set
     */
    public static void setStartMove(boolean aStartMove) {
        startMove = aStartMove;
    }

    /**
     * @return the pictureLabel
     */
    public static PictureLabel getPictureLabel() {
        return pictureLabel;
    }

    /**
     * @param aPictureLabel the pictureLabel to set
     */
    public static void setPictureLabel(PictureLabel aPictureLabel) {
        pictureLabel = aPictureLabel;
    }

    /**
     * @return the changeSize
     */
    public boolean isChangeSize() {
        return changeSize;
    }

    /**
     * @param changeSize the changeSize to set
     */
    public void setChangeSize(boolean changeSize) {
        this.changeSize = changeSize;
    }

    /**
     * @return the canMove
     */
    public boolean isCanMove() {
        return canMove;
    }

    /**
     * @param canMove the canMove to set
     */
    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

}
