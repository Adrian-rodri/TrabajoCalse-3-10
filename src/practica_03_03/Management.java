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
    public void listaEmpleado() throws IOException {
    empleados.seek(0);
    while(empleados.getFilePointer() < empleados.length()) {
        int code= empleados.readInt();
        String nombre= empleados.readUTF();
        double salario= empleados.readDouble();
        long fechaContra= empleados.readLong();
        long fechaDespe= empleados.readLong();

        System.out.println("***Empleado***");
        System.out.println("Codigo: "+code);
        System.out.println("Nombre: "+nombre);
        System.out.println("Salario: "+salario);
        System.out.println("Fecha de Contratacion: " + new Date(fechaContra));
        if(fechaDespe==0) {
            System.out.println("Aun no ha sido Despedido");
        } else {
            System.out.println("Despedido el: "+new Date(fechaDespe));
        }
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
     private boolean isEmployeeActive(int code) throws IOException{
        empleados.seek(0);
        while(empleados.getFilePointer()< empleados.length()){
            int cod=empleados.readInt();
            long pos=empleados.getFilePointer();
            empleados.readUTF();
            empleados.skipBytes(16);
            long fechaDespido = empleados.readLong();
        if (fechaDespido == 0 && cod == code) {
                empleados.seek(pos);
                return true;
            }
        }
        return false;
    }
    
    public boolean fireEmployee(int code) throws IOException{
        if (isEmployeeActive(code)){
            String name= empleados.readUTF();
            empleados.skipBytes(16);
            empleados.writeLong(new Date().getTime());
            System.out.println("Despidiendo a: "+name);
            return true;
        }
        return false;
    }
    public void AddVenta(int code, double ven) throws IOException{
        if(isEmployeeActive(code)){
            RandomAccessFile sales= ventasFileFor(code);
            int pos =(Calendar.getInstance().get(Calendar.MONTH))*9;
            sales.seek(pos);
            double monto= sales.readDouble();
            sales.seek(pos);
            sales.writeDouble(ven+monto);
            
        }}
    private RandomAccessFile billsFileFor(int code) throws IOException{
        String dirPadre=empleadoDir(code);
        String directory=dirPadre+"/recibos.emp";
        return new RandomAccessFile(directory, "rw");
        }
    public boolean isEmployeePayed(int code)throws IOException{
        RandomAccessFile ventas=ventasFileFor(code); 
        int mes= Calendar.getInstance().get(Calendar.MONTH);
        int pos= mes*9;
        ventas.seek(pos);
        ventas.skipBytes(8);
        boolean pagado= ventas.readBoolean();
        if(pagado)
            return true;
        return false;
    }
    /*
    Formato bills file for
    Fecha de pago -8
    Sueldo calculado -8
    Deducción  -8
    Año-4
    Mes-4
    */
    public void payEmployee(int code) throws IOException{
        if(!isEmployeeActive(code)){
            System.out.println("El empleado no existe o no esta activo");
            return;
        }else if(isEmployeePayed(code)){
            System.out.println("Ya se le pago al empleado");
            return;
        }
        String nombre= empleados.readUTF();
        double salario= empleados.readDouble();
        
        int year= Calendar.getInstance().get(Calendar.YEAR);
        int mes= Calendar.getInstance().get(Calendar.MONTH);
        
        int pos=mes*9;
        RandomAccessFile ventas= ventasFileFor(code);
        ventas.seek(pos);
        double totalVentas= ventas.readDouble();
        
        double sueldo= salario+(totalVentas*0.10);
        double deduccion= sueldo*0.035;
        double total= sueldo-deduccion;
        RandomAccessFile recibos= billsFileFor(code);
        recibos.seek(recibos.length());
        recibos.writeLong(System.currentTimeMillis());
        recibos.writeDouble(sueldo);
        recibos.writeDouble(deduccion);
        recibos.writeInt(year);
        recibos.writeInt(mes);
        
        ventas.seek(pos+8);
        ventas.writeBoolean(true);
        System.out.println("Empleado " +nombre+ " se le pago Lps. "+total);
    }
    public void printEmployee(int code)throws IOException{
        if(!isEmployeeActive(code)){
            System.out.println("Empleado no encontrado");
            return;
        }
        String nombre= empleados.readUTF();
        double salario= empleados.readDouble();
        long fechaContratacion= empleados.readLong();
        System.out.println("Codigo: "+ code
                            +"\nNombre: "+nombre
                            +"\nSalario: "+ salario
                            +"Fecha de Contratacion: "+new Date(fechaContratacion));
        RandomAccessFile ventas= ventasFileFor(code);
        ventas.seek(0);
        double totalVentas=0;
        for(int i=0; i<12;i++){
            double monto= ventas.readDouble();
            ventas.readBoolean();
            System.out.println("Mes "+ (i+1)+": "+ monto);
            totalVentas +=monto;
        }
        System.out.println("Total de ventas del año: "+totalVentas);
        RandomAccessFile recibos=billsFileFor(code);
        long totalPagos=recibos.length() / 32;
        System.out.println("Total de pagos realizados: " + totalPagos);
    
    }
        
    
}

