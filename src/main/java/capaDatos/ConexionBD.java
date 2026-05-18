package capaDatos;

import java.sql.Connection;
import java.sql.DriverManager;


public class ConexionBD {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = System.getenv("DB_URL") != null
        ? System.getenv("DB_URL")
        : "jdbc:mysql://localhost:3306/panaderiafausta";

    private static final String USUARIO =System.getenv("DB_USER") != null
        ? System.getenv("DB_USER")
        : "root";

    private static final String CLAVE =System.getenv("DB_PASSWORD") != null
        ? System.getenv("DB_PASSWORD")
        : "admin1";

    private Connection cnn;

    public ConexionBD() {
        this.cnn = null;
    }

    public Connection Conectar() {

        try {
            Class.forName(DRIVER);
            cnn = DriverManager.getConnection(
                URL,
                USUARIO,
                CLAVE
            );
        } catch(Exception e) {
            e.printStackTrace();
        }

        return cnn;
    }

    public void Desconectar() {

        try {
            if(cnn != null) {
                cnn.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    } 
}
