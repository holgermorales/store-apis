package com.todo1.store.general.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.core.Response;

import com.google.gson.JsonObject;
import com.todo1.store.CatalogoDTO;
import com.todo1.store.ResponseDTO;
import com.todo1.store.enumerados.EstadoRegistro;
import com.todo1.store.enumerados.TipoMensaje;
import com.todo1.store.excepciones.GenericException;
import com.todo1.store.general.CatalogoRest;
import com.todo1.store.general.servicio.ServicioCatalogo;
import com.todo1.store.util.JsonUtil;

import org.apache.log4j.Logger;

/**
 * @author holger.morales
 */
public class CatalogoRestImpl implements CatalogoRest {

    private final Logger log = Logger.getLogger(this.getClass());
    private final JsonUtil jsonUtil = new JsonUtil();

    // @EJB(lookup = "java:global/store-ejb/ServicioCatalogoImpl!com.todo1.store.general.servicio.ServicioCatalogo")
    @EJB(lookup = "java:global/store-ejb/ServicioCatalogoImpl!com.todo1.store.general.servicio.ServicioCatalogo")
    private ServicioCatalogo servicioCatalogo;

    /*
     * (non-Javadoc)
     * @see com.todo1.general.CatalogoRest#obtenerPorGrupo(java.lang.String)
     */
    @Override
    public Response obtenerPorGrupo(String param) {
        final ResponseDTO<List<CatalogoDTO>> respuesta = new ResponseDTO<>();
        try {
            final JsonObject data = this.jsonUtil.getJson(param);
            final String grupo = data.get("grupo").getAsString();
            final List<CatalogoDTO> catalogos = this.servicioCatalogo.obtenerPorGrupoEstado(grupo, EstadoRegistro.ACTIVO);

            if (catalogos.isEmpty()) {
                respuesta.setTipoMensaje(TipoMensaje.INFORMACION.toString());
                respuesta
                        .setMensaje("¡No existe catálogos registrado. Por favor contáctese con el Administrado del sistema para la creación. [{grupo: !" + grupo + " }]");
            } else {
                respuesta.setTipoMensaje(TipoMensaje.EXITO.toString());
                respuesta.setTotal(catalogos.size());
            }
            respuesta.setDato(catalogos);
            return Response.status(Response.Status.OK).entity(respuesta).build();
        } catch (final GenericException e) {
            String mensaje = e.getMessage();
            if ((null != e.getInfo()) && !e.getInfo().isEmpty()) {
                mensaje = e.getMessage() + " Datos: [ " + e.getInfo() + " ]";
            }
            respuesta.setMensaje(mensaje);
            respuesta.setTipoMensaje(TipoMensaje.ERROR.toString());
            if (null != e.getCause()) {
                this.log.error(mensaje, e);
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
        } catch (final Exception e) {
            respuesta.setMensaje("Los datos de entrada no son válidos");
            respuesta.setTipoMensaje(TipoMensaje.ERROR.toString());
            if (null != e.getCause()) {
                this.log.error(e.getMessage(), e);
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
        }
    }

}
