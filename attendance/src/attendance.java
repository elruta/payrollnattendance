import java.time.*;
import java.time.format.DateTimeFormatter;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.io.*;


public class attendance {
    public static boolean connect(String username, String password){
        Connection conn = null;
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", username, password);
            dialog("ACCESS GRANTED", "green");
            
            return true;
            
        }catch(Exception e){
            String e1 = String.valueOf("ACCESS DENIED");
            dialog(e1, "red");
            
            return false;
        }
        
    };
    public static void employeetap(){
        File f1 = new File("logo.jpg");
        String fpath = f1.getAbsolutePath();
        Frame f = new Frame("Ivy Sisig Salon - Attendance");
        Image icon = Toolkit.getDefaultToolkit().getImage(fpath);
        Label id;
        TextField tfid;
        Button tapin, tapout, overtime;
        
        // Body-----------------------------------------------
        id = new Label("Employee Tap");
        id.setBounds(50,40,100,50);
        
        tfid = new TextField();
        tfid.setBounds(170, 50, 100, 25);
        
        tapin = new Button("Tap In");
        tapin.setBounds(50, 150, 60, 30);
        tapin.setBackground(Color.red);
        tapin.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                String ids = tfid.getText();
                authentication("tapin", ids);
            }
        });
        
        tapout = new Button("Tap Out");
        tapout.setBounds(150, 150, 60, 30);
        tapout.setBackground(Color.red);
        tapout.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                String ids = tfid.getText();
                authentication("tapout", ids);
            }
        });
        
        overtime = new Button("OverTime");
        overtime.setBounds(250, 150, 60, 30);
        overtime.setBackground(Color.red);
        overtime.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                String ids = tfid.getText();
                overtimehour(ids);
            }
        });
        // Body;----------------------------------------------
        f.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                f.dispose();
                System.exit(0);
            }
        });
        f.add(id); f.add(tfid); f.add(tapin); f.add(tapout); f.add(overtime);
        f.setLayout(null);
        f.setIconImage(icon);
        f.setSize(350, 200);
        f.setBackground(Color.PINK);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    private static void authentication(String tap, String id){
        File f1 = new File("logo.jpg");
        String fpath = f1.getAbsolutePath();
        Frame f = new Frame("Authentication");
        Image icon = Toolkit.getDefaultToolkit().getImage(fpath);
        Label user, pass;
        TextField tfuser, tfpass;
        Button submit;
        
        user = new Label("Username");
        user.setBounds(50, 50, 100, 30);
        
        tfuser = new TextField();
        tfuser.setBounds(150, 50, 100, 25);
        
        pass = new Label("Password");
        pass.setBounds(50, 100, 100, 30);
        
        tfpass = new TextField();
        tfpass.setBounds(150, 100, 100, 25);
        tfpass.setEchoChar('*');
        
        submit = new Button("Submit");
        submit.setBounds(150, 140, 100, 30);
        submit.setBackground(Color.RED);
        submit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(tap.equals("tapin")){
                    String us = tfuser.getText();
                    String ps = tfpass.getText();
                    boolean ret = connect(us, ps);
                    if(ret == true){
                        LocalDate date = LocalDate.now();
                        Connection conn;
                        Statement state;
                        ResultSet rs;
                        try{
                            Class.forName("oracle.jdbc.driver.OracleDriver");
                            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", us, ps);
                            state = conn.createStatement();
                            String sql = String.format("SELECT fullname FROM employee WHERE id=%s", id);
                            rs = state.executeQuery(sql);
                            rs.next();
                            String name = rs.getString("fullname");
                            
                            LocalTime time = LocalTime.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm"), formatter1 = DateTimeFormatter.ofPattern("HH"), formatter2 = DateTimeFormatter.ofPattern("mm");
                            String timeforin = formatter.format(time);
                            String timehour = formatter1.format(time);
                            String timemin = formatter2.format(time);
                            int hour = Integer.parseInt(timehour);
                            int minute = Integer.parseInt(timemin);
                            if(hour == 9){
                                if(minute > 3){
                                    String sql1 = String.format("INSERT INTO attendance VALUES (%s, '%s', '%s', '%s', '%s', '%s')", id, name, date, timeforin, "None", "Late");
                                    state.executeUpdate(sql1);
                                    dialog("Tap In Success", "green");
                                    f.dispose();
                                }else if(minute <= 3){
                                    String sql1 = String.format("INSERT INTO attendance VALUES (%s, '%s', '%s', '%s', '%s', '%s')", id, name, date, timeforin, "None", "ontime");
                                    state.executeUpdate(sql1);
                                    dialog("Tap In Success", "green");
                                    f.dispose();
                                }
                            }else if(hour > 9){
                                String sql1 = String.format("INSERT INTO attendance VALUES (%s, '%s', '%s', '%s', '%s', '%s')", id, name, date, "None", "None", "absent");
                                state.executeUpdate(sql1);
                                dialog("Tap In Success", "green");
                                f.dispose();
                            }else if(hour < 9){
                                String sql1 = String.format("INSERT INTO attendance VALUES (%s, '%s', '%s', '%s', '%s', '%s')", id, name, date, timeforin, "None", "early");
                                state.executeUpdate(sql1);
                                dialog("Tap In Success", "green");
                                f.dispose();
                            }
                        }catch(Exception ee){
                        }
                    }else{
                        f.dispose();
                    }
                }else if(tap.equals("tapout")){
                    String us = tfuser.getText();
                    String ps = tfpass.getText();
                    boolean ret = connect(us, ps);
                    if(ret == true){
                        LocalDate date = LocalDate.now();
                        Connection conn;
                        Statement state;
                        ResultSet rs;
                        try{
                            Class.forName("oracle.jdbc.driver.OracleDriver");
                            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", us, ps);
                            state = conn.createStatement();
                            String sql = String.format("SELECT dater FROM attendance where dater='%s'", date, id);
                            rs = state.executeQuery(sql);
                            rs.next();
                            String dater = rs.getString("dater");
                            
                            LocalTime time = LocalTime.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                            String timeforout = formatter.format(time);
                            String sql1 = String.format("UPDATE attendance SET timeout='%s' WHERE id=%s AND dater='%s'", timeforout, id, dater);
                            state.executeUpdate(sql1);
                            dialog("Tap out Success", "green");
                            f.dispose();
                        }catch(Exception ee){
                        }
                    }else{
                        f.dispose();
                    }
                }
            } 
        });
        f.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                f.dispose();
            }
        });
        f.add(user); f.add(tfuser); f.add(pass); f.add(tfpass); f.add(submit);
        f.setLayout(null);
        f.setIconImage(icon);
        f.setSize(350, 200);
        f.setBackground(Color.PINK);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    public static void overtimehour(String id){
        Frame f = new Frame("Over time Hours");
        File f1 = new File("logo.jpg");
        String fpath = f1.getAbsolutePath();
        Image icon = Toolkit.getDefaultToolkit().getImage(fpath);
        TextField hourtf;
        Label user, pass;
        TextField tfuser, tfpass;
        Button submit;
        
        hourtf = new TextField();
        hourtf.setBounds(300, 50, 30, 25);
        
        user = new Label("Username");
        user.setBounds(50, 50, 100, 30);
        
        tfuser = new TextField();
        tfuser.setBounds(150, 50, 100, 25);
        
        pass = new Label("Password");
        pass.setBounds(50, 100, 100, 30);
        
        tfpass = new TextField();
        tfpass.setBounds(150, 100, 100, 25);
        tfpass.setEchoChar('*');
        
        submit = new Button("Submit");
        submit.setBounds(150, 140, 100, 30);
        submit.setBackground(Color.red);
        submit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
               String us = tfuser.getText();
                String ps = tfpass.getText();
                boolean ret = connect(us, ps);
                if(ret == true){
                    Connection conn;
                    Statement state;
                    ResultSet rs;
                    try{
                        Class.forName("oracle.jdbc.driver.OracleDriver");
                        conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", us, ps);
                        state = conn.createStatement();
                        String h = hourtf.getText();
                        String sql = String.format("INSERT INTO overtime values (%s, %s)", id, h);
                        state.executeUpdate(sql);
                        dialog("Success", "green");
                        f.dispose();
                    }catch(Exception ee){
                        String ah = String.valueOf(ee);
                        dialog(ah, "red");
                    }
                }else{
                    f.dispose();
                }
            }
        });
        f.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent ae){
                f.dispose();
            }
        });
        f.add(hourtf); f.add(user); f.add(tfuser); f.add(pass); f.add(tfpass); f.add(submit);
        f.setLayout(null);
        f.setIconImage(icon);
        f.setSize(350, 200);
        f.setBackground(Color.PINK);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    // Dialog
    public static void dialog(String message, String color){
        Frame f1 = new Frame();
        Label l1;
        Dialog d;
        ScrollPane spane;
        l1 = new Label(message, Label.CENTER);
        l1.setBounds(0,0, 0,0);
        spane = new ScrollPane();
        spane.setBounds(5, 5, 250, 100);
        spane.add(l1);
        d = new Dialog(f1, "!!ALERT!!", true);
        d.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                d.dispose();
            }
        });
        if (color.equals("green")){
            d.add(spane);
            d.setSize(300, 100);
            d.setBackground(Color.green);
            d.setLocationRelativeTo(null);
            d.setVisible(true);
        }else{
            d.add(spane);
            d.setSize(300, 100);
            d.setBackground(Color.red);
            d.setLocationRelativeTo(null);
            d.setVisible(true);
        }
    }
    public static void main(String[] args){
        employeetap();
    }
}

