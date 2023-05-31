package Server;

import Client.ClientMain;
import Shared.GameResponse;
import Shared.Request;
import Shared.Response;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ServerMain {

    static final String DB_URL = "jdbc:postgresql://localhost/postgres";
    static final String USER = "postgres";
    static final String PASS = "123456";
    private Socket socket   = null;
    private ServerSocket server   = null;
    private ObjectInputStream inServer  =  null;
    private ObjectOutputStream outServer     = null;

    public static long userId=0;

    public ServerMain(int port)
    {
        // starts server and waits for a connection
        try
        {
            server = new ServerSocket(port);
            System.out.println("Server started");

            System.out.println("Waiting for a client ...");

            socket = server.accept();
            System.out.println("Client accepted");

            // takes input from the client socket
            inServer = new ObjectInputStream (socket.getInputStream());


            // sends output to the socket
            outServer  = new ObjectOutputStream(socket.getOutputStream());

            int id = -1;
            while (id!=0){
                try {
                    Request request =(Request) inServer.readObject();
                    id=request.getId();
                    if(id==1){
                        String username=accountIsExist(request.getUsername());
                        Response response=new Response();
                        if(username.equals("")){
                            createAccount(request.getUsername(),request.getPassword(),request.getBirthDate());
                            response.setStatus("0");
                            response.setMessage("Success To Create User");
                        }
                        else {

                            response.setStatus("-1");
                            response.setMessage("Fail To Create User");
                            outServer.writeObject(response);
                        }
                        outServer.writeObject(response);

                    }
                    else if(id==2){
                        //request =(Request) inServer.readObject();
                        long idRow=loginUser(request.getUsername(),hashPass(request.getPassword()));
                        Response response=new Response();
                        if(idRow!=0){
                            response.setStatus("0");
                            response.setMessage("User has been login");
                            setTokenAccount(idRow,"3jnjkAjklkBF");
                            userId=idRow;
                        } else {
                            response.setStatus("-1");
                            response.setMessage("Fail to User Login");
                        }
                        outServer.writeObject(response);
                    }
                    else if(id==3){
                        //request =(Request) inServer.readObject();
                        long idRow=loginUser(request.getUsername(),hashPass(request.getPassword()));
                        Response response=new Response();
                        if(idRow!=0){
                            response.setStatus("0");
                            response.setMessage("User has been LogOut");
                            setTokenAccount(idRow,"");
                        } else {
                            response.setStatus("-1");
                            response.setMessage("Fail to User LogOut");
                        }
                        outServer.writeObject(response);
                    }
                    else if(id==4){
                        List<GameResponse> gameResponseList=getGameList();
                        Response response=new Response();
                        response.setStatus("0");
                        response.setMessage("Get List Game success");
                        response.setGameResponseList(gameResponseList);
                        outServer.writeObject(response);
                    }
                    else if(id==5){
                        if(userId==0){
                            Response response=new Response();
                            response.setStatus("-1");
                            response.setMessage("User Not Login");
                            outServer.writeObject(response);
                        }else {
                            List<GameResponse> gameResponseList=getGameById(request.getIdGame());
                            Response response=new Response();

                            if(gameResponseList.size()>0){
                                response.setStatus("0");
                                response.setMessage("Get Data For  Game success");
                                response.setGameResponseList(gameResponseList);
                            }else {
                                response.setStatus("-1");
                                response.setMessage("Data Not Found");
                            }

                            outServer.writeObject(response);
                        }

                    }
                    else if(id==6){
                        if(userId==0){
                            Response response=new Response();
                            response.setStatus("-1");
                            response.setMessage("User Not Login");
                            outServer.writeObject(response);
                        }else {
                            List<GameResponse> gameResponseList=getGameById(request.getIdGame());
                            Response response=new Response();
                            if(gameResponseList.size()>0){
                                File srcFolder = new File("./src/main/java/Server/Resources");
                                File destFolder = new File("./src/main/java/Client/Downloads");
                                int  index=gameResponseList.get(0).getFile_path().lastIndexOf('\\');
                                String fileSrc=gameResponseList.get(0).getFile_path().substring(index+1);
                                copyFolder(srcFolder,destFolder,fileSrc);

                                int count=selectDownload(String.valueOf(userId),request.getIdGame());
                                if(count==0){
                                    saveToDownload(String.valueOf(userId),request.getIdGame(),count+1);
                                }else {
                                    updateDownload(String.valueOf(userId),request.getIdGame(),count+1);
                                }

                                response.setStatus("0");
                                response.setMessage("Download Game Success");
                            }else {
                                response.setStatus("-1");
                                response.setMessage("Data Not Found For Download");
                            }
                            outServer.writeObject(response);
                        }
                    }




                }catch(IOException i)
                {
                    System.out.println(i);
                }
            }


            System.out.println("Closing connection");

            // close connection
            socket.close();
            inServer.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public  void createAccount(String username,String password,String date){

        String sql = "INSERT INTO account(username,password,birth_of_date) VALUES (?,?,?)";

        // Open a connection
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            // Execute a query
            System.out.println("Inserting records into the table...");
            //preparedStatement.setInt(1, 2);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashPass(password));
            preparedStatement.setString(3, date);

            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public  String accountIsExist(String username){
        String query = "SELECT * FROM account WHERE username = ?";
        String result="";
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            PreparedStatement preparedStatement = conn.prepareStatement(query)){
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                result=rs.getString("username");
            }


        }catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public long loginUser(String username,String password){
        String query = "SELECT * FROM account WHERE username = ? and password = ?";
        long id=0;
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            PreparedStatement preparedStatement = conn.prepareStatement(query)){
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                id=rs.getLong("id");
            }


        }catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void setTokenAccount(long id,String token){
        String query="UPDATE account set token = ? WHERE id = ?";
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            PreparedStatement preparedStatement = conn.prepareStatement(query)){
            preparedStatement.setString( 1, token);
            preparedStatement.setLong( 2, id);
            preparedStatement.executeUpdate();

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public String hashPass(String input){

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] hash = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {

        int rowCountGame=countTableGame();
        if(rowCountGame==0){
            final File folder = new File("./src/main/java/Server/Resources");
            listFilesForFolder(folder);
        }

        ServerMain client = new ServerMain( 5000);

    }

    public static void listFilesForFolder(final File folder)throws IOException {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                //if(Files.getFileE)
                String extension = "";
                int i = fileEntry.toString().lastIndexOf('.');
                if (i > 0) {
                    extension = fileEntry.toString().substring(i+1);
                }
                if(extension.contains("txt")){
                    Path path = Paths.get(fileEntry.toURI());
                    byte[] bytes = Files.readAllBytes(path);
                    List<String> allLines = Files.readAllLines(path, StandardCharsets.UTF_8);
                    saveGame(allLines.get(0),allLines.get(1),allLines.get(2),allLines.get(3),Double.parseDouble(allLines.get(4)),Integer.parseInt(allLines.get(5))
                            ,Boolean.parseBoolean(allLines.get(6)),Integer.parseInt(allLines.get(7)),Integer.parseInt(allLines.get(8)),path.toString().replaceAll(".txt",".png"));
                    //System.out.println(fileEntry.getName());
                    //System.out.println(path.toString().replaceAll(".txt",".png"));
                }

            }
        }
    }

    public static void saveGame(String id,String title,String developer,String genre,double price,int release_year,boolean controller_support
            ,int reviews,int size,String path){
        String sql = "INSERT INTO game(id,title,developer,genre,price,release_year,controller_support,reviews,size,file_path) VALUES (?,?,?,?,?,?,?,?,?,?)";

        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            PreparedStatement preparedStatement = conn.prepareStatement(sql)){

            preparedStatement.setString(1, id);
            preparedStatement.setString(2, title);
            preparedStatement.setString(3, developer);
            preparedStatement.setString(4, genre);
            preparedStatement.setDouble(5, price);
            preparedStatement.setInt(6, release_year);
            preparedStatement.setBoolean(7, controller_support);
            preparedStatement.setInt(8, reviews);
            preparedStatement.setInt(9, size);
            preparedStatement.setString(10, path);

            preparedStatement.executeUpdate();

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int countTableGame(){
        int rowCount=0;
        String query = "SELECT count(*) FROM game ";
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
        ) {
            rs.next();
            rowCount= rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowCount;
    }

    public List<GameResponse> getGameList(){
        String query = "SELECT * FROM game ";
        List<GameResponse> gameResponseList=new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
        ) {

            while(rs.next()){
                GameResponse gameResponse=new GameResponse();
                gameResponse.setId(rs.getString("id"));
                gameResponse.setTitle(rs.getString("title"));
                gameResponse.setDeveloper(rs.getString("developer"));
                gameResponse.setGenre(rs.getString("genre"));
                gameResponse.setPrice(String.valueOf(rs.getDouble("price")));
                gameResponse.setRelease_year(String.valueOf(rs.getInt("release_year")));
                gameResponse.setController_support(String.valueOf(rs.getBoolean("controller_support")));
                gameResponse.setReviews(String.valueOf(rs.getInt("reviews")));
                gameResponse.setSize(String.valueOf(rs.getInt("size")));
                gameResponse.setSize(rs.getString("file_path"));
                gameResponseList.add(gameResponse);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gameResponseList;
    }
    public List<GameResponse> getGameById(String id){
        String query = "SELECT * FROM game WHERE id = ?";
        List<GameResponse> gameResponseList=new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            PreparedStatement preparedStatement = conn.prepareStatement(query)){
            preparedStatement.setString(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                GameResponse gameResponse=new GameResponse();
                gameResponse.setId(rs.getString("id"));
                gameResponse.setTitle(rs.getString("title"));
                gameResponse.setDeveloper(rs.getString("developer"));
                gameResponse.setGenre(rs.getString("genre"));
                gameResponse.setPrice(String.valueOf(rs.getDouble("price")));
                gameResponse.setRelease_year(String.valueOf(rs.getInt("release_year")));
                gameResponse.setController_support(String.valueOf(rs.getBoolean("controller_support")));
                gameResponse.setReviews(String.valueOf(rs.getInt("reviews")));
                gameResponse.setSize(String.valueOf(rs.getInt("size")));
                gameResponse.setFile_path(rs.getString("file_path"));
                gameResponseList.add(gameResponse);
            }


        }catch (SQLException e) {
            e.printStackTrace();
        }
        return gameResponseList;
    }
    public void copyFolder(File src, File dest,String fileName)
            throws IOException{

        if(src.isDirectory()){

            //if directory not exists, create it
            if(!dest.exists()){
                dest.mkdir();
                System.out.println("Directory copied from "
                        + src + "  to " + dest);
            }

            //list all the directory contents
            List<String> files = Arrays.stream(src.list()).filter(c->c.equals(fileName)).collect(Collectors.toList());

            for (String file : files) {
                //construct the src and dest file structure
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                //recursive copy
                copyFolder(srcFile,destFile,fileName);
            }

        }else{
            //if file, then copy it
            //Use bytes stream to support all file types
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];

            int length;
            //copy the file content in bytes
            while ((length = in.read(buffer)) > 0){
                out.write(buffer, 0, length);
            }

            in.close();
            out.close();
            System.out.println("File copied from " + src + " to " + dest);
        }
    }
    public void saveToDownload(String account_id,String game_id,int download_count){
        String sql = "INSERT INTO downloads(account_id,game_id,download_count) VALUES (?,?,?)";

        // Open a connection
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            // Execute a query
            System.out.println("Inserting records into the table...");
            //preparedStatement.setInt(1, 2);
            preparedStatement.setString(1, account_id);
            preparedStatement.setString(2, game_id);
            preparedStatement.setInt(3, download_count);

            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDownload(String account_id,String game_id,int download_count){
        String query="UPDATE downloads set download_count = ? WHERE account_id = ? and game_id = ?";
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            PreparedStatement preparedStatement = conn.prepareStatement(query)){
            preparedStatement.setInt( 1, download_count);
            preparedStatement.setString( 2, account_id);
            preparedStatement.setString( 3, game_id);
            preparedStatement.executeUpdate();

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int   selectDownload(String account_id,String game_id){
        String query = "SELECT * FROM downloads WHERE account_id = ? and  game_id = ?";
        int result=0;
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            PreparedStatement preparedStatement = conn.prepareStatement(query)){
            preparedStatement.setString(1, account_id);
            preparedStatement.setString(2, game_id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                result=rs.getInt("download_count");
            }


        }catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }




}
