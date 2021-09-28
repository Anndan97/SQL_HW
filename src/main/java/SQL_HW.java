import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SQL_HW {
    private static final String DB_CONNECTION = "hw_sql";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "qwerty";
    private static final String DB_DRIVER = "org.postgresql.Driver";

    public String getRandomSex() {
        Random random = new Random();
        boolean isMale = random.nextBoolean();
        return isMale ? "M" : "F";
    }

    public double idGroup() {
        double id_group = 1 + Math.random() * 2;
        return id_group;
    }


    //CREATE TABLE
/*
1) Создать таблицу Student
Колонки id, fio, sex, id_group

2) Создать таблицу Group
Колонки id, name, id_curator

3) Создать таблицу Curator
Колонки id, fio

 */
    private static final String CreateTableSQL_1 = "CREATE TABLE Student (\n" +
            "      id INT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,\n" +
            "      fio VARCHAR(100), \n" +
            "      sex VARCHAR(100), \n" +
            "      id_group INT NOT NULL,\n" +
            "      FOREIGN KEY (id_group) REFERENCES Groups (id)\n" +
            "   );";

    private static final String CreateTableSQL_2 = "CREATE TABLE Groups (\n" +
            "      id INT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,\n" +
            "      group_name VARCHAR(100), \n" +
            "      id_curator INT NOT NULL GENERATED ALWAYS AS IDENTITY,\n" +
            "      FOREIGN KEY (id_curator) REFERENCES Curator (id) \n" +
            "   );";

    private static final String CreateTableSQL_3 = "CREATE TABLE Curator (\n" +
            "      id INT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,\n" +
            "      fio VARCHAR(100) \n" +
            "   );";

    //METHOD CREATE TABLE
    private static void createUserTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(CreateTableSQL_3);
            statement.execute(CreateTableSQL_2);
            statement.execute(CreateTableSQL_1);
            System.out.println("Table is created!");
        }
    }

    //INSERT TABLE
    // 4) Заполнить таблицы данными(15 студентов, 3 группы, 4 куратора)
    private static final String insertTableCurator = "INSERT INTO Curator"
            + "(fio) " + "VALUES(?)";

    private static final String insertTableGroups = "INSERT INTO Groups"
            + "(group_name) " + "VALUES(?)";

    private static final String insertTableStudent = "INSERT INTO Student"
            + "(fio, sex, id_group) " + "VALUES(?,?,?)";

    // METHOD INSERT CURATOR
    // 4) Заполнить таблицы данными(15 студентов, 3 группы, 4 куратора)
    public static void insertUserTableCurator(Connection connection, String name) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(insertTableCurator)) {
            statement.setString(1, name);
            int row = statement.executeUpdate();
        }

        System.out.println("Table insert!");
    }

    // METHOD INSERT GROUPS
    // 4) Заполнить таблицы данными(15 студентов, 3 группы, 4 куратора)
    public static void insertUserTableGroups(Connection connection, String name) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(insertTableGroups)) {
            statement.setString(1, name);
            int row = statement.executeUpdate();
        }
    }

    // METHOD INSERT STUDENT
    // 4) Заполнить таблицы данными(15 студентов, 3 группы, 4 куратора)
    public static void insertUserTableStudent(Connection connection, String name, String sex, double id_group) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(insertTableStudent)) {
            statement.setString(1, name);
            statement.setString(2, sex);
            statement.setDouble(3, id_group);
            int row = statement.executeUpdate();
        }

        System.out.println("Table insert!");
    }

    // 7) Вывести студенток
    public static final String selectStudent = "select * from student\n" +
            "where sex = 'F';";

