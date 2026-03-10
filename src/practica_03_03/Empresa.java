package practica_03_03;
import java.util.Scanner;
import java.io.IOException;
public class Empresa {
    static Management control=null;
    public static void main(String[] args) {
        try {
            control=new Management();
        } catch(IOException ex) {
            System.err.println("Error: "+ex.getMessage());
            return;
        }
        Scanner ent=new Scanner(System.in);
        int opcion=0;
        do {
            System.out.println("***Que desea hacer?***\n"
                + "1. Agregar Empleado\n"
                + "2. Listar Empleados\n"
                + "3. Agregar Venta a Empleado\n"
                + "4. Pagar Empleado\n"
                + "5. Despedir Empleado\n"
                + "6. Ver Reporte de Empleado\n"
                + "7. Salir\n");
            try {
                System.out.print("Elija una Opcion: ");
                opcion =ent.nextInt();
                switch(opcion) {
                    case 1:
                        ent.nextLine();
                        System.out.print("Nombre: ");
                        String name=ent.nextLine();
                        System.out.print("Salario: ");
                        double salary=ent.nextDouble();
                        control.addEmpleado(name, salary);
                        System.out.println("Empleado '" + name + "' creado con exito");
                        break;
                    case 2:
                        control.listaEmpleado();
                        break;
                    case 3:
                        System.out.print("Codigo del empleado: ");
                        int codVenta=ent.nextInt();
                        System.out.print("Monto de venta: ");
                        double venta=ent.nextDouble();
                        control.AddVenta(codVenta, venta);
                        break;
                    case 4:
                        System.out.print("Codigo del empleado: ");
                        control.payEmployee(ent.nextInt());
                        break;
                    case 5:
                        System.out.print("Codigo del empleado: ");
                        control.fireEmployee(ent.nextInt());
                        break;
                    case 6:
                        System.out.print("Codigo del empleado: ");
                        control.printEmployee(ent.nextInt());
                        break;
                    case 7:
                        System.out.println("Hasta luego!");
                        break;
                    default:
                        System.out.println("Opcion invalida");
                }
            } catch(IOException e) {
                System.err.println("Error: " +e.getMessage());
            }
        } while(opcion!=7);
    }
}