<%@ page import="com.urise.webapp.model.ListSection" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <link rel="stylesheet" href="css/style.css">
  <jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/>
  <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="../fragments/header.jsp"/>
<section>
  <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></h2>
  <p>
    <c:forEach var="contactEntry" items="${resume.contacts}">
      <jsp:useBean id="contactEntry"
                   type="java.util.Map.Entry<com.urise.webapp.model.ContactType, java.lang.String>"/>
        <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
    </c:forEach>
  <p>
</section>
<section>
  <p>
    <c:forEach var="sectionEntry" items="${resume.sections}">
      <jsp:useBean id="sectionEntry"
                   type="java.util.Map.Entry<com.urise.webapp.model.SectionType, com.urise.webapp.model.Section>"/>
      <%
          switch (sectionEntry.getKey().ordinal()) {
            case 0:
            case 1:
              out.print("<br/><b>" + sectionEntry.getKey().name() + "</b><br/>");
              out.print(sectionEntry.getValue() + "<br/>");
              break;
            case 2:
            case 3:
              out.print("<br/><b>" + sectionEntry.getKey().name() + "</b><br/>");
              for (String line : ((ListSection) sectionEntry.getValue()).getItems()) {
                out.print(line + "<br/>");
              }
              break;
            default:
              break;
          }
          %>
    </c:forEach>
  <p>
</section>
<jsp:include page="../fragments/footer.jsp"/>
</body>
</html>