//    6) Вывести на экран количество студентов
    public static final String selectAmount = "select count(*) from student";

    public static final String allStudent = "select s.id, s.fio, s.sex, s.id_group, g.group_name, c.fio AS fio_curator\n" +
            "from student s, groups g\n" +
            "left join curator c ON c.id = g.id_curator";

    //  8) Обновить данные по группе сменив куратора
    //  9) Вывести список групп с их кураторами
    public static final String updatecurator = "update curator set fio = 'Kate Petrova' where fio = 'Ivan Petrov'";

    // 10) Используя вложенные запросы вывести на экран студентов из определенной группы(поиск по имени группы)
    public static final String subquery = "select fio from student\n" +
            "where id_group  = (select id from groups where group_name = 'A')";

    // 7) Вывести студенток
    public static void printStudent(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectStudent)) {
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String fio = resultSet.getString(2);
                String sex = resultSet.getString(3);
                String id_group = resultSet.getString(4);

                String row = String.format("ID: %s, FIO: %s, SEX: %s, ID_GROUP: %s", id, fio, sex, id_group);
                System.out.println(row);
            }
        }
    }

    //    6) Вывести на экран количество студентов
    public static void printAmount(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectAmount)) {
            while (resultSet.next()) {
                int amount = resultSet.getInt(1);
                String row = String.format("Amount: %s", amount);
                System.out.println(row);
            }
        }
    }

    // 10) Используя вложенные запросы вывести на экран студентов из определенной группы(поиск по имени группы)
    public static void printFio(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(subquery)) {
            while (resultSet.next()) {
                String fio = resultSet.getString("fio");
                String row = String.format("FIO: %s", fio);
                System.out.println(row);
            }
        }
    }

    // 5) Вывести на экран информацию о всех студентах включая название группы и имя куратора
    // 9) Вывести список групп с их кураторами
    public static void printTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(allStudent)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String fio = resultSet.getString("fio");
                String sex = resultSet.getString("sex");
                int id_group = resultSet.getInt("id_group");
                String group_name = resultSet.getString("group_name");
                String fio_curator = resultSet.getString("fio_curator");
                String row = String.format("ID: %s, FIO: %s, SEX: %s, ID_GROUP: %s,"
                        + "GROUP_NAME: %s, FIO_CURATOR: %s", id, fio, sex, id_group, group_name, fio_curator);
                System.out.println(row);
            }
        }
    }


    public static void main(String[] argv) throws SQLException, ClassNotFoundException {
        SQL_HW sql = new SQL_HW();
        Class.forName("org.postgresql.Driver");
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/hw_sql", "postgres", "qwerty")) {

//            SQL_HW.createUserTable(connection);

//            List<String> curatorNames = Arrays.asList("Ivan Petrov", "Anna Ivanova", "Egor Popov", "Kirill Smirnov");
//            for (String name : curatorNames) {
//                insertUserTableCurator(connection, name);
//            }

//            List<String> nameGroups = Arrays.asList("A", "B", "C");
//            for (String name : nameGroups) {
//                insertUserTableGroups(connection, name);
//            }

//            List<String> nameStudent = Arrays.asList("Ann America", "Kate Ivanova", "Sasha Petrova", "Olya America" +
//                    "Maya Smirnova", "Sergey Kozlov", "Kirill Ivanov", "Maksim Petrov", "Mark Ovechkin"
//                    + "Oleg Apple", "Ivan Apple", "Vlad Ku", "Kira Ku", "Mike G", "Kris K");
//            //String sex = sql.getRandomSex();
//            //Double id_group = sql.idGroup();
//
//            for (String name : nameStudent) {
//                insertUserTableStudent(connection, name, sql.getRandomSex(), sql.idGroup());
//            }

//            SQL_HW.printStudent(connection);
//            SQL_HW.printAmount(connection);
//            SQL_HW.printTable(connection);

//  8) Обновить данные по группе сменив куратора
//  9) Вывести список групп с их кураторами
//            String stringUpdate = sql.updatecurator;
//            SQL_HW.printTable(connection);

//  10) Используя вложенные запросы вывести на экран студентов из определенной группы(поиск по имени группы)
//            SQL_HW.printFio(connection);
            
        }
    }
}


