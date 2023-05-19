package model;

import java.util.List;
import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletResponse;


import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;

public class StudentDatastore {
	private DatastoreService datastoreService;
	
	public StudentDatastore() {
	 datastoreService = DatastoreServiceFactory.getDatastoreService();
	}
	
	
	 public void createStudent(int id, String name, int age) throws EntityNotFoundException {
		 
//		 System.out.println(id +""+name+""+age);
//	        KeyFactory keyFactory = datastoreService.
//	        Key studentKey = KeyFactory.createKey("Student", id);
//	        Key studentKey = new Entity("student",id);
		 Transaction transaction=datastoreService.beginTransaction();
	       try {
	        Entity studentEntity = new Entity("student",id);
//	    	   Entity studentEntity=datastoreService.get(transaction, studentKey);
	        studentEntity.setProperty("id", id);
	        studentEntity.setProperty("name", name);
	        studentEntity.setProperty("age", age);

	        datastoreService.put(studentEntity);
	        transaction.commit();
	       }
	       finally {
	    	   if(transaction.isActive())
	    	   transaction.rollback();
	       }
	        
	    }
	 public Student getStudentById(int id) throws EntityNotFoundException {
		 Key studentKey = KeyFactory.createKey("Student", id);
		 
//	        Key key = datastoreService.newKeyFactory().setKind("Student").newKey(id);
		 Entity entity = datastoreService.get(studentKey);

	        if (entity != null) {
	        	
	        	int id1=(int) entity.getProperty("id");
	            String name = (String) entity.getProperty("name");
	            int age = (int) entity.getProperty("age");

	            return new Student(id1, name, age);
	        } else {
	            return null;
	        }
	    }
	 
	 public List<Student> getAllStudents() {
		 
	        Query query = new Query("student");
	        PreparedQuery  preparedQueryObj=datastoreService.prepare(query);
	        List<Student> students = new ArrayList<>();
 
//	        QueryResultIterator<Entity> results = datastoreService.run(query);
	        for(Entity stu:preparedQueryObj.asIterable()) {
	        	int id = (int) stu.getProperty("id");
	            String name = stu.getProperty("name").toString();
	            int age = (int) stu.getProperty("age");
	            students.add(new Student(id, name, age));
	        }
	        
	        
	        
	        return students;
	    }

	 
	 
	 public void updateStudent(int id, String name, int age, HttpServletResponse res) throws IOException {
		 
		
//	        Key studentKey = keyFactory.newKey(id);

	        Transaction transaction = datastoreService.beginTransaction();

	        try {
	            Entity studentEntity = new Entity("Student",id);

	            
	                studentEntity.setProperty("name", name);
	                 studentEntity.setProperty("age", age);
	                 
	                 datastoreService.put(studentEntity);
	                transaction.commit();
	                
//	                res.setStatus(HttpServletResponse.SC_OK);
//	                res.setContentType("text/plain");
//	                res.getWriter().write("Student updated successfully.");
	           
	            
	         
//	            	 res.setStatus(HttpServletResponse.SC_NOT_FOUND);
//	                 res.setContentType("text/plain");
//	                 res.getWriter().write("Student with ID " + id + " not found.");
	        
	        } finally {
	            if (transaction.isActive()) {
	                transaction.rollback();
	            }
	        }
	    }
	 
	 
	 public void deleteStudent(int id,HttpServletResponse res) throws IOException {
	       
	        Key studentKey = KeyFactory.createKey("Student", id);
	       
	      
			try {
				  Entity    studentEntity = datastoreService.get(studentKey);
				if (studentEntity == null) {

					res.setContentType("text/plain");
					res.sendError(HttpServletResponse.SC_NOT_FOUND, "Student not found");
//					res.getWriter().println("Entity not found");
					return;
				}
				
			} catch (EntityNotFoundException e) {
				
				e.printStackTrace();
			}
	        

	        datastoreService.delete(studentKey);
//	        res.setContentType("text/plain");
//			res.getWriter().println("Entity deleted successfully\"");

			
	    }
	

	 
	 
	 
	 
}
