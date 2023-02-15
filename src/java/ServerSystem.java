import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ServerSystem {
    /*
    创建一个服务器对象，持续接收socket发送的消息，目前为注册和登录请求
    当接收到注册请求时，发送给数据库验证注册信息，成功则加入数据库
    当接收到登录请求时，发送给数据库验证登录信息，成功则广播上线并为该用户建立一个单独线程接收其消息
     */
    ServerSocket serverSocket = null;
    public ServerSystem() {}
    public void start() {
        Socket socket = null;
        // hashMap映射socket和用户名的关系
        HashMap<Socket, String> hashMap = new HashMap<Socket, String>();
        //创建数据库对象，传入hashMap
        Sql db = new Sql(hashMap);
        //建一个socket尝试连接，成功了就说明服务器已经开启
        try {
            serverSocket = new ServerSocket(8000);
            for(;;) {
                //持续接收用户请求
                socket = serverSocket.accept();
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                Scanner scanner = new Scanner(socket.getInputStream());
                //每个请求的前三条信息一致为请求类型，用户名和密码
                String type = scanner.nextLine();
                String username = scanner.nextLine();
                String password = scanner.nextLine();
                //随后根据请求类型执行不同操作
                //注册

                if(type.equals("REGISTER")) {
                    String passwordConfirm = scanner.nextLine();
                    String gender = scanner.nextLine();
                    String address = scanner.nextLine();
                    String age = scanner.nextLine();
                    //用户名不能包含空格
                    if(!isUsernameValid(username)) {
                        rejectMessage(printWriter, scanner, "no space, length 1-9 in username");
                        continue;
                    }

                    //用户名是否存在
                    if(!db.checkUsername(username)) {
                        rejectMessage(printWriter, scanner, "exist same username");
                        continue;
                    }
                    //两次密码是否一致
                    if(!password.equals(passwordConfirm)) {
                        rejectMessage(printWriter, scanner,"password is inconsistent");
                        continue;
                    }
                    //性别只能是male和female(不区分大小写)
                    if(!(gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("female"))) {
                        rejectMessage(printWriter, scanner, "male or female!");
                        continue;
                    }
                    //年龄在1-99间的整数，为什么没有一百以上？问就是懒得写正则表达式
                    if(!isAgeCorret(age)) {
                        rejectMessage(printWriter, scanner,"age of 0 ~ 99");
                        continue;
                    }
                    //通过判定，向数据库添加用户
                    db.addNewUser(username, password, gender, address, Integer.valueOf(age));
                    printWriter.println("welcome");
                    printWriter.flush();
                    printWriter.close();
                    scanner.close();
                }
                //登录
                else {
                    //用户名不存在
                    if(db.checkUsername(username)) {
                        rejectMessage(printWriter, scanner, "username does not exist");
                        continue;
                    }
                    //密码错误
                    if(!db.checkPassword(username, password)) {
                        rejectMessage(printWriter, scanner, "incorrect password");
                        continue;
                    }
                    //已经登录
                    if(hashMap.containsValue(username)) {
                        rejectMessage(printWriter, scanner, "you have already logged in");
                        continue;
                    }

                    printWriter.println("welcome");
                    //将目前在线的用户用空格隔开放入一个String发送给用户，用于创建在线用户列表(知道为什么检测空格了吧，程序员能力不足所以用户来承担^_^)
                    String userList = "";
                    for(String value: hashMap.values()) {
                        userList += value + " ";
                    }
                    printWriter.println(userList);
                    printWriter.flush();
                    //添加用户并为其开启独立线程
                    hashMap.put(socket, username);
                    ServerThread serverThread = new ServerThread(socket, username, db);
                    serverThread.start();
                    //为用户发送离线时消息
                    db.sendHistory(socket, username);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            try{
                serverSocket.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    // 拒绝用户登录/注册并发送相应消息
    public static void rejectMessage(PrintWriter printWriter, Scanner scanner,String message) throws Exception {
        printWriter.println(message);
        printWriter.flush();
        printWriter.close();
        scanner.close();
    }
    //检查用户名是否包含空格
    public static boolean isUsernameValid(String username) {
        String pattern = "^[^ ]{1,9}$";
        boolean isMatch = Pattern.matches(pattern, username);
        return isMatch;
    }
    // 检测年龄是否合法
    public static boolean isAgeCorret(String password) {
        String pattern = "^[0-9]?[0-9]{1}$";
        boolean isMatch = Pattern.matches(pattern, password);
        return isMatch;
    }
    //关闭服务器，但现在还不太完善
    public void stop() {
        try {
            if(!serverSocket.isClosed())
                serverSocket.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

