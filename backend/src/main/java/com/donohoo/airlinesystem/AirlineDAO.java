package com.donohoo.airlinesystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class AirlineDAO {
	protected DataSource dataSource;
	
	private static final String SQL_FLIGHTS =
		" SELECT f.*, ac.capacity, fd.mileage, fc.airfare from airline.flight f\n" +
		" JOIN airline.aircraft a ON f.faa# = a.faa#\n" +
		" JOIN airline.aircraft_capacity ac ON ac.type = a.type\n" +
		" JOIN airline.flight_dist fd ON (fd.dest_city = f.dest_city AND fd.dep_city = f.dep_city)\n" +
		" JOIN airline.flight_cost fc ON fd.mileage = fc.mileage";
	
	private static final String SQL_PASS =
		" SELECT * from PASSENGER";
	
	private static final String SQL_PASS_FLIGHT =
		" SELECT * FROM BOOKED_FLIGHT WHERE P_ID = ?";
	
	private static final String SQL_CREW =
		" select c.*, \n" +
		" CASE WHEN EXISTS (select crew_id from copilot where crew_id = c.crew_id) THEN 'COPILOT' \n" +
		" ELSE 'PILOT' END AS position\n" +
		" from crew c";
	
	private static final String SQL_ADD_CREW =
		" INSERT INTO CREW VALUES (?,?,?,?,?,?)";
	
	private static final String SQL_ADD_FLIGHT =
		" INSERT INTO FLIGHT VALUES (?,?,?,?,?,?,?,?,?)";
	
	private static final String SQL_CHECK_FLIGHT_DIST =
		" SELECT * FROM FLIGHT_DIST WHERE DEP_CITY = ? AND DEST_CITY = ?";
	
	private static final String SQL_CHECK_FLIGHT_COST =
		" SELECT * FROM FLIGHT_COST WHERE MILEAGE = ?";
	
	private static final String SQL_UPDATE_CREW =
		" UPDATE CREW SET C_NAME = ?, SALARY = ?, SENIORITY = ?, SUPERVISED_BY = ?, FLY_HOURS = ? WHERE CREW_ID = ?";
	
	private static final String SQL_UPDATE_FLIGHT =
		" UPDATE FLIGHT SET DEP_CITY = ?, DEST_CITY = ?, ARR_TIME = ?, DEP_TIME = ?, TOTAL_PASS = ?, FAA# = ?, PILOT_ID = ?, COPILOT_ID = ? WHERE FLIGHT# = ?";
	
	private static final String SQL_ADD_RECORD =
		" INSERT INTO MAINT_RECORD VALUES (?,?,?,?)";
	
	private static final String SQL_ADD_JOB =
		" INSERT INTO MAINT_JOB VALUES (?,?)";
	
	private static final String SQL_UPDATE_RECORD =
		" UPDATE MAINT_RECORD SET FAA# = ?, M_DATE = ?, NEXT_DATE = ? WHERE LOG# = ?";
	
	private static final String SQL_DELETE_JOB =
		" DELETE FROM MAINT_JOB WHERE LOG_ID = ?";
	
	public AirlineDAO() {
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context)initContext.lookup("java:/comp/env");
			dataSource = (DataSource)envContext.lookup("jdbc/app");
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Flight> getFlights() {
		try (Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_FLIGHTS)) {
			ResultSet rs = ps.executeQuery();
			List<Flight> flights = new ArrayList<>();
			while (rs.next()) {
				Flight f = new Flight();
				f.setId(rs.getInt("FLIGHT#"));
				f.setAirfare(rs.getInt("AIRFARE"));
				f.setArrTime(rs.getInt("ARR_TIME"));
				f.setDepTime(rs.getInt("DEP_TIME"));
				f.setCopilotId(rs.getInt("COPILOT_ID"));
				f.setDepCity(rs.getString("DEP_CITY").trim());
				f.setDestCity(rs.getString("DEST_CITY").trim());
				f.setFaaId(rs.getInt("FAA#"));
				f.setMileage(rs.getInt("MILEAGE"));
				f.setPilotId(rs.getInt("PILOT_ID"));
				f.setTotalPass(rs.getInt("TOTAL_PASS"));
				flights.add(f);
			}
			return flights;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Passenger> getPassengers() {
		try (Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_PASS)) {
			ResultSet rs = ps.executeQuery();
			List<Passenger> pass = new ArrayList<>();
			while (rs.next()) {
				Passenger p = new Passenger();
				p.setId(rs.getInt("P_ID"));
				p.setName(rs.getString("P_NAME"));
				pass.add(p);
			}
			for (Passenger p : pass) {
				List<Integer> flights = new ArrayList<>();
				try (PreparedStatement ps2 = con.prepareStatement(SQL_PASS_FLIGHT)) {
					ps2.setInt(1, p.getId());
					ResultSet rs2 = ps2.executeQuery();
					while (rs2.next()) {
						flights.add(rs2.getInt("FLIGHT#"));
					}
					p.setFlights(flights);
				}
			}
			return pass;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String deleteFlight(String id) {
		try (Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM FLIGHT WHERE FLIGHT# = ?")) {
			ps.setString(1, id);
			ps.execute();
			return "OK";
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Crew updateCrew(Crew crew) {
		try (Connection con = dataSource.getConnection()) {
			try (PreparedStatement ps = con.prepareStatement(SQL_UPDATE_CREW)) {
				
				con.setAutoCommit(false);
				
				// Add to table
				ps.setString(1, crew.getName());
				ps.setInt(2, crew.getSalary());
				ps.setInt(3, crew.getSeniority());
				ps.setInt(4, crew.getSupervisor());
				ps.setInt(5, crew.getFlyHours());
				ps.setInt(6, crew.getId());
				ps.execute();
				
				// Update position
				List<Crew> list = this.getCrew();
				for (Crew c : list) {
					if (c.getId().equals(crew.getId()) && !c.getPosition().equals(crew.getPosition())) {
						// We need to change the position for this person
						Statement s = con.createStatement();
						s.execute("UPDATE FLIGHT SET PILOT_ID = NULL WHERE PILOT_ID =" + crew.getId());
						s.execute("UPDATE FLIGHT SET COPILOT_ID = NULL WHERE COPILOT_ID =" + crew.getId());
						s.execute("DELETE FROM PILOT WHERE CREW_ID =" + crew.getId());
						s.execute("DELETE FROM COPILOT WHERE CREW_ID =" + crew.getId());
						if (crew.getPosition().equals("PILOT")) {
							s.execute("INSERT INTO PILOT VALUES(" + crew.getId() + ")");
						} else {
							s.execute("INSERT INTO COPILOT VALUES(" + crew.getId() + ")");
						}
					}
				}
				
				con.commit();
				
				return crew;
				
			} catch (SQLException e) {
				con.rollback();
				throw new RuntimeException(e);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Map<String, Object>> getPlanes() {
		try (Connection con = dataSource.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT a.*, ac.capacity FROM AIRCRAFT a JOIN AIRCRAFT_CAPACITY ac ON a.type = ac.type")) {
			ResultSet rs = ps.executeQuery();
			List<Map<String, Object>> planes = new ArrayList<>();
			while (rs.next()) {
				Map<String, Object> plane = new HashMap<>();
				plane.put("id", rs.getInt("FAA#"));
				plane.put("type", rs.getString("TYPE"));
				plane.put("capacity", rs.getInt("CAPACITY"));
				plane.put("yearBuilt", rs.getInt("YEAR_BUILT"));
				planes.add(plane);
			}
			return planes;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Crew> getCrew() {
		try (Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(SQL_CREW)) {
			ResultSet rs = ps.executeQuery();
			List<Crew> crew = new ArrayList<>();
			while (rs.next()) {
				Crew c = new Crew();
				c.setId(rs.getInt("CREW_ID"));
				c.setName(rs.getString("C_NAME"));
				c.setPosition(rs.getString("POSITION"));
				c.setSalary(rs.getInt("SALARY"));
				c.setSeniority(rs.getInt("SENIORITY"));
				c.setFlyHours(rs.getInt("FLY_HOURS"));
				c.setSupervisor(rs.getInt("SUPERVISED_BY"));
				crew.add(c);
			}
			return crew;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Flight addFlight(Flight flight) {
		try (Connection con = dataSource.getConnection()) {
			try (PreparedStatement ps = con.prepareStatement(SQL_ADD_FLIGHT); PreparedStatement dist = con.prepareStatement(SQL_CHECK_FLIGHT_DIST);
				PreparedStatement cost = con.prepareStatement(SQL_CHECK_FLIGHT_COST)) {
				con.setAutoCommit(false);
				
				// Check to add new cost
				if (flight.getNewCost()) {
					PreparedStatement add = con.prepareStatement("INSERT INTO FLIGHT_COST VALUES (?,?)");
					add.setInt(1, flight.getMileage());
					add.setInt(2, flight.getAirfare());
					add.execute();
					add.close();
				}
				
				// Check to add new dist
				if (flight.getNewRoute()) {
					PreparedStatement add = con.prepareStatement("INSERT INTO FLIGHT_DIST VALUES (?,?,?)");
					add.setString(1, flight.getDepCity());
					add.setString(2, flight.getDestCity());
					add.setInt(3, flight.getMileage());
					add.execute();
					add.close();
				}
				
				// Add flight
				ps.setInt(1, flight.getId());
				ps.setString(2, flight.getDepCity().trim());
				ps.setString(3, flight.getDestCity().trim());
				ps.setInt(4, flight.getArrTime());
				ps.setInt(5, flight.getDepTime());
				ps.setInt(6, flight.getTotalPass());
				ps.setInt(7, flight.getFaaId());
				ps.setInt(8, flight.getPilotId());
				ps.setInt(9, flight.getCopilotId());
				ps.execute();
				
				con.commit();
				
				return flight;
			} catch (SQLException e) {
				con.rollback();
				throw new RuntimeException(e);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Record updateRecord(Record record) {
		try (Connection con = dataSource.getConnection()) {
			try (PreparedStatement ps = con.prepareStatement(SQL_UPDATE_RECORD); PreparedStatement ps2 = con.prepareStatement(SQL_ADD_JOB); PreparedStatement ps3 = con.prepareStatement(SQL_DELETE_JOB)) {
				con.setAutoCommit(false);
				
				// Update record
				ps.setInt(1, record.getFaaId());
				ps.setTimestamp(2, record.getMaintDate());
				ps.setTimestamp(3, record.getNextDate());
				ps.setInt(4, record.getId());
				ps.execute();
				
				// Delete jobs
				ps3.setInt(1, record.getId());
				ps3.execute();
				
				// Add jobs
				if (record.getJobs().size() > 0) {
					for (Integer i : record.getJobs()) {
						ps2.setInt(1, i);
						ps2.setInt(2, record.getId());
						ps2.addBatch();
					}
					ps2.executeBatch();
				}
				
				con.commit();
				
				return record;
			} catch (SQLException e) {
				con.rollback();
				throw new RuntimeException(e);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Record addRecord(Record record) {
		try (Connection con = dataSource.getConnection()) {
			try (PreparedStatement ps = con.prepareStatement(SQL_ADD_RECORD); PreparedStatement ps2 = con.prepareStatement(SQL_ADD_JOB)) {
				con.setAutoCommit(false);
				
				// Add record
				ps.setInt(1, record.getId());
				ps.setInt(2, record.getFaaId());
				ps.setTimestamp(3, record.getMaintDate());
				ps.setTimestamp(4, record.getNextDate());
				ps.execute();
				
				// Add jobs
				if (record.getJobs().size() > 0) {
					for (Integer i : record.getJobs()) {
						ps2.setInt(1, i);
						ps2.setInt(2, record.getId());
						ps2.addBatch();
					}
					ps2.executeBatch();
				}
				
				con.commit();
				
				return record;
			} catch (SQLException e) {
				con.rollback();
				throw new RuntimeException(e);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Record> getRecords() {
		try (Connection con = dataSource.getConnection()) {
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM MAINT_RECORD");
			List<Record> list = new ArrayList<>();
			while (rs.next()) {
				Record r = new Record();
				r.setId(rs.getInt("LOG#"));
				r.setFaaId(rs.getInt("FAA#"));
				r.setMaintDate(rs.getTimestamp("M_DATE"));
				r.setNextDate(rs.getTimestamp("NEXT_DATE"));
				Statement o = con.createStatement();
				ResultSet os = o.executeQuery("SELECT JOB_ID FROM MAINT_JOB WHERE LOG_ID = " + r.getId());
				List<Integer> jobs = new ArrayList<>();
				r.setJobs(jobs);
				while (os.next()) {
					jobs.add(os.getInt("JOB_ID"));
				}
				o.close();
				list.add(r);
			}
			s.close();
			return list;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Flight updateFlight(Flight flight) {
		try (Connection con = dataSource.getConnection()) {
			try (PreparedStatement ps = con.prepareStatement(SQL_UPDATE_FLIGHT)) {
				con.setAutoCommit(false);
				
				
				// Update flight
				ps.setString(1, flight.getDepCity().trim());
				ps.setString(2, flight.getDestCity().trim());
				ps.setInt(3, flight.getArrTime());
				ps.setInt(4, flight.getDepTime());
				ps.setInt(5, flight.getTotalPass());
				ps.setInt(6, flight.getFaaId());
				ps.setInt(7, flight.getPilotId());
				ps.setInt(8, flight.getCopilotId());
				ps.setInt(9, flight.getId());
				ps.execute();
				
				con.commit();
				
				return flight;
			} catch (SQLException e) {
				con.rollback();
				throw new RuntimeException(e);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Crew addCrew(Crew crew) {
		try (Connection con = dataSource.getConnection()) {
			try(PreparedStatement ps = con.prepareStatement(SQL_ADD_CREW)) {
				con.setAutoCommit(false);
				
				// Add to crew table
				ps.setInt(1, crew.getId());
				ps.setString(2, crew.getName());
				ps.setInt(3, crew.getSalary());
				ps.setInt(4, crew.getSeniority());
				ps.setInt(5, crew.getSupervisor());
				ps.setInt(6, crew.getFlyHours());
				ps.execute();
				
				// Add to pilot/copilot table
				PreparedStatement pos;
				PreparedStatement flight;
				if (crew.getPosition().equals("PILOT")) {
					pos = con.prepareStatement("INSERT INTO PILOT VALUES(?)");
					flight = con.prepareStatement("UPDATE FLIGHT SET PILOT_ID = ? WHERE FLIGHT# = ?");
				} else {
					pos = con.prepareStatement("INSERT INTO COPILOT VALUES(?)");
					flight = con.prepareStatement("UPDATE FLIGHT SET COPILOT_ID = ? WHERE FLIGHT# = ?");
				}
				pos.setInt(1, crew.getId());
				pos.execute();
				
				// Update flights
				for (Integer flightId : crew.getFlights()) {
					flight.setInt(1, crew.getId());
					flight.setInt(2, flightId);
					flight.addBatch();
				}
				
				if (crew.getFlights().size() > 0) {
					flight.executeBatch();
				}
				
				con.commit();
				
				return crew;
			} catch (SQLException e) {
				con.rollback();
				throw new RuntimeException(e);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void reset() {
		try (Connection con = dataSource.getConnection(); Statement s = con.createStatement()) {
			
			// Delete
			s.execute("DELETE FROM AIRLINE.BOOKED_FLIGHT");
			s.execute("DELETE FROM AIRLINE.PASSENGER");
			s.execute("DELETE FROM AIRLINE.MAINT_JOB");
			s.execute("DELETE FROM AIRLINE.MAINT_RECORD");
			s.execute("DELETE FROM AIRLINE.CAN_FLY");
			s.execute("DELETE FROM AIRLINE.FLIGHT");
			s.execute("DELETE FROM AIRLINE.AIRCRAFT");
			s.execute("DELETE FROM AIRLINE.JOBS");
			s.execute("DELETE FROM AIRLINE.AIRCRAFT_CAPACITY");
			s.execute("DELETE FROM AIRLINE.PILOT");
			s.execute("DELETE FROM AIRLINE.COPILOT");
			s.execute("DELETE FROM AIRLINE.CREW");
			s.execute("DELETE FROM AIRLINE.FLIGHT_DIST");
			s.execute("DELETE FROM AIRLINE.FLIGHT_COST");
			
			// FLight cost
			s.execute("INSERT INTO AIRLINE.FLIGHT_COST VALUES (170, 180)");
			s.execute("INSERT INTO AIRLINE.FLIGHT_COST VALUES (180, 100)");
			s.execute("INSERT INTO AIRLINE.FLIGHT_COST VALUES (300, 150)");
			s.execute("INSERT INTO AIRLINE.FLIGHT_COST VALUES (600, 200)");
			s.execute("INSERT INTO AIRLINE.FLIGHT_COST VALUES (80, 50)");
			s.execute("INSERT INTO AIRLINE.FLIGHT_COST VALUES (750, 220)");
			s.execute("INSERT INTO AIRLINE.FLIGHT_COST VALUES (500, 200)");
			s.execute("INSERT INTO AIRLINE.FLIGHT_COST VALUES (210, 85)");
			s.execute("INSERT INTO AIRLINE.FLIGHT_COST VALUES (230, 185)");
			s.execute("INSERT INTO AIRLINE.FLIGHT_COST VALUES (75, 50)");
			s.execute("INSERT INTO AIRLINE.FLIGHT_COST VALUES (450, 150)");
			
			// Flight dist
			s.execute("INSERT INTO AIRLINE.FLIGHT_DIST VALUES ('MONTREAL', 'NY', 170)");
			s.execute("INSERT INTO AIRLINE.FLIGHT_DIST VALUES ('MONTREAL', 'WASHINGTON', 180)");
			s.execute("INSERT INTO AIRLINE.FLIGHT_DIST VALUES ('NY', 'CHICAGO', 300)");
			s.execute("INSERT INTO AIRLINE.FLIGHT_DIST VALUES ('WASHINGTON', 'KANSAS-CITY', 600)");
			s.execute("INSERT INTO AIRLINE.FLIGHT_DIST VALUES ('WASHINGTON', 'NY', 80)");
			s.execute("INSERT INTO AIRLINE.FLIGHT_DIST VALUES ('CHICAGO', 'SLC', 750)");
			s.execute("INSERT INTO AIRLINE.FLIGHT_DIST VALUES ('KANSAS-CITY', 'DENVER', 300)");
			s.execute("INSERT INTO AIRLINE.FLIGHT_DIST VALUES ('KANSAS-CITY', 'SLC', 500)");
			s.execute("INSERT INTO AIRLINE.FLIGHT_DIST VALUES ('SLC', 'SANFRAN', 210)");
			s.execute("INSERT INTO AIRLINE.FLIGHT_DIST VALUES ('SLC', 'LA', 230)");
			s.execute("INSERT INTO AIRLINE.FLIGHT_DIST VALUES ('DENVER', 'SLC', 300)");
			s.execute("INSERT INTO AIRLINE.FLIGHT_DIST VALUES ('SANFRAN', 'LA', 75)");
			s.execute("INSERT INTO AIRLINE.FLIGHT_DIST VALUES ('LA', 'SEATTLE', 450)");
			
			// Crew
			s.execute("INSERT INTO AIRLINE.CREW VALUES (1, 'John Smith', 500000, 15, NULL, 3000)");
			s.execute("INSERT INTO AIRLINE.CREW VALUES (2, 'Rob Anderson', 400000, 12, 1, 2700)");
			s.execute("INSERT INTO AIRLINE.CREW VALUES (3, 'Bill Talbot', 300000, 12, 1, 2500)");
			s.execute("INSERT INTO AIRLINE.CREW VALUES (4, 'Mark Lott', 300000, 18, 1, 4500)");
			s.execute("INSERT INTO AIRLINE.CREW VALUES (22, 'Joe Young', 180000, 9, 1, 0)");
			s.execute("INSERT INTO AIRLINE.CREW VALUES (8, 'Mark Townson', 90000, 8, 22, 3500)");
			s.execute("INSERT INTO AIRLINE.CREW VALUES (11, 'Steve Lowe', 250000, 10, 2, 2000)");
			s.execute("INSERT INTO AIRLINE.CREW VALUES (12, 'Ella Crowe', 200000, 9, 3, 800)");
			s.execute("INSERT INTO AIRLINE.CREW VALUES (13, 'Mike York', 150000, 8, 2, 650)");
			s.execute("INSERT INTO AIRLINE.CREW VALUES (21, 'Sam Carson', 500000, 12, 2, 2400)");
			s.execute("INSERT INTO AIRLINE.CREW VALUES (10, 'Dave Empire', 80000, 2, 21, 300)");
			s.execute("INSERT INTO AIRLINE.CREW VALUES (30, 'Dee Brown', 70000, 13, 21, 1050)");
			s.execute("INSERT INTO AIRLINE.CREW VALUES (32, 'Alan Lee', 85000, 15, 4, 2050)");
			
			// Copilot
			s.execute("INSERT INTO AIRLINE.COPILOT VALUES (8)");
			s.execute("INSERT INTO AIRLINE.COPILOT VALUES (10)");
			s.execute("INSERT INTO AIRLINE.COPILOT VALUES (11)");
			s.execute("INSERT INTO AIRLINE.COPILOT VALUES (12)");
			s.execute("INSERT INTO AIRLINE.COPILOT VALUES (13)");
			s.execute("INSERT INTO AIRLINE.COPILOT VALUES (30)");
			s.execute("INSERT INTO AIRLINE.COPILOT VALUES (32)");
			
			// Pilot
			s.execute("INSERT INTO AIRLINE.PILOT VALUES (1)");
			s.execute("INSERT INTO AIRLINE.PILOT VALUES (2)");
			s.execute("INSERT INTO AIRLINE.PILOT VALUES (3)");
			s.execute("INSERT INTO AIRLINE.PILOT VALUES (4)");
			s.execute("INSERT INTO AIRLINE.PILOT VALUES (21)");
			s.execute("INSERT INTO AIRLINE.PILOT VALUES (22)");
			
			// Airline Capacity
			s.execute("INSERT INTO AIRLINE.AIRCRAFT_CAPACITY (TYPE, CAPACITY) VALUES ('B757', 180)");
			s.execute("INSERT INTO AIRLINE.AIRCRAFT_CAPACITY (TYPE, CAPACITY) VALUES ('B737', 160)");
			s.execute("INSERT INTO AIRLINE.AIRCRAFT_CAPACITY (TYPE, CAPACITY) VALUES ('RJ50', 50)");
			s.execute("INSERT INTO AIRLINE.AIRCRAFT_CAPACITY (TYPE, CAPACITY) VALUES ('A319', 155)");
			s.execute("INSERT INTO AIRLINE.AIRCRAFT_CAPACITY (TYPE, CAPACITY) VALUES ('RJ90', 90)");
			s.execute("INSERT INTO AIRLINE.AIRCRAFT_CAPACITY (TYPE, CAPACITY) VALUES ('A320', 170)");
			s.execute("INSERT INTO AIRLINE.AIRCRAFT_CAPACITY (TYPE, CAPACITY) VALUES ('B767', 280)");
			
			// Jobs
			s.execute("INSERT INTO AIRLINE.JOBS VALUES (1)");
			s.execute("INSERT INTO AIRLINE.JOBS VALUES (23)");
			s.execute("INSERT INTO AIRLINE.JOBS VALUES (10)");
			s.execute("INSERT INTO AIRLINE.JOBS VALUES (13)");
			s.execute("INSERT INTO AIRLINE.JOBS VALUES (44)");
			s.execute("INSERT INTO AIRLINE.JOBS VALUES (22)");
			s.execute("INSERT INTO AIRLINE.JOBS VALUES (11)");
			s.execute("INSERT INTO AIRLINE.JOBS VALUES (5)");
			s.execute("INSERT INTO AIRLINE.JOBS VALUES (19)");
			s.execute("INSERT INTO AIRLINE.JOBS VALUES (33)");
			s.execute("INSERT INTO AIRLINE.JOBS VALUES (28)");
			s.execute("INSERT INTO AIRLINE.JOBS VALUES (18)");
			s.execute("INSERT INTO AIRLINE.JOBS VALUES (14)");
			
			// Aircraft 
			s.execute("INSERT INTO AIRLINE.AIRCRAFT (FAA#, TYPE, YEAR_BUILT) VALUES (10, 'B737', 2011)");
			s.execute("INSERT INTO AIRLINE.AIRCRAFT (FAA#, TYPE, YEAR_BUILT) VALUES (5, 'B757', 2000)");
			s.execute("INSERT INTO AIRLINE.AIRCRAFT (FAA#, TYPE, YEAR_BUILT) VALUES (9, 'RJ50', 2001)");
			s.execute("INSERT INTO AIRLINE.AIRCRAFT (FAA#, TYPE, YEAR_BUILT) VALUES (27, 'A319', 2002)");
			s.execute("INSERT INTO AIRLINE.AIRCRAFT (FAA#, TYPE, YEAR_BUILT) VALUES (16, 'B737', 1999)");
			s.execute("INSERT INTO AIRLINE.AIRCRAFT (FAA#, TYPE, YEAR_BUILT) VALUES (82, 'B757', 2002)");
			s.execute("INSERT INTO AIRLINE.AIRCRAFT (FAA#, TYPE, YEAR_BUILT) VALUES (31, 'RJ90', 1998)");
			s.execute("INSERT INTO AIRLINE.AIRCRAFT (FAA#, TYPE, YEAR_BUILT) VALUES (44, 'A320', 2007)");
			s.execute("INSERT INTO AIRLINE.AIRCRAFT (FAA#, TYPE, YEAR_BUILT) VALUES (79, 'A320', 2005)");
			s.execute("INSERT INTO AIRLINE.AIRCRAFT (FAA#, TYPE, YEAR_BUILT) VALUES (23, 'B737', 2003)");
			s.execute("INSERT INTO AIRLINE.AIRCRAFT (FAA#, TYPE, YEAR_BUILT) VALUES (58, 'RJ90', 2005)");
			s.execute("INSERT INTO AIRLINE.AIRCRAFT (FAA#, TYPE, YEAR_BUILT) VALUES (99, 'B757', 1997)");
			s.execute("INSERT INTO AIRLINE.AIRCRAFT (FAA#, TYPE, YEAR_BUILT) VALUES (19, 'B767', 2005)");
			
			// Flight
			s.execute("INSERT INTO AIRLINE.FLIGHT VALUES (101, 'MONTREAL', 'NY', 0645, 530, 111, 10, 2, 11)");
			s.execute("INSERT INTO AIRLINE.FLIGHT VALUES (102, 'MONTREAL', 'WASHINGTON', 235, 100, 89, 5, 1, 30)");
			s.execute("INSERT INTO AIRLINE.FLIGHT VALUES (103, 'NY', 'CHICAGO', 1000, 800, 15, 9, 2, 11)");
			s.execute("INSERT INTO AIRLINE.FLIGHT VALUES (105, 'WASHINGTON', 'KANSAS-CITY', 845, 600, 109, 27, 1, 30)");
			s.execute("INSERT INTO AIRLINE.FLIGHT VALUES (106, 'WASHINGTON', 'NY', 1330, 1200, 97, 16, 3, 13)");
			s.execute("INSERT INTO AIRLINE.FLIGHT VALUES (107, 'CHICAGO', 'SLC', 1430, 1100, 150, 82, 2, 11)");
			s.execute("INSERT INTO AIRLINE.FLIGHT VALUES (110, 'KANSAS-CITY', 'DENVER', 1225, 1000, 78, 31, 4, 32)");
			s.execute("INSERT INTO AIRLINE.FLIGHT VALUES (111, 'KANSAS-CITY', 'SLC', 1530, 1300, 62, 44, 1, 30)");
			s.execute("INSERT INTO AIRLINE.FLIGHT VALUES (112, 'SLC', 'SANFRAN', 1930, 1800, 118, 79, 22, 8)");
			s.execute("INSERT INTO AIRLINE.FLIGHT VALUES (113, 'SLC', 'LA', 1900, 1730, 138, 23, 21, 10)");
			s.execute("INSERT INTO AIRLINE.FLIGHT VALUES (115, 'DENVER', 'SLC', 1600, 1500, 70, 58, 4, 32)");
			s.execute("INSERT INTO AIRLINE.FLIGHT VALUES (116, 'SANFRAN', 'LA', 2230, 2200, 85, 99, 22, 8)");
			s.execute("INSERT INTO AIRLINE.FLIGHT VALUES (118, 'LA', 'SEATTLE', 2000, 2100, 120, 19, 21, 10)");
			
			// Can Fly
			s.execute("INSERT INTO AIRLINE.CAN_FLY VALUES (1, 5)");
			s.execute("INSERT INTO AIRLINE.CAN_FLY VALUES (1, 27)");
			s.execute("INSERT INTO AIRLINE.CAN_FLY VALUES (1, 44)");
			s.execute("INSERT INTO AIRLINE.CAN_FLY VALUES (2, 10)");
			s.execute("INSERT INTO AIRLINE.CAN_FLY VALUES (2, 9)");
			s.execute("INSERT INTO AIRLINE.CAN_FLY VALUES (2, 82)");
			s.execute("INSERT INTO AIRLINE.CAN_FLY VALUES (3, 16)");
			s.execute("INSERT INTO AIRLINE.CAN_FLY VALUES (3, 19)");
			s.execute("INSERT INTO AIRLINE.CAN_FLY VALUES (4, 31)");
			s.execute("INSERT INTO AIRLINE.CAN_FLY VALUES (8, 99)");
			s.execute("INSERT INTO AIRLINE.CAN_FLY VALUES (8, 79)");
			s.execute("INSERT INTO AIRLINE.CAN_FLY VALUES (10, 19)");
			s.execute("INSERT INTO AIRLINE.CAN_FLY VALUES (10, 23)");
			s.execute("INSERT INTO AIRLINE.CAN_FLY VALUES (11, 9)");
			s.execute("INSERT INTO AIRLINE.CAN_FLY VALUES (11, 10)");
			s.execute("INSERT INTO AIRLINE.CAN_FLY VALUES (11, 82)");
			s.execute("INSERT INTO AIRLINE.CAN_FLY VALUES (12, 99)");
			s.execute("INSERT INTO AIRLINE.CAN_FLY VALUES (13, 16)");
			s.execute("INSERT INTO AIRLINE.CAN_FLY VALUES (13, 58)");
			s.execute("INSERT INTO AIRLINE.CAN_FLY VALUES (21, 23)");
			s.execute("INSERT INTO AIRLINE.CAN_FLY VALUES (21, 19)");
			s.execute("INSERT INTO AIRLINE.CAN_FLY VALUES (22, 79)");
			s.execute("INSERT INTO AIRLINE.CAN_FLY VALUES (22, 99)");
			s.execute("INSERT INTO AIRLINE.CAN_FLY VALUES (30, 5)");
			s.execute("INSERT INTO AIRLINE.CAN_FLY VALUES (30, 27)");
			s.execute("INSERT INTO AIRLINE.CAN_FLY VALUES (30, 44)");
			s.execute("INSERT INTO AIRLINE.CAN_FLY VALUES (32, 58)");
			s.execute("INSERT INTO AIRLINE.CAN_FLY VALUES (32, 9)");
			
			// Maint Record
			s.execute("INSERT INTO AIRLINE.MAINT_RECORD VALUES (101, 19, TO_TIMESTAMP('02/12/2014', 'mm/dd/yyyy'), TO_TIMESTAMP('02/12/2015', 'mm/dd/yyyy'))");
			s.execute("INSERT INTO AIRLINE.MAINT_RECORD VALUES (102, 10, TO_TIMESTAMP('02/09/2014', 'mm/dd/yyyy'), TO_TIMESTAMP('12/31/2014', 'mm/dd/yyyy'))");
			s.execute("INSERT INTO AIRLINE.MAINT_RECORD VALUES (103, 82, TO_TIMESTAMP('12/10/2013', 'mm/dd/yyyy'), TO_TIMESTAMP('09/10/2014', 'mm/dd/yyyy'))");
			s.execute("INSERT INTO AIRLINE.MAINT_RECORD VALUES (104, 44, TO_TIMESTAMP('03/06/2014', 'mm/dd/yyyy'), TO_TIMESTAMP('12/21/2014', 'mm/dd/yyyy'))");
			s.execute("INSERT INTO AIRLINE.MAINT_RECORD VALUES (105, 27, TO_TIMESTAMP('04/18/2014', 'mm/dd/yyyy'), TO_TIMESTAMP('10/22/2014', 'mm/dd/yyyy'))");
			s.execute("INSERT INTO AIRLINE.MAINT_RECORD VALUES (106, 58, TO_TIMESTAMP('06/06/2014', 'mm/dd/yyyy'), TO_TIMESTAMP('11/11/2014', 'mm/dd/yyyy'))");
			s.execute("INSERT INTO AIRLINE.MAINT_RECORD VALUES (107, 5, TO_TIMESTAMP('01/17/2014', 'mm/dd/yyyy'), TO_TIMESTAMP('10/18/2014', 'mm/dd/yyyy'))");
			s.execute("INSERT INTO AIRLINE.MAINT_RECORD VALUES (108, 23, TO_TIMESTAMP('10/01/2013', 'mm/dd/yyyy'), TO_TIMESTAMP('09/09/2014', 'mm/dd/yyyy'))");
			s.execute("INSERT INTO AIRLINE.MAINT_RECORD VALUES (109, 79, TO_TIMESTAMP('01/11/2014', 'mm/dd/yyyy'), TO_TIMESTAMP('10/21/2014', 'mm/dd/yyyy'))");
			s.execute("INSERT INTO AIRLINE.MAINT_RECORD VALUES (110, 9, TO_TIMESTAMP('03/14/2014', 'mm/dd/yyyy'), TO_TIMESTAMP('11/28/2014', 'mm/dd/yyyy'))");
			
			// Maint Job
			s.execute("INSERT INTO AIRLINE.MAINT_JOB VALUES (1, 101)");
			s.execute("INSERT INTO AIRLINE.MAINT_JOB VALUES (23, 101)");
			s.execute("INSERT INTO AIRLINE.MAINT_JOB VALUES (10, 102)");
			s.execute("INSERT INTO AIRLINE.MAINT_JOB VALUES (13, 103)");
			s.execute("INSERT INTO AIRLINE.MAINT_JOB VALUES (10, 103)");
			s.execute("INSERT INTO AIRLINE.MAINT_JOB VALUES (44, 104)");
			s.execute("INSERT INTO AIRLINE.MAINT_JOB VALUES (22, 104)");
			s.execute("INSERT INTO AIRLINE.MAINT_JOB VALUES (11, 104)");
			s.execute("INSERT INTO AIRLINE.MAINT_JOB VALUES (5, 105)");
			s.execute("INSERT INTO AIRLINE.MAINT_JOB VALUES (19, 105)");
			s.execute("INSERT INTO AIRLINE.MAINT_JOB VALUES (13, 106)");
			s.execute("INSERT INTO AIRLINE.MAINT_JOB VALUES (1, 107)");
			s.execute("INSERT INTO AIRLINE.MAINT_JOB VALUES (10, 107)");
			s.execute("INSERT INTO AIRLINE.MAINT_JOB VALUES (33, 108)");
			s.execute("INSERT INTO AIRLINE.MAINT_JOB VALUES (28, 109)");
			s.execute("INSERT INTO AIRLINE.MAINT_JOB VALUES (18, 109)");
			s.execute("INSERT INTO AIRLINE.MAINT_JOB VALUES (5, 110)");
			s.execute("INSERT INTO AIRLINE.MAINT_JOB VALUES (11, 110)");
			s.execute("INSERT INTO AIRLINE.MAINT_JOB VALUES (14, 110)");
			
			// Passenger
			s.execute("INSERT INTO AIRLINE.PASSENGER VALUES (1, 'Jeff Long', 1987, 5, 1, 2023583459, 'M')");
			s.execute("INSERT INTO AIRLINE.PASSENGER VALUES (2, 'Mary Stewards', 1999, 12, 2, 8017023293, 'F')");
			s.execute("INSERT INTO AIRLINE.PASSENGER VALUES (3, 'Tom Draper', 1960, 7, 7, 4513902849, 'M')");
			s.execute("INSERT INTO AIRLINE.PASSENGER VALUES (4, 'Jack Monson', 2001, 11, 28, 9388390838, 'M')");
			s.execute("INSERT INTO AIRLINE.PASSENGER VALUES (5, 'Jose Diego', 1997, 10, 21, 6502913203, 'M')");
			s.execute("INSERT INTO AIRLINE.PASSENGER VALUES (6, 'Megan Moon', 2005, 3, 11, 8378927920, 'F')");
			s.execute("INSERT INTO AIRLINE.PASSENGER VALUES (7, 'Truman Lee', 2001, 8, 14, 3788121279, 'M')");
			s.execute("INSERT INTO AIRLINE.PASSENGER VALUES (8, 'Nina Cook', 1976, 2, 12, 87223394472, 'F')");
			s.execute("INSERT INTO AIRLINE.PASSENGER VALUES (9, 'Fred Miller', 1957, 9, 12, 1293297583, 'M')");
			s.execute("INSERT INTO AIRLINE.PASSENGER VALUES (10, 'Eli Smith', 1945, 4, 22, 0192387238, 'F')");
			s.execute("INSERT INTO AIRLINE.PASSENGER VALUES (11, 'David Chan', 1940, 12, 12, 8730921298, 'M')");
			s.execute("INSERT INTO AIRLINE.PASSENGER VALUES (12, 'Molly Burns', 2000, 9, 18, 1290992302, 'M')");
			
			// Booked flight
			s.execute("INSERT INTO AIRLINE.BOOKED_FLIGHT VALUES (1, 101)");
			s.execute("INSERT INTO AIRLINE.BOOKED_FLIGHT VALUES (2, 102)");
			s.execute("INSERT INTO AIRLINE.BOOKED_FLIGHT VALUES (2, 106)");
			s.execute("INSERT INTO AIRLINE.BOOKED_FLIGHT VALUES (3, 105)");
			s.execute("INSERT INTO AIRLINE.BOOKED_FLIGHT VALUES (3, 111)");
			s.execute("INSERT INTO AIRLINE.BOOKED_FLIGHT VALUES (4, 110)");
			s.execute("INSERT INTO AIRLINE.BOOKED_FLIGHT VALUES (5, 113)");
			s.execute("INSERT INTO AIRLINE.BOOKED_FLIGHT VALUES (5, 118)");
			s.execute("INSERT INTO AIRLINE.BOOKED_FLIGHT VALUES (6, 107)");
			s.execute("INSERT INTO AIRLINE.BOOKED_FLIGHT VALUES (7, 111)");
			s.execute("INSERT INTO AIRLINE.BOOKED_FLIGHT VALUES (7, 113)");
			s.execute("INSERT INTO AIRLINE.BOOKED_FLIGHT VALUES (8, 101)");
			s.execute("INSERT INTO AIRLINE.BOOKED_FLIGHT VALUES (8, 103)");
			s.execute("INSERT INTO AIRLINE.BOOKED_FLIGHT VALUES (8, 107)");
			s.execute("INSERT INTO AIRLINE.BOOKED_FLIGHT VALUES (9, 115)");
			s.execute("INSERT INTO AIRLINE.BOOKED_FLIGHT VALUES (9, 113)");
			s.execute("INSERT INTO AIRLINE.BOOKED_FLIGHT VALUES (10, 112)");
			s.execute("INSERT INTO AIRLINE.BOOKED_FLIGHT VALUES (10, 116)");
			s.execute("INSERT INTO AIRLINE.BOOKED_FLIGHT VALUES (11, 118)");
			s.execute("INSERT INTO AIRLINE.BOOKED_FLIGHT VALUES (12, 115)");
			
			con.commit();
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
