import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author : zhenweiLi
 * @date :2019-04-06 13:38
 * DESC : 聊天客户端主界面
 */
public class ChatClient extends Frame {

    /**
     * 聊天内容框
     */
    private TextArea mTaContent = new TextArea();

    /**
     * 输入文本框
     */
    private TextField mTfInput = new TextField();

    /**
     * 前后端通讯的 Socket
     */
    private Socket mSocket;

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
        //  增加输入控件回车监听
        mTfInput.addActionListener(new OnInputViewEnter());
        //  显示窗口
        setVisible(true);
        //  自动连接
        connect();
    }

    /**
     * 连接服务器端
     */
    private void connect() {
        try {
            mSocket = new Socket("127.0.0.1", 8888);
            System.out.println("connected");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 当输入框回车时触发该监听
     */
    private class OnInputViewEnter implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String inputContent = mTfInput.getText().trim();
            mTaContent.append(inputContent);
            mTfInput.setText("");
            try {
                DataOutputStream dos = new DataOutputStream(mSocket.getOutputStream());
                dos.writeUTF(inputContent);
                dos.flush();
                dos.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

}
