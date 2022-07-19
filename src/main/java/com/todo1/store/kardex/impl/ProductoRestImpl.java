/**
 *
 */
package com.todo1.store.kardex.impl;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.gson.JsonObject;
import com.todo1.store.ProductoDTO;
import com.todo1.store.ResponseDTO;
import com.todo1.store.enumerados.TipoMensaje;
import com.todo1.store.excepciones.GenericException;
import com.todo1.store.kardex.ProductoRest;
import com.todo1.store.kardex.servicio.ServicioProducto;
import com.todo1.store.util.JsonUtil;

/**
 * @author holger.morales
 */
public class ProductoRestImpl implements ProductoRest {

    private final Logger log = Logger.getLogger(this.getClass());
    private final JsonUtil jsonUtil = new JsonUtil();

    @EJB(lookup = "java:global/store-ejb/ServicioProductoImpl!com.todo1.store.kardex.servicio.ServicioProducto")
    private ServicioProducto servicioProducto;

    @Override
    public Response registrar(String param) {
        final ResponseDTO<String> respuesta = new ResponseDTO<>();
        try {
            final ProductoDTO productoDTO = this.jsonUtil.jsonToClass(param, ProductoDTO.class);
            final String login = "ADMIN";//Obtener del token
            this.servicioProducto.registrar(productoDTO, login, LocalDateTime.now());
            respuesta.setMensaje("¡Se registró correctamente los datos del producto!");
            respuesta.setTipoMensaje(TipoMensaje.EXITO.toString());
            respuesta.setDato("ok");
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

    @Override
    public Response obtenerPorCategoria(String param) {
        final ResponseDTO<List<ProductoDTO>> respuesta = new ResponseDTO<>();
        try {
            final JsonObject paramIn = this.jsonUtil.getJson(param);
            // Parameters pagination
            final Integer pagina = paramIn.get("pagina").getAsInt();
            final String cuenta = paramIn.get("cuenta").getAsString();
            final Integer itemsPorPagina = paramIn.get("itemsPorPagina").getAsInt();
            final String categoria = paramIn.get("categoria").getAsString();
            final List<ProductoDTO> productos = this.servicioProducto.obtenerPorCategoria(categoria, pagina, itemsPorPagina);
            if (!productos.isEmpty()) {
                if ("S".equalsIgnoreCase(cuenta)) {
                    respuesta.setTotal(this.servicioProducto.contarPorCategoria(categoria));
                }
                respuesta.setTipoMensaje(TipoMensaje.EXITO.toString());
            } else {
                respuesta.setTipoMensaje(TipoMensaje.INFORMACION.toString());
                respuesta.setMensaje("¡No se encuentra productos registrado!");
            }
            respuesta.setDato(productos);
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

    @Override
    public Response obtenerTodos(String param) {
        final ResponseDTO<List<ProductoDTO>> respuesta = new ResponseDTO<>();
        try {
            final JsonObject paramIn = this.jsonUtil.getJson(param);
            // Parameters pagination
            final Integer pagina = paramIn.get("pagina").getAsInt();
            final String cuenta = paramIn.get("cuenta").getAsString();
            final Integer itemsPorPagina = paramIn.get("itemsPorPagina").getAsInt();
            final List<ProductoDTO> productos = this.servicioProducto.obtenerTodos(pagina, itemsPorPagina);
            if (!productos.isEmpty()) {
                if ("S".equalsIgnoreCase(cuenta)) {
                    respuesta.setTotal(this.servicioProducto.contarTodos());
                }
                respuesta.setTipoMensaje(TipoMensaje.EXITO.toString());
            } else {
                respuesta.setTipoMensaje(TipoMensaje.INFORMACION.toString());
                respuesta.setMensaje("¡No se encuentra productos registrado!");
            }
            respuesta.setDato(productos);
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