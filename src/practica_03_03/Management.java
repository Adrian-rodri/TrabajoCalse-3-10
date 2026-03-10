package practica_03_03;

/**
 *
 * @author adria
 */
import java.io.*;
import java.util.Calendar;
import java.util.Date;
public class Management {
    //Codigos- 1 int codigo, pesa 4 bytes siempre
    /*Empleados- 
    1 int codigo
    2 string nombre
    3 double salario
    4 long fechaContratacion
    5 long fechaDespido
    */
    private RandomAccessFile codigos, empleados;
    Management() throws IOException {
    File file= new File("Company");
    file.mkdir();

    
    codigos= new RandomAccessFile("Company//codigos.emp","rw");
    empleados= new RandomAccessFile("Company//emplaods.emp","rw");
    
    initCodigo();
    }
    
    private void initCodigo() throws IOException{
        if(codigos.length()==0)
        codigos.writeInt(1);
    }
    private int getCodigo()throws IOException{
        codigos.seek(0);
        int codigo= codigos.readInt();
        codigos.seek(0);
        codigos.writeInt(codigo+1);
        return codigo;
    }
    public void addEmpleado(String name, double salary) throws IOException{
        empleados.seek(empleados.length());
        int codigo= getCodigo();
        empleados.writeInt(codigo);
        empleados.writeUTF(name);
        empleados.writeDouble(salary);
        empleados.writeLong(System.currentTimeMillis());
        empleados.writeLong(0);
        crearFolderEmpleado(codigo);
    }
    public void listaEmpleado()throws IOException{
        empleados.seek(0);
        while(empleados.getFilePointer()<empleados.length()){
            System.out.println("***Empleado***");
            System.out.println("Codigo: "+empleados.readInt());
            System.out.println("Nombre: "+ empleados.readUTF().toString());
            System.out.println("Salario: "+ empleados.readDouble());
            System.out.println("Fecha de Contratacion: "+ new Date(empleados.readLong()).toString());
            empleados.readLong();
            System.out.println("Aun no ha sido Despedido");
            
        
        
        }
    }
    
    private String empleadoDir(int code){
        return "Company/Empleado - "+ code;
    }
    private RandomAccessFile ventasFileFor(int code)throws IOException{
        String dirPadre=empleadoDir(code);
        int year= Calendar.getInstance().get(Calendar.YEAR);
        String directory= dirPadre+"/Ventas - "+year+".emp";
        return new RandomAccessFile(directory,"rw");
    }
    /*
    Formate ventas.emp
    double saldo
    boolea  estadoPago
    */
    private void createYearSaleFileFor(int code)throws IOException{
        RandomAccessFile rventas= ventasFileFor(code);
        if(rventas.length()==0){
            for(int i=0; i<12;i++){
                rventas.writeDouble(0);
                rventas.writeBoolean(false);
            }
        }
    }
    private void crearFolderEmpleado(int code)throws IOException{
        File dir= new File(empleadoDir(code));
        dir.mkdir();
        createYearSaleFileFor(code);
        
    }
    /*
    Hacer isEmployeeActive
    fireEmployee
    AddSaleTO
    */
    
}

