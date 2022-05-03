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
		courseEntity.fillEntity(course);
		return courseEntity;
	}
	public void fillEntity(Course course) {
		hours = course.hours;
		cost = course.cost;
		lecturer = course.lecturer;
		name = course.course;
		openingDate = LocalDate.parse(course.openingDate.substring(0, 10));
		
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
	@Override
	public String toString() {
		return "CourseEntity [id=" + id + ", lecturer=" + lecturer + ", name=" + name + ", hours=" + hours + ", cost="
				+ cost + ", openingDate=" + openingDate + "]";
	}
	

}
