package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.repackaged.com.google.datastore.v1.client.Datastore;
import com.google.appengine.repackaged.com.google.datastore.v1.client.DatastoreOptions;
import com.google.appengine.repackaged.com.google.gson.JsonIOException;
import com.google.appengine.repackaged.com.google.gson.JsonSyntaxException;
import com.google.cloud.datastore.DatastoreOptions.DefaultDatastoreFactory;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.QueryResults;
import com.google.gson.Gson;

import model.Student;
import model.StudentDatastore;



public class StudentServlet extends HttpServlet {
	
	
	
	public 	StudentDatastore studentDatastore=new StudentDatastore();
	

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		
		String urlPath=req.getPathInfo();
		String[] pathParts=urlPath.split("/");
		
		if(pathParts.length >=2) {
			
			String idStr=pathParts[1];
			int id=Integer.parseInt(idStr);
			
			Student student = studentDatastore.getStudentById(id);
			if (student != null) {
                Gson gson = new Gson();
                String responseData = gson.toJson(student);

                resp.setContentType("application/json");
                resp.getWriter().write(responseData);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Student not found");
            }
        } else {
            // If no ID is provided, retrieve all students
            List<Student> students = studentDatastore.getAllStudents();

            Gson gson = new Gson();
            String responseData = gson.toJson(students);

            resp.setContentType("application/json");
            resp.getWriter().write(responseData);
        }
		}


	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BufferedReader read = req.getReader();
		StringBuilder requestBody = new StringBuilder();
		String line;
		while ((line = read.readLine()) != null) {
			requestBody.append(line);
		}
		read.close();
		String userData = requestBody.toString();
//	    Builder ud= new Builder();
//	    ud.append(userData);
		Gson gson = new Gson();
		Student newStudent = gson.fromJson(userData, Student.class);
		try {
		String name = newStudent.getName();
		int age = newStudent.getAge();
		int id=newStudent.getId();
		studentDatastore.createStudent(id, name, age);
		
		String successMessage = "Successfully created student: id=" + id + ", name=" + name + ", age=" + age;
            
		//this is response status and content type
		 resp.setStatus(HttpServletResponse.SC_CREATED);
         resp.setContentType("text/plain");
         
         //response of success message
         resp.getWriter().println(successMessage);
		
		
		}
		catch(Exception e) {
			
			// handle the response staus error
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("text/plain");
            resp.getWriter().println("Error occurred: " + e.getMessage());
		}
		
		
	
		
		
		
		

	 


//	    Datastore datastore = new DefaultDatastoreFactory().create(DatastoreOptions.getDefaultInstance());
//		DatastoreService datastore1 = DatastoreServiceFactory.getDatastoreService();

//	    System.out.println(datastore);
//	    KeyFactory keyFactory = datastore.g

//	    KeyFactory keyFactory = datastore.newKeyFactory().setKind("Student");
//	    Key studentKey = datastore.allocateId(keyFactory.newKey());
//	    Key key = keyFactory.newKey("id");
//	    


//	   

	}

	@Override
	public void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int id = Integer.parseInt(req.getPathInfo().substring(1));

		BufferedReader read = req.getReader();
		StringBuilder requestBody = new StringBuilder();
		String line;
		while ((line = read.readLine()) != null) {
			requestBody.append(line);
		}
		read.close();
		String userData = requestBody.toString();
		Gson gson = new Gson();
		Student newStudent = gson.fromJson(userData, Student.class);

		String name = newStudent.getName();
		int age = newStudent.getAge();

	   
       studentDatastore.createStudent(id, name, age);
       resp.setStatus(HttpServletResponse.SC_OK);
       
       resp.setStatus(HttpServletResponse.SC_CREATED);
       resp.setContentType("text/plain");
       resp.getWriter().write("Student created successfully.");
	
	}

	@Override
	public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		int id = Integer.parseInt(req.getPathInfo().substring(1));

		studentDatastore.deleteStudent(id, resp);
		

		
		
	}

}