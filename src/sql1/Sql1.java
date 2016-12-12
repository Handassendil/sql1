/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;


/**
 *
 * @author pacofelix
 */
public class Sql1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/AAD","root","admin");
            Statement sentencia = conexion.createStatement();
            //Almacena los resultados
            //ResultSet resultado = sentencia.executeQuery("select * from EMPLEADOS");
            
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            
            int opcion;
            do{
                System.out.println("Elija una opcion");
                System.out.println("1- Ver los empeleados de un departamento(* para verlos todos");
                System.out.println("2- Ver los datos de los departamentos");
                System.out.println("3- Insertar un empleado");
                System.out.println("4- Insertar un departamento");
                System.out.println("5- Editar un empleado");
                System.out.println("6- Editar un departamento");
                System.out.println("7- Eliminar un empleado");
                System.out.println("8- Eliminar un departamento");
                System.out.println("0- Para salir");
                System.out.println("¿Qué desea hacer ahora?");
                opcion = Integer.parseInt(br.readLine());
                switch(opcion){
                    case 1:
                        listarDepartamentos(sentencia,2);
                        System.out.println("Indique el nombre del departamento del trabajador");
                        String iddep = br.readLine();
                        try {
                            listarEmpleados(sentencia, devolverDepartamento(sentencia, iddep));
                        } catch (Exception e) {
                            System.out.println("No se ha podido acceder al listado solicitado");
                        }
                        break;
                    case 2:
                        listarDepartamentos(sentencia,3);
                        break;
                    //Insertar empleado
                    case 3:
                        System.out.println("Inserte el apellido del empleado");
                        String apel = br.readLine();
                        System.out.println("Inserte el oficio");
                        String ofic = br.readLine();
                        System.out.println("Inserte la fecha de alta(aaaa/mm/dd)");
                        String fech = br.readLine();
                        System.out.println("Inserte el salario");
                        String sala = br.readLine();
                        System.out.println("Y por ultimo inserte uno de los siguientes departamentos:");
                        listarDepartamentos(sentencia,2);
                        String depa = br.readLine();
                        int id = devolverDepartamento(sentencia, depa);
                        String linea = apel+"','"+ofic+"','"+fech+"','"+sala+"','"+id;
                        try{
                            sentencia.executeUpdate("INSERT INTO EMPLEADOS(apellido,oficio,fecha_alta,salario,dept_no) VALUES  ('"+linea+"')");
                        }catch(Exception e){
                            System.out.println("No se ha podido insertar el empleado");
                        }
                        break;
                    //Insertar departamento
                    case 4:
                        listarDepartamentos(sentencia,3);
                        System.out.println("Inserte el nombre del departamento");
                        String nomb = br.readLine();
                        System.out.println("Inserte la localizacion");
                        String loca = br.readLine();
                        String linea2 = nomb+"','"+loca;
                        try{
                            sentencia.executeUpdate("INSERT INTO DEPARTAMENTOS(dnombre,loc) VALUES  ('"+linea2+"')");
                        }catch(Exception e){
                            System.out.println("No se ha podido insertar el departamento");
                        }
                        break;
                    //Editar empleado    
                    case 5:
                        listarEmpleados(sentencia, 0);
                        System.out.println("inserte el apellido del empleado a modificar");
                        String modi = br.readLine();
                        System.out.println("Inserte el nuevo apellido del empleado");
                        String eapel = br.readLine();
                        System.out.println("Inserte el nuevo oficio");
                        String eofic = br.readLine();
                        System.out.println("Inserte la nueva fecha de alta(aaaa/mm/dd)");
                        String efech = br.readLine();
                        System.out.println("Inserte el nuevo salario");
                        String esala = br.readLine();
                        boolean b = true;
                        int eid=0;
                        while(b){
                            System.out.println("Y por ultimo inserte uno de los siguientes departamentos:");
                            listarDepartamentos(sentencia,2);
                            String edepa = br.readLine();

                            try{
                                eid=devolverDepartamento(sentencia, edepa); 
                                b=false;
                            }catch(Exception e){
                                System.out.println("No se ha reconocido el departamento");
                            }
                        }    
                        try {
                            String comando = "UPDATE EMPLEADOS SET apellido = '"+eapel+"', oficio='"+eofic+"', fecha_alta ='"+efech+"', salario='"+esala+"', dept_no='"+eid+"' WHERE apellido ='"+modi+"'";
                            //System.out.println(comando);
                            sentencia.executeUpdate(comando);
                        } catch (Exception e) {
                            System.out.println("No ha insertao ná");
                        }
                        break;
                    //Editar departamento    
                    case 6:
                        listarDepartamentos(sentencia,3);
                        System.out.println("¿Qué departamento desea cambiar?");
                        String modid = br.readLine();
                        ResultSet caso6 = sentencia.executeQuery("select * from DEPARTAMENTOS where dnombre = '"+modid+"'");
                        int mdepa = devolverDepartamento(sentencia, modid);
                        System.out.println("inserte nuevo nombre");
                        String nombd = br.readLine();
                        System.out.println("Inserte la nueva localización");
                        String locad = br.readLine();
                        try {
                            String actud = "UPDATE DEPARTAMENTOS SET dnombre='"+nombd+"', loc ='"+locad+"' where dept_no="+mdepa;
                            sentencia.executeUpdate(actud);
                        } catch (Exception e) {
                            System.out.println("No se ha actualizado nada");
                        }

                        break;
                    //Eliminar empleado
                    case 7:
                        System.out.println("¿Cual es el apellido del empleado que desea eliminar?");
                        String dapel= br.readLine();
                        try {
                            String comando = "DELETE FROM EMPLEADOS WHERE apellido ='"+dapel+"'";
                            sentencia.executeUpdate(comando);
                        } catch (Exception e) {
                            System.out.println("no se ha borrado nada");
                        }
                        break;
                    //Eliminar departamento    
                    case 8:
                        listarDepartamentos(sentencia,3);
                        System.out.println("¿Cual es el nombre del departamento que desea eliminar?");
                        String dnodp= br.readLine();
                        try {
                            borrarDepartamento(sentencia,dnodp);
                           
                        } catch (Exception e) {
                            System.out.println("no se ha borrado nada");
                        }
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("No es una opcion valida");
                        break;
                }
            }while(opcion!= 0);
            //resultado.close();
            sentencia.close();
            conexion.close();
        } catch (Exception e) {
        }
        
       
    }
    public static void listarDepartamentos(Statement sentencia, int num) throws SQLException{
        ResultSet listarD = sentencia.executeQuery("select * from DEPARTAMENTOS");
        if(num == 3){
            while(listarD.next()){
                String iddep = listarD.getString(1);
                String nombre = listarD.getString(2);
                String local = listarD.getString(3);
                System.out.println("ID: "+ iddep+" NOMBRE: "+nombre+ " Localizacion: "+ local);
            }
        }else{
            while(listarD.next()){
                String iddep = listarD.getString(1);
                String nombre = listarD.getString(2);
                String local = listarD.getString(3);
                System.out.println("ID: "+ iddep+" NOMBRE: "+nombre);
            }
        }
        listarD.close();              
    }
    public static void listarEmpleados(Statement sentencia, int DEPA) throws SQLException{
        ResultSet listarE;
        if(DEPA!=0){
            listarE = sentencia.executeQuery("select * from EMPLEADOS where dept_no="+DEPA);
        }else{
            listarE = sentencia.executeQuery("select * from EMPLEADOS");
        }
        while(listarE.next()){
                String iddep = listarE.getString(1);
                String nombre = listarE.getString(2);
                String local = listarE.getString(3);
                System.out.println("ID: "+ iddep+" NOMBRE: "+nombre);
            }
        
        listarE.close();
    }
    public static int devolverDepartamento(Statement sentencia, String DEPA) throws SQLException{
        ResultSet caso3 = sentencia.executeQuery("select * from DEPARTAMENTOS where dnombre = UPPER('"+DEPA+"')");
            String id="";
            while(caso3.next()){
            id = caso3.getString("dept_no");
            }
            int idint = Integer.parseInt(id);
            caso3.close();
        return idint;    
        
    }

    private static void borrarDepartamento(Statement sentencia, String dnodp) throws SQLException {
        String comando = "DELETE FROM DEPARTAMENTOS WHERE dnombre ='"+dnodp+"'";
        sentencia.executeUpdate(comando);
    }
}