/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Carlos Espinoza
 */
@Entity
public class Recargas {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idReca;
    @Basic
    private double Valor;
    private double Saldo;
    private double Megas;
    
    @ManyToOne
    private Celular celular;

    public Recargas() {
    }

    public Recargas(int idReca, double Valor, double Saldo, double Megas, Celular celular) {
        this.idReca = idReca;
        this.Valor = Valor;
        this.Saldo = Saldo;
        this.Megas = Megas;
        this.celular = celular;
    }

    public int getIdReca() {
        return idReca;
    }

    public void setIdReca(int idReca) {
        this.idReca = idReca;
    }

    public double getValor() {
        return Valor;
    }

    public void setValor(double Valor) {
        this.Valor = Valor;
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

    public Celular getCelular() {
        return celular;
    }

    public void setCelular(Celular celular) {
        this.celular = celular;
    }
    
}
