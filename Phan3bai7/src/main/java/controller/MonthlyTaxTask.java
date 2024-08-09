package controller;

import java.sql.*;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MonthlyTaxTask {
    private static Connection conn = null;
    private static String query;

    public static void calculateTax() {
        Timer timer = new Timer();

        Calendar date = Calendar.getInstance();
        date.set(Calendar.DAY_OF_MONTH, 25);
        date.set(Calendar.HOUR_OF_DAY, 23);
        date.set(Calendar.MINUTE, 59);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        if (date.getTime().before(new java.util.Date())) {
            date.add(Calendar.MONTH, 1);
        }

        long period = 1000L * 60 * 60 * 24 * 30;

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                calculateAndStoreIncomeTax();
            }
        }, date.getTime(), period);
    }

    public static void createTaxTableIfNotExists() {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DataSourceProvider.getDataSource().getConnection();

            conn.setAutoCommit(false);

            query = "LOCK TABLES employees WRITE";

            ps = conn.prepareStatement(query);
            ps.execute();

            query = "SHOW TABLES LIKE 'income_tax'";

            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();

            if (!rs.next()) {
                query = "CREATE TABLE income_tax (" +
                        "emp_no INT NOT NULL, " +
                        "tax INT NOT NULL, " +
                        "month INT NOT NULL, " +
                        "year INT NOT NULL, " +
                        "PRIMARY KEY (emp_no, month, year))";

                try {
                    ps = conn.prepareStatement(query);

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
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                query = "UNLOCK TABLES";

                ps = conn.prepareStatement(query);
                ps.execute();

                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    private static void calculateAndStoreIncomeTax() {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Calendar calendar = Calendar.getInstance();
            int month = calendar.get(Calendar.MONTH) + 1;
            int year = calendar.get(Calendar.YEAR);

            conn = DataSourceProvider.getDataSource().getConnection();

            conn.setAutoCommit(false);

            query = "select employees.emp_no, salary from employees " +
                    "join salaries on employees.emp_no = salaries.emp_no " +
                    "where salaries.to_date = '9999-01-01'";

            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                int emp_no = rs.getInt("emp_no");
                int salary = rs.getInt("salary");
                int tax = TaxCalculator.calculateTax(salary);

                query = "INSERT INTO income_tax (emp_no, tax, month, year) " +
                        "VALUES (?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE tax = ?";
                try {
                    ps = conn.prepareStatement(query);

                    ps.setInt(1, emp_no);
                    ps.setInt(2, tax);
                    ps.setInt(3, month);
                    ps.setInt(4, year);
                    ps.setInt(5, tax);

                    ps.executeUpdate();

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
    }
}
