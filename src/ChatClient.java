import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author : zhenweiLi
 * @date :2019-04-06 13:38
 * DESC : 聊天客户端主界面
 */
public class ChatClient extends Frame {

    /**
     * 聊天内容框
     */
    private TextArea mTaContent  = new TextArea();

    /**
     * 输入文本框
     */
    private TextField mTfInput = new TextField();

    public static void main(String[] args) {
        //  显示主界面
        new ChatClient().launchFrame();
    }

    /**
     * 初始化主界面
     */
    private void launchFrame() {
        //  设置主界面
        setLocation(400, 300);
        this.setSize(300, 300);
        //  添加子控件
        add(mTaContent, BorderLayout.NORTH);
        add(mTfInput, BorderLayout.SOUTH);
        pack();
        //  关闭按钮监听
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        //  显示窗口
        setVisible(true);
    }

}
