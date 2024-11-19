package sdalabmid;

import java.util.ArrayList;
import java.util.List;

public class SDAlabMid {

    private final ArrayList<Object> filters;

    public SDAlabMid() {
        this.filters = new ArrayList<>();
    }


    interface Observer {
        void update(String message);
    }


    class AdmissionObserver implements Observer {
        private final String name;

        public AdmissionObserver(String name) {
            this.name = name;
        }

        public void update(String message) {
            System.out.println(name + " Received Notification: " + message);
        }
    }

    
    class AdmissionSubject {
        private final List<Observer> observers = new ArrayList<>();

        public void attach(Observer observer) {
            observers.add(observer);
        }

        public void notifyObservers(String message) {
            for (Observer observer : observers) {
                observer.update(message);
            }
        }
    }

 
    interface AdmissionFilter {
        boolean execute(Student student);
    }

    class EligibilityFilter implements AdmissionFilter {
        public boolean execute(Student student) {
            return student.getAge() >= 18 && student.getGrade() >= 70;
        }
    }

    class TestFilter implements AdmissionFilter {
        public boolean execute(Student student) {
            return student.getTestScore() >= 50;
        }
    }

    class InterviewFilter implements AdmissionFilter {
        public boolean execute(Student student) {
            return student.getInterviewScore() >= 60;
        }
    }

    class AdmissionPipeline {
        private final List<AdmissionFilter> filters = new ArrayList<>();

        public void addFilter(AdmissionFilter filter) {
            filters.add(filter);
        }

        public boolean process(Student student) {
            for (AdmissionFilter filter : filters) {
                if (!filter.execute(student)) {
                    return false;
                }
            }
            return true;
        }
    }

    class Student {
        private final String name;
        private final int age;
        private final double grade;
        private double testScore;
        private double interviewScore;

        public Student(String name, int age, double grade) {
            this.name = name;
            this.age = age;
            this.grade = grade;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public double getGrade() {
            return grade;
        }

        public double getTestScore() {
            return testScore;
        }

        public void setTestScore(double testScore) {
            this.testScore = testScore;
        }

        public double getInterviewScore() {
            return interviewScore;
        }

        public void setInterviewScore(double interviewScore) {
            this.interviewScore = interviewScore;
        }
    }

    class AdmissionService {
        private final AdmissionPipeline pipeline;
        private final AdmissionSubject subject;

        public AdmissionService() {
            pipeline = new AdmissionPipeline();
            subject = new AdmissionSubject();

            pipeline.addFilter(new EligibilityFilter());
            pipeline.addFilter(new TestFilter());
            pipeline.addFilter(new InterviewFilter());
        }

        public void registerObserver(Observer observer) {
            subject.attach(observer);
        }

        public boolean admitStudent(Student student) {
            subject.notifyObservers("Processing student: " + student.getName());

            if (pipeline.process(student)) {
                subject.notifyObservers("Student " + student.getName() + " admitted successfully.");
                return true;
            } else {
                subject.notifyObservers("Student " + student.getName() + " did not qualify.");
                return false;
            }
        }
    }

    public static void main(String[] args) {
        SDAlabMid app = new SDAlabMid();
        AdmissionService service = app.new AdmissionService();

        service.registerObserver(app.new AdmissionObserver("Observer 1"));
        service.registerObserver(app.new AdmissionObserver("Observer 2"));

        Student student1 = app.new Student("Alice", 19, 85);
        student1.setTestScore(65);
        student1.setInterviewScore(70);

        Student student2 = app.new Student("Bob", 17, 90);
        student2.setTestScore(80);
        student2.setInterviewScore(50);

        service.admitStudent(student1); 
        service.admitStudent(student2); 
    }
}

