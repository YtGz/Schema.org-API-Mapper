package core;

import java.io.File;
import java.util.List;
import javax.persistence.*;

import core.Event;
import core.EventFactory;
import core.Restaurant;
import core.RestaurantFactory;
import core.Endpoints;

public class Database {
	private static final String event_database = "database/e_db.odb";
	private static final String restaurant_database = "database/r_db.odb";

	public static List<Event> getAllEvents() {
		return getAllEvents("");
	}
	public static List<Event> getAllEvents(String query_options) {
			// Open a database connection
		    // (create a new database if it doesn't exist yet):
		    EntityManagerFactory emf =
		        Persistence.createEntityManagerFactory(event_database);
		    EntityManager em = emf.createEntityManager();

			//get all events from database
			List<Event> events = 
			  em.createQuery("SELECT e FROM Event e " + query_options, Event.class).getResultList();
	
			em.close();
			emf.close();

			return events;
	}
	public static void addAllEvents(List<Event> events) {
		// Open a database connection
		// (create a new database if it doesn't exist yet):
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(event_database);
		EntityManager em = emf.createEntityManager();

		try {
	    	em.getTransaction().begin();
			for (Event e : events) {
				for (Artist a : e.getArtists()) {
					a.setEvent(e);
					em.persist(a);
				}
				em.persist(e);
			}
			em.getTransaction().commit();
		}
		catch (Exception e) {
			System.out.println("transaction error (add all events)");
		}
		finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
			emf.close();
		}
	}
	public static List<Restaurant> getAllRestaurants() {
		return getAllRestaurants("");
	}
	public static List<Restaurant> getAllRestaurants(String query_options) {
			// Open a database connection
		    // (create a new database if it doesn't exist yet):
		    EntityManagerFactory emf =
		        Persistence.createEntityManagerFactory(restaurant_database);
		    EntityManager em = emf.createEntityManager();

			//get all restaurants from database
			List<Restaurant> restaurants = 
			  em.createQuery("SELECT r FROM Restaurant r " + query_options, Restaurant.class).getResultList();
	
			em.close();
			emf.close();

			return restaurants;
	}
	public static void addAllRestaurants(List<Restaurant> restaurants) {
		// Open a database connection
		// (create a new database if it doesn't exist yet):
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(restaurant_database);
		EntityManager em = emf.createEntityManager();

		try {
	    	em.getTransaction().begin();
			for (Restaurant r : restaurants) {
				em.persist(r);
			}
			em.getTransaction().commit();
		}
		catch (Exception r) {
			System.out.println("transaction error (add all restaurants)");
			System.out.println(r.toString());
		}
		finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
			emf.close();
		}
	}
	public static void wipeEventDatabase() {
		new File(event_database).delete();	
	}
	public static void wipeRestaurantDatabase() {
		new File(restaurant_database).delete();
	}
	public static void wipeDatabase() {
		wipeEventDatabase();
		wipeRestaurantDatabase();
	}
}
