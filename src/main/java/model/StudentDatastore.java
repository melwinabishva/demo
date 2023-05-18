package model;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;

import com.google.appengine.api.search.Query;

import com.google.cloud.datastore.FullEntity;
import com.google.appengine.api.datastore.*;
//import com.google.cloud.datastore.Entity;
//import com.google.cloud.datastore.*;
public class StudentDatastore {

	private DatastoreService datastoreService;
	
	public StudentDatastore() {
		datastoreService=DatastoreServiceFactory.getDatastoreService();
	}
	
	 public void createStudent(int id, String name, int age) {
		 
//		 System.out.println(id +""+name+""+age);
//	        KeyFactory keyFactory = datastoreService.
	        Key studentKey = KeyFactory.createKey("Student", id);
	       
	        Entity studentEntity = new Entity(studentKey);
	        studentEntity.setProperty("name", name);
	        studentEntity.setProperty("age", age);

	        datastoreService.put(studentEntity);
	    }
	 public Student getStudentById(int id) {
		 Key studentKey = KeyFactory.createKey("Student", id);
		 
//	        Key key = datastoreService.newKeyFactory().setKind("Student").newKey(id);
	        Entity entity = datastoreService.get(studentKey);

	        if (entity != null) {
//	        	entity.getProperties("name")
	            String name = entity.getProperties(""));
	            int age = entity.getInt("age");

	            return new Student(id, name, age);
	        } else {
	            return null;
	        }
	    }
	 
	 public List<Student> getAllStudents() {
	        Query<Entity> query = Query.newEntityQueryBuilder().setKind("Student").build();

	        List<Student> students = new ArrayList<>();

	        QueryResultIterator<Entity> results = datastoreService.run(query);
	        while (results.hasNext()) {
	            Entity entity = results.next();
	            int id = entity.getLong("id");
	            String name = entity.getString("name");
	            int age = entity.getLong("age");

	            students.add(new Student(id, name, age)):
	        }

	        return students;
	    }
	 
	 
	 public void updateStudent(int id, String name, int age, HttpServletResponse res) {
		 
		 KeyFactory keyFactory = datastoreService.newKeyFactory().setKind("Student");
	        Key studentKey = keyFactory.newKey(id);

	        Transaction transaction = datastoreService.beginTransaction();

	        try {
	            Entity studentEntity = transaction.get(studentKey);

	            if (studentEntity != null) {
	                studentEntity = Entity.newBuilder(studentKey, studentEntity)
	                        .set("name", name)
	                        .set("age", age)
	                        .build();
	                transaction.update(studentEntity);
	                transaction.commit();
	                
	                res.setStatus(HttpServletResponse.SC_OK);
	                res.setContentType("text/plain");
	                res.getWriter().write("Student updated successfully.");
	            }
	            
	            else {
	            	 res.setStatus(HttpServletResponse.SC_NOT_FOUND);
	                 res.setContentType("text/plain");
	                 res.getWriter().write("Student with ID " + id + " not found.");
	            }
	        } finally {
	            if (transaction.isActive()) {
	                transaction.rollback();
	            }
	        }
	    }
	 
	 
	 public void deleteStudent(int id,HttpServletResponse res) {
	        KeyFactory keyFactory = datastoreService.newKeyFactory().setKind("Student");
	        Key studentKey = keyFactory.newKey(id);

	       
	        Entity studentEntity = datastoreService.get(studentKey);
	        if (studentEntity == null) {

				res.setContentType("text/plain");
				res.getWriter().println("Entity not found");
				return;
			}

	        datastoreService.delete(studentKey);
	        res.setContentType("text/plain");
			res.getWriter().println("Entity deleted successfully\"");

			
	    }
	

	 
	 
	 
	 
}
