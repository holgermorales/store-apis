/**
 * 
 */
package com.todo1.store.kardex;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author hogler.morales
 */
@Path("producto/")
@Produces(MediaType.APPLICATION_JSON)
public interface ProductoRest {

    /**
     * Obtiene todos los productos.
     *
     * @author holger.morales
     * @history Jul 2, 2022 - 10:33:02 AM holger.morales
     *          Versión inicial.
     * @param param
     * @return Lista de catálogo.
     */
    @POST
    @Path("todos")
    @Consumes(MediaType.APPLICATION_JSON)
    Response obtenerPorGrupo(String param);

}
