package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.*;
import com.urise.webapp.storage.Storage;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    private Storage storage; // = Config.get().getStorage();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getStorage();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        if (!fullName.trim().isEmpty()) {
            Resume r;
            try {
                r = storage.get(uuid);
                r.setFullName(fullName);
            } catch (NotExistStorageException e) {
                r = new Resume(uuid, fullName);
                storage.save(r);
            }
            for (ContactType type : ContactType.values()) {
                String value = request.getParameter(type.name());
                if (value != null && !value.trim().isEmpty()) {
                    r.addContact(type, value);
                } else {
                    r.getContacts().remove(type);
                }
            }
            for (SectionType type : SectionType.values()) {
                String value = request.getParameter(type.name());
                if (value != null && !value.trim().isEmpty()) {
                    switch (type) {
                        case PERSONAL, OBJECTIVE -> r.addSections(type, new TextSection(value.replaceAll("[\r\n]+", " ")));
                        case ACHIEVEMENT, QUALIFICATION ->
                                r.addSections(type, new ListSection(List.of(value.split("[\r\n]+"))));
                        case EXPERIENCE, EDUCATION -> {
                        }
                    }
                } else {
                    r.getSections().remove(type);
                }
            }
            storage.update(r);
        }
        response.sendRedirect("resume");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        Resume r;
        switch (action == null ? "all" : action) {
            case "all":
                request.setAttribute("resumes", storage.getAllSorted());
                request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
                return;
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "view":
            case "edit":
                r = storage.get(uuid);
                break;
            case "add":
                r = new Resume();
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", r);
        request.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
    }
}