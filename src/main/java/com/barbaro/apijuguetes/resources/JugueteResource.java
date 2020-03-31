package com.barbaro.apijuguetes.resources;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.TransactionException;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import com.barbaro.apijuguetes.models.Juguete;
import com.barbaro.apijuguetes.util.HibernateUtil;

@Path("juguetes")
public class JugueteResource {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> getJuguetes() {
		
		Map<String, Object> response = new HashMap<>();
		
		int codigo = 0;
		String mensaje = null;
		
		SessionFactory sessionFactory = null;
		Session session = null;
		List<Juguete> lista = null;
		
		try {
			StandardServiceRegistry registry = 
					new StandardServiceRegistryBuilder().configure().build();
			
			sessionFactory = new MetadataSources(registry)
					.buildMetadata().buildSessionFactory();
					
			session = sessionFactory.openSession();
			
			Query query = session.createQuery("FROM Juguete j", Juguete.class);
			lista = query.getResultList();
			
			codigo = Response.Status.OK.getStatusCode();
			mensaje = "Se obtuvieron los datos";
			
		} catch(Exception e){
			e.printStackTrace();
			codigo = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
			mensaje = e.getMessage();
		} finally {
			if(session != null) {
				session.close();
			}
			if(sessionFactory != null) {
				sessionFactory.close();
			}
		}
		
		response.put("data", lista);
		response.put("mensaje", mensaje);
		
		return response;
	}
	
	@POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crearJuguete(Juguete juguete) {
		
		Session session = null;
		Transaction tx = null;
		
		Map<String, Object> response = null;
		
		// HTTP
		int codigo = Status.INTERNAL_SERVER_ERROR.getStatusCode();
		
		// Interno - notificar algun error de la logica
		int codigoInt = 0;
		String mensaje = null;
		
		try {
			
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			
			// Metodos de session
			// save
			// saveOrUpdate
			// update
			// delete
			// persist
			// detached
			juguete.setFecha(new Date());
			session.save(juguete);
			tx.commit();
			
			codigo = Response.Status.CREATED.getStatusCode();
			codigoInt = 1; // La operacion sucedio
			mensaje = "Se almaceno el juguete";
		}catch(TransactionException e){
			if(tx != null) {
				tx.rollback(); // deshacer la opreacion con la base de datos
			}
			mensaje = "Error en el servidor";
		}catch (HibernateException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
			mensaje = "Error";
		}finally {
			if(session != null) {
				HibernateUtil.closeSession(session);
			}
		}
		
		response = new HashMap<>();
		response.put("codigo", codigoInt);
		response.put("codigo", codigo);
		response.put("mensaje", mensaje);
		
        return Response.status(codigo).entity(response).build();
    }
	
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateJuguete(@PathParam("id") Integer id, Juguete juguete) {
		
		Session session = null;
		Transaction tx = null;
		Map<String, Object> response = null;
		
		int status = Status.INTERNAL_SERVER_ERROR.getStatusCode();
		int codigoInt = 0;
		String mensaje = null;
		
		Juguete jugueteDB = null;
		
		try {
			session = HibernateUtil.getSession();

			// Operaciones con la base de datos
			jugueteDB = session.find(Juguete.class, id);
			
			if(jugueteDB != null) {
				tx = session.beginTransaction();
				jugueteDB.setNombre(juguete.getNombre());
				jugueteDB.setDescripcion(juguete.getDescripcion());
				jugueteDB.setPrecio(juguete.getPrecio());
				
				session.update(jugueteDB);
				tx.commit();
				status = Status.OK.getStatusCode();
				mensaje = "Se actualizo el recurso";
				codigoInt = 1;
						
			}else {
				status = Status.NOT_FOUND.getStatusCode();
				mensaje = "Recurso no encontrado";
			}
			
		}catch(TransactionException e) {
			e.printStackTrace();
			mensaje = "Erroe en la transaccion";
		}catch(HibernateException e ) {
			e.printStackTrace();
			mensaje ="Erro con la base de datos";
		}catch(Exception e) {
			e.printStackTrace();
			mensaje = "Error, consulta mas tarde";
		}finally {
			if(session != null) {
				HibernateUtil.closeSession(session);
			}
		}
		
		response = new HashMap<>();
		response.put("codigo", codigoInt);
		response.put("mensaje", mensaje);
		response.put("data", jugueteDB);
		
		return Response.status(status).entity(response).build();
	}
	
	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteJuguete(@PathParam("id") Integer id) {
		
		Session session = null;
		Transaction tx = null;
		Juguete juguete = null;
		
		Map<String, Object> response = null;
		
		int status = Status.INTERNAL_SERVER_ERROR.getStatusCode();
		int codigo = 0;
		
		String mensaje = null;
		
		try {
			
			session = HibernateUtil.getSession();
			
			// Llevar la tarea get, load, find
			juguete = session.get(Juguete.class, id);
			if(juguete != null ) {
				tx = session.beginTransaction();
				session.remove(juguete);
				tx.commit();
				
				mensaje = "Recurso eliminado";
				status = Status.OK.getStatusCode();
			}else {
				status = Status.NOT_FOUND.getStatusCode();
				mensaje = "Recurso no encontrado";
			}
			
		}catch(TransactionException e) {
			e.printStackTrace();
			mensaje = "Error en la transaccion";
		}catch(HibernateException e) {
			e.printStackTrace();
			mensaje  = "Error en la base de datos";
		}catch(Exception e) {
			e.printStackTrace();
			mensaje = "Error, consulta mas tarde";
		}finally {
			if(session != null) {
				HibernateUtil.closeSession(session);
			}
		}
		
		response = new HashMap<>();
		response.put("codigo", codigo);
		response.put("mensaje", mensaje);
		response.put("data", null);
		
		return Response.status(status).entity(response).build();
	}
}
