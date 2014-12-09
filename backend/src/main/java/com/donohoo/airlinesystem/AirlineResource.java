package com.donohoo.airlinesystem;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/")
public class AirlineResource {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	public AirlineDAO dao;
	public AirlineResource() {
		dao = new AirlineDAO();
	}
	
	@Path("resetdb")
	@POST
	public Response resetDb(@Context HttpServletRequest req) {
		try {
			dao.reset();
			return Response.ok().build();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return Response.serverError().build();
		}
	}
	
	@Path("flights")
	@GET
	public Response getFlights(@Context HttpServletRequest req) {
		try {
			return Response.ok(dao.getFlights()).build();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return Response.serverError().build();
		}
	}
	
	@Path("records")
	@GET
	public Response getRecords() {
		try {
			return Response.ok(dao.getRecords()).build();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return Response.serverError().build();
		}
	}
	
	@Path("records")
	@POST
	public Response addRecord(Record record) {
		try {
			return Response.ok(dao.addRecord(record)).build();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return Response.serverError().build();
		}
	}
	
	@Path("flights/{id}")
	@DELETE
	public Response deleteFlight(@PathParam(value="id") String id) {
		try {
			return Response.ok(dao.deleteFlight(id)).build();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return Response.serverError().build();
		}
	}
	
	@Path("flights/{id}")
	@PUT
	public Response updateFlight(Flight flight) {
		try {
			return Response.ok(dao.updateFlight(flight)).build();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return Response.serverError().build();
		}
	}
	
	@Path("records/{id}")
	@PUT
	public Response updateRecord(Record record) {
		try {
			return Response.ok(dao.updateRecord(record)).build();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return Response.serverError().build();
		}
	}
	
	@Path("flights")
	@POST
	public Response addFlight(Flight flight) {
		try {
			return Response.ok(dao.addFlight(flight)).build();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return Response.serverError().build();
		}
	}
	
	@Path("passengers")
	@GET
	public Response getPassengers(@Context HttpServletRequest req) {
		try {
			return Response.ok(dao.getPassengers()).build();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return Response.serverError().build();
		}
	}
	
	@Path("crew")
	@GET
	public Response getCrew() {
		try {
			return Response.ok(dao.getCrew()).build();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return Response.serverError().build();
		}
	}
	
	@Path("planes")
	@GET
	public Response getPlanes() {
		try {
			return Response.ok(dao.getPlanes()).build();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return Response.serverError().build();
		}
	}
	
	@Path("crew")
	@POST
	public Response addCrew(Crew crew) {
		try {
			return Response.ok(dao.addCrew(crew)).build();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return Response.serverError().build();
		}
	}
	
	@Path("crew/{id}")
	@PUT
	public Response updateCrew(Crew crew) {
		try {
			return Response.ok(dao.updateCrew(crew)).build();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return Response.serverError().build();
		}
	}
}
