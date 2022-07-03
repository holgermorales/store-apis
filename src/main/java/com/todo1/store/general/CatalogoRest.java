package com.todo1.store.general;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author holger.morales
 */
@Path("catalogo/")
@Produces(MediaType.APPLICATION_JSON)
public interface CatalogoRest {

    /**
     * Obtiene por grupo.
     *
     * @author holger.morales
     * @history Jul 2, 2022 - 10:33:02 AM holger.morales
     *          Versión inicial.
     * @param param
     * @return Lista de catálogo.
     */
    @POST
    @Path("porGrupo")
    @Consumes(MediaType.APPLICATION_JSON)
    Response obtenerPorGrupo(String param);

}
