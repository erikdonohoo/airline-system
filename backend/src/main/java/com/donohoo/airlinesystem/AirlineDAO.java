package com.donohoo.airlinesystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
			s.execute("INSERT INTO AIRLINE.FLIGHT VALUES (101, 'MONTREAL', 'NY', '0645', '0530', 111, 10, 2, 11)");
			s.execute("INSERT INTO AIRLINE.FLIGHT VALUES (102, 'MONTREAL', 'WASHINGTON', '0235', '0100', 89, 5, 1, 30)");
			s.execute("INSERT INTO AIRLINE.FLIGHT VALUES (103, 'NY', 'CHICAGO', '1000', '0800', 15, 9, 2, 11)");
			s.execute("INSERT INTO AIRLINE.FLIGHT VALUES (105, 'WASHINGTON', 'KANSAS-CITY', '0845', '0600', 109, 27, 1, 30)");
			s.execute("INSERT INTO AIRLINE.FLIGHT VALUES (106, 'WASHINGTON', 'NY', '1330', '1200', 97, 16, 3, 13)");
			s.execute("INSERT INTO AIRLINE.FLIGHT VALUES (107, 'CHICAGO', 'SLC', '1430', '1100', 150, 82, 2, 11)");
			s.execute("INSERT INTO AIRLINE.FLIGHT VALUES (110, 'KANSAS-CITY', 'DENVER', '1225', '1000', 78, 31, 4, 32)");
			s.execute("INSERT INTO AIRLINE.FLIGHT VALUES (111, 'KANSAS-CITY', 'SLC', '1530', '1300', 62, 44, 1, 30)");
			s.execute("INSERT INTO AIRLINE.FLIGHT VALUES (112, 'SLC', 'SANFRAN', '1930', '1800', 118, 79, 22, 8)");
			s.execute("INSERT INTO AIRLINE.FLIGHT VALUES (113, 'SLC', 'LA', '1900', '1730', 138, 23, 21, 10)");
			s.execute("INSERT INTO AIRLINE.FLIGHT VALUES (115, 'DENVER', 'SLC', '1600', '1500', 70, 58, 4, 32)");
			s.execute("INSERT INTO AIRLINE.FLIGHT VALUES (116, 'SANFRAN', 'LA', '2230', '2200', 85, 99, 22, 8)");
			s.execute("INSERT INTO AIRLINE.FLIGHT VALUES (118, 'LA', 'SEATTLE', '2000', '2100', 120, 19, 21, 10)");
			
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
