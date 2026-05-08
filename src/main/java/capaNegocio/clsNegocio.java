package capaNegocio;

import capaEntidad.Clientes;
import capaDatos.clsDatos;
import java.util.ArrayList;


public class clsNegocio {
    private clsDatos objBD;

    public clsNegocio() {
        objBD = new clsDatos();
    }
    
    //metodos a invocar
    public ArrayList<Clientes> Listado() {
        return objBD.Listado();
    }
    
    public void Insertar(Clientes ObjC) {
        objBD.Insertar(ObjC);
    }
}