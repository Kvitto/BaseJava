package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.Storage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        Storage db = Config.get().getStorage();
        List<Resume> resumes = db.getAllSorted();

        PrintWriter writer = response.getWriter();
        StringBuilder sb = new StringBuilder();

        sb.append("<html><head><title>Resume Servlet</title></head>");
        sb.append("<body>");
        sb.append("<h1>Resume Servlet</h1>");
        sb.append("<table border=1>");
        resumes.forEach(resume -> {
            sb.append("<tr>")
                    .append("<td>").append(resume.getUuid()).append("</td>")
                    .append("<td>").append(resume.getFullName()).append("</td>")
                    .append("</tr>");
        });
        sb.append("</table>");
        sb.append("</body></html>");

        writer.append(sb.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
