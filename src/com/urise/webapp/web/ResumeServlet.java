package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.*;
import com.urise.webapp.storage.Storage;
import com.urise.webapp.util.DateUtil;
import com.urise.webapp.util.HtmlUtil;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
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
                String[] values = request.getParameterValues(type.name());
                if (value != null && !value.trim().isEmpty()) {
                    switch (type) {
                        case PERSONAL, OBJECTIVE ->
                                r.addSection(type, new TextSection(value.replaceAll("[\r\n]+", " ")));
                        case ACHIEVEMENT, QUALIFICATION ->
                                r.addSection(type, new ListSection(List.of(value.split("[\r\n]+"))));
                        case EXPERIENCE, EDUCATION -> {
                            List<Company> companies = new ArrayList<>();
                            String[] urls = request.getParameterValues(type.name() + "url");
                            for (int i = 0; i < values.length; i++) {
                                String name = values[i];
                                if (!HtmlUtil.isEmpty(name)) {
                                    List<Company.Position> positions = new ArrayList<>();
                                    String pfx = type.name() + i;
                                    String[] startDates = request.getParameterValues(pfx + "startDate");
                                    String[] endDates = request.getParameterValues(pfx + "endDate");
                                    String[] titles = request.getParameterValues(pfx + "title");
                                    String[] descriptions = request.getParameterValues(pfx + "description");
                                    for (int j = 0; j < titles.length; j++) {
                                        if (!HtmlUtil.isEmpty(titles[j])) {
                                            positions.add(new Company.Position(DateUtil.parse(startDates[j]), DateUtil.parse(endDates[j]), titles[j], descriptions[j]));
                                        }
                                    }
                                    companies.add(new Company(name, urls[i], positions));
                                }
                            }
                            r.addSection(type, new CompanySection(companies));
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
                r = storage.get(uuid);
                break;
            case "edit":
                r = storage.get(uuid);
                for (SectionType type : SectionType.values()) {
                    Section section = r.getSection(type);
                    switch (type) {
                        case PERSONAL, OBJECTIVE -> {
                            if (section == null) {
                                section = TextSection.EMPTY;
                                r.addSection(type, section);
                            }
                        }
                        case ACHIEVEMENT, QUALIFICATION -> {
                            if (section == null) {
                                section = ListSection.EMPTY;
                                r.addSection(type, section);
                            }
                        }
                        case EXPERIENCE, EDUCATION -> {
                            CompanySection orgSection = (CompanySection) section;
                            List<Company> emptyFirstOrganizations = new ArrayList<>();
                            emptyFirstOrganizations.add(Company.EMPTY);
                            if (orgSection != null) {
                                for (Company org : orgSection.getCompanies()) {
                                    List<Company.Position> emptyFirstPositions = new ArrayList<>();
                                    emptyFirstPositions.add(Company.Position.EMPTY);
                                    emptyFirstPositions.addAll(org.getPositions());
                                    emptyFirstOrganizations.add(new Company(org.getWebsite(), emptyFirstPositions));
                                }
                            }
                            section = new CompanySection(emptyFirstOrganizations);
                        }
                    }


                }
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