import com.opencsv.CSVReader;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class Final {
    public static void main(String[] args) throws Exception {
        String csvPath = "src\\main\\resources\\Школы.csv";
        FileReader fileReader = new FileReader(csvPath);
        CSVReader reader = new CSVReader(fileReader);

        String[] headData = reader.readNext();
        HashMap<String, Byte> head = new HashMap<>();
        byte k = 0;
        for (String headColumn : headData) {
            head.put(headColumn, k++);
        }


        ArrayList<School> schools = new ArrayList<>();

        for (String[] dataCsv : reader.readAll()) {

            int id = Integer.parseInt(dataCsv[0]);
            String name = dataCsv[head.get("school")];
            String country = dataCsv[head.get("county")];
            int students = Integer.parseInt(dataCsv[head.get("students")]);
            double expenditure = Double.parseDouble(dataCsv[head.get("expenditure")]);
            double math = Double.parseDouble(dataCsv[head.get("math")]);

            School school = new School(id, name, country, students, expenditure, math);
            schools.add(school);
//            System.out.println(Arrays.toString(dataCsv));
//            System.out.println(school);
        }
        fileReader.close();

        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:SCHOOLS.db");
        Statement statement = connection.createStatement();

        statement.execute("DROP TABLE SCHOOL");
        statement.execute("CREATE TABLE SCHOOL (" +
                "ID INT, name VARCHAR, country VARCHAR,students INT, expenditure REAL, math REAL)");

        for (School s : schools) {
            String sq = "INSERT INTO SCHOOL " + "VALUES (" + s.id + ",'" + s.name + "','" + s.country + "'," + s.students + "," + s.expenditure + "," + s.math + ")";
            statement.executeUpdate(sq);
        }

        System.out.println("Постройте график по среднему количеству студентов, в 10 различных странах, взять на свой выбор.");
        ResultSet resultSet = statement.executeQuery("SELECT country,avg(students) as avg FROM SCHOOL GROUP by country;");
        for (int i = 0; i < 10; i++) {
            resultSet.next();
            System.out.println(resultSet.getString("country") + " " + resultSet.getString("avg"));
        }

        System.out.println("Выведите в консоль среднее количество расходов(expenditure) в Fresno, Contra Costa, El Dorado и Glenn, у которых расход больше 10.");
        resultSet = statement.executeQuery("SELECT country, avg(expenditure) as avg, count(expenditure) as countExp from SCHOOL where country in ('Fresno','Contra Costa','El Dorado','Glenn') GROUP by country ");

        while (resultSet.next()) {
            int countExp = Integer.parseInt(resultSet.getString("countExp"));

//            if (countExp>10)
            System.out.println(resultSet.getString("country") + " " + resultSet.getString("avg"));

        }

        System.out.println("Выведите в консоль учебное заведение, с количеством студентов равному от 5000 до 7500 и с 10000 до 11000, с самым высоким показателем по математике (math)");
        resultSet = statement.executeQuery("SELECT *,max(math) FROM SCHOOL where (students BETWEEN 5000 and 7500) or (students BETWEEN 10000 and 11000)");

        System.out.println(resultSet.getString("name"));

    }
}
