import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ServerThread extends Thread {
    /*
    单独为一个用户建立的服务器进程，接收用户消息并做出不同反应
     */
    private Sql db = null;
    private Socket socket = null;
    private String username = null;
    // 构造方法
    public ServerThread(Socket socket, String username, Sql db) {
        this.socket = socket;
        this.db = db;
        this.username = username;
    }
    // 向HashMap(在线)里的用户传递信息
    @Override
    public void run() {
        //刚启动时向所有用户发送消息表示自己登录了
        db.sendMessage("USERLOGIN", username);
        Scanner scanner = null;
        try {
            scanner = new Scanner(socket.getInputStream());
            //持续接收用户消息
            while(scanner.hasNextLine()) {
                String type = scanner.nextLine();
                //接收到用户登出消息(按下logout按钮)，广播给全体在线用户，并关闭socket
                if(type.equals("LOGOUT")) {
                    db.sendMessage("USERLOGOUT", username);
                    db.removeUser(socket);
                    //记录当前下线时间存入数据库，为下一次登录发送离线消息准备
                    String timeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    db.recordTime(username, timeStr);
                    try {
                        if(!socket.isClosed())
                            socket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //在不是登出的情况下，就是需要广播的消息
                else{
                    String timeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    String message = scanner.nextLine();
                    //加入历史记录
                    db.addNewMessage(type, message, username, timeStr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(scanner != null)
                    scanner.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                //这里为直接关闭窗口触发的登出，和之前一样
                db.removeUser(socket);
                db.sendMessage("USERLOGOUT", username);
                String timeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                db.recordTime(username, timeStr);
                if (!socket.isClosed()){
                    socket.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}