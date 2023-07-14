package sshConfig;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Properties;
import java.util.Scanner;

public class doStuff
{
  private static void reData(BufferedReader br)
  {
    try
    {
      String line = br.readLine();
      System.out.println(line);
      do
      {
        System.out.println(line);
        line = br.readLine();
      } while (!line.contains("#"));
    }
    catch (IOException e)
    {
      System.out.println(e.getMessage());
    }
  }
  
  public static void main(String[] args)
  {
    JSch jsch = new JSch();
    String ip = "127.0.0.1";
    String user = "x";
    String pass = "x";
    int port = 22;
    
    Scanner inputReader = new Scanner(System.in);
    for (int i = 1; i <= 2; i++)
    {
      if (i != 2)
      {
        System.out.println("Auto Konfiguration Unifi AP's");
        System.out.println("IP: ");
        ip = inputReader.nextLine();
        System.out.println("connecting " + ip + " " + user + " " + pass);
      }
      try
      {
        Session session = jsch.getSession(user, ip, port);
        session.setPassword(pass);
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();
        
        Channel channel = session.openChannel("shell");
        channel.connect();
        
        DataInputStream dataIn = new DataInputStream(channel.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(dataIn));
        
        DataOutputStream dataOut = new DataOutputStream(channel.getOutputStream());
        
        dataOut.writeBytes("set-inform http://bla.bla.bla:8080/inform\r\n");
        dataOut.flush();
        System.out.println("sende Daten");
        
        reData(br);
        reData(br);
        if (i != 2)
        {
          System.out.println("Adoptieren Sie den Unify im WebClient");
          System.out.println("Achten Sie darauf das der Ger�tstatus zuerst auf 'Disconnected' wechselt und dr�cken Sie danach 'enter'");
          inputReader.nextLine();
          System.out.print("...");
          Thread.sleep(3300L);
          System.out.print("...");
          Thread.sleep(3300L);
          System.out.print("...");
          Thread.sleep(3300L);
        }
        if (i == 2)
        {
          Thread.sleep(10000L);
          System.out.println("Dr�cken Sie 'enter' um das Programm zu beenden");
          inputReader.nextLine();
        }
        dataIn.close();
        dataOut.close();
        channel.disconnect();
        session.disconnect();
      }
      catch (JSchException e)
      {
        System.out.println(e.getMessage());
      }
      catch (IOException e)
      {
        System.out.println(e.getMessage());
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    }
    inputReader.close();
    System.out.println("Verbindung wird getrennt");
    System.out.println("Programm wird geschlossen");
  }
}
