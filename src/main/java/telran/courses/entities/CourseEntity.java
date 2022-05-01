package telran.courses.entities;
import java.time.LocalDate;

import javax.persistence.*;

import telran.courses.dto.Course;
@Entity
@Table(name = "courses")
public class CourseEntity {
	@Id
	int id;
	String lecturer;
	String name;
	int hours;
	int cost;
	@Column(name = "opening_date")
	LocalDate openingDate;
	public static CourseEntity build(Course course) {
		CourseEntity courseEntity = new CourseEntity();
		courseEntity.id = course.id;
		return fillEntity(course, courseEntity);
	}
	public static CourseEntity fillEntity(Course course, CourseEntity courseEntity) {
		courseEntity.hours = course.hours;
		courseEntity.cost = course.cost;
		courseEntity.lecturer = course.lecturer;
		courseEntity.name = course.course;
		courseEntity.openingDate = LocalDate.parse(course.openingDate);
		return courseEntity;
	}
	public Course getCourseDto() {
		Course course = new Course();
		course.id = id;
		course.course = name;
		course.lecturer = lecturer;
		course.hours = hours;
		course.cost = cost;
		course.openingDate = openingDate.toString();
		return course;
	}
	public int getId() {
		return id;
	}
	public String getLecturer() {
		return lecturer;
	}
	public String getName() {
		return name;
	}
	public int getHours() {
		return hours;
	}
	public int getCost() {
		return cost;
	}
	public LocalDate getOpeningDate() {
		return openingDate;
	}
	

}
