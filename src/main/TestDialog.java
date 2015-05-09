package main;

import components.ViewerDialog;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Administrator
 */
public class TestDialog {

    public static void main(String[] args) {
        List imageList = new ArrayList();
        File img ;
        
        File path=new File("image");
        imageList=Arrays.asList(path.listFiles());
        img=new File("image/Œ¥±ÍÃ‚-1.bmp");
        ViewerDialog  frame = new ViewerDialog(imageList, img);   
        //test();
        //ViewerChildDialog  frame = new ViewerChildDialog(imageList, img);   
    }
   
    public static void test(){
        JFrame f1=new JFrame();
        JFrame f2=new JFrame();
        
        JLabel jl=new JLabel("this is only a label");
        f1.add(jl);
        f2.add(jl);
        f1.setTitle("f1");
        f2.setTitle("f2");
        f1.setVisible(true);
        f2.setVisible(true);
        f1.setSize(100,100);
        f2.setSize(200,200);
        f2.dispose();
        f1.add(jl);
    }
}


