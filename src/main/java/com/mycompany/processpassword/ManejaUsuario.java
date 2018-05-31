/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.processpassword;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Ruben
 */
public class ManejaUsuario {
    private Session sesion;
    private Transaction transaccion;
    
    public ManejaUsuario() {}
    
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
    public void almacenarUsuario (Usuario usuario) {
        try {
            iniciarOperacion();
            sesion.save(usuario);
            System.out.println("Cuenta almacenada correctamente");
        } catch (HibernateException he) {
            manejarExcepcion(he);
            throw he;
        } finally {
            finalizarOperacion();
        }
    }
    
    /* Método que elimina un experto */
    public void borrarUsuario(Usuario usuario) {
        try {
            iniciarOperacion();
            sesion.delete(usuario);
            System.out.println("Cuenta borrada correctamente");
        } catch (HibernateException he) {
            manejarExcepcion(he);
            throw he;
        } finally {
            finalizarOperacion();
        }
    }
    
    /* Método que modifica un experto en la base de datos */
    public void modificarUsuario(Usuario usuario, String username, String password) {
        try {
            iniciarOperacion();
            Usuario u = (Usuario) sesion.get(Usuario.class, usuario.getUsername());
            u.setUsername(username);
            u.setPassword(password);
            sesion.update(u);
        } catch (HibernateException he) {
            manejarExcepcion(he);
            throw he;
        } finally {
            finalizarOperacion();
        }
    }
    
    /* Método que muestra el nombre y las especialidades de cada uno de los expertos */
    public Usuario consultarUsuario(String username, String password) throws NoSuchAlgorithmException {
        Usuario u = null;
        try {
            iniciarOperacion();
            
            String pass = new Cifrado().Encriptar(password);
            String hq1 = "SELECT u FROM Usuario u WHERE USERNAME='" + username + "' AND PASSWORD='" + pass + "'";

            Query query = sesion.createQuery(hq1);
            List<Usuario> lista = query.list();
            if(!lista.isEmpty()) u = new Usuario(lista.get(0).getUsername(), lista.get(0).getPassword());
        } catch (HibernateException he) {
            manejarExcepcion(he);
            throw he;
        } finally {
            finalizarOperacion();
        }
        return u;
    }
}
