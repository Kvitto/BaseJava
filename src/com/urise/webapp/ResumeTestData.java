package com.urise.webapp;

import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.model.Section;
import com.urise.webapp.model.SectionType;

import java.util.Map;

public class ResumeTestData {
    public static Resume createResume(String uuid, String fullName) {
        Resume myResume = new Resume(uuid, fullName);

        Map<ContactType, String> myContacts = myResume.getContacts();
        myContacts.put(ContactType.PHONE, "+7(921) 855-0482");
        myContacts.put(ContactType.SKYPE, "skype:grigory.kislin");
        myContacts.put(ContactType.MAIL, "gkislin@yandex.ru");
        myContacts.put(ContactType.LINKEDIN, "https://www.linkedin.com/in/gkislin");
        myContacts.put(ContactType.GITHUB, "https://github.com/gkislin");
        myContacts.put(ContactType.STACKOVERFLOW, "https://stackoverflow.com/users/548473");
//        myContacts.put(ContactType.HOME_PAGE, "http://gkislin.ru/");

        Map<SectionType, Section> mySections = myResume.getSections();
/*
// OBJECTIVE section
        String myObjective = "Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям";
        mySections.put(SectionType.OBJECTIVE, new TextSection(myObjective));

// PERSONAL section
        String myPersonal = "Аналитический склад ума, сильная логика, креативность, инициативность. " +
                "Пурист кода и архитектуры.";
        mySections.put(SectionType.PERSONAL, new TextSection(myPersonal));

// ACHIEVEMENT section
        List<String> myAchievements = new ArrayList<>();
        myAchievements.add("Организация команды и успешная реализация Java проектов для сторонних заказчиков: " +
                "приложения автопарк на стеке Spring Cloud/микросервисы, система мониторинга показателей спортсменов " +
                "на Spring Boot, участие в проекте МЭШ на Play-2, многомодульный Spring Boot + Vaadin проект для " +
                "комплексных DIY смет");
        myAchievements.add("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", " +
                "\"Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). " +
                "Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и ведение проектов. " +
                "Более 3500 выпускников.");
        myAchievements.add("Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. " +
                "Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk.");
        myAchievements.add("Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. " +
                "Интеграция с 1С, Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением на стеке: " +
                "Scala/Play/Anorm/JQuery. Разработка SSO аутентификации и авторизации различных ERP модулей, " +
                "интеграция CIFS/SMB java сервера.");
        myAchievements.add("Реализация c нуля Rich Internet Application приложения на стеке технологий JPA, Spring, " +
                "Spring-MVC, GWT, ExtGWT (GXT), Commet, HTML5, Highstock для алгоритмического трейдинга.");
        myAchievements.add("Создание JavaEE фреймворка для отказоустойчивого взаимодействия слабо-связанных сервисов " +
                "(SOA-base архитектура, JAX-WS, JMS, AS Glassfish). Сбор статистики сервисов и информации о " +
                "состоянии через систему мониторинга Nagios. Реализация онлайн клиента для администрирования " +
                "и мониторинга системы по JMX (Jython/ Django).");
        myAchievements.add("Реализация протоколов по приему платежей всех основных платежных системы России " +
                "(Cyberplat, Eport, Chronopay, Сбербанк), Белоруcсии(Erip, Osmp) и Никарагуа.");
        mySections.put(SectionType.ACHIEVEMENT, new ListSection(myAchievements));

// QUALIFICATION section
        List<String> myQualifications = new ArrayList<>();
        myQualifications.add("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2");
        myQualifications.add("Version control: Subversion, Git, Mercury, ClearCase, Perforce");
        myQualifications.add("DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle, MySQL, " +
                "SQLite, MS SQL, HSQLDB");
        myQualifications.add("Languages: Java, Scala, Python/Jython/PL-Python, JavaScript, Groovy");
        myQualifications.add("XML/XSD/XSLT, SQL, C/C++, Unix shell scripts");
        myQualifications.add("Java Frameworks: Java 8 (Time API, Streams), Guava, Java Executor, MyBatis, " +
                "Spring (MVC, Security, Data, Clouds, Boot), JPA (Hibernate, EclipseLink), Guice, " +
                "GWT(SmartGWT, ExtGWT/GXT), Vaadin, Jasperreports, Apache Commons, Eclipse SWT, JUnit, " +
                "Selenium (htmlelements).");
        myQualifications.add("Python: Django.");
        myQualifications.add("JavaScript: jQuery, ExtJS, Bootstrap.js, underscore.js");
        myQualifications.add("Scala: SBT, Play2, Specs2, Anorm, Spray, Akka");
        myQualifications.add("Технологии: Servlet, JSP/JSTL, JAX-WS, REST, EJB, RMI, JMS, JavaMail, JAXB, StAX, " +
                "SAX, DOM, XSLT, MDB, JMX, JDBC, JPA, JNDI, JAAS, SOAP, AJAX, Commet, HTML5, ESB, CMIS, BPMN2, " +
                "LDAP, OAuth1, OAuth2, JWT.");
        myQualifications.add("Инструменты: Maven + plugin development, Gradle, настройка Ngnix");
        myQualifications.add("администрирование Hudson/Jenkins, Ant + custom task, SoapUI, JPublisher, Flyway, " +
                "Nagios, iReport, OpenCmis, Bonita, pgBouncer");
        myQualifications.add("Отличное знание и опыт применения концепций ООП, SOA, шаблонов проектрирования, " +
                "архитектурных шаблонов, UML, функционального программирования");
        myQualifications.add("Родной русский, английский \"upper intermediate\"");
        mySections.put(SectionType.QUALIFICATION, new ListSection(myQualifications));
/*
// EXPERIENCE section
        List<Company> myCompanies = new ArrayList<>();
        myCompanies.add(new Company("Java Online Projects", "http://javaops.ru/",
                List.of(new Company.Position(LocalDate.of(2013, 10, 1), LocalDate.now(),
                        "Автор проекта.", "Создание, организация и проведение Java онлайн " +
                        "проектов и стажировок."))));
        myCompanies.add(new Company("Wrike", "https://www.wrike.com/",
                List.of(new Company.Position(LocalDate.of(2014, 10, 1),
                        LocalDate.of(2016, 1, 1),
                        "Старший разработчик (backend)",
                        "Проектирование и разработка онлайн платформы управления проектами Wrike " +
                                "(Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). " +
                                "Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO."))));
        myCompanies.add(new Company("RIT Center", "",
                List.of(new Company.Position(LocalDate.of(2016, 1, 1),
                        LocalDate.of(2012, 4, 1),
                        "Java архитектор", "Организация процесса разработки системы ERP для разных " +
                        "окружений: релизная политика, версионирование, ведение CI (Jenkins), миграция базы " +
                        "(кастомизация Flyway), конфигурирование системы (pgBoucer, Nginx), AAA via SSO. Архитектура " +
                        "БД и серверной части системы. Разработка интергационных сервисов: CMIS, BPMN2, " +
                        "1C (WebServices), сервисов общего назначения (почта, экспорт в pdf, doc, html). Интеграция " +
                        "Alfresco JLAN для online редактирование из браузера документов MS Office. Maven + plugin " +
                        "development, Ant, Apache Commons, Spring security, Spring MVC, Tomcat,WSO2, xcmis, OpenCmis," +
                        " Bonita, Python scripting, Unix shell remote scripting via ssh tunnels, PL/Python"))));
        mySections.put(SectionType.EXPERIENCE, new CompanySection(myCompanies));

// EDUCATION section
        List<Company> myEducation = new ArrayList<>();
        myEducation.add(new Company("Coursera", "https://www.coursera.org/course/progfun",
                List.of(new Company.Position(LocalDate.of(2013, 3, 1),
                        LocalDate.of(2013, 5, 1),
                        "'Functional Programming Principles in Scala' by Martin Odersky", ""))));
        myEducation.add(new Company("Luxoft", "http://www.luxoft-training.ru/training/catalog/course.html?ID=22366",
                List.of(new Company.Position(LocalDate.of(2011, 3, 1),
                        LocalDate.of(2011, 4, 1),
                        "Курс 'Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.'", ""))));
        myEducation.add(new Company("Siemens AG", "http://www.siemens.ru/",
                List.of(new Company.Position(LocalDate.of(2005, 1, 1),
                        LocalDate.of(2005, 4, 1),
                        "3 месяца обучения мобильным IN сетям (Берлин)", ""))));
        myEducation.add(new Company("Alcatel", "http://www.alcatel.ru/",
                List.of(new Company.Position(LocalDate.of(1997, 9, 1),
                        LocalDate.of(1998, 3, 1),
                        "6 месяцев обучения цифровым телефонным сетям (Москва)", ""))));
        Company.Position Position1 = new Company.Position(LocalDate.of(1993, 9, 1),
                LocalDate.of(1996, 7, 1),
                "Аспирантура (программист С, С++)", "");
        Company.Position Position2 = new Company.Position(LocalDate.of(1987, 9, 1),
                LocalDate.of(1993, 7, 1),
                "Инженер (программист Fortran, C)", "");
        myEducation.add(new Company("Санкт-Петербургский национальный исследовательский университет " +
                "информационных технологий, механики и оптики.", "http://www.ifmo.ru/",
                List.of(Position1, Position2)));
        myEducation.add(new Company("Заочная физико-техническая школа при МФТИ", "https://mipt.ru/",
                List.of(new Company.Position(LocalDate.of(1984, 9, 1),
                        LocalDate.of(1987, 6, 1),
                        "Закончил с отличием", ""))));
        mySections.put(SectionType.EDUCATION, new CompanySection(myEducation));

        System.out.println("\n" + myResume.getFullName() + "\n");
        for (ContactType contact : ContactType.values()) {
            System.out.println(contact.getTitle() + ": " + myResume.getContacts().get(contact));
        }
        for (SectionType section : SectionType.values()) {
            System.out.println(section.getTitle() + "\n" + myResume.getSections().get(section));
        }
*/
        return myResume;
    }
}
