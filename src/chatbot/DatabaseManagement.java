package chatbot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by slastic on 2/12/2015.
 */
public class DatabaseManagement {

    protected void connectToDatabase(String sql) {
        Connection connect = null;
        Statement stm = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connect = DriverManager.getConnection("jdbc:sqlite:commands.db");

            stm = connect.createStatement();
            stm.executeUpdate(sql);
            stm.close();
            connect.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    protected String[] getCommandFromDatabase(String command, String channel) {
        String sql = "SELECT response, permission FROM customcommands WHERE command='"+command+"' AND channel='"+channel + "';";
        Connection connect = null;
        Statement stm = null;
        String[] answer= new String[2];

        try {
            Class.forName("org.sqlite.JDBC");
            connect = DriverManager.getConnection("jdbc:sqlite:commands.db");

            stm = connect.createStatement();
            ResultSet set = stm.executeQuery(sql);
            answer[0] = set.getString("response");
            answer[1] = set.getString("permission");
            stm.close();
            connect.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }


        return answer;
    }

    protected int getDatabaseSize() {
        String sql = "SELECT * FROM customcommands";
        Connection connect = null;
        Statement stm = null;
        int answer=0;

        try {
            Class.forName("org.sqlite.JDBC");
            connect = DriverManager.getConnection("jdbc:sqlite:commands.db");

            stm = connect.createStatement();
            ResultSet set = stm.executeQuery(sql);
            //answer= set.getMetaData().getColumnCount();
            while(set.next()){
                answer = set.getInt(1);
            }
            stm.close();
            connect.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }


        return answer;
    }

    protected ArrayList getCommands(String channelName) {
        String sql = "SELECT * FROM customcommands WHERE permission = '-e' AND channel='"+channelName+"' order by COMMAND ASC;";
        Connection connect = null;
        Statement stm = null;
        ArrayList<String> answer = new ArrayList<String>();

        try {
            Class.forName("org.sqlite.JDBC");
        connect = DriverManager.getConnection("jdbc:sqlite:commands.db");

        stm = connect.createStatement();
        ResultSet set = stm.executeQuery(sql);
        //answer= set.getMetaData().getColumnCount();
        while(set.next()){
            answer.add(set.getString("command"));
        }
        stm.close();
        connect.close();
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }


        return answer;
    }

    protected int getQuoteSize() {
        String sql = "SELECT * FROM quotes";
        Connection connect = null;
        Statement stm = null;
        int answer=0;

        try {
            Class.forName("org.sqlite.JDBC");
            connect = DriverManager.getConnection("jdbc:sqlite:commands.db");

            stm = connect.createStatement();
            ResultSet set = stm.executeQuery(sql);
            //answer= set.getMetaData().getColumnCount();
            while(set.next()){
                answer = set.getInt(1);
            }
            stm.close();
            connect.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }


        return answer;
    }
    protected String[] getQuote(String channel, String index){
        String sql = "SELECT quote, source FROM quotes WHERE num = '"+ index +"' and channel = '"+ channel+ "';";
        Connection connect = null;
        System.out.println(sql);
        Statement stm = null;
        String[] answer = new String[2];

        try {
            Class.forName("org.sqlite.JDBC");
            connect = DriverManager.getConnection("jdbc:sqlite:commands.db");

            stm = connect.createStatement();
            ResultSet set = stm.executeQuery(sql);
            answer[0] = set.getString("quote");
            answer[1] = set.getString("source");
            stm.close();
            connect.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }


        return answer;

    }
}
