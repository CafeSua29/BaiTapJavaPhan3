package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeService {
    private static Connection conn = null;
    private static String sql;

    public static void spud_dept_emp_changeDepartment(int empNo, String newDeptNo, String newTitle) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            sql = "{CALL spud_dept_emp_changeDepartment(?, ?, ?)}";
            conn = DataSourceProvider.getDataSource().getConnection();

            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sql);
            ps.setInt(1, empNo);
            ps.setString(2, newDeptNo);
            ps.setString(3, newTitle);

            rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String fullName = rs.getString("full_name");
                String gender = rs.getString("gender");
                String title = rs.getString("title");
                String deptName = rs.getString("dept_name");

                System.out.println("ID: " + id);
                System.out.println("Full Name: " + fullName);
                System.out.println("Gender: " + gender);
                System.out.println("Title: " + title);
                System.out.println("Department Name: " + deptName);
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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void thangChuc1(Connection connection) throws SQLException {
        PreparedStatement ps1 = null;

        String updateOldTitle = "update titles set to_date = now() where emp_no = 10002 and to_date = now()";

        ps1 = connection.prepareStatement(updateOldTitle);
        ps1.executeUpdate();
    }

    public static void thangChuc2(Connection connection) throws SQLException {
        PreparedStatement ps2 = null;

        String insertNewTitle = "insert into titles values (10002, 'Senior Staff', now(), '9999-01-01')";

        ps2 = connection.prepareStatement(insertNewTitle);
        ps2.executeUpdate();
    }

    public static void thangChuc3() {
        try {
            conn = DataSourceProvider.getDataSource().getConnection();

            conn.setAutoCommit(false);

            thangChuc1(conn);
            thangChuc2(conn);

            conn.commit();

            System.out.println("Transaction committed successfully.");

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
    }

    public static void thangChuc() {
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;

        String updateOldTitle = "update titles set to_date = now() where emp_no = 10002 and to_date = now()";
        String insertNewTitle = "insert into titles values (10002, 'Senior Staff', now(), '9999-01-01')";

        try {
            conn = DataSourceProvider.getDataSource().getConnection();

            conn.setAutoCommit(false);

            ps1 = conn.prepareStatement(updateOldTitle);
            ps1.executeUpdate();

            ps2 = conn.prepareStatement(insertNewTitle);
            ps2.executeUpdate();

            conn.commit();

            System.out.println("Transaction committed successfully.");

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
                if (ps1 != null) ps1.close();
                if (ps2 != null) ps2.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
