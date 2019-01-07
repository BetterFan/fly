package com.wxc.hwt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;

@WebServlet("/transferServlet")
public class transferServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String myID = (String) session.getAttribute("IDcard");//我的卡号
        String zid = request.getParameter("IDcard");//需要转账的卡号
        double zcash = Double.parseDouble(request.getParameter("cash"));//需要转帐的金额
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/bank";
            String username = "root";
            String password = "2016013924";
            Connection conn = DriverManager.getConnection(url, username, password);
            String sql = "select * from balance where IDcard=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, zid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                sql = "select endCash from balance where IDcard=?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, myID);
                rs = ps.executeQuery();
                rs.next();
                Double myCash = rs.getDouble("endCash");
                ps.setString(1, zid);
                rs = ps.executeQuery();
                rs.next();
                Double yourCash = rs.getDouble("endCash");
                System.out.print(myCash+"  "+yourCash);
                if (myCash >= zcash) {
                    sql = "update balance set endCash=? where IDcard=?";
                    ps = conn.prepareStatement(sql);
                    ps.setDouble(1, myCash - zcash);
                    ps.setString(2, myID);
                    ps.executeUpdate();
                    ps.setDouble(1, yourCash + zcash);
                    ps.setString(2, zid);
                    ps.executeUpdate();
                }
            }
        } catch (Exception e) {
        }

        response.sendRedirect("/SearchServlet");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
