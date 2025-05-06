/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Carlos Espinoza
 */
@Entity
public class Celular implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCel;
    @Basic
    private String Numero;
    private String Estado;
    private double Saldo;
    private double Megas;
    
    @ManyToOne
    private Cliente cliente;

    @OneToMany(mappedBy = "celular")
    private List<Recargas> recargas ;

    public Celular() {
    }

    public Celular(int idCel, String Numero, String Estado, double Saldo, double Megas, Cliente cliente, List<Recargas> recargas) {
        this.idCel = idCel;
        this.Numero = Numero;
        this.Estado = Estado;
        this.Saldo = Saldo;
        this.Megas = Megas;
        this.cliente = cliente;
        this.recargas = recargas;
    }

    public int getIdCel() {
        return idCel;
    }

    public void setIdCel(int idCel) {
        this.idCel = idCel;
    }

    public String getNumero(String cedula) {
        return Numero;
    }

    public void setNumero(String Numero) {
        this.Numero = Numero;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String Estado) {
        this.Estado = Estado;
    }

    public double getSaldo() {
        return Saldo;
    }

    public void setSaldo(double Saldo) {
        this.Saldo = Saldo;
    }

    public double getMegas() {
        return Megas;
    }

    public void setMegas(double Megas) {
        this.Megas = Megas;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Recargas> getRecargas() {
        return recargas;
    }

    public void setRecargas(List<Recargas> recargas) {
        this.recargas = recargas;
    }


    
    
    
}
