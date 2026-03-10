package practica_03_03;

/**
 *
 * @author adria
 */
import java.util.Scanner;
import java.io.IOException;
public class Empresa {
    static Management control=null;
    public static void main(String[] args) {
        try{
            control= new Management();
    }catch(IOException ex){}
    Scanner ent= new Scanner(System.in);
    int opcion=0;
    do{
    
        System.out.println("***Que desea hacer?***\n"
                            +"1. Agregar Empleado\n"
                            +"2. Listar Empleados No Despedido\n"
                            +"3. Agregar Venta a Empleado\n"
                            +"4. Pagar Empleado\n"
                            +"5. Despedir Empleado\n"
                            +"6. Salir\n");
        try{
        System.out.print("Elija una Opcion: ");
        
        opcion=ent.nextInt();
        
        
        switch(opcion){
            case 1:
                System.out.print("Ingrese el nombre del Empleado: ");
                ent.nextLine();
                String name=ent.nextLine();
                System.out.print("Ingrese el salario para el empleado: ");
                double salary=ent.nextDouble();
                control.addEmpleado(name, salary);
                System.out.println("Empleado '"+name+"' Creado con exito");
                break;
            case 2: 
                control.listaEmpleado();
                break;
        }
        }catch(IOException e){
            System.err.println("Error: "+ e.getMessage());
            
        }
    }while(opcion<6);
    }
    
}
