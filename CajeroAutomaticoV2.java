import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
public class CajeroAutomaticoV2 {

    public static void main(String[] args) {
        Banco banco = new Banco();
        banco.agregarCuenta(new Cuenta("1001", "Jose Pérez", 3000));
        banco.agregarCuenta(new Cuenta("1002", "María De Todos los Angeles", 10000));
        banco.agregarCuenta(new Cuenta("1003", "Pedro Luis Domínguez", 777777777));
        banco.agregarCuenta(new Cuenta("1004", "Ana Frank", 1945));

        CajeroVista vista = new CajeroVista();
        CajeroControlador controlador = new CajeroControlador(banco, vista);
        controlador.iniciar();
    }
}
class Cuenta {

    private String numeroCuenta;
    private String titular;
    private long  saldo;

    public Cuenta(String numeroCuenta, String titular, long  saldo) {
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.saldo = saldo;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public String getTitular() {
        return titular;
    }

    public double getSaldo() {
        return saldo;
    }

    public boolean depositar(double cantidad) {
        if (cantidad > 0) {
            saldo += cantidad;
            return true;
        }
        return false;
    }

    public boolean retirar(double cantidad) {
        if (cantidad > 0 && cantidad <= saldo) {
            saldo -= cantidad;
            return true;
        }
        return false;
    }
}

class CajeroVista {

    private Scanner scanner;

    public CajeroVista() {
        // Usa Locale.US para asegurar el uso del punto '.' en números decimales
        this.scanner = new Scanner(System.in).useLocale(Locale.US);
    }

    public String pedirNumeroCuenta() {
        System.out.println("===== CAJERO AUTOMÁTICO =====");
        System.out.print("Ingrese su número de cuenta: ");
        return scanner.nextLine();
    }

    public void mostrarBienvenida(String titular, String numeroCuenta) {
        System.out.println("\nBienvenido: " + titular);
        System.out.println("Número de cuenta: " + numeroCuenta);
    }

    public int mostrarMenu() {
        System.out.println("\n===== MENÚ PRINCIPAL =====");
        System.out.println("1. Consultar saldo");
        System.out.println("2. Depositar dinero");
        System.out.println("3. Retirar dinero");
        System.out.println("4. Salir");
        System.out.print("Seleccione una opción: ");

        while (!scanner.hasNextInt()) {
            System.out.print("Error: opción no válida. Intente nuevamente: ");
            scanner.next();
        }
        int opcion = scanner.nextInt();
        scanner.nextLine();
        return opcion;
    }

    public double pedirCantidad(String operacion) {
        System.out.print("Cantidad a " + operacion + ": $");
        while (!scanner.hasNextDouble()) {
            System.out.print("Error: ingrese un valor numérico válido: $");
            scanner.next();
        }
        double cantidad = scanner.nextDouble();
        scanner.nextLine();
        return cantidad;
    }

    public void mostrarSaldo(double saldo) {
        System.out.printf("Saldo actual: $%.2f%n", saldo);
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
}

class CajeroControlador {

    private Banco banco;
    private CajeroVista vista;
    private Cuenta cuentaActual;

    public CajeroControlador(Banco banco, CajeroVista vista) {
        this.banco = banco;
        this.vista = vista;
    }

    public void iniciar() {
        String numeroCuenta = vista.pedirNumeroCuenta();
        cuentaActual = banco.buscarCuenta(numeroCuenta);

        if (cuentaActual == null) {
            vista.mostrarMensaje("Error: la cuenta no existe.");
            return;
        }

        vista.mostrarBienvenida(cuentaActual.getTitular(), cuentaActual.getNumeroCuenta());

        int opcion;
        do {
            opcion = vista.mostrarMenu();
            procesarOpcion(opcion);
        } while (opcion != 4);
    }

    private void procesarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                vista.mostrarSaldo(cuentaActual.getSaldo());
                break;

            case 2:
                double montoDeposito = vista.pedirCantidad("depositar");
                if (cuentaActual.depositar(montoDeposito)) {
                    vista.mostrarMensaje("Depósito exitoso.");
                } else {
                    vista.mostrarMensaje("Error: el depósito debe ser mayor a $0.");
                }
                vista.mostrarSaldo(cuentaActual.getSaldo());
                break;

            case 3:
                double montoRetiro = vista.pedirCantidad("retirar");
                if (montoRetiro <= 0) {
                    vista.mostrarMensaje("Error: la cantidad debe ser mayor a $0.");
                } else if (montoRetiro > cuentaActual.getSaldo()) {
                    vista.mostrarMensaje("Error: saldo insuficiente.");
                } else if (cuentaActual.retirar(montoRetiro)) {
                    vista.mostrarMensaje("Retiro exitoso.");
                }
                vista.mostrarSaldo(cuentaActual.getSaldo());
                break;

            case 4:
                vista.mostrarMensaje("Gracias por utilizar el cajero automático.");
                break;

            default:
                vista.mostrarMensaje("Error: opción no válida.");
        }
    }
}

class Banco {

    private List<Cuenta> cuentas;

    public Banco() {
        this.cuentas = new ArrayList<>();
    }

    public void agregarCuenta(Cuenta cuenta) {
        cuentas.add(cuenta);
    }

    public Cuenta buscarCuenta(String numeroCuenta) {
        for (Cuenta cuenta : cuentas) {
            if (cuenta.getNumeroCuenta().equals(numeroCuenta)) {
                return cuenta;
            }
        }
        return null;
    }
}

