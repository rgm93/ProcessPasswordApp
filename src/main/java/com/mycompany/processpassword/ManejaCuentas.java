/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.processpassword;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Ruben
 */
public class ManejaCuentas {
    private Session sesion;
    private Transaction transaccion;
    
    public ManejaCuentas() {}
    
    /* Método que inicializa la sesión y la transacción */
    public void iniciarOperacion() throws HibernateException {
        System.out.println("Iniciando sesión con Hibernate");
        sesion = HibernateUtil.getSessionFactory().openSession();
        transaccion = sesion.beginTransaction();
    }
    
    /* Método que finaliza la sesión y la transacción */
    public void finalizarOperacion() throws HibernateException {
        transaccion.commit();
        sesion.close();
        System.out.println("Cerrando la sesión");
    }
    
    /* Método que maneja una excepción */
    public void manejarExcepcion(HibernateException he) throws HibernateException {
        transaccion.rollback();
        System.out.println("Ocurrió un error en el acceso a datos - " + he.getMessage());
        System.exit(0);
    }
    
    /* Método que almacena un experto */
    public void almacenarCuenta (Cuentas cuenta) {
        try {
            iniciarOperacion();
            sesion.save(cuenta);
            System.out.println("Cuenta almacenada correctamente");
        } catch (HibernateException he) {
            manejarExcepcion(he);
            throw he;
        } finally {
            finalizarOperacion();
        }
    }
    
    /* Método que elimina un experto */
    public void borrarCuenta(Cuentas cuenta) {
        try {
            iniciarOperacion();
            sesion.delete(cuenta);
            System.out.println("Cuenta borrada correctamente");
        } catch (HibernateException he) {
            manejarExcepcion(he);
            throw he;
        } finally {
            finalizarOperacion();
        }
    }
    
    /* Método que muestra el nombre y las especialidades de cada uno de los expertos */
    public List<Cuentas> obtenerCuentas(String username) {
        List<Cuentas> lista;
        try {
            iniciarOperacion();
            String hq1 = "SELECT c FROM Cuentas c WHERE USUARIO='" + username + "'";

            Query query = sesion.createQuery(hq1);
            lista = query.list();

        } catch (HibernateException he) {
            manejarExcepcion(he);
            throw he;
        } finally {
            finalizarOperacion();
        }
        
        return lista;
    }
}
