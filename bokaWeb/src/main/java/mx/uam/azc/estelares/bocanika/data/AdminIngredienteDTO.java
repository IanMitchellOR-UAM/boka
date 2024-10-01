/**
 * 
 */
package mx.uam.azc.estelares.bocanika.data;

import java.io.Serializable;

/**
 * 
 */
public class AdminIngredienteDTO implements Serializable {
	private int idIngrediente;
    private String nombreIngrediente;

    // Getters y Setters

    public int getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(int idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public String getNombreIngrediente() {
        return nombreIngrediente;
    }

    public void setNombreIngrediente(String nombreIngrediente) {
        this.nombreIngrediente = nombreIngrediente;
    }
}
