package Client;

import Shared.GameResponse;
import Shared.Request;
import Shared.Response;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientMain {

    private Socket socket            = null;
    private DataInputStream inputClient   = null;
    private ObjectOutputStream outClient     = null;

    private ObjectInputStream inServer  =  null;

    public ClientMain(String address, int port)
    {
        // establish a connection
        try
        {
            socket = new Socket(address, port);
            System.out.println("Connected");

            // takes input from terminal
            inputClient  = new DataInputStream(System.in);

            // sends output to the socket
            outClient  = new ObjectOutputStream(socket.getOutputStream());

            inServer = new ObjectInputStream(socket.getInputStream());
        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }

        // string to read message from input
        String line = "";

        // keep reading until "Over" is input
        while (!line.equals("0"))
        {
            runMenu();
            try
            {
                line = inputClient.readLine();
                switch (line){
                    case "1"://Create Account:
                        Scanner myObj = new Scanner(System.in);
                        myObj = new Scanner(System.in);
                        System.out.println("Welcome to create User");
                        System.out.println("Enter Username :");
                        String username = myObj.nextLine();
                        System.out.println("Enter Password :");
                        String passwordUser = myObj.nextLine();
                        System.out.println("Enter BirthDate :");
                        String birthDate = myObj.nextLine();
                        Request request=new Request();
                        request.setId(1);
                        request.setUsername(username);
                        request.setPassword(passwordUser);
                        request.setBirthDate(birthDate);
                        outClient.writeObject(request);

                        Response response =(Response) inServer.readObject();
                        System.out.println(response.getMessage());
                        break;
                    case "2"://Login
                        Scanner myObj1 = new Scanner(System.in);
                        System.out.println("Welcome to Login User");
                        System.out.println("Enter Username :");
                        username = myObj1.nextLine();
                        System.out.println("Enter Password :");
                        passwordUser = myObj1.nextLine();
                        request=new Request();
                        request.setId(2);
                        request.setUsername(username);
                        request.setPassword(passwordUser);
                        outClient.writeObject(request);

                        response =(Response) inServer.readObject();
                        System.out.println(response.getMessage());
                        break;

                    case "3"://LogOut
                        Scanner myObj2 = new Scanner(System.in);
                        System.out.println("Welcome to LogOut User");
                        System.out.println("Enter Username :");
                        username = myObj2.nextLine();
                        System.out.println("Enter Password :");
                        passwordUser = myObj2.nextLine();
                        request=new Request();
                        request.setId(3);
                        request.setUsername(username);
                        request.setPassword(passwordUser);
                        outClient.writeObject(request);

                        response =(Response) inServer.readObject();
                        System.out.println(response.getMessage());
                        break;
                    case "4"://View Video Game Catalog
                        //Scanner myObj3 = new Scanner(System.in);
                        System.out.println("Welcome to View Video Game Catalog");
                        request=new Request();
                        request.setId(4);
                        outClient.writeObject(request);

                        response =(Response) inServer.readObject();
                        int count=1;
                        for (GameResponse gameResponse:response.getGameResponseList()){
                            System.out.println("Row :"+count+" , Id:("+gameResponse.getId()+") , Title:("+gameResponse.getTitle()+") , Genre:("+gameResponse.getGenre()
                                    +") , Price:("+gameResponse.getPrice()+")");
                            count++;
                        }
                        break;
                    case "5"://View Detail Video Game
                        Scanner myObj3 = new Scanner(System.in);
                        System.out.println("Welcome to View Detail Video Game");
                        System.out.println("Enter ID Of Video Game :");
                        String id = myObj3.nextLine();
                        request=new Request();
                        request.setId(5);
                        request.setIdGame(id);
                        outClient.writeObject(request);

                        response =(Response) inServer.readObject();
                        if(response.getStatus().equals("0")){
                            for (GameResponse gameResponse:response.getGameResponseList()){
                                System.out.println("Id:("+gameResponse.getId()+") , Title:("+gameResponse.getTitle()+") , Genre:("+gameResponse.getGenre()
                                        +") , Price:("+gameResponse.getPrice()+") , file_path:("+gameResponse.getFile_path()+")");

                            }
                        }else {
                            System.out.println(response.getMessage());
                        }
                        break;
                    case "6"://Download Video Games
                        Scanner myObj4 = new Scanner(System.in);
                        System.out.println("Welcome to Download Video Game");
                        System.out.println("Enter ID Of Video Game :");
                        id = myObj4.nextLine();
                        request=new Request();
                        request.setId(6);
                        request.setIdGame(id);
                        outClient.writeObject(request);

                        response =(Response) inServer.readObject();
                        System.out.println(response.getMessage());
                        break;

                }

            }
            catch(IOException i)
            {
                System.out.println(i);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        // close the connection
        try
        {
            Request request=new Request();
            request.setId(0);
            outClient.writeObject(request);
            inputClient.close();
            outClient.close();
            socket.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }
    public static void main(String[] args) {

        ClientMain client = new ClientMain("127.0.0.1", 5000);
        runMenu();

    }

    public static void runMenu(){
        String[] options = {
                "0- Exist Menu",
                "1- Create Account",
                "2- Login",
                "3- LogOut",
                "4- List Of Games",
                "5- Get Detail Video Game",
                "6- Download Video Game",

        };
        System.out.println("------------------MAIN MENU-----------------");

        for (String option : options){
            System.out.println(option);
        }
        System.out.print("Choose your option : ");
    }
}
