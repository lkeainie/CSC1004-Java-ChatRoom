import org.sqlite.SQLiteConfig;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.*;
import java.util.HashMap;

public class Sql
{
    /*
    数据库，主要功能是验证信息和存储用户信息，历史记录，发送消息
     */
    private HashMap<Socket, String> hashMap = null;
    private SQLiteConfig sqLiteConfig = null;
    //}
    public Sql(HashMap<Socket, String> hashMap) {
        //构造方法，创建数据库dateBase.db
        try {
            //Class.forName("org.sqlite.JDBC");
            sqLiteConfig = new SQLiteConfig();
            //设置Text最大长度限制
            sqLiteConfig.setPragma(SQLiteConfig.Pragma.LIMIT_SQL_LENGTH, "2147483647");
            sqLiteConfig.setSharedCache(true);
            sqLiteConfig.enableRecursiveTriggers(true);
            this.hashMap = hashMap;
            //连接
            String sql = "SELECT name FROM sqlite_master WHERE type='table'";
            Connection connection = DriverManager.getConnection("jdbc:sqlite:dateBase.db", sqLiteConfig.toProperties());
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            boolean checkTableUser = true, checkTableHistory = true;
            while(resultSet.next()) {
                if(resultSet.getString("name").equals("USER")) checkTableUser = false;
                if(resultSet.getString("name").equals("HISTORY")) checkTableHistory = false;
            }
            if(checkTableHistory) {
                String tableSql = "CREATE TABLE HISTORY " +
                                "(TYPE TEXT NOT NULL, " +
                                "MESSAGE TEXT NOT NULL, " +
                                "SENDUSER TEXT NOT NULL, " +
                                "DATETIME TEXT NOT NULL)";
                statement = connection.prepareStatement(tableSql);
                statement.executeUpdate();
            }
            if(checkTableUser) {
                String tableSql = "CREATE TABLE USER " +
                                "(USERNAME TEXT NOT NULL, " +
                                "PASSWORD TEXT NOT NULL, " +
                                "GENDER TEXT NOT NULL, " +
                                "ADDRESS TEXT NOT NULL, " +
                                "AGE INT, " +
                                "DATETIME TEXT NULL)";
                statement = connection.prepareStatement(tableSql);
                statement.executeUpdate();

            }
            statement.close();
            connection.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    //检查用户名是否存在
    public boolean checkUsername(String username_) {
        ResultSet resultSet = null;
        boolean res = true;
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:dateBase.db", sqLiteConfig.toProperties());
            String sql = "SELECT USERNAME FROM USER WHERE USERNAME = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username_);
            resultSet = statement.executeQuery();
            res = !resultSet.next();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(res) return true;
            else return false;
        }
    }
    //检查密码是否正确
    public boolean checkPassword(String username_, String password_) {
        ResultSet resultSet = null;
        boolean res = false;
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:dateBase.db", sqLiteConfig.toProperties());
            String sql = "SELECT * FROM USER WHERE USERNAME = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username_);
            resultSet = statement.executeQuery();
            if(resultSet.getString("PASSWORD").equals(password_))
                res = true;
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }
    //发送历史记录给上线的用户
    public void sendHistory(Socket socket, String username_) {
        ResultSet resultSet = null;
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:dateBase.db", sqLiteConfig.toProperties());
            String sql = "SELECT * FROM USER WHERE USERNAME = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username_);
            ResultSet rs = statement.executeQuery();
            String timestr = rs.getString("DATETIME");
            //发送离线时间后的所有消息
            //sql = "SELECT * from HISTORY WHERE datetime(DATETIME) > datetime(?)";
            sql = "SELECT * from HISTORY";
            statement = connection.prepareStatement(sql);
            //statement.setString(1, timestr);
            resultSet = statement.executeQuery();
            while(resultSet.next()) {
                sendMessage(resultSet.getString("TYPE"), resultSet.getString("MESSAGE"),
                            resultSet.getString("SENDUSER"), resultSet.getString("DATETIME"), socket);
            }
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //记录离线时间
    public void recordTime(String username_, String timestr) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:dateBase.db", sqLiteConfig.toProperties());
            String sql = "UPDATE USER SET DATETIME = ? WHERE USERNAME = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, timestr);
            statement.setString(2, username_);
            statement.executeUpdate();
            statement.close();
            connection.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    //添加新用户
    public void addNewUser(String username_, String password_, String gender_, String address_, int age_) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:dateBase.db", sqLiteConfig.toProperties());
            String sql = "insert into User values(?, ?, ?, ?, ?, NULL)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username_);
            statement.setString(2, password_);
            statement.setString(3, gender_);
            statement.setString(4, address_);
            statement.setInt(5, age_);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    // 移除在线用户
    public void removeUser(Socket socket) {
        hashMap.remove(socket);
    }
    //添加新消息
    public void addNewMessage(String type_, String message_, String sendUser_, String dateTime_) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:dateBase.db", sqLiteConfig.toProperties());
            String sql = "INSERT INTO HISTORY VALUES(?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, type_);
            statement.setString(2, message_);
            statement.setString(3, sendUser_);
            statement.setString(4, dateTime_);
            statement.executeUpdate();
            sendMessage(type_, message_, sendUser_, dateTime_);
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //发送消息,历史记录重载版，单独发送所以多了一个socket参数
    public void sendMessage(String type, String message, String sendUser, String dateTime, Socket socket) {
        PrintWriter printWriter = null;
        try{
            printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.println(type);
            printWriter.println(sendUser);
            printWriter.println(dateTime);
            printWriter.println(message);
            printWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //发送消息，群发重载版
    public void sendMessage(String type, String message, String sendUser, String dateTime) {
        for(Socket sockets : hashMap.keySet()) {
            PrintWriter printWriter = null;
            try{
                printWriter = new PrintWriter(sockets.getOutputStream());
                printWriter.println(type);
                printWriter.println(sendUser);
                printWriter.println(dateTime);
                printWriter.println(message);
                printWriter.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    // 发送消息，登录登出重载版，参数较少
    public void sendMessage(String type, String sendUser) {
        for(Socket sockets : hashMap.keySet()) {
            PrintWriter printWriter = null;
            try{
                printWriter = new PrintWriter(sockets.getOutputStream());
                printWriter.println(type);
                printWriter.println(sendUser);
                printWriter.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
