package controller;

import com.google.gson.Gson;
import model.Employee;
import model.EntityWithSalary;
import model.Gender;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.servlet.MultipartConfigElement;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

public class EmployeeService {
    private static Connection conn = null;
    private static String query;

    public static void start() {
        get("/employees", getEmployees);
        post("/employee", addEmployee);
        put("/dept_emp", changeDept);
        get("/salaries", getSalaries);
    }

    private static Route getEmployees = (Request request, Response response) -> {
        response.type("application/json");

        PreparedStatement ps = null;
        ResultSet rs = null;

        String deptNo = request.queryParams("dept_no");
        String title = request.queryParams("title");
        String hireDateFrom = request.queryParams("hire_date_from");
        String salary = request.queryParams("salary");

        List<Employee> employees = new ArrayList<>();

        query = "select employees.emp_no as id, CONCAT(first_name, ' ', last_name) AS full_name, gender, hire_date, title, dept_name, salary " +
                "from employees " +
                "join dept_emp on employees.emp_no = dept_emp.emp_no " +
                "join departments on dept_emp.dept_no = departments.dept_no " +
                "join titles on employees.emp_no = titles.emp_no " +
                "join salaries on employees.emp_no = salaries.emp_no " +
                "where salaries.to_date = '9999-01-01' AND dept_emp.to_date = '9999-01-01' AND titles.to_date = '9999-01-01'";

        if (hireDateFrom != null) {
            query += " AND employees.hire_date > ?";
        }

        if (salary != null) {
            query += " AND salaries.salary > ?";
        }

        if (deptNo != null) {
            query += " AND dept_emp.dept_no = ?";
        }

        if (title != null) {
            query += " AND titles.title = ?";
        }

        try {
            conn = DataSourceProvider.getDataSource().getConnection();

            ps = conn.prepareStatement(query);

            int index = 1;

            if (hireDateFrom != null) {
                ps.setDate(index++, Date.valueOf(hireDateFrom));
            }

            if (salary != null) {
                ps.setInt(index++, Integer.parseInt(salary));
            }

            if (deptNo != null) {
                ps.setString(index++, deptNo);
            }

            if (title != null) {
                ps.setString(index, title);
            }

            rs = ps.executeQuery();

            while (rs.next()) {
                Employee employee = new Employee();

                employee.setEmpNo(rs.getInt("id"));
                employee.setFullName(rs.getString("full_name"));
                employee.setGender(Gender.valueOf(rs.getString("gender")));
                employee.setTitle(rs.getString("title"));
                employee.setDeptName(rs.getString("dept_name"));
                employee.setSalary(rs.getInt("salary"));
                employee.setHireDate(rs.getDate("hire_date"));

                employees.add(employee);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        return new Gson().toJson(employees);
    };

    private static Route addEmployee = (Request request, Response response) -> {
        PreparedStatement ps = null;
        ResultSet rs = null;

        request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

        String empNo = request.queryParams("emp_no");
        String birthDate = request.queryParams("birth_date");
        String fullName = request.queryParams("full_name");
        String gender = request.queryParams("gender");
        String hireDate = request.queryParams("hire_date");
        String title = request.queryParams("title");
        String salary = request.queryParams("salary");

        String[] s = fullName.split(" ");
        String firstName = s[0];
        String lastName = s[1];

        query = "insert into employees"
                    + " values (?, ?, ?, ?, ?, ?)";

        try {
            conn = DataSourceProvider.getDataSource().getConnection();

            conn.setAutoCommit(false);

            ps = conn.prepareStatement(query);
            ps.setInt(1, Integer.parseInt(empNo));
            ps.setDate(2, Date.valueOf(birthDate));
            ps.setString(3, firstName);
            ps.setString(4, lastName);
            ps.setString(5, gender);
            ps.setDate(6, Date.valueOf(hireDate));

            ps.execute();

            try {
                query = "insert into titles"
                            + " values (?, ?, ?, ?)";

                ps = conn.prepareStatement(query);
                ps.setInt(1, Integer.parseInt(empNo));
                ps.setString(2, title);
                ps.setDate(3, Date.valueOf(hireDate));
                ps.setDate(4, Date.valueOf("9999-01-01"));

                ps.execute();

                try {
                    query = "insert into salaries"
                            + " values (?, ?, ?, ?)";

                    ps = conn.prepareStatement(query);
                    ps.setInt(1, Integer.parseInt(empNo));
                    ps.setInt(2, Integer.parseInt(salary));
                    ps.setDate(3, Date.valueOf(hireDate));
                    ps.setDate(4, Date.valueOf("9999-01-01"));

                    ps.execute();

                    conn.commit();
                } catch (SQLException e) {
                    if (conn != null) {
                        try {
                            conn.rollback();

                            System.out.println("Transaction rolled back due to error: " + e.getMessage());
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }

                    e.printStackTrace();
                }
            } catch (SQLException e) {
                if (conn != null) {
                    try {
                        conn.rollback();

                        System.out.println("Transaction rolled back due to error: " + e.getMessage());
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }

                e.printStackTrace();
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();

                    System.out.println("Transaction rolled back due to error: " + e.getMessage());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        return "Add Successfully";
    };

    private static Route changeDept = (Request request, Response response) -> {
        PreparedStatement ps = null;
        ResultSet rs = null;

        request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

        String empNo = request.queryParams("emp_no");
        String deptNo = request.queryParams("dept_no");
        LocalDate now = LocalDate.now();

        query = "update dept_emp"
                    + " set to_date = ?"
                    + " where emp_no = ? and to_date = '9999-01-01'";

        try {
            conn = DataSourceProvider.getDataSource().getConnection();

            conn.setAutoCommit(false);

            ps = conn.prepareStatement(query);
            ps.setDate(1, Date.valueOf(now));
            ps.setInt(2, Integer.parseInt(empNo));

            ps.execute();

            try {
                query = "insert into dept_emp"
                        + " values (?, ?, ?, ?)";

                ps = conn.prepareStatement(query);
                ps.setInt(1, Integer.parseInt(empNo));
                ps.setString(2, deptNo);
                ps.setDate(3, Date.valueOf(now));
                ps.setDate(4, Date.valueOf("9999-01-01"));

                ps.execute();

                conn.commit();
            } catch (SQLException e) {
                if (conn != null) {
                    try {
                        conn.rollback();

                        System.out.println("Transaction rolled back due to error: " + e.getMessage());
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }

                e.printStackTrace();
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();

                    System.out.println("Transaction rolled back due to error: " + e.getMessage());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        return "Change Successfully";
    };

    private static Route getSalaries = (Request request, Response response) -> {
        response.type("application/json");

        PreparedStatement ps = null;
        ResultSet rs = null;

        request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

        String time = request.queryParams("time");

        if (time == null || time.isEmpty() || time.isBlank())
            return "time parameter is required";

        if (!time.equals("month"))
            if (!time.equals("year"))
                return "Invalid time parameter";

        List<EntityWithSalary> entityWithSalaries = new ArrayList<>();

        // tung nguoi

        query = "select employees.emp_no, CONCAT(first_name, ' ', last_name) AS full_name, salary from employees" +
                " join salaries on employees.emp_no = salaries.emp_no" +
                " where to_date = '9999-01-01'";

        try {
            conn = DataSourceProvider.getDataSource().getConnection();

            ps = conn.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()) {
                EntityWithSalary entityWithSalary = new EntityWithSalary();

                entityWithSalary.setId(String.valueOf(rs.getInt("emp_no")));
                entityWithSalary.setName(rs.getString("full_name"));

                if (time.equals("year"))
                    entityWithSalary.setSalary(rs.getDouble("salary") * 12);
                else
                    entityWithSalary.setSalary(rs.getDouble("salary"));

                entityWithSalaries.add(entityWithSalary);
            }

            // tung phong

            query = "select departments.dept_no, dept_name, sum(salary) as sumsalary from departments " +
                    "join dept_emp on departments.dept_no = dept_emp.dept_no " +
                    "join salaries on dept_emp.emp_no = salaries.emp_no " +
                    "where salaries.to_date = '9999-01-01' and dept_emp.to_date = '9999-01-01' " +
                    "group by departments.dept_no, dept_name";

            try {
                conn = DataSourceProvider.getDataSource().getConnection();

                ps = conn.prepareStatement(query);

                rs = ps.executeQuery();

                while (rs.next()) {
                    EntityWithSalary entityWithSalary = new EntityWithSalary();

                    entityWithSalary.setId(rs.getString("dept_no"));
                    entityWithSalary.setName(rs.getString("dept_name"));

                    if (time.equals("year"))
                        entityWithSalary.setSalary(rs.getDouble("sumsalary") * 12);
                    else
                        entityWithSalary.setSalary(rs.getDouble("sumsalary"));

                    entityWithSalaries.add(entityWithSalary);
                }

                // ca cong ty

                query = "select sum(salary) as luongcacty from employees " +
                        "join salaries on employees.emp_no = salaries.emp_no " +
                        "where to_date = '9999-01-01';";

                try {
                    conn = DataSourceProvider.getDataSource().getConnection();

                    ps = conn.prepareStatement(query);

                    rs = ps.executeQuery();

                    while (rs.next()) {
                        EntityWithSalary entityWithSalary = new EntityWithSalary();

                        entityWithSalary.setName("Ca cong ty");

                        if (time.equals("year"))
                            entityWithSalary.setSalary(rs.getDouble("luongcacty") * 12);
                        else
                            entityWithSalary.setSalary(rs.getDouble("luongcacty"));

                        entityWithSalaries.add(entityWithSalary);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        return new Gson().toJson(entityWithSalaries);
    };
}
