import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.HashSet;
import java.util.Properties;

/**
 * Created by saian on 19.08.16.
 */
public class DbDAO {

    private HashSet<Master> listMasters = new HashSet<Master>();
    //public static final Logger LOG  =Logger.getLogger(DepartmentDAO.class);
    private String url;
    private String userId;
    private String passWord;
    private Connection conn = null;
    private Statement statement = null;

    static
        {
            try {
                Class.forName("org.postgresql.Driver");

            } catch (Exception e) {
                e.printStackTrace();
                // LOG.error("problem with connect DB");
                System.err.println("операция не произведена.проблема с базой данных");
            }
        }

    private void readDBconfig(){

        InputStream fis;
        Properties property = new Properties();

        try {

            //fis = new FileInputStream("src/resurces/log4j.properties");
            fis = getClass().getClassLoader().getResourceAsStream("log4j.properties");
            //getClass().getResource("/images/logo.png")
            property.load(fis);

            this.url = property.getProperty("db.host");
            this.userId = property.getProperty("db.login");
            this.passWord = property.getProperty("db.password");

        } catch (IOException e) {
            e.printStackTrace();
           // LOG.error("problem with connect DB");
            System.err.println("ОШИБКА: Файл свойств отсуствует!");

        }


    }

    private void  createConnect(){
        try
        {
            conn = DriverManager.getConnection(this.url, this.userId, this.passWord);
           // LOG.info("connect to DB");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
           // LOG.error("problem with connect DB");
        }
    }
    public DbDAO()
    {
        readDBconfig();
        createConnect();
    }

    public void finalize()
    {
        try
        {
            conn.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
          //  LOG.error("programm can not close connect  DB");
        }
    }
        public HashSet<Master> getListMasters () {
            String strSQL="SELECT * from master";
                if (conn==null)
                {
                   // LOG.error("Could not connect to database");
                    System.out.println("\"Could not connect to database\"");
                }
            Statement stmt = null;
            try {
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(strSQL);
                while(rs.next())
                {
                    Master master = new Master(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5));
                    listMasters.add(master);
                }
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
             finally {
                finalize();
                    }
        return listMasters;
    }
    }
