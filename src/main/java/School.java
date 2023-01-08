public class School {
//    school,county, students,expenditure,math
    public int id;
    public String name;
    public String country;
    public int students;
    public double expenditure;
    public double math;

    public School(int id, String name, String country, int students, double expenditure, double math) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.students = students;
        this.expenditure = expenditure;
        this.math = math;
    }

    @Override
    public String toString() {
        return "School{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", students=" + students +
                ", expenditure=" + expenditure +
                ", math=" + math +
                '}';
    }
}
