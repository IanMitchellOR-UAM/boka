/**
 * 
 */
package mx.uam.azc.estelares.bocanika.data;

import java.io.Serializable;

/**
 * 
 */
public class AdminTipoProductoDTO implements Serializable {
	private int idTipoProducto;
    private String nombreTipoProducto;

    // Getters y Setters

    public int getIdTipoProducto() {
        return idTipoProducto;
    }

    public void setIdTipoProducto(int idTipoProducto) {
        this.idTipoProducto = idTipoProducto;
    }

    public String getNombreTipoProducto() {
        return nombreTipoProducto;
    }

    public void setNombreTipoProducto(String nombreTipoProducto) {
        this.nombreTipoProducto = nombreTipoProducto;
    }
}
